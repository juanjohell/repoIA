package es.ubu.ecosystemIA.modelo;

import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;


@Entity
@Table(name="ficheros")
public class Ficheros implements Serializable{
	
	private static final long serialVersionUID = 1L;
		
	@Id
    @Column(name = "idModelo")
	private int idModelo;
	
	@Lob
	@Column(name="fichero")
	private Blob fichero;


	public Blob getFichero() {
		return fichero;
	}

	public void setFichero(Blob fichero) {
		this.fichero = fichero;
	}

	public int getIdModelo() {
		return idModelo;
	}

	public void setIdModelo(int idModelo) {
		this.idModelo = idModelo;
	}
	
}
