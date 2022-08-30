package es.ubu.ecosystemIA.logica;

import es.ubu.ecosystemIA.modelo.TipoPrediccion;


import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import es.ubu.ecosystemIA.db.TipoPrediccionRnDao;


@Component
@Transactional
public class SimpleTipoPrediccionManager implements TipoPrediccionManager{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final Log logger = LogFactory.getLog(getClass());
	
	private TipoPrediccion tipoPrediccion;
	private List<TipoPrediccion> tiposPrediccion;
	@Autowired
	private TipoPrediccionRnDao tipoPrediccionDao;
	
	@Transactional
	@Override
	public void nuevoTipoPrediccion(TipoPrediccion tipoPrediccion) {
		this.tipoPrediccionDao.nuevoTipoPrediccion(tipoPrediccion);
	}
	
	@Transactional
	@Override
	public void borrarTipoPrediccion(TipoPrediccion tipoPrediccion) {
		this.tipoPrediccionDao.borrarTipoPrediccion(tipoPrediccion);
	}
	@Transactional
	@Override
	public void editarTipoPrediccion(TipoPrediccion tipoPrediccion) {
		logger.info("tipoPrediccionDao: update de "+tipoPrediccion.getNombre());
		this.tipoPrediccionDao.editarTipoPrediccion(tipoPrediccion);
	}
	@Transactional
	public TipoPrediccion devuelveTipoPrediccion(Integer idTipoPrediccion) {
		return this.tipoPrediccionDao.getTipoPrediccion(idTipoPrediccion);
	}
	
	@Transactional
	@Override
	public List<TipoPrediccion> getTiposPrediccion(){
		return this.tipoPrediccionDao.getTiposPrediccionList();
	}

	public TipoPrediccion getTipoPrediccion() {
		return tipoPrediccion;
	}

	public void setTipoPrediccion(TipoPrediccion tipoPrediccion) {
		this.tipoPrediccion = tipoPrediccion;
	}

	public TipoPrediccionRnDao getTipoPrediccionDao() {
		return tipoPrediccionDao;
	}

	public void setTipoFicheroDao(TipoPrediccionRnDao tipoPrediccionDao) {
		this.tipoPrediccionDao = tipoPrediccionDao;
	}

	public void setTiposPrediccion(List<TipoPrediccion> tiposPrediccion) {
		this.tiposPrediccion = tiposPrediccion;
	}
	
	
	
}
