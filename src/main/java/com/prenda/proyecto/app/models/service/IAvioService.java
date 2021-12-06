package com.prenda.proyecto.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.prenda.proyecto.app.models.entity.Avio;

public interface IAvioService {
	
	public List<Avio> findAll();
	
	public Page<Avio> findAll(Pageable pageable);
	
	public Page<Avio> findAllEnable(Pageable pageable);
	
	public void save(Avio avio);
	
	public Avio findOne(Integer id);
	
	public void delete(Integer id);
	
	public List<Avio> findByNombre(String nombre);
	
	public List<Avio> findByTipo(String tipo);
	
	public Page<Avio> findAllByTipo( String tipo, Pageable pageable);

}
