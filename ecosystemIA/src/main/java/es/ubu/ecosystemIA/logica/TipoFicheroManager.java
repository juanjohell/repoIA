package es.ubu.ecosystemIA.logica;

import java.io.Serializable;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

import es.ubu.ecosystemIA.modelo.TipoFichero;
@Transactional
public interface TipoFicheroManager extends Serializable{
	
	public List<TipoFichero> getTiposFichero();
	public TipoFichero devuelveTipoFichero(Integer idTipoFichero);
	public void nuevoTipoFichero(TipoFichero tipoFichero);
	public void borrarTipoFichero(TipoFichero tipoFichero);
	public void editarTipoFichero(TipoFichero tipoFichero);
} 	
