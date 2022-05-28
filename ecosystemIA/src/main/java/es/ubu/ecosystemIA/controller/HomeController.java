package es.ubu.ecosystemIA.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	protected final Log logger = LogFactory.getLog(getClass());
	@RequestMapping(value="/")
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		String now = (new Date()).toString();
		logger.info("Retornando la vista de Bienvenida, hora: "+now);
		//pasamos el parámetro now a la pagina jsp
		return new ModelAndView("home", "now", now);
	}
}
