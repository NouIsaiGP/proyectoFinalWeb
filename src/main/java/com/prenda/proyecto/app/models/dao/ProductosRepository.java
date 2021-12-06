package com.prenda.proyecto.app.models.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.prenda.proyecto.app.models.entity.Avio;
import com.prenda.proyecto.app.models.entity.Producto;

public interface ProductosRepository extends PagingAndSortingRepository<Producto, Integer> {

    Producto findFirstByCodigo(String codigo);
    
    @Query("select a from Producto a where a.enable = 0")
	public Page<Producto> findAllValidos(Pageable page);
	
	@Query("select a from Producto a where a.enable = 0 and a.nombre like %:keyword% or a.enable = 0 and a.codigo like %:keyword%")
	public Page<Producto> findAllByKeyWord(@Param("keyword") String keyword, Pageable page);
	
	@Query("select a from Producto a where a.enable = 0 and a.nombre like %:keyword% or a.codigo like %:keyword%")
	public List<Producto> findByKeyWord(@Param("keyword") String keyword);
	
	@Query("select a from Producto a where a.enable = 0")
	public List<Map<String, Producto>> findAllEnable();
	
	@Query("select a from Producto a where a.enable = 0")
	public List<Producto> findAllList();
	
}
