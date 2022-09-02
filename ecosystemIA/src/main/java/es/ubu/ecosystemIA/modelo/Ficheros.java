package es.ubu.ecosystemIA.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="ficheros")
public class Ficheros implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="idFichero")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idFichero;
	
	@Column(name="idModelo")
	private int idModelo;
	
	@Lob
	@Column(name="fichero")
	private byte[] fichero;

	public int getIdFichero() {
		return idFichero;
	}

	public void setIdFichero(int idFichero) {
		this.idFichero = idFichero;
	}

	public int getIdModelo() {
		return idModelo;
	}

	public void setIdModelo(int idModelo) {
		this.idModelo = idModelo;
	}

	public byte[] getFichero() {
		return fichero;
	}

	public void setFichero(byte[] fichero) {
		this.fichero = fichero;
	}
	
}
