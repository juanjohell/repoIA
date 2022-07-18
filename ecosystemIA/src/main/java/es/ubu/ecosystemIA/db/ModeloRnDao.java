package es.ubu.ecosystemIA.db;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;

@Transactional
@Repository(value = "ModeloRnDao")
public interface ModeloRnDao {
	
	public List<ModeloRedConvolucional> getModelosList();
	
	public ModeloRedConvolucional getModelo(Integer idModelo);
	
    public void nuevoModelo(ModeloRedConvolucional modelo);
	
    public void editarModelo(ModeloRedConvolucional modelo);
    
    public void borrarModelo(ModeloRedConvolucional modelo);
    
    public void establecerModeloPorDefecto(ModeloRedConvolucional modelo);
    
    public ModeloRedConvolucional devuelveModeloPorDefecto();
}
