package com.prenda.proyecto.app.models.dao;


import java.util.Date;
import java.util.List;

import com.prenda.proyecto.app.models.entity.Prenda;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface IPrendaDao extends PagingAndSortingRepository<Prenda, Integer>{

    @Modifying
	@Query("update from Prenda p set p.enable = 1 where p.id =?1")
	public void deshabilitar(Integer id);
	
	@Query("select p from Prenda p where p.enable = 0")
	public Page<Prenda> findAllDisponibles(Pageable page);
	
	@Query("select p from Prenda p where p.enable = 0 and p.nombre like %:keyword%")
	public Page<Prenda> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
	
	@Query("select a from Prenda a where a.enable = 0 and a.fecha like %:keyword%")
	public Page<Prenda> findAllByFecha(@Param("keyword") String keyword, Pageable page);
	
	public List<Prenda> findByNombre(String nombre);
    
}
