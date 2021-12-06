package com.prenda.proyecto.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.prenda.proyecto.app.models.entity.Servicio;

public interface IServicioService {
	
public Page<Servicio> findAllEnable(Pageable pageable);
	
	public void save(Servicio tela);
	
	public Servicio findOne(Integer id);
	
	public void delete(Integer id);
	
	public Page<Servicio> findByKeyWord(String keyword, Pageable pageable);
	
	public List<Servicio> findByKeyWord(String keyword);
	
	public void update(Servicio servicio);

}
