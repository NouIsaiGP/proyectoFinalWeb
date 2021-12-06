package com.prenda.proyecto.app.models.service.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prenda.proyecto.app.dao.rest.ProductoDao;
import com.prenda.proyecto.app.model.interfaces.ProductoInterface;
import com.prenda.proyecto.app.models.entity.Producto;

@Service
public class ProductoService implements ProductoInterface{
	
	@Autowired
	ProductoDao dao;

	@Override
	public List<Map<String, Object>> listar() {
		return dao.listar();
	}

	@Override
	public List<Map<String, Object>> listarId(int id) {
		// TODO Auto-generated method stub
		return null;
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
	public List<Map<String, Object>> listarKeyword(String keyword) {
		// TODO Auto-generated method stub
		return null;
	}

}
