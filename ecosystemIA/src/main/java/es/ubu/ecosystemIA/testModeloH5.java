package es.ubu.ecosystemIA;


import java.net.URL;
import java.nio.file.Paths;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.beans.factory.annotation.Autowired;

import es.ubu.ecosystemIA.modelo.Imagen;
import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;
import es.ubu.ecosystemIA.logica.NeuralNetworkManager;
import es.ubu.ecosystemIA.logica.TipoAlmacenamientoManager;
import es.ubu.ecosystemIA.logica.UtilidadesCnn;


public class testModeloH5 {
	
	private static final Integer IMAGE_CHANNELS = 3;
	private static final Integer IMAGE_MODEL_WIDTH = 32;
	private static final Integer IMAGE_MODEL_HEIGHT = 32;
	private static MultiLayerNetwork multilayerNetwork;
	@Autowired
	private static NeuralNetworkManager managerModelos;
	
	public static void main(String[] args) throws Exception
    {
		
		// cargamos utilidades
		UtilidadesCnn utils = new UtilidadesCnn();
		
		//Importamos el modelo de red convolucional
		URL resource = testModeloH5.class.getClassLoader().getResource("modelos\\modelo_sencillo_cifar10_100epochs.h5");
		String ruta = Paths.get(resource.toURI()).toString();
		ModeloRedConvolucional modeloCnn = new ModeloRedConvolucional();
	    
		//leemos imagen
		Imagen imagen = null;
		resource = testModeloH5.class.getClassLoader().getResource("datos\\coche.jpg");
		ruta = Paths.get(resource.toURI()).toString();
		imagen = new Imagen(ruta, IMAGE_MODEL_WIDTH, IMAGE_MODEL_HEIGHT, IMAGE_CHANNELS);
		//resize
		imagen.setImg(utils.resizeImage(imagen.getImg(), IMAGE_MODEL_WIDTH, IMAGE_MODEL_HEIGHT));
		
			try {
				multilayerNetwork = managerModelos.cargaModeloH5(modeloCnn);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		// matriz de entrada al modelo
		INDArray input = utils.devuelve_matriz_de_imagen_normalizada(imagen, modeloCnn,false);
		// recogemos salida del modelo
        INDArray output = multilayerNetwork.output(input);
        System.out.println("Test " + output);
    }
}
