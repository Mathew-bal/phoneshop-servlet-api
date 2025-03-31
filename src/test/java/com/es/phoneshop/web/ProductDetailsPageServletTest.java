package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.cartservice.DefaultCartService;
import com.es.phoneshop.service.recentlyviewedservice.DefaultRecentlyViewedService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {

    private static final long PRODUCT_TEST_ID = 3;

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

    private ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();

    private ProductDao productDao;

    private List<Product> recentlyViewed;

    private Cart cart;

    @Before
    public void setup() throws ServletException {
        recentlyViewed = new ArrayList<>();
        cart = new Cart();

        servlet.init(config);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getPathInfo()).thenReturn("/" + PRODUCT_TEST_ID);

        when(servletContext.getInitParameter("insertDemoData")).thenReturn("true");
        when(servletContextEvent.getServletContext()).thenReturn(servletContext);
        demoDataServletContextListener.contextInitialized(servletContextEvent);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(eq(DefaultCartService.class.getName() + ".cart"))).thenReturn(cart);
        when(session.getAttribute(eq(DefaultRecentlyViewedService.class.getName() + ".viewed"))).thenReturn(recentlyViewed);
        when(request.getLocale()).thenReturn(Locale.ENGLISH);

        productDao = ArrayListProductDao.getInstance();
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("product"), eq(productDao.getProduct(PRODUCT_TEST_ID)));
        assertFalse(recentlyViewed.isEmpty());
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        when(request.getParameter(eq("quantity"))).thenReturn("1");

        servlet.doPost(request, response);

        verify(response).sendRedirect(anyString());
        assertFalse(cart.getCartItems().isEmpty());
    }
}
