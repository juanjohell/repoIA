package es.ubu.ecosystemIA.logica;

import es.ubu.ecosystemIA.modelo.Ficheros;


import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import es.ubu.ecosystemIA.db.FicherosRnDao;


@Component
@Transactional
public class SimpleFicherosManager implements FicherosManager{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final Log logger = LogFactory.getLog(getClass());
	
	private Ficheros fichero;
	
	@Autowired
	private FicherosRnDao ficherosDao;
	
	@Transactional
	@Override
	public void nuevoFichero(Ficheros fichero) {
		this.ficherosDao.nuevoFichero(fichero);
	}
	
	@Transactional
	@Override
	public void borrarFichero(Ficheros fichero) {
		this.ficherosDao.borrarFichero(fichero);
	}
	@Transactional
	@Override
	public void editarFichero(Ficheros fichero) {
		logger.info("FicherosDao: update de fichero.");
		this.ficherosDao.editarFichero(fichero);
	}
	@Transactional
	
	public Ficheros devuelveFichero(Integer idModelo) {
		return this.ficherosDao.getFichero(idModelo);
	}

	public Ficheros getFichero() {
		return fichero;
	}

	public void setFichero(Ficheros fichero) {
		this.fichero = fichero;
	}

	public FicherosRnDao getFicherosDao() {
		return ficherosDao;
	}

	public void setFicherosDao(FicherosRnDao ficherosDao) {
		this.ficherosDao = ficherosDao;
	}
	
}
