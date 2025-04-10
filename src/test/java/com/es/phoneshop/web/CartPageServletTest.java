package com.es.phoneshop.web;

import com.es.phoneshop.dao.implementations.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cartitem.CartItem;
import com.es.phoneshop.service.implementations.DefaultCartService;
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
import java.util.HashMap;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartPageServletTest {

    private static final long PRODUCT_TEST_ID_1 = 1;

    private static final long PRODUCT_TEST_ID_2 = 3;

    private static final int PRODUCT_TEST_QUANTITY_DEFAULT = 1;

    private static final int PRODUCT_TEST_QUANTITY_1 = 3;

    private static final int PRODUCT_TEST_QUANTITY_2 = 4;

    private static final int PRODUCT_TEST_QUANTITY_2_INCORRECT = -1;

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

    private CartPageServlet servlet = new CartPageServlet();

    private ProductDao productDao;

    private Cart cart;

    @Before
    public void setup() throws ServletException {
        cart = new Cart();

        servlet.init(config);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        when(servletContext.getInitParameter("insertDemoData")).thenReturn("true");
        when(servletContextEvent.getServletContext()).thenReturn(servletContext);
        demoDataServletContextListener.contextInitialized(servletContextEvent);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(eq(DefaultCartService.class.getName() + ".cart"))).thenReturn(cart);
        when(request.getLocale()).thenReturn(Locale.ENGLISH);

        productDao = ArrayListProductDao.getInstance();
        cart.getCartItems().add(new CartItem(productDao.get(PRODUCT_TEST_ID_1), PRODUCT_TEST_QUANTITY_DEFAULT));
        cart.getCartItems().add(new CartItem(productDao.get(PRODUCT_TEST_ID_2), PRODUCT_TEST_QUANTITY_DEFAULT));
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("cart"), eq(cart));
    }

    @Test
    public void testDoPostCorrectly() throws ServletException, IOException {
        when(request.getParameterValues(eq("productId"))).thenReturn(new String[]{String.valueOf(PRODUCT_TEST_ID_1), String.valueOf(PRODUCT_TEST_ID_2)});
        when(request.getParameterValues(eq("quantity"))).thenReturn(new String[]{String.valueOf(PRODUCT_TEST_QUANTITY_1), String.valueOf(PRODUCT_TEST_QUANTITY_2)});

        servlet.doPost(request, response);

        verify(response).sendRedirect(any());
        assertFalse(cart.getCartItems().isEmpty());
        assertEquals(PRODUCT_TEST_QUANTITY_1, cart.getCartItems().get(0).getQuantity());
        assertEquals(PRODUCT_TEST_QUANTITY_2, cart.getCartItems().get(1).getQuantity());
    }

    @Test
    public void testDoPostWithError() throws ServletException, IOException {
        when(request.getParameterValues(eq("productId"))).thenReturn(new String[]{String.valueOf(PRODUCT_TEST_ID_1), String.valueOf(PRODUCT_TEST_ID_2)});
        when(request.getParameterValues(eq("quantity"))).thenReturn(new String[]{String.valueOf(PRODUCT_TEST_QUANTITY_1), String.valueOf(PRODUCT_TEST_QUANTITY_2_INCORRECT)});

        servlet.doPost(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("cart"), any());
        verify(request).setAttribute(eq("messages"), any(HashMap.class));
        verify(request).setAttribute(eq("errors"), any(HashMap.class));
        assertEquals(PRODUCT_TEST_QUANTITY_1, cart.getCartItems().get(0).getQuantity());
        assertEquals(PRODUCT_TEST_QUANTITY_DEFAULT, cart.getCartItems().get(1).getQuantity());
    }
}
