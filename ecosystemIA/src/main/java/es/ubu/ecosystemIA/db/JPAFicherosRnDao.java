package es.ubu.ecosystemIA.db;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;
import es.ubu.ecosystemIA.modelo.Ficheros;

import org.springframework.transaction.annotation.Transactional;


@Repository(value = "FicherosRnDao")
@Transactional
public class JPAFicherosRnDao implements FicherosRnDao {
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
	public Ficheros getFichero(Integer idModelo) {
		logger.info("seleccionar fichero binario por id modelo: "+idModelo.toString());
		Ficheros fichero = new Ficheros();
		fichero = (Ficheros) em.createQuery("select t from Ficheros t where t.idModelo = "+idModelo).getSingleResult();
		logger.info("retornando fichero del modelo");
		return fichero;
	}
	
	@Transactional
	 @SuppressWarnings("unchecked")
	public void nuevoFichero(Ficheros fichero) {
		em.merge(fichero);
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	public void editarFichero(Ficheros fichero) {
		// TODO Auto-generated method stub
		logger.info("DAO: editar fichero de modelo.");
		em.merge(fichero);
	}
	@Transactional
	public void borrarFichero(Ficheros fichero) {
		em.remove(fichero);
	}

}
