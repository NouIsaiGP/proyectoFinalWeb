package com.prenda.proyecto.app.controllers;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CarritoController {
	
	
	@GetMapping("/master")
	public String registrarAdmin() {

		return "master";
	}

}
