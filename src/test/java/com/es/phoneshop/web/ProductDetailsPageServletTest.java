package com.es.phoneshop.web;

import com.es.phoneshop.dao.implementations.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.implementations.DefaultCartService;
import com.es.phoneshop.service.implementations.DefaultRecentlyViewedService;
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
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {

    private static final long PRODUCT_TEST_ID = 3;

    private static final int PRODUCT_TEST_QUANTITY = 1;

    private static final int PRODUCT_TEST_QUANTITY_INCORRECT = -1;

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
        verify(request).setAttribute(eq("product"), eq(productDao.get(PRODUCT_TEST_ID)));
        assertFalse(recentlyViewed.isEmpty());
    }

    @Test
    public void testDoPostWithError() throws ServletException, IOException {
        when(request.getParameter("quantity")).thenReturn(String.valueOf(PRODUCT_TEST_QUANTITY_INCORRECT));

        AtomicReference<String> errorAttribute = new AtomicReference<>();
        Mockito.doAnswer((Answer<Void>) invocationOnMock -> {
            errorAttribute.set(invocationOnMock.getArgument(1));
            return null;
        }).when(request).setAttribute(eq("error"), anyString());
        when(request.getAttribute("error")).then((Answer<String>) invocationOnMock -> errorAttribute.get());

        servlet.doPost(request, response);

        verify(response).sendRedirect(Mockito.argThat(s -> s.contains("error=") && s.contains("previousInput")));
        assertTrue(cart.getCartItems().isEmpty());
    }

    @Test
    public void testDoPostCorrectly() throws ServletException, IOException {
        when(request.getParameter("quantity")).thenReturn(String.valueOf(PRODUCT_TEST_QUANTITY));

        servlet.doPost(request, response);

        verify(response).sendRedirect(Mockito.argThat(s -> s.contains("message=")));
        assertEquals(PRODUCT_TEST_QUANTITY, cart.getCartItems().get(0).getQuantity());
        assertEquals(PRODUCT_TEST_ID, cart.getCartItems().get(0).getProduct().getId().longValue());
    }
}
