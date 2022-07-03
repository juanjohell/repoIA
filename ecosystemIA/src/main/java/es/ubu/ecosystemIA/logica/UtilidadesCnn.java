package es.ubu.ecosystemIA.logica;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.conf.CNN2DFormat;
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

import es.ubu.ecosystemIA.modelo.Imagen;
import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;

public class UtilidadesCnn {

	// MÉTODO que hace un resize de una imagen
    public static BufferedImage resizeImage(BufferedImage originalImage, Integer img_width, Integer img_height)
    {
    	int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB
                : originalImage.getType();
    	BufferedImage resizedImage = new BufferedImage(img_width, img_height, type);
    	Graphics2D g = resizedImage.createGraphics();
    	g.drawImage(originalImage, 0, 0, img_width, img_height, null);
    	g.dispose();

    	return resizedImage;
    }
    
    // MÉTODO PARA CAMBIAR FORMATO (adaptada de reshape4dTo2d):
    // Si el formato es el de canal primero, lo pone al final
    // Tensorflow toma el canal al final en la conformación de matrices
    // con las imágenes.
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
         // TODO Ver cuándo hay que realizar ésta transformación :
         entrada_a_CNN = cambia_formato_nchw_a_nhwc(entrada_a_CNN,CNN2DFormat.NCHW,workspaceMgr, ArrayType.INPUT);
    	
         return entrada_a_CNN;
    }
    
    public String devuelve_categoria(INDArray resultado, ModeloRedConvolucional modelo, ArrayList<String> categorias) {
    	String categoria = resultado.toString();
    	INDArray valores = resultado.getRow(0);
    	Number maximo = valores.maxNumber();
    	Integer indice = 0;
    	for (int i=0; i< (int) resultado.length(); i++) {
    		if (valores.getDouble(i) == maximo.doubleValue())
    			categoria = categorias.get(i);
    	}
    	return categoria;
    }
}
