package es.ubu.ecosystemIA.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="categorias")
public class Categoria implements Serializable{
	

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ID_ITEM")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idCategoria;
	
	@Column(name="ID_MODELO")
	private int idModelo;
	
	@Column(name="ID_ORDEN")
	private int idOrden;
	
	@Column(name="NOMBRE_CATEGORIA")
	private String nombreCategoria;

	public int getIdCategoria() {
		return idCategoria;
	}

	public int getIdModelo() {
		return idModelo;
	}

	public void setIdModelo(int idModelo) {
		this.idModelo = idModelo;
	}

	public int getIdOrden() {
		return idOrden;
	}

	public void setIdOrden(int idOrden) {
		this.idOrden = idOrden;
	}

	public String getNombreCategoria() {
		return nombreCategoria;
	}

	public void setNombreCategoria(String nombreCategoria) {
		this.nombreCategoria = nombreCategoria;
	}
	
	

}
