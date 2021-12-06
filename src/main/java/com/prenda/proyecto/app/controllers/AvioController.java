package com.prenda.proyecto.app.controllers;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.data.domain.Page;

import com.prenda.proyecto.app.models.entity.Avio;
import com.prenda.proyecto.app.models.service.IAvioService;
import com.prenda.proyecto.app.util.paginator.PageRender;

/*Controllador principal para trafico de datos de avios*/

@Controller
@SessionAttributes("avio")
public class AvioController {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private IAvioService avioService;

	@RequestMapping(value = { "avios/listar", "avios" }, method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request, String keyword) {
		/*
		 * PAGINATOR
		 * 
		 * paginador 4 paginas por datos de Avios
		 * 
		 */

		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Avio> avios = null;
		avios = avioService.findAllEnable(pageRequest);
		PageRender<Avio> pageRender = new PageRender<Avio>("/avios", avios);
		model.addAttribute("titulo", "Listado de Avios");
		model.addAttribute("avios", avios);
		model.addAttribute("avio", new Avio());
		model.addAttribute("page", pageRender);

		return "avios";

	}

	@GetMapping("/avio/listar")
	public String findAll(@RequestParam String keyword, @RequestParam int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Avio> avios = avioService.findAllByTipo(keyword, pageRequest);
		PageRender<Avio> pageRender = new PageRender<Avio>("/avio/listar?keyword=" + keyword, avios);
		model.addAttribute("titulo", "Listado de Avios");
		model.addAttribute("avios", avios);
		model.addAttribute("avio", new Avio());
		model.addAttribute("page", pageRender);

		return "avio/listar";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "avio/form")
	public String crear(Map<String, Object> model) {

		Avio avio = new Avio();
		model.put("avio", avio);
		model.put("titulo", "Crear Avio");

		return "avio/form";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "avio/form/{id}")
	public String editar(@PathVariable(value = "id") Integer id, Map<String, Object> model, RedirectAttributes flash) {
		Avio avio = null;

		if (id > 0) {
			avio = avioService.findOne(id);
			if (avio == null) {
				flash.addFlashAttribute("error", "El ID del cliente no existe en la BBDD!");
				return "redirect:/avios";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del cliente no puede ser cero!");
			return "redirect:/avios";
		}

		model.put("avio", avio);
		model.put("titulo", "Editar Avio");
		return "avio/form";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "avio/form", method = RequestMethod.POST)
	public String guardar(@Valid Avio avio, BindingResult result, Model model, RedirectAttributes flash,
			SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Avios");
			return "avio/form";
		}

		String mensajeFlash = (avio.getId() != null) ? "Avio editado con éxito!" : "Avio creado con éxito!";

		avio.setEnable("0");

		avioService.save(avio);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);

		return "redirect:/avios/listar";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "avios/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Integer id, RedirectAttributes flash) {

		if (id > 0) {
			Avio avio = avioService.findOne(id);

			avioService.delete(id);
			flash.addFlashAttribute("success", "Avio eliminado con éxito!");

		}
		return "redirect:/avios/listar";
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
