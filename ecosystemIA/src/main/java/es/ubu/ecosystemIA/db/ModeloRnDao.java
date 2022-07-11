package es.ubu.ecosystemIA.db;

import java.util.List;
import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;

public interface ModeloRnDao {
	public List<ModeloRedConvolucional> getModelosList();
	public ModeloRedConvolucional getModelo(String idModelo);
    public void saveModelo(ModeloRedConvolucional modelo);
    public void modifyModelo(ModeloRedConvolucional modelo);

}
