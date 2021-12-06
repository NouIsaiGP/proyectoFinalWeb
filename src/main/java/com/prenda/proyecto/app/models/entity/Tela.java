package com.prenda.proyecto.app.models.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "telas")
public class Tela implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idtela")
	private Integer id;
	
	private String nombre;
	
	private String largo;
	
	private String ancho;
	
	private String costo;
	
	private String color;
	
	@Column(name = "cant_rollos")
	private Integer cantidad;
	
	private String enable;

	
	
	public Tela(Integer id, String nombre, String largo, String ancho, String costo, String color, Integer cantidad,
			String enable) {
		this.id = id;
		this.nombre = nombre;
		this.largo = largo;
		this.ancho = ancho;
		this.costo = costo;
		this.color = color;
		this.cantidad = cantidad;
		this.enable = enable;
	}

	public Tela() {
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

	public String getLargo() {
		return largo;
	}

	public void setLargo(String largo) {
		this.largo = largo;
	}

	public String getAncho() {
		return ancho;
	}

	public void setAncho(String ancho) {
		this.ancho = ancho;
	}

	public String getCosto() {
		return costo;
	}

	public void setCosto(String costo) {
		this.costo = costo;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}
	
	
	
}
