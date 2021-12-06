package com.prenda.proyecto.app.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prenda.proyecto.app.models.entity.Usuario;
import com.prenda.proyecto.app.models.service.JpaUserDetailsService;

@Controller
public class LoginController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JpaUserDetailsService userService;

	@PostMapping(value = { "/crear/usuario", "crear", "/crear" })
	public String crearUsuario(RedirectAttributes flash, @RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "user", required = false) String user,
			@RequestParam(name = "password", required = true) String password) {

		Usuario us = userService.findByUser(user);
		Usuario us2 = userService.findByEmail(email);
		if (us != null || us2 != null) {
			if (us.getUsername().equals(user)) {
				flash.addFlashAttribute("error", "El nombre de usuario ya esta ocupado");
				return "redirect:/login";
			} else if (us2.getEmail().equals(email)) {
				flash.addFlashAttribute("error", "El email ya esta registrado con otra cuenta");
				return "redirect:/login";
			}
		}
		
		String pass = "";
		for (int i = 0; i < 2; i++) {
			pass = passwordEncoder.encode(password);
		}

		Usuario usuario = new Usuario();
		usuario.setEmail(email);
		usuario.setEnabled(true);
		usuario.setUsername(user);
		usuario.setPassword(pass);

		userService.save(usuario);
		flash.addFlashAttribute("succes", "Usuario Registrado");

		return "redirect:/login";
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/crear/admin")
	public String crearAdmin(RedirectAttributes flash, @RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "user", required = false) String user,
			@RequestParam(name = "password", required = true) String password) {

		Usuario us = userService.findByUser(user);
		Usuario us2 = userService.findByEmail(email);
		if (us != null) {
			flash.addFlashAttribute("error", "El nombre de usuario ya esta ocupado");
			return "redirect:/crearadmin";
		} else if (us2 != null) {
			flash.addFlashAttribute("error", "El email ya esta registrado con otra cuenta");
			return "redirect:/crearadmin";
		}

		String pass = "";

		// Codificacion de contraseñas
		for (int i = 0; i < 2; i++) {
			pass = passwordEncoder.encode(password);
		}

		Usuario usuario = new Usuario();
		usuario.setEmail(email);
		usuario.setEnabled(true);
		usuario.setUsername(user);
		usuario.setPassword(pass);

		userService.saveAdmin(usuario);

		return "redirect:/admin";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/crearadmin")
	public String registrarAdmin() {

		return "crearadmin";
	}

	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, Model model, Principal principal,
			RedirectAttributes flash, Authentication authentication) {

		if (principal != null) {
			flash.addFlashAttribute("info", "Ya ha inciado sesión anteriormente");
			return "redirect:/admin";
		}

		if (error != null) {
			model.addAttribute("error",
					"Error en el login: Nombre de usuario o contraseña incorrecta, por favor vuelva a intentarlo!");
		}

		return "login";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/admin")
	public String administrador(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, Model model, Principal principal,
			RedirectAttributes flash) {

		return "admin";
	}

	@GetMapping({ "index", "/", "" })
	public String usuario(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, Model model, Principal principal,
			RedirectAttributes flash, Authentication authentication) {

		if (hasRole("ROLE_ADMIN")) {
			return "redirect:/admin";
		}

		return "index";
	}

	private boolean hasRole(String role) {

		SecurityContext context = SecurityContextHolder.getContext();

		if (context == null) {
			return false;
		}

		Authentication auth = context.getAuthentication();

		if (auth == null) {
			return false;
		}

		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

		return authorities.contains(new SimpleGrantedAuthority(role));

		/*
		 * for(GrantedAuthority authority: authorities) {
		 * if(role.equals(authority.getAuthority())) {
		 * logger.info("Hola usuario ".concat(auth.getName()).concat(" tu role es: "
		 * .concat(authority.getAuthority()))); return true; } }
		 * 
		 * return false;
		 */

	}

}
