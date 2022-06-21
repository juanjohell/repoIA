package es.ubu.ecosystemIA.logica;

import java.io.Serializable;
import java.util.List;

import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;

public interface NeuralNetworkManager extends Serializable{
	public void cargaModeloH5(String ruta);
	public List<ModeloRedConvolucional> getModelos();
} 	
