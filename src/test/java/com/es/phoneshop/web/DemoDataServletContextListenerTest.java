package com.es.phoneshop.web;

import com.es.phoneshop.dao.implementations.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DemoDataServletContextListenerTest {
    @Mock
    private ServletContext servletContext;
    @Mock
    ServletContextEvent servletContextEvent;

    private DemoDataServletContextListener servlet = new DemoDataServletContextListener();

    private ProductDao productDao;

    @Before
    public void setup() throws ServletException {
        when(servletContext.getInitParameter("insertDemoData")).thenReturn("true");
        when(servletContextEvent.getServletContext()).thenReturn(servletContext);
        productDao = ArrayListProductDao.getInstance();
    }

    @Test
    public void testDoGet() {
        servlet.contextInitialized(servletContextEvent);

        assertFalse(productDao.findProducts().isEmpty());
    }
}
