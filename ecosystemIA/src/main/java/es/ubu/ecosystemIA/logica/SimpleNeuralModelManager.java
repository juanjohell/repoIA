package es.ubu.ecosystemIA.logica;

import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;

import java.util.List;

import es.ubu.ecosystemIA.logica.UtilidadesCnn;

public class SimpleNeuralModelManager implements NeuralNetworkManager{

	private static final long serialVersionUID = -5392895280098494635L;
	private ModeloRedConvolucional modelo;
	private List<ModeloRedConvolucional> modelos;
	
	public void cargaModeloH5(String ruta) {
		UtilidadesCnn utils = new UtilidadesCnn();
		modelo.setMultilayerNetwork(utils.cargaModeloH5(ruta));
	}

	public ModeloRedConvolucional getModelo() {
		return modelo;
	}

	public void setModelo(ModeloRedConvolucional modelo) {
		this.modelo = modelo;
	}
	
	public List<ModeloRedConvolucional> getModelos(){
		return modelos;
	}

	public void setModelos(List<ModeloRedConvolucional> modelos) {
		this.modelos = modelos;
	}
	
	
}
