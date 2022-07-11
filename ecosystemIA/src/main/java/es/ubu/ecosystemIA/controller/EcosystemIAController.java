package es.ubu.ecosystemIA.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.ubu.ecosystemIA.db.JPAModeloRnDao;
import es.ubu.ecosystemIA.logica.NeuralNetworkManager;
import es.ubu.ecosystemIA.logica.SimpleNeuralModelManager;
import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;

@Controller
public class EcosystemIAController {

	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private NeuralNetworkManager modelManager;
	
	@RequestMapping(value="home.do")
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		String now = (new Date()).toString();
		logger.info("Retornando la vista de Bienvenida, hora: "+now);
		//pasamos el parámetro now a la pagina jsp
		return new ModelAndView("home", "now", now);
	}
	
	@RequestMapping(value="modelos.do")
	public ModelAndView mostrarModelos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		logger.info("Retornando la vista de lista de modelos");
		String now = (new Date()).toString();
		Map<String, Object> myModel = new HashMap<>();
		myModel.put("now", now);
        myModel.put("listadoModelos", this.modelManager.getModelos());
		//pasamos el parámetro now a la pagina jsp
		return new ModelAndView("modelos", "modeloMVC", myModel);
	}
	
	@GetMapping(value="editarModelo.do")
	public ModelAndView editarModelo(@RequestParam String idModelo) {
            
		logger.info("Consultando datos del modelo id"+idModelo);
		Map<String,Object> myModel = new HashMap<>();
		myModel.put("modelo", this.modelManager.getModelo(idModelo));
		//pasamos el parámetro now a la pagina jsp
		return new ModelAndView("editarModelo","modeloMVC", myModel);
	}
	
	public void setModelManager(NeuralNetworkManager modelManager) {
		this.modelManager = modelManager;
	}
}
