package es.ubu.ecosystemIA.logica;

import java.io.Serializable;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

import es.ubu.ecosystemIA.modelo.TipoAlmacenamiento;
@Transactional
public interface TipoAlmacenamientoManager extends Serializable{
	
	public List<TipoAlmacenamiento> getTiposAlmacenamiento();
	public TipoAlmacenamiento devuelveTipoAlmacenamiento(Integer idTipoAlmacenamiento);
	public void nuevoTipoAlmacenamiento(TipoAlmacenamiento tipoAlmacenamiento);
	public void borrarTipoAlmacenamiento(TipoAlmacenamiento tipoAlmacenamiento);
	public void editarTipoAlmacenamiento(TipoAlmacenamiento tipoAlmacenamiento);
} 	
