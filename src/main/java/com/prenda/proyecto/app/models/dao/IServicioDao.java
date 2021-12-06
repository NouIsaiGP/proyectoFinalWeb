package com.prenda.proyecto.app.models.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.prenda.proyecto.app.models.entity.Servicio;
import com.prenda.proyecto.app.models.entity.Tela;

public interface IServicioDao extends PagingAndSortingRepository<Servicio, Integer>{
	
	@Modifying
	@Query("update from Servicio s set s.enable = 1 where s.id =?1")
	public void deshabilitarServicio(Integer id);
	
	@Query("select s from Servicio s join s.tipo t where s.id=?1")
	public Page<Servicio> fetchByIdWithServicioWithTipoServicio(Pageable page ,Integer id);
	
	@Query("select s from Servicio s where s.enable = 0")
	public Page<Servicio> findAllDisponibles(Pageable page);
	
	@Query("select s from Servicio s where s.enable = 0 and s.tipo.nombre like %:keyword% or s.enable = 0 and s.contacto like %:keyword%")
	public Page<Servicio> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
	
	@Modifying
	@Query("update from Servicio s set s.descripcion =?2, s.telefono =?3, s.contacto =?4, s.costo =?5, s.tipo.id =?6 where s.id =?1")
	public void update(@Param("id")Integer id, @Param("descripcion") String descripcion, @Param("telefono") String telefono, @Param("contacto") String contacto, @Param("costo") String costo, @Param("tipo") Integer idTipo);
	
	@Query("select s from Servicio s where s.enable = 0 and s.telefono like %:term% or s.contacto like %:term% or s.costo like %:term% or s.tipo.nombre like %:term%")
	public List<Servicio> findByTipo(String term);

}
