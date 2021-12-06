package com.prenda.proyecto.app.view.json;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Component("/tel.json")
public class ProductoListJsonView extends MappingJackson2JsonView{

	@Override
	protected Object filterModel(Map<String, Object> model) {

		
		model.remove("titulo");
		model.remove("page");
		model.remove("tela");
		
		return super.filterModel(model);
	}

	
	
}
