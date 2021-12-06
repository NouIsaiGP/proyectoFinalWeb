package com.prenda.proyecto.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "prenda")
public class Prenda implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "idprenda")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String tipo;
	
	@Column(name = "nombre_prenda")
	private String nombre;
	
	private Integer cantidad;
	
	@Column(name = "precio_pieza")
	private Double precio;
	
	@Column(name = "costo_total")
	private Double costo;
	
	@Column(name = "cantidad_tela")
	private Integer cantidadTela;
	
	private String fecha;
	
	private String enable;
	
	

	public Prenda(Integer id, String tipo, String nombre, Integer cantidad, Double precio, Double costo,
			Integer cantidadTela, String fecha, String enable) {
		
		this.id = id;
		this.tipo = tipo;
		this.nombre = nombre;
		this.cantidad = cantidad;
		this.precio = precio;
		this.costo = costo;
		this.cantidadTela = cantidadTela;
		this.fecha = fecha;
		this.enable = enable;
	}

	public Prenda() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Double getCosto() {
		return costo;
	}

	public void setCosto(Double costo) {
		this.costo = costo;
	}

	public Integer getCantidadTela() {
		return cantidadTela;
	}

	public void setCantidadTela(Integer cantidadTela) {
		this.cantidadTela = cantidadTela;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	@Override
	public String toString() {
		return "Prenda [id=" + id + ", tipo=" + tipo + ", nombre=" + nombre + ", cantidad=" + cantidad + ", precio="
				+ precio + ", costo=" + costo + ", cantidadTela=" + cantidadTela + ", fecha=" + fecha + ", enable="
				+ enable + "]";
	}
	
	

}
