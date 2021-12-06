package com.prenda.proyecto.app.models.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lote")
public class Lote implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idlote")
	private Integer id;
	
	private String nombre;
	
	private String tipo;
	
	@Column(name = "costo_inicial")
	private Float costoInicial;
	
	private Integer cantidad;
	
	private Integer ventas;
	
	private Float ganancias;
	
	@Column(name = "costo_venta")
	private Float costoVenta;
	
	public Lote() {
	}	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Float getCostoInicial() {
		return costoInicial;
	}

	public void setCostoInicial(Float costoInicial) {
		this.costoInicial = costoInicial;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Integer getVentas() {
		return ventas;
	}

	public void setVentas(Integer ventas) {
		this.ventas = ventas;
	}

	public Float getGanancias() {
		return ganancias;
	}

	public void setGanancias(Float ganancias) {
		this.ganancias = ganancias;
	}

	public Float getCostoVenta() {
		return costoVenta;
	}

	public void setCostoVenta(Float costoVenta) {
		this.costoVenta = costoVenta;
	}

	
	
}
