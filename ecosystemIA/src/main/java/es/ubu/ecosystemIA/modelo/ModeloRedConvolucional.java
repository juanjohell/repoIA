package es.ubu.ecosystemIA.modelo;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="modelos")
public class ModeloRedConvolucional implements Serializable{
	
	private static final long serialVersionUID = 1L;
	// Aunque redimensionemos imágenes en el modelo keras, hay que indicar
	// las dimensiones que espera el modelo finalmente, una vez redimensionado
	// ya que estas propiedades se utilizan para definir la composición de las capas
	
	@Id
	@Column(name="ID_MODELO")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idModelo;
	
	@Column(name="NOMBRE")
	private String nombreModelo;
	
	@Column(name="DESCRIPCION")
	private String descripcion;
	
	@Column(name="INPUT_WIDTH")
	private Integer modelImageWidth ;
	
	@Column(name="INPUT_HEIGHT")
	private Integer modelImageHeight;
	
	@Column(name="INPUT_CHANNELS")
	private Integer imageChannels;
	//NCHW = Canales,Height,Width NHWC: Height,Width,Canales
	// Tiene que ver con el motor que hay por debajo de nuestro modelo.
	// TensorFlow trabaja en formato: NHWC
	
	@Column(name="FORMATO_MATRIZ_IMG")
	//CNN2DFormat formatoImagenModelo;
	private String formatoImagenModelo;
	
	@Column(name="PATH_FICHERO")
	private String pathToModel;
	
	
	public Integer getIdModelo() {
		return idModelo;
	}
	public void setIdModelo(Integer idModelo) {
		idModelo = idModelo;
	}
	public String getPathToModel() {
		return pathToModel;
	}
	public void setPathToModel(String pathToModel) {
		pathToModel = pathToModel;
	}
	
	public Integer getModelImageWidth() {
		return modelImageWidth;
	}
	
	public String getNombreModelo() {
		return nombreModelo;
	}
	public void setNombreModelo(String nombreModelo) {
		nombreModelo = nombreModelo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		descripcion = descripcion;
	}
	public void setModelImageWidth(Integer modelImageWidth) {
		modelImageWidth = modelImageWidth;
	}
	public Integer getModelImageHeight() {
		return modelImageHeight;
	}
	public void setModelImageHeight(Integer modelImageHeight) {
		modelImageHeight = modelImageHeight;
	}
	public Integer getImageChannels() {
		return imageChannels;
	}
	public void setImageChannels(Integer imageChannels) {
		imageChannels = imageChannels;
	}
	public String getFormatoImagenModelo() {
		return formatoImagenModelo;
	}
	public void setFormatoImagenModelo(String formatoImagenModelo) {
		this.formatoImagenModelo = formatoImagenModelo;
	}
	
	public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Descripcion: " + descripcion + ";");
        buffer.append("Nombre: " + nombreModelo);
        return buffer.toString();
    }
	
}
