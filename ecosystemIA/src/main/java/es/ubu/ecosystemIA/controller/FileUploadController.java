package es.ubu.ecosystemIA.controller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.opencv.core.Mat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import es.ubu.ecosystemIA.testModeloH5;
import es.ubu.ecosystemIA.modelo.Categoria;
import es.ubu.ecosystemIA.modelo.Imagen;
import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;
import es.ubu.ecosystemIA.logica.CategoriaManager;
import es.ubu.ecosystemIA.logica.NeuralNetworkManager;
import es.ubu.ecosystemIA.logica.SimpleCategoriaManager;
import es.ubu.ecosystemIA.logica.SimpleNeuralModelManager;
import es.ubu.ecosystemIA.logica.UtilidadesCnn;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class FileUploadController extends FileBaseController{

	public static final String PARAM_LATESTPHOTO = "LATEST_PHOTO_PARAM";
	public static final String PARAM_RESULTADO = "RESULTADO";
	public static final String PARAM_ERROR = "ERROR";
	public static final Integer SALIDA_UNIDIMENSIONAL = (int) 1; 
	public static final Integer SALIDA_MULTIDIMENSIONAL = (int) 2;
	public static final Integer CLASIFICACION = (int) 1; 
	public static final Integer DETECCION = (int) 2;
    
	protected final Log logger = LogFactory.getLog(getClass());
	@Autowired
	private NeuralNetworkManager modelManager;
	@Autowired
	private CategoriaManager categoriaManager;
	private UtilidadesCnn utilsCnn;
	File serverfile;
	String latestUploadPhoto;
	
	// ACCIONES EN LA CARGA DEL MODELO
	@GetMapping(value="cargarModelo.do")
	public ModelAndView probarModelo(@RequestParam String idModelo) {
		logger.info("Peticion Carga modelo ");
		ModeloRedConvolucional redconv = this.modelManager.devuelveModelo(Integer.valueOf(idModelo));
		logger.info("Cargando modelo "+redconv.getNombreModelo());
		this.modelManager.setModeloCargado(redconv);
		this.modelManager.setModeloDl4j(redconv);
		Map<String,Object> myModel = new HashMap<>();
		myModel.put("nombreModelo", redconv.getNombreModelo());
		myModel.put("descripcion", redconv.getDescripcion());
		return new ModelAndView("usarModelo","modelo",myModel);
	}
	

	@RequestMapping(value="cargarModelo.do")
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		return new ModelAndView("usarModelo");
	}
	
    @RequestMapping(value = "uploadImage.do", method = RequestMethod.GET)
    public String uploadPhotoForm(ModelMap model, HttpServletRequest request){
        model.addAttribute(PARAM_BASE_URL, getBaseURL(request));
        return "usarModelo";
    }
     
    //BUSCAR IMAGEN Y MOSTRARLA
    @RequestMapping(value = "uploadimgctlr.do", method = RequestMethod.POST)
    public String uploadImageCtlr(ModelMap model,
            HttpServletRequest request, 
            @RequestParam MultipartFile file){
        latestUploadPhoto = "";
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        File dir = new File(rootPath + File.separator + "img");
        if (!dir.exists()) {
            dir.mkdirs();
        } 
        serverfile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
        latestUploadPhoto = file.getOriginalFilename();
        //write uploaded image to disk
        try {
            try (InputStream is = file.getInputStream(); BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverfile))) {
            	Imagen imagenCargada = new Imagen(rootPath, this.modelManager.getModeloCargado().getModelImageWidth(), this.modelManager.getModeloCargado().getModelImageHeight(), this.modelManager.getModeloCargado().getImageChannels()); 
                imagenCargada.setNombre(latestUploadPhoto);
                this.modelManager.setImagenCargada(imagenCargada);
            	int i;
                //
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
        model.addAttribute("modelo.nombreModelo", this.modelManager.getModeloCargado().getNombreModelo());
        model.addAttribute("modelo.descripcion", this.modelManager.getModeloCargado().getDescripcion());
        return "usarModelo";
    }  
    
    // CARGA DE IMAGEN EN ENTRADA Y PREDICCION DEL MODELO
    @RequestMapping(value = "testCnnModel.do", method = RequestMethod.POST)
    public String testCnnModel(ModelMap model,
            HttpServletRequest request, 
            @RequestParam String imagen,
            @RequestParam Integer rangoGrosor,
            @RequestParam String color){
        
      // cargamos utilidades
    	utilsCnn = new UtilidadesCnn();
 		
 		// Recuperamos categorias del modelo
    	// TODO Si lista viene vacia
 		ArrayList<Categoria> categorias = (ArrayList<Categoria>) categoriaManager.getCategorias(this.modelManager.getModeloCargado().getIdModelo());
 		//modeloCnn.setCategorias(categorias);
 		
 		//leemos imagen
 		String rootPath = request.getSession().getServletContext().getRealPath("/");
 		
 		File file = Paths.get(URI.create(imagen).getPath()).toFile();
 		String rutaImagenOriginal = rootPath + "img"+File.separator + file.getName();
 		logger.info("ruta imagen: "+rutaImagenOriginal);
 		
 		//generamos nombre que tendrá nueva imagen una vez anotada
 		int sufijo = (int) (Math.random() * 10000) + 1;
 		//TODO: almacenar extension en imagen
 		String nombre_imagen_anotada = "Imagen_anotada"+Integer.toString(sufijo)+".jpg";
 		String rutaImagenFinal = rootPath +"img"+File.separator +nombre_imagen_anotada;
 		
 		//Configuramos los datos de entrada esperados por el modelo
 		Imagen imagenInput = new Imagen(rutaImagenOriginal, this.modelManager.getModeloCargado().getModelImageWidth(), this.modelManager.getModeloCargado().getModelImageHeight(), this.modelManager.getModeloCargado().getImageChannels());
		//REDIMENSIONAMOS LA IMAGEN POR SI VIENE EN OTRO TAMAÑO AL ESPERADO
 		BufferedImage bi = null;
 		try {
			bi = ImageIO.read(new File(rutaImagenOriginal));
		} catch (IOException e1) {
			model.addAttribute(PARAM_ERROR, e1.getMessage());
	        return "error";
		}
 		imagenInput.setImg(utilsCnn.resizeImage(bi, this.modelManager.getModeloCargado().getModelImageWidth(), this.modelManager.getModeloCargado().getModelImageHeight()));
		
		// CONVERTIMOS IMAGEN EN matriz de entrada al modelo
		INDArray input = utilsCnn.devuelve_matriz_de_imagen_normalizada(imagenInput, this.modelManager.getModeloCargado());
		
		// TODO: Tratamiento segun tipo de modelo, categorias o clasificacion 
		
		// PRESENTAMOS IAMGEN AL MODELO Y RECOGEMOS RESPUESTA
		INDArray[] multi_output = null;
		INDArray output = null;
		Map<String, INDArray> outputMap;
        String resultado = "error";
      
       
        File dir = new File(rootPath + File.separator + "img");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        //En funcion del tipo de modelo obtenemos una imagen o un texto que nos da la categoria
        // PUEDE TRATARSE DE UN FICHERO H5 DE KERAS O UN FICHERO DE TENSORFLOW PB
        
        //TIPO_PREDICCION 1 ES CLASIFICACION DE IMAGENES
        if (this.modelManager.getModeloCargado().getTipoPrediccion().intValue() == CLASIFICACION) {
        	logger.info("CLASIFICACION DE IMAGENES");
        	// SI LA SALIDA ES UNIDIMENSIONAL (USAR MULTILAYERNETWORK)
        	if (this.modelManager.getModeloCargado().getTipoSalida().intValue() == SALIDA_UNIDIMENSIONAL) {
        		logger.info("SALIDA_UNIDIMENSIONAL");
        		output = this.modelManager.getMultilayerNetwork().output(input);
        		logger.info("output "+output.toString());
        		resultado = utilsCnn.devuelve_categoria(output, categorias);
        	}
        	// SI ES MULTIDIMENSIONAL (USAR COMPUTATIONGRAPH)
        	if (this.modelManager.getModeloCargado().getTipoSalida().intValue() == SALIDA_MULTIDIMENSIONAL) {
        		output = this.modelManager.getComputationGraph().outputSingle(input);
        		logger.info("SALIDA_MULTIDIMENSIONAL");
        		//TODO:
        		resultado = utilsCnn.devuelve_categorias_imagenet(output);
        		resultado = resultado.split(":")[1];
        		logger.info("resultado "+resultado.toString());
        	}
        	//ANOTAMOS CATEGORIA EN LA IMAGEN
			logger.info("imagen a anotar: "+ rutaImagenFinal.toString());
			if (utilsCnn.rotulaImagen(resultado, rutaImagenOriginal, rutaImagenFinal, color))
				latestUploadPhoto = nombre_imagen_anotada;
        }
        
        //TIPO_PREDICCION 2 ES DETECCION DE OBJETOS (BOUNDING BOXES)
        if (this.modelManager.getModeloCargado().getTipoPrediccion().intValue() == DETECCION) {
        	logger.info("DETECCION");
        	//TODO:  tipo de fichero 2 es TF
        	if (this.modelManager.getModeloCargado().getTipoFichero().intValue() == (int) 2)
        	{
        		logger.info("modelo tensorFlow");
        		outputMap = utilsCnn.predictPB(input, this.modelManager.getSameDiff());
        		logger.info("recogida salida");
        	//Obtenemos imagen en formato matricial
        	}
        	else
        		multi_output = this.modelManager.getComputationGraph().output(input);
			logger.info("imagen a anotar: "+ rutaImagenFinal.toString());
			if (utilsCnn.anotacionSimpleImagen(this.modelManager.getModeloCargado(), multi_output, rutaImagenOriginal, rutaImagenFinal, rangoGrosor, color))
			latestUploadPhoto = nombre_imagen_anotada;
			logger.info("obteniendo imagen cargada en formato matricial ");
        }			
        	if(output != null)
        		resultado = output.toString();
        	if(multi_output != null)
        		resultado = multi_output[0].toString();
        
       //url a la imagen 
        model.addAttribute(PARAM_BASE_URL, getBaseURL(request));
        //imagen
        model.addAttribute(PARAM_LATESTPHOTO, latestUploadPhoto);
        //nombre modelo
        model.addAttribute("modelo.nombreModelo", this.modelManager.getModeloCargado().getNombreModelo());
        model.addAttribute("modelo.descripcion", this.modelManager.getModeloCargado().getDescripcion());
        //MOSTRAMOS RESULTADO EN EL JSP
        model.addAttribute(PARAM_RESULTADO, resultado);
        return "usarModelo";
    }
    
    // CANCELAR Y VOLVER A MODELOS
    @RequestMapping(value = "/uploadimgctlr.do", method = RequestMethod.POST, params = "cancelar")
    public ModelAndView cancelUsarModelo(@Valid @ModelAttribute("modelo") ModeloRedConvolucional modelo, BindingResult result, final ModelMap model) {
        model.addAttribute("message", "Modificación Cancelada");
  
     // se regresa al listado de modelos
        Map<String, Object> myModel = new HashMap<>();
        myModel.put("listadoModelos", this.modelManager.getModelos());
		//pasamos el parÃ¡metro now a la pagina jsp
		return new ModelAndView("modelos", "modeloMVC", myModel);
    }
    
    
    public void setModelManager(SimpleNeuralModelManager modelManager) {
		this.modelManager = modelManager;
	}
    public void setCategoriaManager(SimpleCategoriaManager modelManager) {
		this.categoriaManager = categoriaManager;
	}
}
