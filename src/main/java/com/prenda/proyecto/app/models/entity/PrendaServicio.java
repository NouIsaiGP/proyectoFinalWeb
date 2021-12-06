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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "prenda_servicios")
public class PrendaServicio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "idprenda_servicios")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "cantidad_avios")
	private Integer cantidad;

	@JoinColumn(name = "idprenda")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = Prenda.class)
	private Prenda prenda;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idtela")
	private Tela tela;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idmolde_falda")
	private MoldeFalda falda;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idmolde")
	private MoldePantalon pantalon;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idservicio")
	private Servicio servicio;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idavio")
	private Avio avio;
	
	

	public PrendaServicio(Integer id, Integer cantidad, Prenda prenda, Tela tela, MoldeFalda falda,
			MoldePantalon pantalon, Servicio servicio, Avio avio) {
		
		this.id = id;
		this.cantidad = cantidad;
		this.prenda = prenda;
		this.tela = tela;
		this.falda = falda;
		this.pantalon = pantalon;
		this.servicio = servicio;
		this.avio = avio;
	}

	public PrendaServicio() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Prenda getPrenda() {
		return prenda;
	}

	public void setPrenda(Prenda prenda) {
		this.prenda = prenda;
	}

	public Tela getTela() {
		return tela;
	}

	public void setTela(Tela tela) {
		this.tela = tela;
	}

	public MoldeFalda getFalda() {
		return falda;
	}

	public void setFalda(MoldeFalda falda) {
		this.falda = falda;
	}

	public MoldePantalon getPantalon() {
		return pantalon;
	}

	public void setPantalon(MoldePantalon pantalon) {
		this.pantalon = pantalon;
	}

	public Servicio getServicio() {
		return servicio;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}

	public Avio getAvio() {
		return avio;
	}

	public void setAvio(Avio avio) {
		this.avio = avio;
	}

	@Override
	public String toString() {
		return "PrendaServicio [id=" + id + ", cantidad=" + cantidad + ", prenda=" + prenda + ", tela=" + tela
				+ ", falda=" + falda + ", pantalon=" + pantalon + ", servicio=" + servicio + ", avio=" + avio + "]";
	}

	
	

}
