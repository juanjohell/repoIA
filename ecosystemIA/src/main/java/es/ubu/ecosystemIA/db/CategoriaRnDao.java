package es.ubu.ecosystemIA.db;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;
import es.ubu.ecosystemIA.modelo.Categoria;

@Transactional
@Repository(value = "CategoriaRnDao")
public interface CategoriaRnDao {
	
	public List<Categoria> getCategoriasList(Integer idModelo);
	
	public Categoria getCategoria(Integer idCategoria);
	
    public void nuevaCategoria(Categoria categoria);
	
    public void editarCategoria(Categoria categoria);
    
    public void borrarCategoria(Categoria categoria);
    
}
