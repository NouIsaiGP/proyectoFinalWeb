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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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

import com.prenda.proyecto.app.models.entity.Servicio;
import com.prenda.proyecto.app.models.entity.TipoServicio;
import com.prenda.proyecto.app.models.service.IServicioService;
import com.prenda.proyecto.app.util.paginator.PageRender;

@Controller
@SessionAttributes("servicio")
public class ServicioController {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private IServicioService servicioService;

	@RequestMapping(value = { "servicio/servicios", "servicios" }, method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {

		Pageable pageRequest = PageRequest.of(page, 8);
		Page<Servicio> servicios = null;

		servicios = servicioService.findAllEnable(pageRequest);
		PageRender<Servicio> pageRender = new PageRender<Servicio>("/servicios", servicios);
		model.addAttribute("titulo", "Listado de Servicios");
		model.addAttribute("servicios", servicios);
		model.addAttribute("servicio", new Servicio());
		model.addAttribute("page", pageRender);

		return "servicio/servicios";

	}

	@GetMapping("/servicio/listar")
	public String findAll(@RequestParam String keyword, @RequestParam int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 8);
		Page<Servicio> servicios = servicioService.findByKeyWord(keyword, pageRequest);
		Page<Servicio> ser = servicios;
		
		PageRender<Servicio> pageRender = new PageRender<Servicio>("/servicio/listar?keyword=" + keyword, servicios);
		model.addAttribute("titulo", "Listado de Servicios");
		model.addAttribute("servicios", servicios);
		model.addAttribute("servicio", new Servicio());
		model.addAttribute("page", pageRender);

		return "servicio/listar";
	}

	@Secured("ROLE_ADMIN")
	@Transactional
	@RequestMapping(value = "servicio/form")
	public String crear(Map<String, Object> model) {

		Servicio servicio = new Servicio();
		model.put("servicio", servicio);
		model.put("titulo", "Crear Servicio");

		return "servicio/form";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "servicio/editar/{id}")
	public String editar(@PathVariable(value = "id") Integer id, Map<String, Object> model, RedirectAttributes flash) {
		Servicio servicio = null;

		if (id > 0) {
			servicio = servicioService.findOne(id);
			if (servicio == null) {
				flash.addFlashAttribute("error", "El ID del Servicio no existe en la BBDD!");
				return "redirect:/servicio/servicios";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del Servicio no puede ser cero!");
			return "redirect:/servicio/servicios";
		}

		model.put("servicio", servicio);
		model.put("titulo", "Editar Servicio");
		return "servicio/editar";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "servicio/editar", method = RequestMethod.POST)
	public String editar(@RequestParam Integer id, @RequestParam String descripcion, @RequestParam String telefono,
			@RequestParam String contacto, @RequestParam String costo, @RequestParam Integer tipo, Model model,
			RedirectAttributes flash, SessionStatus status) {

		Servicio servicio = new Servicio();
		servicio.setId(id);
		servicio.setDescripcion(descripcion);
		servicio.setTelefono(telefono);
		servicio.setContacto(contacto);
		servicio.setCosto(costo);
		servicio.setTipo(new TipoServicio(tipo));

		String mensajeFlash = (id != null) ? "Servicio editado con éxito!" : "Servicio creado con éxito!";

		servicioService.update(servicio);

		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);

		return "redirect:/servicio/servicios";
	}

	@Secured("ROLE_ADMIN")
	@Transactional
	@RequestMapping(value = "servicio/form", method = RequestMethod.POST)
	public String guardar(@Valid Servicio servicio, BindingResult result, Model model, RedirectAttributes flash,
			SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Servicio");
			return "servicio/form";
		}

		String mensajeFlash = (servicio.getId() != null) ? "Servicio editado con éxito!" : "Servicio creado con éxito!";

		servicio.setEnable("0");

		servicioService.save(servicio);

		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);

		return "redirect:/servicio/servicios";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "servicio/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Integer id, RedirectAttributes flash) {

		if (id > 0) {
			Servicio servicio = servicioService.findOne(id);

			servicioService.delete(id);
			flash.addFlashAttribute("success", "Servicio eliminado con éxito!");

		}
		return "redirect:/servicio/servicios";
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
