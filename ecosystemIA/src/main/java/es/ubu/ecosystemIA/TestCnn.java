package es.ubu.ecosystemIA;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.conf.CNN2DFormat;
import org.deeplearning4j.nn.conf.inputs.InputType;
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
import org.nd4j.linalg.factory.Nd4j;

// MultiDataSet handles multiple inputs and outputs.
public class TestCnn
{
    private static final int[] TARGET_SIZE = new int[]{150, 150};   // w, h
    private static final int IMAGE_CHANNEL = 3;
    private String mInputPath;
    
    public TestCnn(String modelFile, String inputDir)
    {
        mInputPath = inputDir;
    }
    
    public INDArray getInput() throws IOException
    {
        String[] testImgs = new String[]{"cat.1500.jpg","cat.1501.jpg","dog.1500.jpg","dog.1501.jpg"};
        
        INDArray[] ret = new INDArray[testImgs.length];
        int count = 0;
        for (String testImg: testImgs)
        {
            File f = new File(mInputPath, testImg);
            NativeImageLoader loader = new NativeImageLoader(TARGET_SIZE[1], TARGET_SIZE[0], IMAGE_CHANNEL);
            INDArray image = loader.asMatrix(f);
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
    
    public MultiLayerNetwork loadModel(String modelFile)
        throws IOException, 
        InvalidKerasConfigurationException,
        UnsupportedKerasConfigurationException
    {
        return(KerasModelImport.importKerasSequentialModelAndWeights(modelFile));
    }
    
    public static void main(String[] args) 
        throws Exception
    {
        
        URL resource = TestCnn.class.getClassLoader().getResource("modelos\\cnnSample1Model.h5");
        String modelFile = Paths.get(resource.toURI()).toString();
        
        resource = TestCnn.class.getClassLoader().getResource("datos");
        String inputDir = Paths.get(resource.toURI()).toString();
        
        TestCnn loader = new TestCnn(modelFile, inputDir);
        loader.getInput();
        
        MultiLayerNetwork model = loader.loadModel(modelFile);
        INDArray features = loader.getInput();
      
        long[] shape = features.shape();
        
        
        
        System.out.println("Feature shape=" + Arrays.toString(shape));
        INDArray output = model.output(features);
        
        System.out.println("Prediction: " + output);
    }
}
