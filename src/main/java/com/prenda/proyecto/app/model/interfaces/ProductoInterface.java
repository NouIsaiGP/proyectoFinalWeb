package com.prenda.proyecto.app.model.interfaces;

import java.util.List;
import java.util.Map;

import com.prenda.proyecto.app.models.entity.Producto;

public interface ProductoInterface {
	
	public List<Map<String, Object>>listar();
	public List<Map<String, Object>>listarId(int id);
	public int add(Producto p);
	public int edit(Producto p);
	public int delete(int id);
	List<Map<String, Object>> listarKeyword(String keyword);

}
