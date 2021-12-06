package com.prenda.proyecto.app.models.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "molde_pantalon")
public class MoldePantalon implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_molde")
	private Integer id;
	
	private String nombre;
	
	@Column(name = "ancho_cadera")
	private Double anchoCadera;
	
	@Column(name = "largo_tiro")
	private Double largoTiro;
	
	@Column(name = "avance_tiro")
	private Double avanceTiro;
	
	@Column(name = "entrada_en_cintura_derecha")
	private Double entradaCintura;
	
	@Column(name = "cuarto_contorno_cintura")
	private Double cuartoCintura;
	
	@Column(name = "largo_rodilla")
	private Double largoRodilla;
	
	@Column(name = "largo_pantalon")
	private Double largoPantalon;
	
	@Column(name = "cuarto_contorno_rodilla")
	private Double contornoRodilla;
	
	@Column(name = "cuarto_contorno_tobillo")
	private Double contornoTobillo;
	
	@Column(name = "cuarto_contorno_cinturapt")
	private Double contornoCinturaPT;
	
	@Column(name = "avance_tiropt")
	private Double avanceTiroPT;
	
	@Column(name = "sale_costadopt")
	private Double saleCostadoPT;
	
	
	@Column(name = "largo_pinza")
	private Double largoPinza;
	
	private String enable;

	public MoldePantalon() {
	}
	
	public MoldePantalon(Integer id, String nombre, Double anchoCadera, Double largoTiro, Double avanceTiro,
			Double entradaCintura, Double cuartoCintura, Double largoRodilla, Double largoPantalon,
			Double contornoRodilla, Double contornoTobillo, Double contornoCinturaPT, Double avanceTiroPT,
			Double saleCostadoPT, Double largoPinza, String enable) {
		this.id = id;
		this.nombre = nombre;
		this.anchoCadera = anchoCadera;
		this.largoTiro = largoTiro;
		this.avanceTiro = avanceTiro;
		this.entradaCintura = entradaCintura;
		this.cuartoCintura = cuartoCintura;
		this.largoRodilla = largoRodilla;
		this.largoPantalon = largoPantalon;
		this.contornoRodilla = contornoRodilla;
		this.contornoTobillo = contornoTobillo;
		this.contornoCinturaPT = contornoCinturaPT;
		this.avanceTiroPT = avanceTiroPT;
		this.saleCostadoPT = saleCostadoPT;
		this.largoPinza = largoPinza;
		this.enable = enable;
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

	public Double getAnchoCadera() {
		return anchoCadera;
	}

	public void setAnchoCadera(Double anchoCadera) {
		this.anchoCadera = anchoCadera;
	}

	public Double getLargoTiro() {
		return largoTiro;
	}

	public void setLargoTiro(Double largoTiro) {
		this.largoTiro = largoTiro;
	}

	public Double getAvanceTiro() {
		return avanceTiro;
	}

	public void setAvanceTiro(Double avanceTiro) {
		this.avanceTiro = avanceTiro;
	}

	public Double getEntradaCintura() {
		return entradaCintura;
	}

	public void setEntradaCintura(Double entradaCintura) {
		this.entradaCintura = entradaCintura;
	}

	public Double getCuartoCintura() {
		return cuartoCintura;
	}

	public void setCuartoCintura(Double cuartoCintura) {
		this.cuartoCintura = cuartoCintura;
	}

	public Double getLargoRodilla() {
		return largoRodilla;
	}

	public void setLargoRodilla(Double largoRodilla) {
		this.largoRodilla = largoRodilla;
	}

	public Double getLargoPantalon() {
		return largoPantalon;
	}

	public void setLargoPantalon(Double largoPantalon) {
		this.largoPantalon = largoPantalon;
	}

	public Double getContornoRodilla() {
		return contornoRodilla;
	}

	public void setContornoRodilla(Double contornoRodilla) {
		this.contornoRodilla = contornoRodilla;
	}

	public Double getContornoTobillo() {
		return contornoTobillo;
	}

	public void setContornoTobillo(Double contornoTobillo) {
		this.contornoTobillo = contornoTobillo;
	}

	public Double getContornoCinturaPT() {
		return contornoCinturaPT;
	}

	public void setContornoCinturaPT(Double contornoCinturaPT) {
		this.contornoCinturaPT = contornoCinturaPT;
	}

	public Double getAvanceTiroPT() {
		return avanceTiroPT;
	}

	public void setAvanceTiroPT(Double avanceTiroPT) {
		this.avanceTiroPT = avanceTiroPT;
	}

	public Double getSaleCostadoPT() {
		return saleCostadoPT;
	}

	public void setSaleCostadoPT(Double saleCostadoPT) {
		this.saleCostadoPT = saleCostadoPT;
	}

	public Double getLargoPinza() {
		return largoPinza;
	}

	public void setLargoPinza(Double largoPinza) {
		this.largoPinza = largoPinza;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	@Override
	public String toString() {
		return "MoldePantalon [id=" + id + ", nombre=" + nombre + ", anchoCadera=" + anchoCadera + ", largoTiro="
				+ largoTiro + ", avanceTiro=" + avanceTiro + ", entradaCintura=" + entradaCintura + ", cuartoCintura="
				+ cuartoCintura + ", largoRodilla=" + largoRodilla + ", largoPantalon=" + largoPantalon
				+ ", contornoRodilla=" + contornoRodilla + ", contornoTobillo=" + contornoTobillo
				+ ", contornoCinturaPT=" + contornoCinturaPT + ", avanceTiroPT=" + avanceTiroPT + ", saleCostadoPT="
				+ saleCostadoPT + ", largoPinza=" + largoPinza + ", enable=" + enable + "]";
	}
	
	
	
	
	
}
