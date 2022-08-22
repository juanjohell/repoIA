package es.ubu.ecosystemIA.logica;

import es.ubu.ecosystemIA.modelo.TipoFichero;


import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import es.ubu.ecosystemIA.db.TipoFicheroRnDao;


@Component
@Transactional
public class SimpleTipoFicheroManager implements TipoFicheroManager{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final Log logger = LogFactory.getLog(getClass());
	
	private TipoFichero tipoFichero;
	private List<TipoFichero> tiposFichero;
	@Autowired
	private TipoFicheroRnDao tipoFicheroDao;
	
	@Transactional
	@Override
	public void nuevoTipoFichero(TipoFichero tipoFichero) {
		this.tipoFicheroDao.nuevoTipoFichero(tipoFichero);
	}
	
	@Transactional
	@Override
	public void borrarTipoFichero(TipoFichero tipoFichero) {
		this.tipoFicheroDao.borrarTipoFichero(tipoFichero);
	}
	@Transactional
	@Override
	public void editarTipoFichero(TipoFichero tipoFichero) {
		logger.info("tipoFicheroDao: update de "+tipoFichero.getNombreCorto());
		this.tipoFicheroDao.editarTipoFichero(tipoFichero);
	}
	@Transactional
	public TipoFichero devuelveTipoFichero(Integer idTipoFichero) {
		return this.tipoFicheroDao.getTipoFichero(idTipoFichero);
	}
	
	@Transactional
	@Override
	public List<TipoFichero> getTiposFichero(){
		return this.tipoFicheroDao.getTipoFicheroList();
	}

	public TipoFichero getTipoFichero() {
		return tipoFichero;
	}

	public void setTipoFichero(TipoFichero tipoFichero) {
		this.tipoFichero = tipoFichero;
	}

	public TipoFicheroRnDao getTipoFicheroDao() {
		return tipoFicheroDao;
	}

	public void setTipoFicheroDao(TipoFicheroRnDao tipoFicheroDao) {
		this.tipoFicheroDao = tipoFicheroDao;
	}

	public void setTiposFichero(List<TipoFichero> tiposFichero) {
		this.tiposFichero = tiposFichero;
	}
	
	
	
}
