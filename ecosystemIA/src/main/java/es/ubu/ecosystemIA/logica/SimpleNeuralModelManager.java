package es.ubu.ecosystemIA.logica;

import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import es.ubu.ecosystemIA.db.ModeloRnDao;

@Component
@Transactional
public class SimpleNeuralModelManager implements NeuralNetworkManager{

	private static final long serialVersionUID = -5392895280098494635L;
	private ModeloRedConvolucional modelo;
	private List<ModeloRedConvolucional> modelos;
	@Autowired
	private ModeloRnDao modeloDao;
	private MultiLayerNetwork multilayerNetwork;
	
	@Transactional
	public void nuevoModelo(ModeloRedConvolucional modelo) {
		modeloDao.nuevoModelo(modelo);
	}
	@Transactional
	public void borrarModelo(ModeloRedConvolucional modelo) {
		modeloDao.borrarModelo(modelo);
	}
	@Transactional
	public void editarModelo(ModeloRedConvolucional modelo) {	
		modeloDao.editarModelo(modelo);
	}
	@Transactional
	public ModeloRedConvolucional getModelo(String idModelo) {
		return modeloDao.getModelo(idModelo);
	}

	public void setModelo(ModeloRedConvolucional modelo) {
		this.modelo = modelo;
	}
	@Transactional
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
