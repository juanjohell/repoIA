package es.ubu.ecosystemIA.logica;

import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;

import java.io.IOException;
import java.util.List;
import org.deeplearning4j.nn.conf.CNN2DFormat;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.KerasSequentialModel;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import es.ubu.ecosystemIA.logica.UtilidadesCnn;
import es.ubu.ecosystemIA.db.JPAModeloRnDao;
import es.ubu.ecosystemIA.db.ModeloRnDao;

@Component
public class SimpleNeuralModelManager implements NeuralNetworkManager{

	private static final long serialVersionUID = -5392895280098494635L;
	private ModeloRedConvolucional modelo;
	private List<ModeloRedConvolucional> modelos;
	@Autowired
	private ModeloRnDao modeloDao;
	private MultiLayerNetwork multilayerNetwork;
	
	
	public void borrarModelo(String idModelo) {
		
	}
	public ModeloRedConvolucional getModelo(String idModelo) {
		return modeloDao.getModelo(idModelo);
	}

	public void setModelo(ModeloRedConvolucional modelo) {
		this.modelo = modelo;
	}
	
	public List<ModeloRedConvolucional> getModelos(){
		return modeloDao.getModelosList();
	}

	public void setModelos(List<ModeloRedConvolucional> modelos) {
		this.modelos = modelos;
	}

	public void setMoldeloDao(ModeloRnDao modeloDao) {
		this.modeloDao = modeloDao;
	}
	
}
