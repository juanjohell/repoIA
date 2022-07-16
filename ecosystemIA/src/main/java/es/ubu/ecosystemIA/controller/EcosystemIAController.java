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
import es.ubu.ecosystemIA.logica.NeuralNetworkManager;
import es.ubu.ecosystemIA.logica.SimpleNeuralModelManager;
import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;


@Controller
@Transactional
public class EcosystemIAController {

	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private NeuralNetworkManager modelManager;

	@PersistenceContext
	private EntityManager em;
	
	@RequestMapping(value="home.do")
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		logger.info("mostrando la vista de Bienvenida");
		//redirigimos
		return new ModelAndView("home");
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
    public ModelAndView editarModelo(@Valid @ModelAttribute("modelo") ModeloRedConvolucional modelo, BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("error");
        }
        ModeloRedConvolucional redconv = this.em.find(ModeloRedConvolucional.class, modelo.getIdModelo());
        this.modelManager.editarModelo(redconv);
        return new ModelAndView("modelos");
    }
	
	@RequestMapping(value = "/editarModelo.do", method = RequestMethod.POST, params = "cancelar")
    public ModelAndView cancel(@Valid @ModelAttribute("modelo") final ModeloRedConvolucional modelo, final BindingResult result, final ModelMap model) {
        model.addAttribute("message", "Modificaci�n Cancelada");
  
		Map<String,Object> myModel = new HashMap<>();
		myModel.put("modelo", modelo);
		//pasamos el parámetro now a la pagina jsp
		return new ModelAndView("editarModelo","modeloMVC", myModel);
    }
	
	
	/*@GetMapping(value="editarModelo.do")
	public ModelAndView editarModelo(@RequestParam String idModelo, Model m) {
		logger.info("Consultando datos del modelo id "+idModelo);
		ModeloRedConvolucional redconv = this.modelManager.getModelo(idModelo);
		m.addAttribute("command",redconv);
		logger.info("modelo neuronal: "+redconv.getNombreModelo());
		return new ModelAndView("editarModelo", "modelo",m);
	}*/
	
	 /* It displays object data into form for the given id.   
     * The @PathVariable puts URL data into variable.*/    
    @RequestMapping(value="editarModelo.do")    
    public ModelAndView edit(@RequestParam String id, Model m){    
    	logger.info("Consultando datos del modelo id "+id);
		ModeloRedConvolucional redconv = this.modelManager.getModelo(id);   
        m.addAttribute("command",redconv);  
        return new ModelAndView("editarModelo", "modelo",m);    
    }    
	
	public void setModelManager(NeuralNetworkManager modelManager) {
		this.modelManager = modelManager;
	}
}
