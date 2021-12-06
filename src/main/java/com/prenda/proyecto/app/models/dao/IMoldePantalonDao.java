package com.prenda.proyecto.app.models.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.prenda.proyecto.app.models.entity.MoldePantalon;

public interface IMoldePantalonDao extends PagingAndSortingRepository<MoldePantalon, Integer>{


    @Modifying
	@Query("update from MoldePantalon m set m.enable = 1 where m.id =?1")
	public void deshabilitar(Integer id);
	
	@Query("select m from MoldePantalon m where m.enable = 0")
	public Page<MoldePantalon> findAllDisponibles(Pageable page);
	
	@Query("select m from MoldePantalon m where m.enable = 0 and m.nombre like %:keyword%")
	public Page<MoldePantalon> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
	
	@Query("select m from MoldePantalon m where m.enable = 0 and m.nombre like %:keyword%")
	public List<MoldePantalon> findByKeyword(@Param("keyword") String keyword);
	
	@Query("select m from MoldePantalon m where m.enable = 0 and m.nombre like %:term%")
	public List<MoldePantalon> findByKeyName(String term);
	
	public MoldePantalon findByNombre(String nombre);

}
