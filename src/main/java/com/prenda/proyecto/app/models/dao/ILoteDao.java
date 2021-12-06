package com.prenda.proyecto.app.models.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.prenda.proyecto.app.models.entity.Avio;
import com.prenda.proyecto.app.models.entity.Lote;

public interface ILoteDao  extends PagingAndSortingRepository<Lote, Integer>{

	@Query("select a from Lote a")
	public Page<Lote> findAll(Pageable page);
	
	@Query("select a from Lote a where a.tipo like %:keyword%")
	public Page<Lote> findAllByTipo(@Param("keyword") String keyword, Pageable page);
	
	public Lote findByNombre(String nombre);
	
}
