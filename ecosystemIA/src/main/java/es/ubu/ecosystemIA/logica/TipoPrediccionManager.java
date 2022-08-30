package es.ubu.ecosystemIA.logica;

import java.io.Serializable;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

import es.ubu.ecosystemIA.modelo.TipoPrediccion;
@Transactional
public interface TipoPrediccionManager extends Serializable{
	
	public List<TipoPrediccion> getTiposPrediccion();
	public TipoPrediccion devuelveTipoPrediccion(Integer idTipoTipoPrediccion);
	public void nuevoTipoPrediccion(TipoPrediccion tipoPrediccion);
	public void borrarTipoPrediccion(TipoPrediccion tipoPrediccion);
	public void editarTipoPrediccion(TipoPrediccion tipoPrediccion);
} 	
