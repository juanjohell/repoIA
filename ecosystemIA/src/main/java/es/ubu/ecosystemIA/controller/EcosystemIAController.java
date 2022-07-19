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
import org.springframework.web.servlet.ModelAndView;

import es.ubu.ecosystemIA.db.JPAModeloRnDao;
import es.ubu.ecosystemIA.logica.CategoriaManager;
import es.ubu.ecosystemIA.logica.NeuralNetworkManager;
import es.ubu.ecosystemIA.logica.SimpleNeuralModelManager;
import es.ubu.ecosystemIA.logica.UtilidadesCnn;
import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;


@Controller
@Transactional
public class EcosystemIAController {

	protected final Log logger = LogFactory.getLog(getClass());
	
	
	@Autowired
	private NeuralNetworkManager modelManager;
	@Autowired
	private CategoriaManager categoriaManager;
	@PersistenceContext
	private EntityManager em;
	
	@RequestMapping(value="/home.do")
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		logger.info("mostrando la vista de Bienvenida");
		//String modeloPrecargado = modelManager.getModelo().getNombreModelo();
		String modeloPrecargado = "Cifar10";
		Map<String, Object> myModel = new HashMap<>();
		myModel.put("modeloPrecargado", modeloPrecargado);
		
		//redirigimos
		return new ModelAndView("home","mensaje",myModel);
	}
	
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
	
	@Transactional
	@RequestMapping(value = "editarModelo.do", method = RequestMethod.POST, params = "grabar")
    public ModelAndView editarModelo(@RequestParam int idModelo, 
    		@RequestParam String nombreModelo,
    		@RequestParam String descripcion, 
    		HttpServletRequest request) {
        
        ModeloRedConvolucional modelo = this.modelManager.devuelveModelo(idModelo);
        modelo.setNombreModelo(nombreModelo);
        modelo.setDescripcion(descripcion);
        logger.info("Grabar cambios realizados en modelo "+ modelo.getNombreModelo());
        logger.info("Grabar cambios realizados en modelo con ID "+ modelo.getIdModelo().toString());
        this.modelManager.editarModelo(modelo);
        return new ModelAndView("modelos");
    }
	
	@RequestMapping(value = "/editarModelo.do", method = RequestMethod.POST, params = "cancelar")
    public ModelAndView cancel(@Valid @ModelAttribute("modelo") ModeloRedConvolucional modelo, BindingResult result, final ModelMap model) {
        model.addAttribute("message", "Modificaci�n Cancelada");
  
		Map<String,Object> myModel = new HashMap<>();
		myModel.put("modelo", modelo);
		//pasamos el parámetro now a la pagina jsp
		return new ModelAndView("editarModelo","modeloMVC", myModel);
    }
	
	
	@GetMapping(value="editarModelo.do")
	public ModelAndView editarModelo(@RequestParam String idModelo) {
		logger.info("Consultando datos del modelo id "+idModelo);
		ModeloRedConvolucional redconv = this.modelManager.devuelveModelo(Integer.valueOf(idModelo));
		ModelAndView model = new ModelAndView("editarModelo");
		model.addObject("modelo",redconv);
		logger.info("modelo neuronal: "+redconv.getNombreModelo());
		return model;
	}
	
	@GetMapping(value="verCategorias.do")
	public ModelAndView verCategorias(@RequestParam Integer idModelo) {
		logger.info("Consultando categorias del modelo id "+idModelo);
		Map<String, Object> myModel = new HashMap<>();
        myModel.put("listadoCategorias", this.categoriaManager.getCategorias(idModelo));
        return new ModelAndView("categorias", "modeloMVC", myModel);
	}
	
	public void setModelManager(SimpleNeuralModelManager modelManager) {
		this.modelManager = modelManager;
	}
}
