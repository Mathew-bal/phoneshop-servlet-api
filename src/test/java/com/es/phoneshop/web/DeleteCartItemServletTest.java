package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.cartservice.CartService;
import com.es.phoneshop.service.cartservice.DefaultCartService;
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

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeleteCartItemServletTest {

    private static final long PRODUCT_TEST_ID = 1;

    private static final int PRODUCT_TEST_QUANTITY = 1;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletConfig config;
    @Mock
    private ServletContext servletContext;
    @Mock
    ServletContextEvent servletContextEvent;
    @Mock
    HttpSession session;

    private DemoDataServletContextListener demoDataServletContextListener = new DemoDataServletContextListener();

    private DeleteCartItemServlet servlet = new DeleteCartItemServlet();
    private Cart cart;

    private CartService cartService;

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        cartService = DefaultCartService.getInstance();
        cart = new Cart();

        when(request.getPathInfo()).thenReturn("/" + PRODUCT_TEST_ID);

        when(servletContext.getInitParameter("insertDemoData")).thenReturn("true");
        when(servletContextEvent.getServletContext()).thenReturn(servletContext);
        demoDataServletContextListener.contextInitialized(servletContextEvent);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(eq(DefaultCartService.class.getName() + ".cart"))).thenReturn(cart);
    }

    @Test
    public void testDoGet() throws ServletException, IOException, OutOfStockException {
        cartService.add(session, PRODUCT_TEST_ID, PRODUCT_TEST_QUANTITY);

        servlet.doPost(request, response);

        verify(response).sendRedirect(any());
        assertTrue(cart.getCartItems().isEmpty());
    }
}
