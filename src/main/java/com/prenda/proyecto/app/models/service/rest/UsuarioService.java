package com.prenda.proyecto.app.models.service.rest;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.prenda.proyecto.app.dao.rest.UsuarioDao;
import com.prenda.proyecto.app.model.interfaces.UserInterface;
import com.prenda.proyecto.app.models.entity.Usuario;

@Service
public class UsuarioService implements UserInterface {
	
	UsuarioDao dao;

	@Override
	public List<Map<String, Object>> listar(String username) {
		return dao.listar(username);
	}

	@Override
	public int add(Usuario u) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int edit(Usuario u) {
		// TODO Auto-generated method stub
		return 0;
	}

}
