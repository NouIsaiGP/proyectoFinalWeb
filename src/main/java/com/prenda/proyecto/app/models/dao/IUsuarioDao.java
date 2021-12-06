package com.prenda.proyecto.app.models.dao;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.prenda.proyecto.app.models.entity.Usuario;

@Repository
public interface IUsuarioDao extends PagingAndSortingRepository<Usuario, Long>{
	
	
	public Usuario findByUsername(String keyword);
	
	@Query("select a from Usuario a where a.username like %:keyword%")
	public Usuario findUserName(String keyword);
	
	public Usuario findByEmail(String email);
	
	@Query("select a.email, a.username from Usuario a where a.username like %:keyword%")
	public Usuario findUser(String keyword);
	

}
