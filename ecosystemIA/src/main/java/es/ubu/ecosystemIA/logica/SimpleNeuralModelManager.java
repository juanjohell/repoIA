package es.ubu.ecosystemIA.logica;

import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.ubu.ecosystemIA.controller.FileUploadController;
import es.ubu.ecosystemIA.db.ModeloRnDao;

@Component
@Transactional
public class SimpleNeuralModelManager implements NeuralNetworkManager{
	protected final Log logger = LogFactory.getLog(getClass());
	private static final long serialVersionUID = -5392895280098494635L;
	private ModeloRedConvolucional modelo;
	private List<ModeloRedConvolucional> modelos;
	@Autowired
	private ModeloRnDao modeloDao;
	private UtilidadesCnn utilsCnn;
	private MultiLayerNetwork multilayerNetwork;
	
	// EN EL CONSTRUCTOR SE CARGA EL MODELO POR DEFECTO
	/*public SimpleNeuralModelManager() {
		utilsCnn = new UtilidadesCnn();
		logger.info("Intentando cargar el modelo establecido por defecto...");
		this.modelo = modeloDao.devuelveModeloPorDefecto();
		if (this.modelo != null) {
			URL resource = SimpleNeuralModelManager.class.getClassLoader().getResource(modelo.getPathToModel());
			String ruta = null;
			try {
				ruta = Paths.get(resource.toURI()).toString();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.multilayerNetwork = utilsCnn.cargaModeloH5(ruta);
			}
		// TODO Auto-generated constructor stub
	}
	*/
	@Transactional
	@Override
	public void nuevoModelo(ModeloRedConvolucional modelo) {
		this.modeloDao.nuevoModelo(modelo);
	}
	@Transactional
	@Override
	public void borrarModelo(ModeloRedConvolucional modelo) {
		this.modeloDao.borrarModelo(modelo);
	}
	@Transactional
	@Override
	public void editarModelo(ModeloRedConvolucional modelo) {
		logger.info("ModeloDAO: update de "+modelo.getIdModelo());
		//ModeloRedConvolucional model = this.modeloDao.getModelo(modelo.getIdModelo().toString());
		//model.setNombreModelo(modelo.getNombreModelo());
		//model.setDescripcion(modelo.getDescripcion());
		this.modeloDao.editarModelo(modelo);
	}
	@Transactional
	public ModeloRedConvolucional devuelveModelo(Integer idModelo) {
		return this.modeloDao.getModelo(idModelo);
	}
	
	public ModeloRedConvolucional getModelo() {
		return this.modelo;
	}
	
	@Transactional
	@Override
	public ModeloRedConvolucional devuelveModeloDefecto() {
		return this.modeloDao.devuelveModeloPorDefecto();
	}

	@Transactional
	@Override
	public List<ModeloRedConvolucional> getModelos(){
		return this.modeloDao.getModelosList();
	}
	
	public void setModelos(List<ModeloRedConvolucional> modelos) {
		this.modelos = modelos;
	}
	public void setMoldeloDao(ModeloRnDao modeloDao) {
		this.modeloDao = modeloDao;
	}
	@Override
	public void establecerModeloDefecto(ModeloRedConvolucional modelo) {
		modeloDao.establecerModeloPorDefecto(modelo);
	}
	
	public void setModelo(ModeloRedConvolucional modelo) {
		this.modelo = modelo;
	}
	public MultiLayerNetwork getMultilayerNetwork() {
		return this.multilayerNetwork;
	}
	public void setMultilayerNetwork(MultiLayerNetwork multilayerNetwork) {
		this.multilayerNetwork = multilayerNetwork;
	}
	
	
}
