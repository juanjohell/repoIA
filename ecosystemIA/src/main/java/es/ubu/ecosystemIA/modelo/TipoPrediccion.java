package es.ubu.ecosystemIA.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tipo_prediccion")
public class TipoPrediccion implements Serializable{
	

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="idTipoPrediccion")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idTipoPrediccion;
	
	@Column(name="nombre")
	private String nombre;
	
	@Column(name="descripcion")
	private String descripcion;

	public int getIdTipoPrediccion() {
		return idTipoPrediccion;
	}

	public void setIdTipoPrediccion(int idTipoPrediccion) {
		this.idTipoPrediccion = idTipoPrediccion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
