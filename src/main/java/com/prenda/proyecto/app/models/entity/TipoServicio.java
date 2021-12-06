package com.prenda.proyecto.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
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

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "tipo_servicio")
public class TipoServicio implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idtipo_servicio")
	private Integer id;
	
	@Column(name = "nombre_servicio")
	private String nombre;
	
	
	
	@JsonBackReference
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Servicio> servicios;

	public TipoServicio() {
		servicios = new ArrayList<Servicio>();
	}
	
	public TipoServicio(Integer id) {
		this.id = id;
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

	public List<Servicio> getServicios() {
		return servicios;
	}

	public void setServicios(List<Servicio> servicios) {
		this.servicios = servicios;
	}
	
	public void addServicio(Servicio servicio) {
		servicios.add(servicio);
	}

	@Override
	public String toString() {
		return "TipoServicio [id=" + id + ", nombre=" + nombre + ", servicios=" + servicios + "]";
	}
	
	
	
}
