package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.cartservice.DefaultCartService;
import com.es.phoneshop.service.recentlyviewedservice.DefaultRecentlyViewedService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductListPageServletTest {
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

    private ProductListPageServlet servlet = new ProductListPageServlet();

    private List<Product> recentlyViewed;

    private Cart cart;

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        recentlyViewed = new ArrayList<>();
        cart = new Cart();

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(servletContext.getInitParameter("insertDemoData")).thenReturn("true");
        when(servletContextEvent.getServletContext()).thenReturn(servletContext);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(eq(DefaultCartService.class.getName() + ".cart"))).thenReturn(cart);
        when(session.getAttribute(eq(DefaultRecentlyViewedService.class.getName() + ".viewed"))).thenReturn(recentlyViewed);

        demoDataServletContextListener.contextInitialized(servletContextEvent);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(request.getParameter("sort")).thenReturn("price");
        when(request.getParameter("order")).thenReturn("desc");
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("products"), any());
    }

    @Test
    public void testDoGetRedirectsWithoutSortAttributes() throws ServletException, IOException {
        when(request.getParameter("sort")).thenReturn(null);
        when(request.getParameter("order")).thenReturn(null);
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request, times(0)).setAttribute(eq("products"), anyString());
    }
}
