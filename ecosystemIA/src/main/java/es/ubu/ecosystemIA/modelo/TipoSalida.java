package es.ubu.ecosystemIA.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tipo_salida")
public class TipoSalida implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="idTipoSalida")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idTipoSalida;
	
	@Column(name="nombre")
	private String nombre;
	
	@Column(name="descripcion")
	private String descripcion;

	public int getIdTipoSalida() {
		return idTipoSalida;
	}

	public void setIdTipoSalida(int idTipoSalida) {
		this.idTipoSalida = idTipoSalida;
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
