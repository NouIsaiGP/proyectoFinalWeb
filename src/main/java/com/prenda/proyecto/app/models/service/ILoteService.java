package com.prenda.proyecto.app.models.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.prenda.proyecto.app.models.entity.Lote;

public interface ILoteService {

	public Page<Lote> findAll(Pageable pageable);
	
	public void save(Lote lote);
	
	public Lote findOne(Integer id);
	
	public Page<Lote> findAllByTipo( String tipo, Pageable pageable);
	
	public Lote findByName(String name);
	
}
