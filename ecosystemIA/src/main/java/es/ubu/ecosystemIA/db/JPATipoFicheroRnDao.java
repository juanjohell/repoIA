package es.ubu.ecosystemIA.db;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import es.ubu.ecosystemIA.modelo.Categoria;
import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;
import es.ubu.ecosystemIA.modelo.TipoFichero;

import org.springframework.transaction.annotation.Transactional;


@Repository(value = "TipoFicheroRnDao")
@Transactional
public class JPATipoFicheroRnDao implements TipoFicheroRnDao {
	public EntityManager em = null;
	protected final Log logger = LogFactory.getLog(getClass());
	/*
     * Sets the entity manager.
     */
	@PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

	@Transactional
    @SuppressWarnings("unchecked")
	public List<TipoFichero> getTipoFicheroList() {
		logger.info("tipos de fichero ");
		List<TipoFichero> listaTiposFichero = new ArrayList<TipoFichero>();
		listaTiposFichero = em.createQuery("select t from TipoFichero t order by t.idTipoFichero").getResultList();
		return listaTiposFichero;
	}
	
	@Transactional
    @SuppressWarnings("unchecked")
	public TipoFichero getTipoFichero(Integer idTipoFichero) {
		logger.info("seleccionar tipo de fichero por id : "+idTipoFichero.toString());
		TipoFichero tipoFichero = new TipoFichero();
		tipoFichero = (TipoFichero) em.createQuery("select t from TipoFichero t where t.idTipoFichero = "+idTipoFichero).getSingleResult();
		logger.info("retornando tipo de fichero del modelo  : "+tipoFichero.getNombreLargo());
		return tipoFichero;
	}
	
	
	@Transactional
	 @SuppressWarnings("unchecked")
	public void nuevoTipoFichero(TipoFichero tipoFichero) {
		em.merge(tipoFichero);
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	public void editarTipoFichero(TipoFichero tipoFichero) {
		// TODO Auto-generated method stub
		logger.info("DAO: editar tipo de fichero " +tipoFichero.getNombreLargo());
		em.merge(tipoFichero);
	}
	@Transactional
	public void borrarTipoFichero(TipoFichero tipoFichero) {
		em.remove(tipoFichero);
	}

}
