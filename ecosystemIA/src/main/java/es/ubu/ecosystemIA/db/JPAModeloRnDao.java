package es.ubu.ecosystemIA.db;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;
import org.springframework.transaction.annotation.Transactional;


@Repository(value = "ModeloRnDao")
@Transactional
public class JPAModeloRnDao implements ModeloRnDao {
	public EntityManager em = null;

	/*
     * Sets the entity manager.
     */
	@PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

	@Transactional
    @SuppressWarnings("unchecked")
	public List<ModeloRedConvolucional> getModelosList() {
		List<ModeloRedConvolucional> listaModelos = new ArrayList<ModeloRedConvolucional>();
		listaModelos = em.createQuery("select m from ModeloRedConvolucional m order by m.idModelo").getResultList();
		return listaModelos;
	}
	
	@Transactional
    @SuppressWarnings("unchecked")
	public ModeloRedConvolucional getModelo(String idModelo) {
		ModeloRedConvolucional modelo = new ModeloRedConvolucional();
		modelo = (ModeloRedConvolucional) em.createQuery("select m from ModeloRedConvolucional m where m.idModelo = "+idModelo).getSingleResult();
		return modelo;
	}
	
	@Transactional
	 @SuppressWarnings("unchecked")
	public void nuevoModelo(ModeloRedConvolucional modelo) {
		em.merge(modelo);
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	public void editarModelo(ModeloRedConvolucional modelo) {
		// TODO Auto-generated method stub
		em.unwrap(Session.class).update(modelo);
	}
	@Transactional
	public void borrarModelo(ModeloRedConvolucional modelo) {
		em.remove(modelo);
	}

}
