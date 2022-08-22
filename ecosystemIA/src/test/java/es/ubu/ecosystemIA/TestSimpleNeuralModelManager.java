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
	

	
	private SimpleNeuralModelManager gestorModelos;
	private List<ModeloRedConvolucional> listaModelos;
	
	@Test
	public void testGetModelos() {
		List<ModeloRedConvolucional> listaModelos = gestorModelos.getModelos();
		assertNotNull(listaModelos);
		ModeloRedConvolucional modelo = listaModelos.get(0);
		assertNotNull(modelo);
		
	}

}
