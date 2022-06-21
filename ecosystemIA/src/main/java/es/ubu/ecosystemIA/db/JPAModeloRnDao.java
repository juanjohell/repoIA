package es.ubu.ecosystemIA.db;

import java.util.List;

import org.springframework.stereotype.Repository;
import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//@Repository(value = "modeloRnDao")
public class JPAModeloRnDao implements ModeloRnDao {
	private EntityManager em = null;
	/*
     * Sets the entity manager.
     */
    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
	@Override
	public List<ModeloRedConvolucional> getModelosList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveModelo(ModeloRedConvolucional modelo) {
		// TODO Auto-generated method stub

	}

}
