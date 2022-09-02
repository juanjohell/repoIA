package es.ubu.ecosystemIA.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import es.ubu.ecosystemIA.db.JPAModeloRnDao;
import es.ubu.ecosystemIA.logica.CategoriaManager;
import es.ubu.ecosystemIA.logica.FicherosManager;
import es.ubu.ecosystemIA.logica.NeuralNetworkManager;
import es.ubu.ecosystemIA.logica.SimpleNeuralModelManager;
import es.ubu.ecosystemIA.logica.TipoAlmacenamientoManager;
import es.ubu.ecosystemIA.logica.TipoFicheroManager;
import es.ubu.ecosystemIA.logica.TipoPrediccionManager;
import es.ubu.ecosystemIA.logica.TipoSalidaManager;
import es.ubu.ecosystemIA.logica.UtilidadesCnn;
import es.ubu.ecosystemIA.modelo.Categoria;
import es.ubu.ecosystemIA.modelo.Ficheros;
import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;
import es.ubu.ecosystemIA.modelo.TipoAlmacenamiento;
import es.ubu.ecosystemIA.modelo.TipoFichero;
import es.ubu.ecosystemIA.modelo.TipoPrediccion;
import es.ubu.ecosystemIA.modelo.TipoSalida;

@Controller
@Transactional
public class EcosystemIAController {

	protected final Log logger = LogFactory.getLog(getClass());
	public static final String PARAM_ERROR = "ERROR";
	public static final Integer ALMACENAM_BASE_DATOS = (int) 1;
	public static final Integer ALMACENAM_CARPETA = (int) 2;
	public static final Integer ALMACENAM_URL = (int) 3;
	
	@Autowired
	private NeuralNetworkManager modelManager;
	@Autowired
	private FicherosManager ficherosManager;
	@Autowired
	private CategoriaManager categoriaManager;
	@Autowired
	private TipoAlmacenamientoManager tipoAlmacenamientoManager;
	@Autowired
	private TipoFicheroManager tipoFicheroManager;
	@Autowired
	private TipoPrediccionManager tipoPrediccionManager;
	@Autowired
	private TipoSalidaManager tipoSalidaManager;
	@PersistenceContext
	private EntityManager em;
	
	// P�GINA DE INICIO
	@RequestMapping(value="/home.do")
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		logger.info("mostrando la vista de Bienvenida");
		Map<String, Object> myModel = new HashMap<>();
		//redirigimos
		return new ModelAndView("home","mensaje",myModel);
	}
	
	// LISTADO DE MODELOS
	@Transactional
	@RequestMapping(value="modelos.do")
	public ModelAndView mostrarModelos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		logger.info("Retornando la vista de lista de modelos");
		Map<String, Object> myModel = new HashMap<>();
        myModel.put("listadoModelos", this.modelManager.getModelos());
		//pasamos el parámetro now a la pagina jsp
		return new ModelAndView("modelos", "modeloMVC", myModel);
	}
	
	// EDICION DE MODELOS /////////////////////////////////////////
	
	// MOSTRAR DATOS DE MODELO PARA EDICION EN FORMULARIO
		@GetMapping(value="editarModelo.do")
		public ModelAndView editarModelo(@RequestParam String idModelo) {
			logger.info("Consultando datos del modelo id "+idModelo);
			ModeloRedConvolucional redconv = this.modelManager.devuelveModelo(Integer.valueOf(idModelo));
			List<TipoAlmacenamiento> listadoTiposAlmacenamiento = this.tipoAlmacenamientoManager.getTiposAlmacenamiento();
			List<TipoFichero> listadoTiposFichero = this.tipoFicheroManager.getTiposFichero();
			List<TipoPrediccion> listadoTiposPrediccion = this.tipoPrediccionManager.getTiposPrediccion();
			List<TipoSalida> listadoTiposSalida = this.tipoSalidaManager.getTiposSalida();
			ModelAndView model = new ModelAndView("editarModelo");
			model.addObject("modelo",redconv);
			model.addObject("tiposAlm",listadoTiposAlmacenamiento);
			model.addObject("tiposFic",listadoTiposFichero);
			model.addObject("tiposPred",listadoTiposPrediccion);
			model.addObject("tiposSal",listadoTiposSalida);
			logger.info("modelo neuronal: "+redconv.getNombreModelo());
			return model;
		}
	
	// GRABACION EN LA EDICION DE UN MODELO DESDE FORMULARIO
	@Transactional
	@RequestMapping(value = "editarModelo.do", method = RequestMethod.POST, params = "grabar")
    public ModelAndView editarModelo(@RequestParam int idModelo, 
    		@RequestParam String nombreModelo,
    		@RequestParam String descripcion,
    		@RequestParam Integer modelImageHeight,
    		@RequestParam Integer modelImageWidth,
    		@RequestParam Integer imageChannels,
    		@RequestParam String pathToModel,
    		@RequestParam Integer tipoAlmacenamiento,
    		@RequestParam Integer tipoFichero,
    		@RequestParam Integer tipoPrediccion,
    		@RequestParam Integer tipoSalida,
    		@RequestParam MultipartFile ficheroModelo,
    		HttpServletRequest request) {
        
        ModeloRedConvolucional modelo = this.modelManager.devuelveModelo(idModelo);
        modelo.setNombreModelo(nombreModelo);
        modelo.setDescripcion(descripcion);
        modelo.setModelImageHeight(modelImageHeight);
        modelo.setModelImageWidth(modelImageWidth);
        modelo.setImageChannels(imageChannels);
        modelo.setTipoAlmacenamiento(tipoAlmacenamiento);
        modelo.setTipoFichero(tipoFichero);
        modelo.setTipoPrediccion(tipoPrediccion);
        modelo.setTipoSalida(tipoSalida);
        modelo.setPathToModel(pathToModel);
        
        logger.info("Grabar cambios realizados en modelo "+ modelo.getNombreModelo());
        logger.info("Grabar cambios realizados en modelo con ID "+ modelo.getIdModelo().toString());
        // edicion del modelo
        this.modelManager.editarModelo(modelo);
        logger.info("Grabado modelo "+ modelo.getIdModelo().toString());
        // Grabar fichero si procede:
        if (modelo.getTipoAlmacenamiento() == ALMACENAM_BASE_DATOS) {
        	byte[] ArrBytes = null;
        	try {
				ArrBytes = ficheroModelo.getBytes();
				Ficheros fichero = new Ficheros();
				fichero.setIdModelo(modelo.getIdModelo());
				fichero.setFichero(ArrBytes);
				this.ficherosManager.nuevoFichero(fichero);
				logger.info("Grabado fichero binario para modelo");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        // se regresa al listado de modelos
        Map<String, Object> myModel = new HashMap<>();
        myModel.put("listadoModelos", this.modelManager.getModelos());
		//pasamos el parámetro now a la pagina jsp
		return new ModelAndView("modelos", "modeloMVC", myModel);
    }
	
	// CANCELAR EDICION DE UN MODELO
	@RequestMapping(value = "/editarModelo.do", method = RequestMethod.POST, params = "cancelar")
    public ModelAndView cancelEditar(@Valid @ModelAttribute("modelo") ModeloRedConvolucional modelo, BindingResult result, final ModelMap model) {
        model.addAttribute("message", "Modificaci�n Cancelada");
     // se regresa al listado de modelos
        Map<String, Object> myModel = new HashMap<>();
        myModel.put("listadoModelos", this.modelManager.getModelos());
		//pasamos el parámetro now a la pagina jsp
		return new ModelAndView("modelos", "modeloMVC", myModel);
    }
	
	
	// NUEVO MODELO /////////////////////////////////////////
	
	// MOSTRAR FORMULARIO DE NUEVO MODELO
		@GetMapping(value="nuevoModelo.do")
		public ModelAndView nuevoModelo() {
			logger.info("Nuevo modelo");
			ModelAndView model = new ModelAndView("nuevoModelo");
			ModeloRedConvolucional modelo = new ModeloRedConvolucional();
			List<TipoAlmacenamiento> listadoTiposAlmacenamiento = this.tipoAlmacenamientoManager.getTiposAlmacenamiento();
			List<TipoFichero> listadoTiposFichero = this.tipoFicheroManager.getTiposFichero();
			List<TipoPrediccion> listadoTiposPrediccion = this.tipoPrediccionManager.getTiposPrediccion();
			List<TipoSalida> listadoTiposSalida = this.tipoSalidaManager.getTiposSalida();
			model.addObject("modelo",modelo);
			model.addObject("tiposAlm",listadoTiposAlmacenamiento);
			model.addObject("tiposFic",listadoTiposFichero);
			model.addObject("tiposPred",listadoTiposPrediccion);
			model.addObject("tiposSal",listadoTiposSalida);
			return model;
		}
		
		// FORMULARIO CREACION DE NUEVO MODELO GRABAR DATOS
		@Transactional
		@RequestMapping(value = "nuevoModelo.do", method = RequestMethod.POST, params = "grabar")
	    public ModelAndView grabarNuevoModelo(
	    		@RequestParam String nombreModelo,
	    		@RequestParam String descripcion,
	    		@RequestParam Integer modelImageHeight,
	    		@RequestParam Integer modelImageWidth,
	    		@RequestParam Integer imageChannels,
	    		@RequestParam String pathToModel,
	    		@RequestParam Integer tipoAlmacenamiento,
	    		@RequestParam Integer tipoFichero,
	    		@RequestParam Integer tipoPrediccion,
	    		@RequestParam Integer tipoSalida,
	    		HttpServletRequest request) {
	        
	        ModeloRedConvolucional modelo = new ModeloRedConvolucional();
	        modelo.setNombreModelo(nombreModelo);
	        modelo.setDescripcion(descripcion);
	        modelo.setModelImageHeight(modelImageHeight);
	        modelo.setModelImageWidth(modelImageWidth);
	        modelo.setImageChannels(imageChannels);
	        modelo.setPathToModel(pathToModel);
	        modelo.setTipoAlmacenamiento(tipoAlmacenamiento);
	        modelo.setTipoFichero(tipoFichero);
	        modelo.setTipoPrediccion(tipoPrediccion);
	        modelo.setTipoSalida(tipoSalida);
	        logger.info("Grabar nuevo modelo "+ modelo.getNombreModelo());
	        // alta
	        this.modelManager.nuevoModelo(modelo);
	        // se regresa al listado de modelos
	        Map<String, Object> myModel = new HashMap<>();
	        myModel.put("listadoModelos", this.modelManager.getModelos());
			//pasamos el parámetro now a la pagina jsp
			return new ModelAndView("modelos", "modeloMVC", myModel);
	    }
		
		// FORMULARIO CREACION DE NUEVO MODELO CANCELAR
		@RequestMapping(value = "/nuevoModelo.do", method = RequestMethod.POST, params = "cancelar")
	    public ModelAndView cancelNuevo(@Valid @ModelAttribute("modelo") ModeloRedConvolucional modelo, BindingResult result, final ModelMap model) {
	        model.addAttribute("message", "Nuevo modelo Cancelada");
	  
	     // se regresa al listado de modelos
	        Map<String, Object> myModel = new HashMap<>();
	        myModel.put("listadoModelos", this.modelManager.getModelos());
			//pasamos el parámetro now a la pagina jsp
			return new ModelAndView("modelos", "modeloMVC", myModel);
	    }
		
	/////////////  VER DATOS DE MODELO
	
	// MOSTRAR FORMULARIO PARA VISUALIZAR DATOS DE MODELO:
	@GetMapping(value="verModelo.do")
	public ModelAndView verModelo(@RequestParam String idModelo) {
		logger.info("Consultando datos del modelo id "+idModelo);
		ModeloRedConvolucional redconv = this.modelManager.devuelveModelo(Integer.valueOf(idModelo));
		ModelAndView model = new ModelAndView("verModelo");
		model.addObject("modelo",redconv);
		logger.info("modelo neuronal: "+redconv.getNombreModelo());
		return model;
	}
	
	// FORMULARIO VER DATOS DE MODELO DE NUEVO MODELO CANCELAR
	@RequestMapping(value = "/verModelo.do", method = RequestMethod.POST, params = "cancelar")
	public ModelAndView cancelVerModelo(@Valid @ModelAttribute("modelo") ModeloRedConvolucional modelo, BindingResult result, final ModelMap model) {
		model.addAttribute("message", "Nuevo modelo Cancelada");
		// se regresa al listado de modelos
		Map<String, Object> myModel = new HashMap<>();
		myModel.put("listadoModelos", this.modelManager.getModelos());
		//pasamos el parámetro now a la pagina jsp
		return new ModelAndView("modelos", "modeloMVC", myModel);
	}
			
	
	// MOSTRAR ARQUITECTURA
	@GetMapping(value="verEstructura.do")
	public ModelAndView verArquitectura(@RequestParam String idModelo) {
		logger.info("Consultando arquitectura del modelo id "+idModelo);
		ModeloRedConvolucional redconv = this.modelManager.devuelveModelo(Integer.valueOf(idModelo));
		String arquitectura = this.modelManager.devuelveArquitectura(redconv);
		ModelAndView model = new ModelAndView("verEstructura");
		model.addObject("modelo",redconv);
		model.addObject("estructura", arquitectura);
		logger.info("arquitectura de : "+redconv.getNombreModelo());
		return model;
	}
	
	// MOSTRAR ARQUITECTURA CANCELAR
			@RequestMapping(value = "/verEstructura.do", method = RequestMethod.POST, params = "cancelar")
		    public ModelAndView cancelVerArquitectura(@Valid @ModelAttribute("modelo") ModeloRedConvolucional modelo, BindingResult result, final ModelMap model) {
		        model.addAttribute("message", "ver estructura Cancelada");
		     // se regresa al listado de modelos
		        Map<String, Object> myModel = new HashMap<>();
		        myModel.put("listadoModelos", this.modelManager.getModelos());
				//pasamos el parámetro now a la pagina jsp
				return new ModelAndView("modelos", "modeloMVC", myModel);
		    }
			
			
	// LISTADO DE MODELOS /////////////////////////////////////////
	
	// ELIMINAR MODELO DESDE LISTADO:
	@GetMapping(value="eliminarModelo.do")
	public ModelAndView eliminarModelo(@RequestParam String idModelo) {
		logger.info("Consultando datos del modelo id "+idModelo);
		ModeloRedConvolucional redconv = this.modelManager.devuelveModelo(Integer.valueOf(idModelo));
        logger.info("Eliminar modelo "+ redconv.getNombreModelo());
        
        // eliminar modelo
        this.modelManager.borrarModelo(redconv);
        // se regresa al listado de modelos
        Map<String, Object> myModel = new HashMap<>();
        myModel.put("listadoModelos", this.modelManager.getModelos());
		//pasamos el parámetro now a la pagina jsp
		return new ModelAndView("modelos", "modeloMVC", myModel);
	}
	
	
	//// CATEGORIAS :
	
	// LISTADO CATEGORIAS
	@GetMapping(value="verCategorias.do")
	public ModelAndView verCategorias(@RequestParam Integer idModelo) {
		logger.info("Consultando categorias del modelo id "+idModelo);
		Map<String, Object> myModel = new HashMap<>();
		ModeloRedConvolucional redconv = this.modelManager.devuelveModelo(Integer.valueOf(idModelo));
        myModel.put("listadoCategorias", this.categoriaManager.getCategorias(idModelo));
        myModel.put("modelo", redconv);
        return new ModelAndView("categorias", "modeloMVC", myModel);
	}
	
	//cancelar ver categoria
		@RequestMapping(value = "/verCategorias.do", method = RequestMethod.POST, params = "cancelar")
	    public ModelAndView cancelVerCategoria(@Valid @ModelAttribute("modelo") ModeloRedConvolucional modelo, BindingResult result, final ModelMap model) {
	        model.addAttribute("message", "Modificaci�n Cancelada");
	     // se regresa al listado de modelos
	        Map<String, Object> myModel = new HashMap<>();
	        myModel.put("listadoModelos", this.modelManager.getModelos());
			//pasamos el parámetro now a la pagina jsp
			return new ModelAndView("modelos", "modeloMVC", myModel);
	    }
	
		//cancelar edicion categoria
		@RequestMapping(value = "/editarCategoria.do", method = RequestMethod.POST, params = "cancelar")
	    public ModelAndView cancelEditarCategoria(@Valid @ModelAttribute("modelo") ModeloRedConvolucional modelo, BindingResult result, final ModelMap model) {
	        model.addAttribute("message", "Modificaci�n Cancelada");
	     // se regresa al listado de modelos
	        Map<String, Object> myModel = new HashMap<>();
	        myModel.put("listadoModelos", this.modelManager.getModelos());
			//pasamos el parámetro now a la pagina jsp
			return new ModelAndView("modelos", "modeloMVC", myModel);
	    }
		
		@GetMapping(value="editarCategoria.do")
		public ModelAndView editarCategoria(@RequestParam String idModelo, @RequestParam String idOrden ) {
			logger.info("Consultando datos de la categor�a idModelo: "+idModelo+ " idOrden: "+idOrden);
			ModeloRedConvolucional redconv = this.modelManager.devuelveModelo(Integer.valueOf(idModelo));
			Categoria categoria = this.categoriaManager.devuelveCategoria(Integer.valueOf(idModelo), Integer.valueOf(idOrden));
			ModelAndView model = new ModelAndView("editarCategoria");
			model.addObject("modelo",redconv);
			model.addObject("categoria",categoria);
			logger.info("categoria: "+redconv.getNombreModelo()+"-"+categoria.getNombreCategoria());
			return model;
		}
		
		@Transactional
		@RequestMapping(value = "editarCategoria.do", method = RequestMethod.POST, params = "grabar")
	    public ModelAndView editarCategoria(@RequestParam int idModelo, 
	    		@RequestParam int idOrden,
	    		@RequestParam String nombreCategoria,
	    		HttpServletRequest request) {
	        
	        Categoria categoria = this.categoriaManager.devuelveCategoria(idModelo, idOrden);
	        categoria.setIdOrden(idOrden);
	        categoria.setIdModelo(idModelo);
	        categoria.setNombreCategoria(nombreCategoria);
	        logger.info("Grabar cambios realizados en categoria "+ categoria.getNombreCategoria());
	        
	        // edicion de la categoria
	        this.categoriaManager.editarCategoria(categoria);
	        // se regresa al listado de categorias
	        Map<String, Object> myModel = new HashMap<>();
	        myModel.put("modelo", this.modelManager.devuelveModelo(idModelo));
	        myModel.put("listadoCategorias", this.categoriaManager.getCategorias(idModelo));
			//pasamos el parámetro now a la pagina jsp
			return new ModelAndView("categorias", "modeloMVC", myModel);
	    }
		
	
	public void setModelManager(SimpleNeuralModelManager modelManager) {
		this.modelManager = modelManager;
	}
}
