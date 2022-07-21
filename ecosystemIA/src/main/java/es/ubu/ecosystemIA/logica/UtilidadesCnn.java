package es.ubu.ecosystemIA.logica;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.conf.CNN2DFormat;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.workspace.ArrayType;
import org.deeplearning4j.nn.workspace.LayerWorkspaceMgr;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.shape.Shape;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;


import es.ubu.ecosystemIA.controller.FileUploadController;
import es.ubu.ecosystemIA.modelo.Categoria;
import es.ubu.ecosystemIA.modelo.Imagen;
import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;
import java.nio.file.Paths;
import java.net.URISyntaxException;
import java.net.URL;

public class UtilidadesCnn {
	protected final Log logger = LogFactory.getLog(getClass());
	private static final int GRID_WIDTH = 13;
    private static final int GRID_HEIGHT = 13;
    
    
	// METODO que hace un resize de una imagen
    public BufferedImage resizeImage(BufferedImage originalImage, Integer img_width, Integer img_height)
    {
    	int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB
                : originalImage.getType();
    	BufferedImage resizedImage = new BufferedImage(img_width, img_height, type);
    	Graphics2D g = resizedImage.createGraphics();
    	g.drawImage(originalImage, 0, 0, img_width, img_height, null);
    	g.dispose();
    	logger.info("imagen redimensionada");
    	return resizedImage;
    }
    
    // M√âTODO PARA CAMBIAR FORMATO (adaptada de reshape4dTo2d):
    // Si el formato es el de canal primero, lo pone al final
    // Tensorflow toma el canal al final en la conformaci√≥n de matrices
    // con las im√°genes.
    public static INDArray cambia_formato_nchw_a_nhwc(INDArray in, CNN2DFormat format, LayerWorkspaceMgr workspaceMgr, ArrayType type){
        
        long[] shape = in.shape();
        
        //Canal primero
        if(format == CNN2DFormat.NCHW){
            //Reshape: from [n,c,h,w] to [n,h,w,c]
            INDArray out = in.permute(0, 2, 3, 1);
            if (out.ordering() != 'c' || !Shape.strideDescendingCAscendingF(out))
                out = workspaceMgr.dup(type, out, 'c');
            return workspaceMgr.leverageTo(type, out.reshape('c', shape[0] , shape[2] , shape[3], shape[1]));
        } else {
            //Reshape: from [n,h,w,c] to [n,h,w,c]
            if (in.ordering() != 'c' || !Shape.strideDescendingCAscendingF(in))
                in = workspaceMgr.dup(type, in, 'c');
            return workspaceMgr.leverageTo(type, in.reshape('c', shape[0] , shape[1] , shape[2], shape[3]));
        }
    }
    
    // CARGA DE UN MODELO EN FORMATO KERAS H5
    public MultiLayerNetwork cargaModeloH5(String rutaFichero) {
    	
    	MultiLayerNetwork model = null;
    	try {
			model = KerasModelImport.importKerasSequentialModelAndWeights(rutaFichero,false);
		} catch (IOException | InvalidKerasConfigurationException | UnsupportedKerasConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model;
    }
    
 // CARGA DE UN MODELO EN FORMATO KERAS H5
    public ComputationGraph cargaModeloRCNNH5(String rutaFichero) {
    	
    	ComputationGraph model = null;
    	try {
			model = KerasModelImport.importKerasModelAndWeights(rutaFichero);
		} catch (IOException | InvalidKerasConfigurationException | UnsupportedKerasConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model;
    }
    
    // Dada una ruta de una imagen la carga y las dimensiones de la matriz de entrada
    // que acepta el modelo que estamos usando la convierte a matriz y normaliza los valores
    // En un rango [0..1] 
    public INDArray devuelve_matriz_de_imagen_normalizada(Imagen imagen, ModeloRedConvolucional model) {
    	BufferedImage img = null;
    	INDArray entrada_a_CNN = null;
    	try {
			img = ImageIO.read(new File(imagen.getRutaImagen()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 img = resizeImage(img, model.getModelImageWidth(), model.getModelImageHeight());
    	
    	 NativeImageLoader loader = new NativeImageLoader(img.getHeight(),img.getWidth(), imagen.getIMAGE_CHANNELS());
         try {
			entrada_a_CNN = loader.asMatrix(img);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         DataNormalization scalar = new ImagePreProcessingScaler(0, 1);
         scalar.transform(entrada_a_CNN);
         LayerWorkspaceMgr workspaceMgr;
         workspaceMgr = LayerWorkspaceMgr.noWorkspaces();
         // TODO Ver cu√°ndo hay que realizar √©sta transformaci√≥n :
         entrada_a_CNN = cambia_formato_nchw_a_nhwc(entrada_a_CNN,CNN2DFormat.NCHW,workspaceMgr, ArrayType.INPUT);
    	
         return entrada_a_CNN;
    }
    
    public String devuelve_categoria(INDArray resultado, ArrayList<Categoria> categorias) {
    	String categoria = resultado.toString();
    	INDArray valores = resultado.getRow(0);
    	Number maximo = valores.maxNumber();
    	Integer indice = 0;
    	for (int i=0; i< (int) resultado.length(); i++) {
    		if (valores.getDouble(i) == maximo.doubleValue())
    			categoria = categorias.get(i).getNombreCategoria();
    	}
    	return categoria;
    }
    
    public String devuelve_p‡th_real(String resource_path) {
    	URL resource = FileUploadController.class.getClassLoader().getResource(resource_path);
 		String ruta = null;
		try {
			ruta = Paths.get(resource.toURI()).toString();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("ruta real al fichero h5: "+ruta);
		return ruta;
    }
    
    // SI SE OBTIENE UNA UNICA COORDENADA
    // TODO: MULTIPLES COORDENADAS
    // model: modelo actualmente cargado
    // modelOutput : coordenadas obtenidas del modelo
    // ruta_escritura : RUTA + NOMBRE COMPLETO DEL FICHERO EN EL QUE SE VA A ESCRIBIR LA IMAGEN
    // ruta_lectura : RUTA + NOMBRE COMPLETO DE LA IMAGEN ORIGINAL
    public boolean anotacionSimpleImagen(ModeloRedConvolucional model, INDArray modelOutput, String ruta_lectura, String ruta_escritura) {
	    		// Se marca el objeto detectado con un rect·ngulo y una etiqueta
    			// Se calcula la posiciÛn del rect·ngulo respecto al tamaÒo de la imagen
    		boolean resultado = true;
    	
    		INDArray valores = modelOutput.getRow(0);
    		double x1 = valores.getDouble(0);
    		double y1 = valores.getDouble(1);
    		double x2 = valores.getDouble(2);
    		double y2 = valores.getDouble(3);
    		//TODO: Extraccion de etiquetas			
    		String label = "botella";
    		// calculo de coordenadas
    		int xmin = (int) Math.round(model.getModelImageWidth() * x1);
    		int ymin = (int) Math.round(model.getModelImageHeight() * y1);
    		int xmax = (int) Math.round(model.getModelImageWidth() * x2);
    		int ymax = (int) Math.round(model.getModelImageHeight() * y2);
    		logger.info("rectangulo...xmin="+Integer.toString(xmin));
    		logger.info("rectangulo...ymin="+Integer.toString(ymin));
    		logger.info("rectangulo...xmax="+Integer.toString(xmax));
    		logger.info("rectangulo...ymax="+Integer.toString(ymax));
    		// Se dibuja el rectangulo en la imagen
    		int width = Math.abs(xmax - xmin);
    		int height = Math.abs(ymax - ymin);
    		
    		File imageFile = new File(ruta_lectura);
            BufferedImage img = null;
            try {
                img = ImageIO.read(imageFile);
            } catch (IOException e1) {
                e1.printStackTrace();
                resultado = false;
            }
            //TODO: parametrizar color y ancho del borde para el usuario
            if (resultado) {
            	Graphics2D graph = img.createGraphics();
            	graph.setColor(Color.ORANGE);
            	//graph.fill(new Rectangle(100, 100, 100, 100));
            	graph.setStroke(new BasicStroke(2));
            	graph.drawRect(xmin, ymin, width, height);
            	//graph.fill(new Rectangle(xmin, ymin, width, height));
            	graph.dispose();

            	try {
            		ImageIO.write(img, "jpg", 
                        new File(ruta_escritura));
            	} catch (IOException e) {
            		e.printStackTrace();
            	}
            }
            return resultado;
    }
    
    // CONVERSIONES ENTRE IMAGENES EN BYTES Y EN FORMA
    // MATRICIAL
   /* public byte[] Mat2Bytes(Mat mat){
    	MatOfByte bytemat = new MatOfByte();
    	Imgcodecs.imencode("jpg", mat, bytemat);
    	 byte[] bytes = bytemat.toArray();
    	 return bytes;
    }

    public Mat Bytes2Mat(byte[] b){
    	Mat mat = Imgcodecs.imdecode(new MatOfByte(b), Imgcodecs.IMREAD_UNCHANGED);
    	return mat;
    }*/
}
