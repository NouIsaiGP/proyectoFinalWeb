package com.prenda.proyecto.app.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prenda.proyecto.app.models.entity.Lote;
import com.prenda.proyecto.app.models.entity.Servicio;
import com.prenda.proyecto.app.models.service.ILoteService;
import com.prenda.proyecto.app.util.paginator.PageRender;

@Controller
@SessionAttributes("lote")
public class LoteController {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired(required = true)
	private ILoteService service;
	
	@RequestMapping(value = { "ventas/ganancias", "ganancias" }, method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {

		Pageable pageRequest = PageRequest.of(page, 8);
		Page<Lote> lotes = null;

		lotes = service.findAll(pageRequest);
		PageRender<Lote> pageRender = new PageRender<Lote>("/ganancias", lotes);
		model.addAttribute("lotes", lotes);
		model.addAttribute("lote", new Lote());
		model.addAttribute("page", pageRender);

		return "ventas/ganancias";
	}
	
	@GetMapping("/ventas/lote")
	public String findAll(@RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Lote> lotes = service.findAllByTipo(keyword, pageRequest);
		
		PageRender<Lote> pageRender = new PageRender<Lote>("/ventas/lote?keyword=" + keyword, lotes);
		model.addAttribute("lotes", lotes);
		model.addAttribute("lote", new Lote());
		model.addAttribute("page", pageRender);

		return "ventas/lote";
	}
	
	@Secured("ROLE_ADMIN")
	@Transactional
	@RequestMapping(value = "venta/guardar", method = RequestMethod.POST)
	public void guardar(Lote lote, BindingResult result, Model model, RedirectAttributes flash,
			SessionStatus status) {
		service.save(lote);
		status.setComplete();
	}
	
}
