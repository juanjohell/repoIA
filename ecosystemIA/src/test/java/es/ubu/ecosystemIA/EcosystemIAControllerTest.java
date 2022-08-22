package es.ubu.ecosystemIA;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import es.ubu.ecosystemIA.controller.EcosystemIAController;
import es.ubu.ecosystemIA.logica.SimpleNeuralModelManager;

public class EcosystemIAControllerTest {

	@Test
	public void testHandleRequestView() throws Exception {
		EcosystemIAController homeController = new EcosystemIAController();
		homeController.setModelManager(new SimpleNeuralModelManager());
		ModelAndView modelAndView = homeController.handleRequest(null, null);
		assertEquals("home", modelAndView.getViewName());
        assertNotNull(modelAndView.getModel());
	}

}
