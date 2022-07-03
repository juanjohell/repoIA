package es.ubu.ecosystemIA.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import es.ubu.ecosystemIA.testModeloH5;
import es.ubu.ecosystemIA.modelo.Imagen;
import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;
import es.ubu.ecosystemIA.logica.SimpleNeuralModelManager;
import es.ubu.ecosystemIA.logica.UtilidadesCnn;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class FileUploadController extends FileBaseController{

	public static final String PARAM_LATESTPHOTO = "LATEST_PHOTO_PARAM";
	public static final String PARAM_RESULTADO = "RESULTADO";
	private static final int IMAGE_CHANNELS = 3;
	private static final int IMAGE_MODEL_WIDTH = 32;
	private static final int IMAGE_MODEL_HEIGHT = 32;
    
	protected final Log logger = LogFactory.getLog(getClass());
	
	
	@RequestMapping(value="cargarModelo.do")
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		return new ModelAndView("uploadSimpleImage");
	}
	
    @RequestMapping(value = "uploadImage.do", method = RequestMethod.GET)
    public String uploadPhotoForm(ModelMap model, HttpServletRequest request){
        model.addAttribute(PARAM_BASE_URL, getBaseURL(request));
        return "uploadSimpleImage";
    }
     
    @RequestMapping(value = "uploadimgctlr.do", method = RequestMethod.POST)
    public String uploadImageCtlr(ModelMap model,
            HttpServletRequest request, 
            @RequestParam MultipartFile file){
        String latestUploadPhoto = "";
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        File dir = new File(rootPath + File.separator + "img");
        if (!dir.exists()) {
            dir.mkdirs();
        }
         
        File serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
        latestUploadPhoto = file.getOriginalFilename();
         
    //write uploaded image to disk
        try {
            try (InputStream is = file.getInputStream(); BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
                int i;
                while ((i = is.read()) != -1) {
                    stream.write(i);
                }
                stream.flush();
            }
        } catch (IOException e) {
            System.out.println("error : " + e.getMessage());
        }
         
    //send baseURL to jsp
        model.addAttribute(PARAM_BASE_URL, getBaseURL(request));
    //send photo name to jsp
        model.addAttribute(PARAM_LATESTPHOTO, latestUploadPhoto);
        return "uploadSimpleImage";
    }  
    
    // tratar de cargar testear imagen
    @RequestMapping(value = "testCnnModel.do", method = RequestMethod.POST)
    public String testCnnModel(ModelMap model,
            HttpServletRequest request, 
            @RequestParam String imagen){
        
       
      // cargamos utilidades
 		UtilidadesCnn utils = new UtilidadesCnn();
 		
 		//TODO : Debe pasarse por aplicación
 		//Importamos el modelo de red convolucional
 		URL resource = FileUploadController.class.getClassLoader().getResource("modelos\\cifar10_entrenado_10epochs.h5");
 		String ruta = null;
		try {
			ruta = Paths.get(resource.toURI()).toString();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleNeuralModelManager manager = new SimpleNeuralModelManager();
		
 		ModeloRedConvolucional modeloCnn = new ModeloRedConvolucional();
 	    //TODO: configuramos el modelo, esto debe hacerse por
 		// aplicación
 		// categorias de cifar10
 		ArrayList<String> categorias = new ArrayList<String>(
 	         Arrays.asList("Airplane", "Automobile", "Bird", "Cat", "Deer", "Dog", "Frog", "Horse", "Ship", "Truck"));
 		//modeloCnn.setCategorias(categorias);
 		
 		
 		//leemos imagen
 		File file = Paths.get(URI.create(imagen).getPath()).toFile();
 		String rootPath = request.getSession().getServletContext().getRealPath("/") + "img"+File.separator + file.getName();
    
 		logger.info("ruta imagen: "+rootPath);
    
 	
 		Imagen imagenInput = new Imagen(rootPath, IMAGE_MODEL_WIDTH, IMAGE_MODEL_HEIGHT, IMAGE_CHANNELS);
		//resize
 		imagenInput.setImg(utils.resizeImage(imagenInput.getImg(), IMAGE_MODEL_WIDTH, IMAGE_MODEL_HEIGHT));
		
		
		// matriz de entrada al modelo
		INDArray input = utils.devuelve_matriz_de_imagen_normalizada(imagenInput, modeloCnn);
		// recogemos salida del modelo
		
        INDArray output = utils.cargaModeloH5(ruta).output(input);
        
        String categoria = utils.devuelve_categoria(output, modeloCnn, categorias);
         
        
    //send result to jsp
        model.addAttribute(PARAM_RESULTADO, categoria);
        return "uploadSimpleImage";
    }

}
