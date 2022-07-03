package es.ubu.ecosystemIA.logica;

import java.io.Serializable;
import java.util.List;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;

public interface NeuralNetworkManager extends Serializable{
	
	public List<ModeloRedConvolucional> getModelos();
} 	
