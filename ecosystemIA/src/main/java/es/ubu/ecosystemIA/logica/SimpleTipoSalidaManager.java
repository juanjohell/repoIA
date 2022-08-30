package es.ubu.ecosystemIA.logica;

import es.ubu.ecosystemIA.modelo.TipoSalida;


import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import es.ubu.ecosystemIA.db.TipoSalidaRnDao;


@Component
@Transactional
public class SimpleTipoSalidaManager implements TipoSalidaManager{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final Log logger = LogFactory.getLog(getClass());
	
	private TipoSalida tipoSalida;
	private List<TipoSalida> tiposSalida;
	@Autowired
	private TipoSalidaRnDao tipoSalidaDao;
	
	@Transactional
	@Override
	public void nuevoTipoSalida(TipoSalida tipoSalida) {
		this.tipoSalidaDao.nuevoTipoSalida(tipoSalida);
	}
	
	@Transactional
	@Override
	public void borrarTipoSalida(TipoSalida tipoSalida) {
		this.tipoSalidaDao.borrarTipoSalida(tipoSalida);
	}
	@Transactional
	@Override
	public void editarTipoSalida(TipoSalida tipoSalida) {
		logger.info("tipoSalidaDao: update de "+tipoSalida.getNombre());
		this.tipoSalidaDao.editarTipoSalida(tipoSalida);
	}
	@Transactional
	public TipoSalida devuelveTipoSalida(Integer idTipoSalida) {
		return this.tipoSalidaDao.getTipoSalida(idTipoSalida);
	}
	
	@Transactional
	@Override
	public List<TipoSalida> getTiposSalida(){
		return this.tipoSalidaDao.getTiposSalidaList();
	}

	public TipoSalida getTipoSalida() {
		return tipoSalida;
	}

	public void setTipoSalida(TipoSalida tipoSalida) {
		this.tipoSalida = tipoSalida;
	}

	public TipoSalidaRnDao getTipoSalidaDao() {
		return tipoSalidaDao;
	}

	public void setTipoFicheroDao(TipoSalidaRnDao tipoSalidaDao) {
		this.tipoSalidaDao = tipoSalidaDao;
	}

	public void setTiposSalida(List<TipoSalida> tiposSalida) {
		this.tiposSalida = tiposSalida;
	}
	
	
	
}
