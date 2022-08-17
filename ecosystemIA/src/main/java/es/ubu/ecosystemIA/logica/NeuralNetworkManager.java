package es.ubu.ecosystemIA.logica;

import java.io.Serializable;
import java.util.List;

import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.autodiff.samediff.SameDiff;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.transaction.annotation.Transactional;

import es.ubu.ecosystemIA.modelo.Imagen;
import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;
@Transactional
public interface NeuralNetworkManager extends Serializable{
	
	public List<ModeloRedConvolucional> getModelos();
	public ModeloRedConvolucional devuelveModelo(Integer idModelo);
	public void nuevoModelo(ModeloRedConvolucional modelo);
	public void borrarModelo(ModeloRedConvolucional modelo);
	public void editarModelo(ModeloRedConvolucional modelo);
	public void setMultilayerNetwork(ModeloRedConvolucional modelo);
	public MultiLayerNetwork getMultilayerNetwork();
	public void setComputationGraph(ModeloRedConvolucional modelo);
	public ComputationGraph getComputationGraph();
	public void setSameDiff(ModeloRedConvolucional modelo);
	public SameDiff getSameDiff();
	public ModeloRedConvolucional getModeloCargado();
	public void setModeloCargado(ModeloRedConvolucional modelo);
	public Imagen getImagenCargada();
	public void setImagenCargada(Imagen imagenCargada);
} 	
