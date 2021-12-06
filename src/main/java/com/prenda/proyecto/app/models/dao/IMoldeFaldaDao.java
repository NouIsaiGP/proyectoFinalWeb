package com.prenda.proyecto.app.models.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.prenda.proyecto.app.models.entity.MoldeFalda;

public interface IMoldeFaldaDao extends PagingAndSortingRepository<MoldeFalda, Integer>{

	@Modifying
	@Query("update from MoldeFalda m set m.enable = 1 where m.id =?1")
	public void deshabilitar(Integer id);
	
	@Query("select m from MoldeFalda m where m.enable = 0")
	public Page<MoldeFalda> findAllDisponibles(Pageable page);
	
	@Query("select m from MoldeFalda m where m.enable = 0 and m.nombre like %:keyword%")
	public Page<MoldeFalda> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
	
	@Query("select m from MoldeFalda m where m.enable = 0 and m.nombre like %:keyword%")
	public List<MoldeFalda> findByKeyword(@Param("keyword") String keyword);
	
	@Query("select m from MoldeFalda m where m.enable = 0 and m.nombre like %:term%")
	public List<MoldeFalda> findByKeyName(String term);
	
	public MoldeFalda findByNombre(String nombre);
	
}
