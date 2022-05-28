package es.ubu.ecosystemIA;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import es.ubu.ecosystemIA.controller.HomeController;

public class HomeControllerTest {

	@Test
	public void testHandleRequestView() throws Exception {
		HomeController homeController = new HomeController();
		ModelAndView modelAndView = homeController.handleRequest(null, null);
		assertEquals("home", modelAndView.getViewName());
        assertNotNull(modelAndView.getModel());
        String nowValue = (String) modelAndView.getModel().get("now");
        assertNotNull(nowValue);
		
	}

}
