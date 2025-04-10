package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
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
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductListPageServletTest {

    private static final long PRODUCT_TEST_ID = 1;

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

    private ProductListPageServlet servlet = new ProductListPageServlet();

    private Cart cart;

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        cart = new Cart();

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(servletContext.getInitParameter("insertDemoData")).thenReturn("true");
        when(servletContextEvent.getServletContext()).thenReturn(servletContext);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(eq(DefaultCartService.class.getName() + ".cart"))).thenReturn(cart);
        when(request.getLocale()).thenReturn(Locale.ENGLISH);

        when(request.getLocale()).thenReturn(Locale.ENGLISH);

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

    @Test
    public void testDoPostWithError() throws IOException {
        when(request.getParameter("productAddedId")).thenReturn(String.valueOf(PRODUCT_TEST_ID));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(PRODUCT_TEST_QUANTITY_INCORRECT));

        AtomicReference<String> errorAttribute = new AtomicReference<>();
        Mockito.doAnswer((Answer<Void>) invocationOnMock -> {
            errorAttribute.set(invocationOnMock.getArgument(1));
            return null;
        }).when(request).setAttribute(eq("error"), anyString());
        when(request.getAttribute("error")).then((Answer<String>) invocationOnMock -> errorAttribute.get());

        servlet.doPost(request, response);

        verify(response).sendRedirect(Mockito.argThat(s -> s.contains("error=") && s.contains("addedId=") && s.contains("previousInput")));
        assertTrue(cart.getCartItems().isEmpty());
    }

    @Test
    public void testDoPostCorrectly() throws IOException {
        when(request.getParameter("productAddedId")).thenReturn(String.valueOf(PRODUCT_TEST_ID));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(PRODUCT_TEST_QUANTITY));

        servlet.doPost(request, response);

        verify(response).sendRedirect(Mockito.argThat(s -> s.contains("message=")));
        assertEquals(PRODUCT_TEST_QUANTITY, cart.getCartItems().get(0).getQuantity());
        assertEquals(PRODUCT_TEST_ID, cart.getCartItems().get(0).getProduct().getId().longValue());
    }
}
