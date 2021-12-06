package com.prenda.proyecto.app.models.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.prenda.proyecto.app.models.entity.Prenda;
import com.prenda.proyecto.app.models.entity.PrendaServicio;

public interface IPrendaServicioDao extends CrudRepository<PrendaServicio, Integer>{

	public List<PrendaServicio> findByPrenda(Prenda prenda);
}
