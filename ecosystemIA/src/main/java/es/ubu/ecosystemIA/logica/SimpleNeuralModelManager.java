package es.ubu.ecosystemIA.logica;

import es.ubu.ecosystemIA.modelo.Imagen;
import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.autodiff.samediff.SameDiff;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import es.ubu.ecosystemIA.db.ModeloRnDao;

@Component
@Transactional
public class SimpleNeuralModelManager implements NeuralNetworkManager{
	
	public static final Integer SALIDA_UNIDIMENSIONAL = (int) 1; 
	public static final Integer SALIDA_MULTIDIMENSIONAL = (int) 2;
	public static final Integer CLASIFICACION = (int) 1; 
	public static final Integer DETECCION = (int) 2;
	public static final Integer FICHERO_PB = (int) 2;
	public static final Integer FICHERO_H5 = (int) 1;
	public static final Integer FICHERO_ON = (int) 3;
	
	protected final Log logger = LogFactory.getLog(getClass());
	private static final long serialVersionUID = -5392895280098494635L;
	private ModeloRedConvolucional modeloCargado;
	@Autowired
	private ModeloRnDao modeloDao;
	@Autowired
	private TipoAlmacenamientoManager managerAlmacenamiento;
	// para almacenar modelos secuenciales H5:
	private MultiLayerNetwork multilayerNetwork;
	private UtilidadesCnn utilsCnn;
	//para almacenar modelos con salida multiple H5:
	private ComputationGraph computationGraph;
	//para almacenar modelos Tipo TensorFlow PB (PROTOBUF):
	private SameDiff sameDiff;
	private Imagen imagenCargada;
	
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
	
	public ModeloRedConvolucional getModeloCargado() {
		return this.modeloCargado;
	}
	
	public void setModeloCargado(ModeloRedConvolucional modelo) {
		this.modeloCargado = modelo;
	}
	

	@Transactional
	@Override
	public List<ModeloRedConvolucional> getModelos(){
		return this.modeloDao.getModelosList();
	}
	
	public void setMoldeloDao(ModeloRnDao modeloDao) {
		this.modeloDao = modeloDao;
	}
	
	public MultiLayerNetwork getMultilayerNetwork() {
		return this.multilayerNetwork;
	}
	
	public void setModeloDl4j(ModeloRedConvolucional modelo) {
		if (modelo.getTipoFichero() == FICHERO_H5)
			if (modelo.getTipoSalida() == SALIDA_UNIDIMENSIONAL)
				this.setMultilayerNetwork(modelo);
			if (modelo.getTipoSalida() == SALIDA_MULTIDIMENSIONAL)
				this.setComputationGraph(modelo);
		if (modelo.getTipoFichero() == FICHERO_PB)
				this.setSameDiff(modelo);
	}
	public void setMultilayerNetwork(ModeloRedConvolucional modelo) {
		utilsCnn = new UtilidadesCnn();
		this.multilayerNetwork = utilsCnn.cargaModeloH5(modelo);
		logger.info("modelo cargado de fichero h5 MultiLayer");
	}
	public ComputationGraph getComputationGraph() {
		return this.computationGraph;
	}
	
	public void setComputationGraph(ModeloRedConvolucional modelo) {
		utilsCnn = new UtilidadesCnn();
		// ruta a sistema de ficheros local
		this.computationGraph = utilsCnn.cargaModeloRCNNH5(modelo);
		logger.info("modelo cargado de fichero h5 ComputationGraph");
	}
	
	
	public String devuelveArquitectura(ModeloRedConvolucional modelo) {
		String arquitectura ="no puede representarse la arquitectura";
		this.setComputationGraph(modelo);
		ComputationGraph cg = this.getComputationGraph();
		if (cg != null) arquitectura = cg.summary();
		return arquitectura;
	}
	
	public void setSameDiff(ModeloRedConvolucional modelo) {
		utilsCnn = new UtilidadesCnn();
		String path = modelo.getPathToModel();
		Integer tipoAlmacenamiento = modelo.getTipoAlmacenamiento();
		this.sameDiff = utilsCnn.cargaModeloRCNNPB(modelo);
		logger.info("modelo cargado de fichero h5");
	}
	
	public SameDiff getSameDiff() {
		return this.sameDiff;
	}
	
	public Imagen getImagenCargada() {
		return imagenCargada;
	}
	public void setImagenCargada(Imagen imagenCargada) {
		this.imagenCargada = imagenCargada;
	}
	
}
