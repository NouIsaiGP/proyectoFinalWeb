package com.prenda.proyecto.app.models.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.io.Serializable;
import java.util.Set;

@Entity
public class Venta implements Serializable {
	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String fechaYHora;
    
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private Integer total;
    
    private String estado;
    
    @JsonManagedReference
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProductoVendido> productos;    
    

    public Venta(Usuario usuario, Integer total, String estado) {
		this.usuario = usuario;
		this.total = total;
		this.estado = estado;
		this.fechaYHora = Utiles.obtenerFechaYHoraActual();
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Venta() {
        this.fechaYHora = Utiles.obtenerFechaYHoraActual();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getTotal2() {
        Float total = 0f;
        for (ProductoVendido productoVendido : this.productos) {
            total += productoVendido.getTotal();
        }
        return total;
    }

    public String getFechaYHora() {
        return fechaYHora;
    }

    public void setFechaYHora(String fechaYHora) {
        this.fechaYHora = fechaYHora;
    }

    public Set<ProductoVendido> getProductos() {
        return productos;
    }

    public void setProductos(Set<ProductoVendido> productos) {
        this.productos = productos;
    }

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getTotal() {
		return total;
	}

	@Override
	public String toString() {
		return "Venta [id=" + id + ", fechaYHora=" + fechaYHora + ", usuario=" + usuario + ", total=" + total
				+ ", estado=" + estado + ", productos=" + productos + "]";
	}

	
    
    
}
