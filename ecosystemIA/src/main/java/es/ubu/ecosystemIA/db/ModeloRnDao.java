package es.ubu.ecosystemIA.db;

import java.util.List;
import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;

public interface ModeloRnDao {
	public List<ModeloRedConvolucional> getModelosList();

    public void saveModelo(ModeloRedConvolucional modelo);
    
    public void modifyModelo(ModeloRedConvolucional modelo);

}
