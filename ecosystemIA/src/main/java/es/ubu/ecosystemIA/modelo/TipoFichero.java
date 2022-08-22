package es.ubu.ecosystemIA.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tipos_fichero")
public class TipoFichero implements Serializable{
	

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="idTipoFichero")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idTipoFichero;
	
	@Column(name="nombreCorto")
	private String nombreCorto;
	
	@Column(name="nombreLargo")
	private String nombreLargo;
	
	@Column(name="descripcion")
	private String descripcion;

	public int getIdTipoFichero() {
		return idTipoFichero;
	}

	public void setIdCategoria(int idTipoFichero) {
		this.idTipoFichero = idTipoFichero;
	}

	public String getNombreCorto() {
		return nombreCorto;
	}

	public void setNombreCorto(String nombreCorto) {
		this.nombreCorto = nombreCorto;
	}

	public String getNombreLargo() {
		return nombreLargo;
	}

	public void setNombreLargo(String nombreLargo) {
		this.nombreLargo = nombreLargo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
