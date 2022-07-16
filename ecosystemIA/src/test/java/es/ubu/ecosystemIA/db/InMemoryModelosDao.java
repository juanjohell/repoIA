package es.ubu.ecosystemIA.db;

import java.util.List;

import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;

public class InMemoryModelosDao implements ModeloRnDao {

    private List<ModeloRedConvolucional> modelosList;

    public InMemoryModelosDao(List<ModeloRedConvolucional> modelosList) {
        this.modelosList = modelosList;
    }

    public List<ModeloRedConvolucional> getModelosList() {
        return modelosList;
    }

	@Override
	public void nuevoModelo(ModeloRedConvolucional modelo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void editarModelo(ModeloRedConvolucional modelo) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public ModeloRedConvolucional getModelo(String idModelo) {
		// TODO Auto-generated method stub
		ModeloRedConvolucional modelo = new ModeloRedConvolucional();
		return modelo;
	}

	@Override
	public void borrarModelo(ModeloRedConvolucional modelo) {
		// TODO Auto-generated method stub
		
	}
}
