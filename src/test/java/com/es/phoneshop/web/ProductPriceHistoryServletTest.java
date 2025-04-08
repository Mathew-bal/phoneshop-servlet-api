package com.es.phoneshop.web;

import com.es.phoneshop.dao.implementations.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductPriceHistoryServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;
    @Mock
    private ServletContext servletContext;
    @Mock
    ServletContextEvent servletContextEvent;

    private DemoDataServletContextListener demoDataServletContextListener = new DemoDataServletContextListener();

    private ProductPriceHistoryPageServlet servlet = new ProductPriceHistoryPageServlet();

    private ProductDao productDao;

    private static final long PRODUCT_TEST_ID = 3;

    @Before
    public void setup() throws ServletException {
        servlet.init(config);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getPathInfo()).thenReturn("/" + PRODUCT_TEST_ID);

        when(servletContext.getInitParameter("insertDemoData")).thenReturn("true");
        when(servletContextEvent.getServletContext()).thenReturn(servletContext);
        demoDataServletContextListener.contextInitialized(servletContextEvent);

        productDao = ArrayListProductDao.getInstance();
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("product"), eq(productDao.get(PRODUCT_TEST_ID)));
    }
}
