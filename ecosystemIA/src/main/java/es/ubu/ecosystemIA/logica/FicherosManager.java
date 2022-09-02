package es.ubu.ecosystemIA.logica;

import java.io.Serializable;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

import es.ubu.ecosystemIA.modelo.Ficheros;
@Transactional
public interface FicherosManager extends Serializable{
	
	public Ficheros devuelveFichero(Integer idModelo);
	public void nuevoFichero(Ficheros fichero);
	public void borrarFichero(Ficheros fichero);
	public void editarFichero(Ficheros fichero);
} 	
