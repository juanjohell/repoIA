package es.ubu.ecosystemIA.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tipos_almacenamiento")
public class TipoAlmacenamiento implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="IdTipoAlmacenamiento")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idTipoAlmacenamiento;
	
	@Column(name="nombre")
	private String nombre;

	public int getIdTipoAlmacenamiento() {
		return idTipoAlmacenamiento;
	}

	public void setIdTipoAlmacenamiento(int idTipoAlmacenamiento) {
		this.idTipoAlmacenamiento = idTipoAlmacenamiento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
