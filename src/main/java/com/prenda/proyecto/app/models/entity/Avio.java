package com.prenda.proyecto.app.models.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "avios")
public class Avio implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idavios")
	private Integer id;
	
	private String nombre;
	
	private String tipo;
	
	private String color;
	
	private Integer cantidad;

	@Column(name = "costo_pieza")
	private Double costo;
	
	private String enable;

	public Avio(Integer id, String nombre, String tipo, String color, Integer cantidad, Double costoPieza,
			String enable) {
		this.id = id;
		this.nombre = nombre;
		this.tipo = tipo;
		this.color = color;
		this.cantidad = cantidad;
		this.costo = costoPieza;
		this.enable = enable;
	}
	
	public Avio() {
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

	public Double getCosto() {
		return costo;
	}

	public void setCosto(Double costoPieza) {
		this.costo = costoPieza;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	@Override
	public String toString() {
		return "Avio [id=" + id + ", nombre=" + nombre + ", tipo=" + tipo + ", color=" + color + ", cantidad="
				+ cantidad + ", costo=" + costo + ", enable=" + enable + "]";
	}
	
}
