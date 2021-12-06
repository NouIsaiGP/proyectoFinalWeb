package com.prenda.proyecto.app.models.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "molde_falda")
public class MoldeFalda implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idmolde_falda")
	private Integer id;

	private String nombre;

	@Column(name = "largo_falda")
	private Double largoFalda;

	@Column(name = "ancho_falda")
	private Double anchoFalda;

	@Column(name = "cuarto_cintura")
	private Double cuartoCintura;

	@Column(name = "aumento_pinza")
	private Double aumentoPinza;

	@Column(name = "profundidad_curva_cintura")
	private Double curvaCintura;

	@Column(name = "largo_cadera")
	private Double largoCadera;

	@Column(name = "cuarto_cadera")
	private Double cuartoCadera;

	@Column(name = "curva_del_costado")
	private Double curvaCostado;

	private String enable;

	public MoldeFalda(Integer id, String nombre, Double largoFalda, Double anchoFalda, Double cuartoCintura,
			Double aumentoPinza, Double curvaCintura, Double largoCadera, Double cuartoCadera, Double curvaCostado,
			String enable) {
		this.id = id;
		this.nombre = nombre;
		this.largoFalda = largoFalda;
		this.anchoFalda = anchoFalda;
		this.cuartoCintura = cuartoCintura;
		this.aumentoPinza = aumentoPinza;
		this.curvaCintura = curvaCintura;
		this.largoCadera = largoCadera;
		this.cuartoCadera = cuartoCadera;
		this.curvaCostado = curvaCostado;
		this.enable = enable;
	}

	public MoldeFalda() {
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

	public Double getLargoFalda() {
		return largoFalda;
	}

	public void setLargoFalda(Double largoFalda) {
		this.largoFalda = largoFalda;
	}

	public Double getAnchoFalda() {
		return anchoFalda;
	}

	public void setAnchoFalda(Double anchoFalda) {
		this.anchoFalda = anchoFalda;
	}

	public Double getCuartoCintura() {
		return cuartoCintura;
	}

	public void setCuartoCintura(Double cuartoCintura) {
		this.cuartoCintura = cuartoCintura;
	}

	public Double getAumentoPinza() {
		return aumentoPinza;
	}

	public void setAumentoPinza(Double aumentoPinza) {
		this.aumentoPinza = aumentoPinza;
	}

	public Double getCurvaCintura() {
		return curvaCintura;
	}

	public void setCurvaCintura(Double curvaCintura) {
		this.curvaCintura = curvaCintura;
	}

	public Double getLargoCadera() {
		return largoCadera;
	}

	public void setLargoCadera(Double largoCadera) {
		this.largoCadera = largoCadera;
	}

	public Double getCuartoCadera() {
		return cuartoCadera;
	}

	public void setCuartoCadera(Double cuartoCadera) {
		this.cuartoCadera = cuartoCadera;
	}

	public Double getCurvaCostado() {
		return curvaCostado;
	}

	public void setCurvaCostado(Double curvaCostado) {
		this.curvaCostado = curvaCostado;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	@Override
	public String toString() {
		return "MoldeFalda [id=" + id + ", nombre=" + nombre + ", largoFalda=" + largoFalda + ", anchoFalda="
				+ anchoFalda + ", cuartoCintura=" + cuartoCintura + ", aumentoPinza=" + aumentoPinza + ", curvaCintura="
				+ curvaCintura + ", largoCadera=" + largoCadera + ", cuartoCadera=" + cuartoCadera + ", curvaCostado="
				+ curvaCostado + ", enable=" + enable + "]";
	}

}
