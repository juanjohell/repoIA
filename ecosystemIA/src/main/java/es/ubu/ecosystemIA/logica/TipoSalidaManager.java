package es.ubu.ecosystemIA.logica;

import java.io.Serializable;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

import es.ubu.ecosystemIA.modelo.TipoSalida;
@Transactional
public interface TipoSalidaManager extends Serializable{
	
	public List<TipoSalida> getTiposSalida();
	public TipoSalida devuelveTipoSalida(Integer idTipoTipoSalida);
	public void nuevoTipoSalida(TipoSalida tipoSalida);
	public void borrarTipoSalida(TipoSalida tipoSalida);
	public void editarTipoSalida(TipoSalida tipoSalida);
} 	
