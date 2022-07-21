package es.ubu.ecosystemIA.modelo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.nd4j.linalg.api.ndarray.INDArray;

public class Imagen {
	String rutaImagen;
	String nombre;
	String nombreAnotada;
	Integer IMAGE_WIDTH;
	Integer IMAGE_HEIGHT;
	Integer IMAGE_CHANNELS;
	BufferedImage img;
	INDArray Matriz_cnn_normalizada;
	InputStream imagenStream;
	
	

	public Imagen(String rutaImagen, Integer iMAGE_WIDTH, Integer iMAGE_HEIGHT, Integer iMAGE_CHANNELS) {
		super();
		this.rutaImagen = rutaImagen;
		IMAGE_WIDTH = iMAGE_WIDTH;
		IMAGE_HEIGHT = iMAGE_HEIGHT;
		IMAGE_CHANNELS = iMAGE_CHANNELS;
		img = null;
	}
	
	public String getRutaImagen() {
		return rutaImagen;
	}
	public void setRutaImagen(String rutaImagen) {
		this.rutaImagen = rutaImagen;
	}
	public Integer getIMAGE_WIDTH() {
		return IMAGE_WIDTH;
	}
	public void setIMAGE_WIDTH(Integer iMAGE_WIDTH) {
		IMAGE_WIDTH = iMAGE_WIDTH;
	}
	public Integer getIMAGE_HEIGHT() {
		return IMAGE_HEIGHT;
	}
	public void setIMAGE_HEIGHT(Integer iMAGE_HEIGHT) {
		IMAGE_HEIGHT = iMAGE_HEIGHT;
	}
	public Integer getIMAGE_CHANNELS() {
		return IMAGE_CHANNELS;
	}
	public void setIMAGE_CHANNELS(Integer iMAGE_CHANNELS) {
		IMAGE_CHANNELS = iMAGE_CHANNELS;
	}
	public INDArray getMatriz_cnn_normalizada() {
		return Matriz_cnn_normalizada;
	}
	public void setMatriz_cnn_normalizada(INDArray matriz_cnn_normalizada) {
		Matriz_cnn_normalizada = matriz_cnn_normalizada;
	}

	public BufferedImage getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}

	public InputStream getImagenStream() {
		return imagenStream;
	}

	public void setImagenStream(InputStream imagenStream) {
		this.imagenStream = imagenStream;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombreAnotada() {
		return nombreAnotada;
	}

	public void setNombreAnotada(String nombreAnotada) {
		this.nombreAnotada = nombreAnotada;
	}
	
}
