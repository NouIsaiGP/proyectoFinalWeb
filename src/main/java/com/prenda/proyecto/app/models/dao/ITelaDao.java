package com.prenda.proyecto.app.models.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.prenda.proyecto.app.models.entity.Tela;

public interface ITelaDao extends PagingAndSortingRepository<Tela, Integer>{
	
	@Modifying
	@Query("update from Tela t set t.enable = 1 where t.id =?1")
	public void deshabilitarTela(Integer id);
	
	@Query("select t from Tela t where t.enable = 0")
	public Page<Tela> findAllDisponibles(Pageable page);
	
	@Query("select t from Tela t where t.enable = 0 and t.nombre like %:keyword% and t.color like %:keyword% and t.costo like %:keyword%")
	public Page<Tela> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
	
	@Query("select t from Tela t where t.enable = 0 and t.nombre like %:keyword%")
	public List<Tela> findByKeyword2(@Param("keyword") String keyword);
	
	@Query("select t from Tela t where t.enable = 0 and t.nombre like %:term%")
	public List<Tela> findByName(String term);
	
	List<Tela> findByNombreLikeIgnoreCase(String term);
}
