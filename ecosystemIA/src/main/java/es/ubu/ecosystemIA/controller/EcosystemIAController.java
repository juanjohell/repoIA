package es.ubu.ecosystemIA.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import es.ubu.ecosystemIA.logica.NeuralNetworkManager;
import es.ubu.ecosystemIA.logica.SimpleNeuralModelManager;

@Controller
public class EcosystemIAController {

	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private SimpleNeuralModelManager modelManager;
	
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
		
		Map<String, Object> myModel = new HashMap<String, Object>();
        myModel.put("modelos", this.modelManager.getModelos());
		//pasamos el parámetro now a la pagina jsp
		return new ModelAndView("modelos", "modelos", myModel);
	}
	
	public void setModelManager(SimpleNeuralModelManager modelManager) {
		this.modelManager = modelManager;
	}
}
