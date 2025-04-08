package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.OrderService;
import com.es.phoneshop.service.implementations.DefaultCartService;
import com.es.phoneshop.service.implementations.DefaultOrderService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderOverviewPageServletTest {
    private static final long PRODUCT_TEST_ID_1 = 1;
    private static final long PRODUCT_TEST_ID_2 = 3;
    private static final int PRODUCT_TEST_QUANTITY_1 = 3;
    private static final int PRODUCT_TEST_QUANTITY_2 = 4;
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

    private OrderOverviewPageServlet servlet = new OrderOverviewPageServlet();

    private Cart cart;

    private OrderService orderService;

    private Order order;

    private CartService cartService;

    @Before
    public void setup() throws ServletException, OutOfStockException {
        cart = new Cart();
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderService.getInstance();

        servlet.init(config);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        when(servletContext.getInitParameter("insertDemoData")).thenReturn("true");
        when(servletContextEvent.getServletContext()).thenReturn(servletContext);
        demoDataServletContextListener.contextInitialized(servletContextEvent);

        when(session.getAttribute(eq(DefaultCartService.class.getName() + ".cart"))).thenReturn(cart);

        cartService.add(session, PRODUCT_TEST_ID_1, PRODUCT_TEST_QUANTITY_1);
        cartService.add(session, PRODUCT_TEST_ID_2, PRODUCT_TEST_QUANTITY_2);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        order = orderService.getOrder(cart);
        orderService.placeOrder(order);
        when(request.getPathInfo()).thenReturn("/" + order.getSecureId());

        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("order"), any(Order.class));
    }
}
