package es.ubu.ecosystemIA.controller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bytedeco.opencv.opencv_core.Mat;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class FileUploadController extends FileBaseController{

	public static final String PARAM_LATESTPHOTO = "LATEST_PHOTO_PARAM";
	public static final String PARAM_RESULTADO = "RESULTADO";

    
	protected final Log logger = LogFactory.getLog(getClass());
	@Autowired
	private NeuralNetworkManager modelManager;
	@Autowired
	private CategoriaManager categoriaManager;
	private UtilidadesCnn utilsCnn;
	
	
	@GetMapping(value="cargarModelo.do")
	public ModelAndView probarModelo(@RequestParam String idModelo) {
		logger.info("Peticion Carga modelo ");
		ModeloRedConvolucional redconv = this.modelManager.devuelveModelo(Integer.valueOf(idModelo));
		logger.info("Cargando modelo "+redconv.getNombreModelo());
		this.modelManager.setModeloCargado(redconv);
		if (redconv.getTipoSalida().equals("texto"))
			this.modelManager.setMultilayerNetwork(redconv);
		if (redconv.getTipoSalida().equals("imagen"))
			this.modelManager.setComputationGraph(redconv);
		
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
            	Imagen imagenCargada = new Imagen(rootPath, this.modelManager.getModeloCargado().getModelImageWidth(), this.modelManager.getModeloCargado().getModelImageHeight(), this.modelManager.getModeloCargado().getImageChannels());
                imagenCargada.setImagenStream(is);
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
        return "usarModelo";
    }  
    
    // CARGA DE IMAGEN EN ENTRADA Y PREDICCION DEL MODELO
    @RequestMapping(value = "testCnnModel.do", method = RequestMethod.POST)
    public String testCnnModel(ModelMap model,
            HttpServletRequest request, 
            @RequestParam String imagen){
        
      // cargamos utilidades
    	utilsCnn = new UtilidadesCnn();
 		
 		// Recuperamos categorias del modelo
    	// TODO Si lista viene vacia
 		ArrayList<Categoria> categorias = (ArrayList<Categoria>) categoriaManager.getCategorias(this.modelManager.getModeloCargado().getIdModelo());
 		//modeloCnn.setCategorias(categorias);
 		
 		//leemos imagen
 		File file = Paths.get(URI.create(imagen).getPath()).toFile();
 		String rootPath = request.getSession().getServletContext().getRealPath("/") + "img"+File.separator + file.getName();
 		logger.info("ruta imagen: "+rootPath);
 		
 		//Configuramos los datos de entrada esperados por el modelo
 		Imagen imagenInput = new Imagen(rootPath, this.modelManager.getModeloCargado().getModelImageWidth(), this.modelManager.getModeloCargado().getModelImageHeight(), this.modelManager.getModeloCargado().getImageChannels());
		//REDIMENSIONAMOS LA IMAGEN POR SI VIENE EN OTRO TAMAÑO AL ESPERADO
 		imagenInput.setImg(utilsCnn.resizeImage(imagenInput.getImg(), this.modelManager.getModeloCargado().getModelImageWidth(), this.modelManager.getModeloCargado().getModelImageHeight()));
		
		// CONVERTIMOS IMAGEN EN matriz de entrada al modelo
		INDArray input = utilsCnn.devuelve_matriz_de_imagen_normalizada(imagenInput, this.modelManager.getModeloCargado());
		
		// TODO: Tratamiento segun tipo de modelo, categorias o clasificacion 
		
		// PRESENTAMOS IAMGEN AL MODELO Y RECOGEMOS RESPUESTA
		INDArray output = null;
        String resultado = "error";
        //En funcion del tipo de modelo obtenemos una imagen o un texto que nos da la categoria
        if (this.modelManager.getModeloCargado().getTipoSalida().equals("texto")) {
        	output = this.modelManager.getMultilayerNetwork().output(input);
        	logger.info("se espera texto como salida del modelo");
        	resultado = utilsCnn.devuelve_categoria(output, categorias);
        }
        if (this.modelManager.getModeloCargado().getTipoSalida().equals("imagen")) {
        	logger.info("se espera una imagen salida del modelo");
        	output = this.modelManager.getComputationGraph().outputSingle(input);
        	//Obtenemos imagen en formato matricial
        	try {
				Mat rawImagen = utilsCnn.Bytes2Mat(IOUtils.toByteArray(this.modelManager.getImagenCargada().getImagenStream()));
				utilsCnn.anotacionSimpleImagen(this.modelManager.getModeloCargado(), rawImagen, output);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	INDArray valores = output.getRow(0);
        	resultado = valores.toString();
        }
         
        //MOSTRAMOS RESULTADO EN EL JSP
        model.addAttribute(PARAM_RESULTADO, resultado);
        
        return "usarModelo";
    }
    public void setModelManager(SimpleNeuralModelManager modelManager) {
		this.modelManager = modelManager;
	}
    public void setCategoriaManager(SimpleCategoriaManager modelManager) {
		this.categoriaManager = categoriaManager;
	}
}
