package es.ubu.ecosystemIA.db;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import es.ubu.ecosystemIA.modelo.ModeloRedConvolucional;

public class JPAModeloRnDaoTest {
	
	private ApplicationContext context;
	private ModeloRnDao modeloDao;
	
	@Before
    public void setUp() throws Exception {
        context = new ClassPathXmlApplicationContext("classpath:test-context.xml");
        modeloDao = (ModeloRnDao) context.getBean("modeloRnDao");
    }

	@Test
    public void testGetModelosList() {
       
		List<ModeloRedConvolucional> modelos = modeloDao.getModelosList();
        assertEquals(modelos.size(), 2, 0);
	}
}
