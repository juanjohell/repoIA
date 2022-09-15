package es.ubu.ecosystemIA.logica;

import es.ubu.ecosystemIA.modelo.Ficheros;
import es.ubu.ecosystemIA.modelo.Imagen;
import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.persistence.Lob;

import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.autodiff.samediff.SameDiff;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
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
	public static final Integer ALMACENAM_BASE_DATOS = (int) 1;
	public static final Integer ALMACENAM_CARPETA = (int) 2;
	public static final Integer ALMACENAM_URL = (int) 3;
	
	protected final Log logger = LogFactory.getLog(getClass());
	private static final long serialVersionUID = -5392895280098494635L;
	private ModeloRedConvolucional modeloCargado;
	@Autowired
	private ModeloRnDao modeloDao;
	@Autowired
	private TipoAlmacenamientoManager managerAlmacenamiento;
	@Autowired 
	private FicherosManager managerFicheros;
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
		this.multilayerNetwork = this.cargaModeloH5(modelo);
		logger.info("modelo cargado de fichero h5 MultiLayer");
	}
	public ComputationGraph getComputationGraph() {
		return this.computationGraph;
	}
	
	public void setComputationGraph(ModeloRedConvolucional modelo) {
		utilsCnn = new UtilidadesCnn();
		// ruta a sistema de ficheros local
		this.computationGraph = this.cargaModeloRCNNH5(modelo);
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
		this.sameDiff = this.cargaModeloRCNNPB(modelo);
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
	
	// DEVUELVE INSTANCIA DE MULTILAYERNETWORK RECOGIENDO EL FICHERO SEGUN
    // EL LUGAR DE ALMACENAMIENTO
    public MultiLayerNetwork cargaModeloH5(ModeloRedConvolucional model) {
    	MultiLayerNetwork mlModel = null;
    	URL url=null;
    	InputStream is=null;
    	URLConnection connection=null;
    	Blob blobFichero = null;
    	Ficheros fichero = new Ficheros();
    	InputStream isFichero = null;
    	
    	logger.info("tipo_almacenamiento :" + model.getTipoAlmacenamiento().toString());
    	// ALMACENADO EN BASE DE DATOS:
    	if (model.getTipoAlmacenamiento() == ALMACENAM_BASE_DATOS) {
    		logger.info("BASE DE DATOS");
    		fichero = managerFicheros.devuelveFichero(model.getIdModelo());
    		blobFichero = fichero.getFichero();
    		try {
				isFichero = blobFichero.getBinaryStream();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		try {
				mlModel = KerasModelImport.importKerasSequentialModelAndWeights(isFichero);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKerasConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedKerasConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	//ALMACENAMIENTO EN FICHERO LOCAL (RESOURCES)
    	if (model.getTipoAlmacenamiento() == ALMACENAM_CARPETA) {
    		logger.info("FICHERO LOCAL");
			try {
				mlModel = KerasModelImport.importKerasSequentialModelAndWeights(utilsCnn.devuelve_path_real(model.getPathToModel()),false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKerasConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedKerasConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		//ALMACENAMIENTO EN FICHERO REMOTO (URL GITHUB)
		if (model.getTipoAlmacenamiento() == ALMACENAM_URL) {
			logger.info("FICHERO REMOTO");
			try {
				url = new URL(model.getPathToModel());
				try {
					isFichero = new BufferedInputStream(url.openStream());
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				logger.info("abierta conexion");
				logger.info("leido contenido remoto");
				try {
					mlModel = KerasModelImport.importKerasSequentialModelAndWeights(isFichero);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidKerasConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedKerasConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				logger.info("cargado modelo");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mlModel;
    }
    
     // CARGA DE UN MODELO COMPUTATIONGRAPH MULTICAPA ENTRADA/SALIDA EN FORMATO KERAS H5
    // EN FUNCION DE DONDE ESTÉ ALMACENADO
   public ComputationGraph cargaModeloRCNNH5(ModeloRedConvolucional model) {
       	
       	ComputationGraph cgModel = null;
       	URL url=null;
       	InputStream is=null;
       	URLConnection connection=null;
       	InputStream isFichero = null;
       	Ficheros fichero = new Ficheros();
       	
       	// ALMACENADO EN BASE DE DATOS:
       	if (model.getTipoAlmacenamiento() == ALMACENAM_BASE_DATOS) {
       		fichero = managerFicheros.devuelveFichero(model.getIdModelo());
       		try {
				isFichero = fichero.getFichero().getBinaryStream();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
       		try {
   				cgModel = KerasModelImport.importKerasModelAndWeights(isFichero);
   			} catch (IOException e) {
   				// TODO Auto-generated catch block
   				e.printStackTrace();
   			} catch (UnsupportedKerasConfigurationException e) {
   				// TODO Auto-generated catch block
   				e.printStackTrace();
   			} catch (InvalidKerasConfigurationException e) {
   				// TODO Auto-generated catch block
   				e.printStackTrace();
   			}
       	}
       	//ALMACENAMIENTO EN FICHERO LOCAL (RESOURCES)
       	if (model.getTipoAlmacenamiento() == ALMACENAM_CARPETA) {
       		try {
       			cgModel = KerasModelImport.importKerasModelAndWeights(utilsCnn.devuelve_path_real(model.getPathToModel()));
       		} catch (IOException | InvalidKerasConfigurationException | UnsupportedKerasConfigurationException e) {
       			// TODO Auto-generated catch block
       			e.printStackTrace();
       		}
       	}
       	//ALMACENAMIENTO EN FICHERO REMOTO (URL GITHUB)
       	if (model.getTipoAlmacenamiento() == ALMACENAM_URL) {
       		try {
       			url = new URL(model.getPathToModel());
       			try {
       				connection = url.openConnection();
       			} catch (IOException e) {
       						// TODO Auto-generated catch block
       						e.printStackTrace();
       				}
       			try {
       				isFichero = connection.getInputStream();
       			} catch (IOException e) {
       					// TODO Auto-generated catch block
       						e.printStackTrace();
       				}
       			try {
       				cgModel = KerasModelImport.importKerasModelAndWeights(isFichero);	
       			} catch (IOException e) {
   					// TODO Auto-generated catch block
   					e.printStackTrace();
   				} catch (InvalidKerasConfigurationException e) {
   					// TODO Auto-generated catch block
   					e.printStackTrace();
   				} catch (UnsupportedKerasConfigurationException e) {
   					// TODO Auto-generated catch block
   					e.printStackTrace();
   				}
   			} catch (MalformedURLException e) {
   				// TODO Auto-generated catch block
   				e.printStackTrace();
   		    }
   		
       	}
   		return cgModel;
       }
       
    // CARGA DE UN MODELO EN FORMATO TENSORFLOW PROTOBUF (PB)
       // TODO: DIFERENCIAR POR LUGARES DE ALMACENAMIENTO
    public SameDiff cargaModeloRCNNPB(ModeloRedConvolucional model) {
          	SameDiff sdModel = null;
          	URL url=null;
          	InputStream is=null;
          	URLConnection connection=null;
          	InputStream isFichero = null;
          	Ficheros fichero = new Ficheros();
          	// ALMACENADO EN BASE DE DATOS:
          	if (model.getTipoAlmacenamiento() == ALMACENAM_BASE_DATOS) {
          		fichero = managerFicheros.devuelveFichero(model.getIdModelo());
          		try {
					isFichero = fichero.getFichero().getBinaryStream();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
          		sdModel = SameDiff.importFrozenTF(isFichero);
          	}
          	
          	//ALMACENAMIENTO EN FICHERO LOCAL (RESOURCES)
          	if (model.getTipoAlmacenamiento() == ALMACENAM_CARPETA) {
          			File modeloPb = new File((utilsCnn.devuelve_path_real(model.getPathToModel())));
          			sdModel = SameDiff.importFrozenTF(modeloPb);
          			logger.info("cargando modelo GRAPH "+model.getPathToModel());
          	}		
             
          	//ALMACENAMIENTO EN FICHERO REMOTO (URL GITHUB)
          	if (model.getTipoAlmacenamiento() == ALMACENAM_URL) {
          		try {
          			url = new URL(model.getPathToModel());
          			try {
          				connection = url.openConnection();
          			} catch (IOException e) {
          						// TODO Auto-generated catch block
          						e.printStackTrace();
          				}
          			try {
          				isFichero = connection.getInputStream();
          			} catch (IOException e) {
          					// TODO Auto-generated catch block
          						e.printStackTrace();
          				}
          				sdModel = SameDiff.importFrozenTF(isFichero);	
          			
      			} catch (MalformedURLException e) {
      				// TODO Auto-generated catch block
      				e.printStackTrace();
      		    }
          	}
      		logger.info("Cargado modelo GRAPH");
      		return sdModel;
          } 
	
}
