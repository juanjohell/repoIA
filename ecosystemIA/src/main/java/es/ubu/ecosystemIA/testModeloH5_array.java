package es.ubu.ecosystemIA;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.conf.CNN2DFormat;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.workspace.ArrayType;
import org.deeplearning4j.nn.workspace.LayerWorkspaceMgr;
import org.deeplearning4j.util.ConvolutionUtils;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.shape.Shape;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.factory.Nd4j;

public class testModeloH5_array {

	
    private static final int IMAGE_CHANNELS = 3;
    private static final int IMAGE_HEIGHT = 150;
    private static final int IMAGE_WIDTH = 150;
    
    private String mInputPath;
    
    public testModeloH5_array(String modelFile, String inputDir)
    {
        mInputPath = inputDir;
    }
    
    public INDArray getInput() throws IOException
    {
    	// TODO : ver formato RGB , BGR...
    	// https://community.konduit.ai/t/imported-pretrained-keras-model-produces-wrong-output-in-dl4j/120
        String[] testImgs = new String[]{"coche.jpg","gato.jpg"};
        
        BufferedImage img = null;
        URL resource = null;
        String ruta = "";
        INDArray[] ret = new INDArray[testImgs.length];
        int count = 0;
        for (String testImg: testImgs)
        {
        	try 
    		{
    			resource = testModeloH5_array.class.getClassLoader().getResource("datos\\"+testImg);
				try {
					ruta = Paths.get(resource.toURI()).toString();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		    img = ImageIO.read(new File(ruta));
    		    //RESIZE:
    		    img = resizeImage(img, IMAGE_WIDTH, IMAGE_HEIGHT);
    		    
    		} 
    		catch (IOException e) 
    		{
    		    e.printStackTrace();
    		}
        	
            //File f = new File(mInputPath, testImg);
            NativeImageLoader loader = new NativeImageLoader(img.getHeight(), img.getWidth(), IMAGE_CHANNELS);
            INDArray image = loader.asMatrix(img);
            DataNormalization scalar = new ImagePreProcessingScaler(0, 1);
            scalar.transform(image);
            
            LayerWorkspaceMgr workspaceMgr;
            workspaceMgr = LayerWorkspaceMgr.noWorkspaces();
            
            image = cambia_formato(image,CNN2DFormat.NCHW,workspaceMgr, ArrayType.INPUT);
            
            ret[count] = image;
            count++;
        }
        // Concatenate the array along dimension 0.
        return(Nd4j.concat(0, ret));
    }
    
    public MultiLayerNetwork loadModel(String modelFile)
        throws IOException, 
        InvalidKerasConfigurationException,
        UnsupportedKerasConfigurationException
    {
        return(KerasModelImport.importKerasSequentialModelAndWeights(modelFile));
    }
    
    // Funcion que hace un resize de una imagen
    private static BufferedImage resizeImage(BufferedImage originalImage, Integer img_width, Integer img_height)
    {
    	int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB
                : originalImage.getType();
    	BufferedImage resizedImage = new BufferedImage(img_width, img_height, type);
    	Graphics2D g = resizedImage.createGraphics();
    	g.drawImage(originalImage, 0, 0, img_width, img_height, null);
    	g.dispose();

    	return resizedImage;
    }
    
    //FUNCION PARA CAMBIAR FORMATO (adaptada de reshape4dTo2d):
    public static INDArray cambia_formato(INDArray in, CNN2DFormat format, LayerWorkspaceMgr workspaceMgr, ArrayType type){
        
        long[] shape = in.shape();

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
    
    
    public static void main(String[] args) 
        throws Exception
    {
    	URL resource = testModeloH5_array.class.getClassLoader().getResource("modelos\\cifar10_entrenado_10epochs.h5");
    	String modelFile = Paths.get(resource.toURI()).toString();
    	resource = testModeloH5_array.class.getClassLoader().getResource("datos");
    	String inputDir = Paths.get(resource.toURI()).toString();
        
        testModeloH5_array loader = new testModeloH5_array(modelFile, inputDir);
        loader.getInput();
        MultiLayerNetwork model = loader.loadModel(modelFile);
        INDArray features = loader.getInput();
        long[] shape = features.shape();
        System.out.println("Feature shape=" + Arrays.toString(shape));
        
        
        
        shape = features.shape();
     
        System.out.println("Feature shape=" + Arrays.toString(shape));
        INDArray output = model.output(features);
        
        System.out.println("Prediction: " + output);
    }
}
