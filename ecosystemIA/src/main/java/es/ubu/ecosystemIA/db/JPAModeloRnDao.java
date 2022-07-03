package es.ubu.ecosystemIA.db;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;

import javax.management.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "ModeloRnDao")
public class JPAModeloRnDao implements ModeloRnDao {
	private EntityManager em = null;
	/*
     * Sets the entity manager.
     */
    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

	@Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
	public List<ModeloRedConvolucional> getModelosList() {
		List<ModeloRedConvolucional> listaModelos = new ArrayList<ModeloRedConvolucional>();
		listaModelos = em.createQuery("select m from ModeloRedConvolucional m order by m.idModelo").getResultList();
		return listaModelos;
	}
	
	@Transactional(readOnly = false)
	public void saveModelo(ModeloRedConvolucional modelo) {
		em.merge(modelo);
	}
	
	@Transactional(readOnly = false)
	public void modifyModelo(ModeloRedConvolucional modelo) {
		// TODO Auto-generated method stub

	}

}
