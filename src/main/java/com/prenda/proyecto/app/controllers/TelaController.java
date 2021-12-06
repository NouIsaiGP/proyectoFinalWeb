package com.prenda.proyecto.app.controllers;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.prenda.proyecto.app.models.entity.Tela;
import com.prenda.proyecto.app.models.service.ITelaService;
import com.prenda.proyecto.app.util.paginator.PageRender;

@Controller
@SessionAttributes("tela")
public class TelaController {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private ITelaService telaService;

	@RequestMapping(value = {"/tela/telas", "/telas", "telas"}, method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {

		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Tela> telas = null;
		
		telas = telaService.findAllEnable(pageRequest);
		PageRender<Tela> pageRender = new PageRender<Tela>("/tela/telas", telas);
		model.addAttribute("titulo", "Listado de Telas");
		model.addAttribute("telas", telas);
		model.addAttribute("tela", new Tela());
		model.addAttribute("page", pageRender);

		return "tela/telas";
		
	}
	
	@RequestMapping(value = {"/tel"}, method = RequestMethod.GET)
	public String jsonTel(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Tela> telas = null;
		
		telas = telaService.findAllEnable(pageRequest);
		PageRender<Tela> pageRender = new PageRender<Tela>("/tela/telas", telas);
		model.addAttribute("titulo", "Listado de Telas");
		model.addAttribute("telas", telas);
		model.addAttribute("page", pageRender);

		return "/tel";
		
	}
	
	
	@GetMapping("/tela/listar")
	public String findAll(@RequestParam String keyword, @RequestParam int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Tela> telas = telaService.findByKeyWord(keyword, pageRequest);
		PageRender<Tela> pageRender = new PageRender<Tela>("/tela/listar?keyword=" + keyword, telas);
		model.addAttribute("titulo", "Listado de Telas");
		model.addAttribute("telas", telas);
		model.addAttribute("tela", new Tela());
		model.addAttribute("page", pageRender);

		return "tela/listar";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "tela/form")
	public String crear(Map<String, Object> model) {

		Tela tela = new Tela();
		model.put("tela", tela);
		model.put("titulo", "Crear Tela");

		return "tela/form";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "tela/form/{id}")
	public String editar(@PathVariable(value = "id") Integer id, Map<String, Object> model, RedirectAttributes flash) {
		Tela tela = null;

		if (id > 0) {
			tela = telaService.findOne(id);
			if (tela == null) {
				flash.addFlashAttribute("error", "El ID de la Tela no existe en la BBDD!");
				return "redirect:/telas";
			}
		} else {
			flash.addFlashAttribute("error", "El ID de la Tela no puede ser cero!");
			return "redirect:/telas";
		}

		model.put("tela", tela);
		model.put("titulo", "Editar Tela");
		return "tela/form";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "tela/form", method = RequestMethod.POST)
	public String guardar(@Valid Tela tela, BindingResult result, Model model, RedirectAttributes flash,
			SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Telas");
			return "tela/form";
		}

		String mensajeFlash = (tela.getId() != null) ? "Tela editado con éxito!" : "Tela creado con éxito!";

		tela.setEnable("0");

		telaService.save(tela);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);

		return "redirect:/tela/telas";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "tela/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Integer id, RedirectAttributes flash) {

		if (id > 0) {
			Tela tela = telaService.findOne(id);

			telaService.delete(id);
			flash.addFlashAttribute("success", "Tela eliminado con éxito!");

		}
		return "redirect:/tela/telas";
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
	}
	
	

}
