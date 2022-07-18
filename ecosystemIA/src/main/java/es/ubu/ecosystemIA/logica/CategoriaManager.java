package es.ubu.ecosystemIA.logica;

import java.io.Serializable;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

import es.ubu.ecosystemIA.modelo.Categoria;
@Transactional
public interface CategoriaManager extends Serializable{
	
	public List<Categoria> getCategorias(Integer idModelo);
	public Categoria devuelveCategoria(Integer idCategoria);
	public void nuevaCategoria(Categoria categoria);
	public void borrarCategoria(Categoria categoria);
	public void editarCategoria(Categoria categoria);
	
} 	
