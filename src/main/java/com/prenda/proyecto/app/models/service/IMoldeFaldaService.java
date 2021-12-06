package com.prenda.proyecto.app.models.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.prenda.proyecto.app.models.entity.MoldeFalda;

public interface IMoldeFaldaService {

	public Page<MoldeFalda> findAllEnable(Pageable pageable);

	public void save(MoldeFalda molde);

	public MoldeFalda findOne(Integer id);

	public void delete(Integer id);

	public Page<MoldeFalda> findByKeyWord(String keyword, Pageable pageable);
	
	public MoldeFalda findByName(String nombre);

}
