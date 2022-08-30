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
import es.ubu.ecosystemIA.modelo.TipoPrediccion;

import org.springframework.transaction.annotation.Transactional;


@Repository(value = "TiposPrediccionRnDao")
@Transactional
public class JPATipoPrediccionRnDao implements TipoPrediccionRnDao {
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
	public List<TipoPrediccion> getTiposPrediccionList() {
		logger.info("tipos de Prediccion ");
		List<TipoPrediccion> listaTiposPrediccion = new ArrayList<TipoPrediccion>();
		listaTiposPrediccion = em.createQuery("select t from TipoPrediccion t order by t.idTipoPrediccion").getResultList();
		return listaTiposPrediccion;
	}
	
	@Transactional
    @SuppressWarnings("unchecked")
	public TipoPrediccion getTipoPrediccion(Integer idTipoPrediccion) {
		logger.info("seleccionar tipo de Prediccion por id : "+idTipoPrediccion.toString());
		TipoPrediccion tipoPrediccion = new TipoPrediccion();
		tipoPrediccion = (TipoPrediccion) em.createQuery("select t from TipoPrediccion t where t.idTipoPrediccion = "+idTipoPrediccion).getSingleResult();
		logger.info("retornando tipo de fichero del modelo  : "+tipoPrediccion.getNombre());
		return tipoPrediccion;
	}
	
	
	@Transactional
	 @SuppressWarnings("unchecked")
	public void nuevoTipoPrediccion(TipoPrediccion tipoPrediccion) {
		em.merge(tipoPrediccion);
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	public void editarTipoPrediccion(TipoPrediccion tipoPrediccion) {
		// TODO Auto-generated method stub
		logger.info("DAO: editar tipo de Prediccion " +tipoPrediccion.getNombre());
		em.merge(tipoPrediccion);
	}
	@Transactional
	public void borrarTipoPrediccion(TipoPrediccion tipoPrediccion) {
		em.remove(tipoPrediccion);
	}

}
