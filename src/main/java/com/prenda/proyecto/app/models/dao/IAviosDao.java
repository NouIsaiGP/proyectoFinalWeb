package com.prenda.proyecto.app.models.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.prenda.proyecto.app.models.entity.Avio;
import com.prenda.proyecto.app.models.entity.Servicio;

public interface IAviosDao extends PagingAndSortingRepository<Avio, Integer>{
	
	public List<Avio> findByNombre(String nombre);
	
	public List<Avio> findByTipo(String tipo);
	
	@Modifying
	@Query("update from Avio a set a.enable = 1 where a.id =?1")
	public void deshabilitarAvio(Integer id);
	
	@Query("select a from Avio a where a.enable = 0")
	public Page<Avio> findAllValidos(Pageable page);
	
	@Query("select a from Avio a where a.enable = 0 and a.tipo like %:keyword%")
	public Page<Avio> findAllByTipo(@Param("keyword") String keyword, Pageable page);
	
	@Query("select a from Avio a where a.enable = 0 and a.tipo like %:term% or a.nombre like %:term%")
	public List<Avio> findByTerm(String term);
}
