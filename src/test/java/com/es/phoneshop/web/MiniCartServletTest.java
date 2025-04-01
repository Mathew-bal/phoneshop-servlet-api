package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.cartservice.DefaultCartService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
public class MiniCartServletTest {
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
    @Mock
    HttpSession session;

    private DemoDataServletContextListener demoDataServletContextListener = new DemoDataServletContextListener();

    private MiniCartServlet servlet = new MiniCartServlet();

    private Cart cart;

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        cart = new Cart();

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        when(servletContext.getInitParameter("insertDemoData")).thenReturn("true");
        when(servletContextEvent.getServletContext()).thenReturn(servletContext);
        demoDataServletContextListener.contextInitialized(servletContextEvent);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(eq(DefaultCartService.class.getName() + ".cart"))).thenReturn(cart);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(requestDispatcher).include(request, response);
        verify(request).setAttribute(eq("cart"), eq(cart));
    }
}
