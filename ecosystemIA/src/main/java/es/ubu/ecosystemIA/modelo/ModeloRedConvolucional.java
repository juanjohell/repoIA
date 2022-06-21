package es.ubu.ecosystemIA.modelo;

import java.io.IOException;
import java.util.ArrayList;

import org.deeplearning4j.nn.conf.CNN2DFormat;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.KerasSequentialModel;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import java.io.Serializable;

public class ModeloRedConvolucional {
	
	private static final long serialVersionUID = 1L;
	// Aunque redimensionemos imágenes en el modelo keras, hay que indicar
	// las dimensiones que espera el modelo finalmente, una vez redimensionado
	// ya que estas propiedades se utilizan para definir la composición de las capas
	String NombreModelo;
	String Descripcion;
	Integer ModelImageWidth ;
	Integer ModelImageHeight;
	Integer ImageChannels;
	//NCHW = Canales,Height,Width NHWC: Height,Width,Canales
	// Tiene que ver con el motor que hay por denbajo de nuestro modelo.
	// TensorFlow trabaja en formato: NCHW
	CNN2DFormat formatoImagenModelo;
	Boolean UsaTensorFlow;
	String PathToModel;
	private MultiLayerNetwork multilayerNetwork;
	INDArray resultado;
	ArrayList<String> categorias;
	// Constructores
	
	
	public Integer getModelImageWidth() {
		return ModelImageWidth;
	}
	public ModeloRedConvolucional() {
		
	}
	public ModeloRedConvolucional(Integer modelImageWidth, Integer modelImageHeight, Integer imageChannels,
			Boolean usaTensorFlow, String pathToModel) {
		super();
		ModelImageWidth = modelImageWidth;
		ModelImageHeight = modelImageHeight;
		ImageChannels = imageChannels;
		UsaTensorFlow = usaTensorFlow;
		PathToModel = pathToModel;
		multilayerNetwork = null;
		// false indica que no hay entrenamiento, se carga para su uso
		try {
			multilayerNetwork = KerasModelImport.importKerasSequentialModelAndWeights(pathToModel,false);
		} catch (IOException | InvalidKerasConfigurationException | UnsupportedKerasConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void cargarModelo(String path) {
		try {
			multilayerNetwork = KerasModelImport.importKerasSequentialModelAndWeights(path,false);
		} catch (IOException | InvalidKerasConfigurationException | UnsupportedKerasConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getNombreModelo() {
		return NombreModelo;
	}
	public void setNombreModelo(String nombreModelo) {
		NombreModelo = nombreModelo;
	}
	public String getDescripcion() {
		return Descripcion;
	}
	public void setDescripcion(String descripcion) {
		Descripcion = descripcion;
	}
	public void setModelImageWidth(Integer modelImageWidth) {
		ModelImageWidth = modelImageWidth;
	}
	public Integer getModelImageHeight() {
		return ModelImageHeight;
	}
	public void setModelImageHeight(Integer modelImageHeight) {
		ModelImageHeight = modelImageHeight;
	}
	public Integer getImageChannels() {
		return ImageChannels;
	}
	public void setImageChannels(Integer imageChannels) {
		ImageChannels = imageChannels;
	}
	public CNN2DFormat getFormatoImagenModelo() {
		return formatoImagenModelo;
	}
	public void setFormatoImagenModelo(CNN2DFormat formatoImagenModelo) {
		this.formatoImagenModelo = formatoImagenModelo;
	}
	public Boolean getUsaTensorFlow() {
		return UsaTensorFlow;
	}
	public void setUsaTensorFlow(Boolean usaTensorFlow) {
		UsaTensorFlow = usaTensorFlow;
	}
	
	public MultiLayerNetwork getMultilayerNetwork() {
		return multilayerNetwork;
	}
	public void setMultilayerNetwork(MultiLayerNetwork multilayerNetwork) {
		this.multilayerNetwork = multilayerNetwork;
	}
	public INDArray getResultado() {
		return resultado;
	}
	
	
	public ArrayList<String> getCategorias() {
		return categorias;
	}
	public void setCategorias(ArrayList<String> categorias) {
		this.categorias = categorias;
	}
	// dada la ruta de un fichero HDF5 de Keras lo importa a un objeto del
	// tipo MultiLayerNetwork
	private static MultiLayerNetwork importKerasSequentialModelAndWeights(String modelHdf5Filename)
	        throws IOException, InvalidKerasConfigurationException, UnsupportedKerasConfigurationException {
		KerasSequentialModel kerasModel = new KerasSequentialModel().modelBuilder().modelHdf5Filename(modelHdf5Filename)
	          .enforceTrainingConfig(false).buildSequential();
	  MultiLayerNetwork model = kerasModel.getMultiLayerNetwork();
	  return model;
	}
	
}
