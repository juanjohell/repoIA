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
import es.ubu.ecosystemIA.modelo.TipoAlmacenamiento;

import org.springframework.transaction.annotation.Transactional;


@Repository(value = "TiposAlmacenamientoRnDao")
@Transactional
public class JPATipoAlmacenamientoRnDao implements TipoAlmacenamientoRnDao {
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
	public List<TipoAlmacenamiento> getTiposAlmacenamientoList() {
		logger.info("tipos de almacenamiento ");
		List<TipoAlmacenamiento> listaTiposAlmacenamiento = new ArrayList<TipoAlmacenamiento>();
		listaTiposAlmacenamiento = em.createQuery("select t from TipoAlmacenamiento t order by t.idTipoAlmacenamiento").getResultList();
		return listaTiposAlmacenamiento;
	}
	
	@Transactional
    @SuppressWarnings("unchecked")
	public TipoAlmacenamiento getTipoAlmacenamiento(Integer idTipoAlmacenamiento) {
		logger.info("seleccionar tipo de almacenamiento por id : "+idTipoAlmacenamiento.toString());
		TipoAlmacenamiento tipoAlmacenamiento = new TipoAlmacenamiento();
		tipoAlmacenamiento = (TipoAlmacenamiento) em.createQuery("select t from TipoAlmacenamiento t where t.idTipoAlmacenamiento = "+idTipoAlmacenamiento).getSingleResult();
		logger.info("retornando tipo de fichero del modelo  : "+tipoAlmacenamiento.getNombre());
		return tipoAlmacenamiento;
	}
	
	
	@Transactional
	 @SuppressWarnings("unchecked")
	public void nuevoTipoAlmacenamiento(TipoAlmacenamiento tipoAlmacenamiento) {
		em.merge(tipoAlmacenamiento);
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	public void editarTipoAlmacenamiento(TipoAlmacenamiento tipoAlmacenamiento) {
		// TODO Auto-generated method stub
		logger.info("DAO: editar tipo de almacenamiento " +tipoAlmacenamiento.getNombre());
		em.merge(tipoAlmacenamiento);
	}
	@Transactional
	public void borrarTipoAlmacenamiento(TipoAlmacenamiento tipoAlmacenamiento) {
		em.remove(tipoAlmacenamiento);
	}

}
