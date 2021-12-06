package com.prenda.proyecto.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.prenda.proyecto.app.models.entity.Role;

public interface IRoleDao extends CrudRepository<Role, Long>{
	
}
