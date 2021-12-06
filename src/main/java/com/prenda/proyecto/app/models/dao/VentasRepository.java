package com.prenda.proyecto.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.prenda.proyecto.app.models.entity.Venta;

public interface VentasRepository extends CrudRepository<Venta, Integer> {
}
