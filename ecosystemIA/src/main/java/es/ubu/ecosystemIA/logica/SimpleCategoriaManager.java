package es.ubu.ecosystemIA.logica;

import es.ubu.ecosystemIA.modelo.Categoria;
import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import es.ubu.ecosystemIA.db.CategoriaRnDao;


@Component
@Transactional
public class SimpleCategoriaManager implements CategoriaManager{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final Log logger = LogFactory.getLog(getClass());
	
	private Categoria categoria;
	private List<Categoria> categorias;
	@Autowired
	private CategoriaRnDao categoriaDao;
	
	@Transactional
	@Override
	public void nuevaCategoria(Categoria categoria) {
		this.categoriaDao.nuevaCategoria(categoria);
	}
	@Transactional
	@Override
	public void borrarCategoria(Categoria categoria) {
		this.categoriaDao.borrarCategoria(categoria);
	}
	@Transactional
	@Override
	public void editarCategoria(Categoria categoria) {
		logger.info("CategoriaDAO: update de "+categoria.getIdCategoria());
		this.categoriaDao.editarCategoria(categoria);
	}
	@Transactional
	public Categoria devuelveCategoria(Integer idCategoria) {
		return this.categoriaDao.getCategoria(idCategoria);
	}
	
	public Categoria getCategoria() {
		return this.categoria;
	}
	
	
	@Transactional
	@Override
	public List<Categoria> getCategorias(Integer idModelo){
		return this.categoriaDao.getCategoriasList(idModelo);
	}
	
	public void setCategorias(List<Categoria> categoria) {
		this.categorias = categorias;
	}
	public void setCategoriaDao(CategoriaRnDao categoriaDao) {
		this.categoriaDao = categoriaDao;
	}
	
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
}
