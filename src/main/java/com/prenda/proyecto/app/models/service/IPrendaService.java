package com.prenda.proyecto.app.models.service;

import java.util.Date;
import java.util.List;

import com.prenda.proyecto.app.models.entity.Avio;
import com.prenda.proyecto.app.models.entity.MoldeFalda;
import com.prenda.proyecto.app.models.entity.MoldePantalon;
import com.prenda.proyecto.app.models.entity.Prenda;
import com.prenda.proyecto.app.models.entity.PrendaServicio;
import com.prenda.proyecto.app.models.entity.Servicio;
import com.prenda.proyecto.app.models.entity.Tela;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPrendaService {

    public Page<Prenda> findAllEnable(Pageable pageable);
	
	public void save(Prenda prenda);
	
	public Prenda findOne(Integer id);
	
	public void delete(Integer id);
	
	public Page<Prenda> findByKeyWord(String keyword, Pageable pageable);
	
	public void save(PrendaServicio prenda);
	
	public PrendaServicio findOneServicio(Integer id);
	
	public List<PrendaServicio> findByPrenda(Prenda prenda);
	
	public List<Tela> findTelaByName(String term);
	
	public List<Servicio> findServiciobyTipo(String term);
	
	public List<Avio> findAvioByTerm(String term);
	
	public List<MoldePantalon> findPantalonByTerm(String term);
	
	public List<MoldeFalda> findFaldaByTerm(String term);
	
	public List<Prenda> findPrendaByName(String name);
    
}
