package com.prenda.proyecto.app.model.interfaces;

import java.util.List;
import java.util.Map;

import com.prenda.proyecto.app.models.entity.Usuario;


public interface UserInterface {
	public List<Map<String, Object>>listar(String username);
	public int add(Usuario u);
	public int edit(Usuario u);
}
