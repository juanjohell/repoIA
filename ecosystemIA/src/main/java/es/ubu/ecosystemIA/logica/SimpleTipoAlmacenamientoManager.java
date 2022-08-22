package es.ubu.ecosystemIA.logica;

import es.ubu.ecosystemIA.modelo.TipoAlmacenamiento;


import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import es.ubu.ecosystemIA.db.TipoAlmacenamientoRnDao;


@Component
@Transactional
public class SimpleTipoAlmacenamientoManager implements TipoAlmacenamientoManager{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final Log logger = LogFactory.getLog(getClass());
	
	private TipoAlmacenamiento tipoAlmacenamiento;
	private List<TipoAlmacenamiento> tiposAlmacenamiento;
	@Autowired
	private TipoAlmacenamientoRnDao tipoAlmacenamientoDao;
	
	@Transactional
	@Override
	public void nuevoTipoAlmacenamiento(TipoAlmacenamiento tipoAlmacenamiento) {
		this.tipoAlmacenamientoDao.nuevoTipoAlmacenamiento(tipoAlmacenamiento);
	}
	
	@Transactional
	@Override
	public void borrarTipoAlmacenamiento(TipoAlmacenamiento tipoAlmacenamiento) {
		this.tipoAlmacenamientoDao.borrarTipoAlmacenamiento(tipoAlmacenamiento);
	}
	@Transactional
	@Override
	public void editarTipoAlmacenamiento(TipoAlmacenamiento tipoAlmacenamiento) {
		logger.info("tipoAlmacenamientoDao: update de "+tipoAlmacenamiento.getNombre());
		this.tipoAlmacenamientoDao.editarTipoAlmacenamiento(tipoAlmacenamiento);
	}
	@Transactional
	public TipoAlmacenamiento devuelveTipoAlmacenamiento(Integer idTipoAlmacenamiento) {
		return this.tipoAlmacenamientoDao.getTipoAlmacenamiento(idTipoAlmacenamiento);
	}
	
	@Transactional
	@Override
	public List<TipoAlmacenamiento> getTiposAlmacenamiento(){
		return this.tipoAlmacenamientoDao.getTiposAlmacenamientoList();
	}

	public TipoAlmacenamiento getTipoAlmacenamiento() {
		return tipoAlmacenamiento;
	}

	public void setTipoAlmacenamiento(TipoAlmacenamiento tipoAlmacenamiento) {
		this.tipoAlmacenamiento = tipoAlmacenamiento;
	}

	public TipoAlmacenamientoRnDao getTipoAlmacenamientoDao() {
		return tipoAlmacenamientoDao;
	}

	public void setTipoFicheroDao(TipoAlmacenamientoRnDao tipoAlmacenamientoDao) {
		this.tipoAlmacenamientoDao = tipoAlmacenamientoDao;
	}

	public void setTiposAlmacenamiento(List<TipoAlmacenamiento> tiposAlmacenamiento) {
		this.tiposAlmacenamiento = tiposAlmacenamiento;
	}
	
	
	
}
