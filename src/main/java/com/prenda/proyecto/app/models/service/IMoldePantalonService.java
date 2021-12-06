package com.prenda.proyecto.app.models.service;

import com.prenda.proyecto.app.models.entity.MoldePantalon;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMoldePantalonService {

    public Page<MoldePantalon> findAllEnable(Pageable pageable);

	public void save(MoldePantalon molde);

	public MoldePantalon findOne(Integer id);

	public void delete(Integer id);

	public Page<MoldePantalon> findByKeyWord(String keyword, Pageable pageable);
	
	public MoldePantalon findByName(String noae);

}
