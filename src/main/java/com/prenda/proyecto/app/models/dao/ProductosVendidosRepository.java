package com.prenda.proyecto.app.models.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.prenda.proyecto.app.models.entity.ProductoVendido;
import com.prenda.proyecto.app.models.entity.Servicio;
import com.prenda.proyecto.app.models.entity.Venta;

public interface ProductosVendidosRepository extends CrudRepository<ProductoVendido, Integer> {
	
	@Query("select a from ProductoVendido a where a.venta.usuario.id = ?1")
	public Page<ProductoVendido> findAllValidos(Long id, Pageable page);
	
	@Query("select a from Venta a where a.usuario.id = ?1")
	public List<Venta> findAllValidos(Long id);
	
	@Query("select a from ProductoVendido a")
	public Page<ProductoVendido> findAll(Pageable page);
	
	@Query("select s from ProductoVendido s where s.venta.usuario.telefono like %:keyword% or s.venta.usuario.email like %:keyword% or s.venta.usuario.username like %:keyword%")
	public Page<ProductoVendido> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
	
	@Query("select a from ProductoVendido a where a.venta = ?1")
	public Page<ProductoVendido> findAllByTipo(Venta venta, Pageable page);
	
	@Query("select s from Venta s where s.usuario.telefono like %:keyword% or s.usuario.email like %:keyword% or s.usuario.username like %:keyword%")
	public Page<Venta> findByKeywordVenta(@Param("keyword") String keyword, Pageable pageable);
	
	@Query("select a from Venta a")
	public Page<Venta> findAllVenta(Pageable page);

}
