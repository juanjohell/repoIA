package es.ubu.ecosystemIA;

import static org.junit.Assert.*;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;
import es.ubu.ecosystemIA.controller.FileUploadController;
import es.ubu.ecosystemIA.logica.SimpleNeuralModelManager;
import es.ubu.ecosystemIA.logica.UtilidadesCnn;


public class TestSimpleNeuralModelManager {
	
	private static final int IMAGE_CHANNELS = 3;
	private static final int IMAGE_MODEL_WIDTH = 32;
	private static final int IMAGE_MODEL_HEIGHT = 32;
	
	private SimpleNeuralModelManager gestorModelos;
	private List<ModeloRedConvolucional> listaModelos;
	

	@Test
	public void testGetModelos() {
		List<ModeloRedConvolucional> listaModelos = gestorModelos.getModelos();
		assertNotNull(listaModelos);
		ModeloRedConvolucional modelo = listaModelos.get(0);
		assertNotNull(modelo);
		
	}
	
	@Test
	public void testCargaModelo() {
		
		//Importamos el modelo de red convolucional
 		URL resource = TestSimpleNeuralModelManager.class.getClassLoader().getResource("modelos\\cifar10_entrenado_10epochs.h5");
 		String ruta = null;
		try {
			ruta = Paths.get(resource.toURI()).toString();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//ModeloRedConvolucional modeloCifar10 = new ModeloRedConvolucional();
		//modeloCifar10.cargarModelo(ruta);
		//assertNotNull(modeloCifar10.getMultilayerNetwork());
		assertNotNull(ruta);
	}

}
