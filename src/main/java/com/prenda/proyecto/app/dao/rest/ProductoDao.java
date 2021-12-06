package com.prenda.proyecto.app.dao.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.prenda.proyecto.app.model.interfaces.ProductoInterface;
import com.prenda.proyecto.app.models.entity.Producto;

@Repository
public class ProductoDao implements ProductoInterface{
	
	@Autowired
	JdbcTemplate template;

	@Override
	public List<Map<String, Object>> listar() {
		List<Map<String, Object>> list = template.queryForList("select * from producto");
		return list;
	}

	@Override
	public List<Map<String, Object>> listarKeyword(String keyword) {
		List<Map<String, Object>> list = template.queryForList("select * from producto nombre like " +"'"+ keyword + "'");
		return list;
	}

	@Override
	public int add(Producto p) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int edit(Producto p) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Map<String, Object>> listarId(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
