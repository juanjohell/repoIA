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
import es.ubu.ecosystemIA.modelo.TipoSalida;

import org.springframework.transaction.annotation.Transactional;


@Repository(value = "TiposSalidaRnDao")
@Transactional
public class JPATipoSalidaRnDao implements TipoSalidaRnDao {
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
	public List<TipoSalida> getTiposSalidaList() {
		logger.info("tipos de Salida ");
		List<TipoSalida> listaTiposSalida = new ArrayList<TipoSalida>();
		listaTiposSalida = em.createQuery("select t from TipoSalida t order by t.idTipoSalida").getResultList();
		return listaTiposSalida;
	}
	
	@Transactional
    @SuppressWarnings("unchecked")
	public TipoSalida getTipoSalida(Integer idTipoSalida) {
		logger.info("seleccionar tipo de Salida por id : "+idTipoSalida.toString());
		TipoSalida tipoSalida = new TipoSalida();
		tipoSalida = (TipoSalida) em.createQuery("select t from TipoSalida t where t.idTipoSalida = "+idTipoSalida).getSingleResult();
		logger.info("retornando tipo de fichero del modelo  : "+tipoSalida.getNombre());
		return tipoSalida;
	}
	
	
	@Transactional
	 @SuppressWarnings("unchecked")
	public void nuevoTipoSalida(TipoSalida tipoSalida) {
		em.merge(tipoSalida);
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	public void editarTipoSalida(TipoSalida tipoSalida) {
		// TODO Auto-generated method stub
		logger.info("DAO: editar tipo de Salida " +tipoSalida.getNombre());
		em.merge(tipoSalida);
	}
	@Transactional
	public void borrarTipoSalida(TipoSalida tipoSalida) {
		em.remove(tipoSalida);
	}

}
