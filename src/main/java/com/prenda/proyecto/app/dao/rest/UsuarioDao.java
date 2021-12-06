package com.prenda.proyecto.app.dao.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.prenda.proyecto.app.model.interfaces.UserInterface;
import com.prenda.proyecto.app.models.entity.Usuario;

@Repository
public class UsuarioDao implements UserInterface {

	@Autowired
	JdbcTemplate template;

	@Override
	public List<Map<String, Object>> listar(String username) {
		String sql = "select * from users where username=?";
		List<Map<String, Object>> list = template.queryForList(sql);
		return list;
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
