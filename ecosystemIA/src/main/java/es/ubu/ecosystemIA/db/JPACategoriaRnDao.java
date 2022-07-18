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
import org.springframework.transaction.annotation.Transactional;


@Repository(value = "CategoriaRnDao")
@Transactional
public class JPACategoriaRnDao implements CategoriaRnDao {
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
	public List<Categoria> getCategoriasList(Integer idModelo) {
		logger.info("categorias del modelo : "+idModelo.toString());
		List<Categoria> listaCategorias = new ArrayList<Categoria>();
		listaCategorias = em.createQuery("select c from Categoria c where c.idModelo = "+idModelo.toString()+" order by c.idOrden").getResultList();
		return listaCategorias;
	}
	
	@Transactional
    @SuppressWarnings("unchecked")
	public Categoria getCategoria(Integer idCategoria) {
		logger.info("seleccionar categoria por id Categoria: "+idCategoria.toString());
		Categoria categoria = new Categoria();
		categoria = (Categoria) em.createQuery("select c from Categoria c where c.idCategoria = "+idCategoria).getSingleResult();
		logger.info("retornando categoria : "+categoria.getNombreCategoria());
		return categoria;
	}
	
	@Transactional
	 @SuppressWarnings("unchecked")
	public void nuevaCategoria(Categoria categoria) {
		em.merge(categoria);
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	public void editarCategoria(Categoria categoria) {
		// TODO Auto-generated method stub
		logger.info("DAO: editar categoria " +categoria.getNombreCategoria());
		em.merge(categoria);
	}
	@Transactional
	public void borrarCategoria(Categoria categoria) {
		em.remove(categoria);
	}

}
