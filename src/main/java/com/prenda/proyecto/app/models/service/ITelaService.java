package com.prenda.proyecto.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.prenda.proyecto.app.models.entity.Tela;

public interface ITelaService {

	public Page<Tela> findAllEnable(Pageable pageable);
	
	public void save(Tela tela);
	
	public Tela findOne(Integer id);
	
	public void delete(Integer id);
	
	public Page<Tela> findByKeyWord(String keyword, Pageable pageable);
	
	public List<Tela> findByKeyWord(String keyword);
}
