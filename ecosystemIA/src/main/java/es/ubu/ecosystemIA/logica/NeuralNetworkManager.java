package es.ubu.ecosystemIA.logica;

import java.io.Serializable;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;
@Transactional
public interface NeuralNetworkManager extends Serializable{
	
	public List<ModeloRedConvolucional> getModelos();
	public ModeloRedConvolucional getModelo(String idModelo);
	public void borrarModelo(ModeloRedConvolucional modelo);
	public void editarModelo(ModeloRedConvolucional modelo);
} 	
