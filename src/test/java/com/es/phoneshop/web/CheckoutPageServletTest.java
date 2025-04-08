package com.es.phoneshop.web;

import com.es.phoneshop.dao.implementations.ArrayListGenericDao;
import com.es.phoneshop.dao.implementations.ArrayListOrderDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.dateinterval.DateInterval;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.implementations.DefaultCartService;
import com.es.phoneshop.service.implementations.DefaultOrderService;
import com.es.phoneshop.service.OrderService;
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
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {
    private static final long PRODUCT_TEST_ID_1 = 1;
    private static final long PRODUCT_TEST_ID_2 = 3;
    private static final int PRODUCT_TEST_QUANTITY_1 = 3;
    private static final int PRODUCT_TEST_QUANTITY_2 = 4;
    private static final String FIRST_NAME = "Checkout";
    private static final String LAST_NAME = "AAAAA";
    private static final String DELIVERY_ADDRESS = "AAAAA";
    private static final String PHONE = "+375 99 222 33 11";
    private static final PaymentMethod PAYMENT_METHOD = PaymentMethod.CASH;
    private static final LocalDate DELIVERY_DATE = LocalDate.now().plusDays(4);
    private static final LocalDate DELIVERY_DATE_INCORRECT = LocalDate.now().plusDays(12);
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

    private CheckoutPageServlet servlet = new CheckoutPageServlet();

    private Cart cart;

    private OrderService orderService;

    private OrderDao orderDao;

    private List<Order> orders;

    private CartService cartService;

    @Before
    public void setup() throws ServletException, OutOfStockException, NoSuchFieldException, IllegalAccessException {
        cart = new Cart();
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
        orderDao = ArrayListOrderDao.getInstance();
        Field field = ArrayListGenericDao.class.getDeclaredField("items");
        field.setAccessible(true);
        orders = (List<Order>) field.get(orderDao);

        servlet.init(config);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        when(servletContext.getInitParameter("insertDemoData")).thenReturn("true");
        when(servletContextEvent.getServletContext()).thenReturn(servletContext);
        demoDataServletContextListener.contextInitialized(servletContextEvent);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(eq(DefaultCartService.class.getName() + ".cart"))).thenReturn(cart);

        cartService.add(session, PRODUCT_TEST_ID_1, PRODUCT_TEST_QUANTITY_1);
        cartService.add(session, PRODUCT_TEST_ID_2, PRODUCT_TEST_QUANTITY_2);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("order"), any(Order.class));
        verify(request).setAttribute(eq("paymentMethods"), any(List.class));
        verify(request).setAttribute(eq("deliveryDateInterval"), any(DateInterval.class));
    }

    @Test
    public void testDoPostCorrectly() throws ServletException, IOException {
        when(request.getParameter(eq("firstName"))).thenReturn(FIRST_NAME);
        when(request.getParameter(eq("lastName"))).thenReturn(LAST_NAME);
        when(request.getParameter(eq("phone"))).thenReturn(PHONE);
        when(request.getParameter(eq("deliveryAddress"))).thenReturn(DELIVERY_ADDRESS);
        when(request.getParameter(eq("deliveryDate"))).thenReturn(DELIVERY_DATE.toString());
        when(request.getParameter(eq("paymentMethod"))).thenReturn(PAYMENT_METHOD.toString());
        AtomicBoolean sessionReset = new AtomicBoolean(false);
        doAnswer(invocationOnMock -> {
            if (invocationOnMock.getArgument(1) == null) {
                sessionReset.set(true);
            }
            return null;
        }).when(session).setAttribute(eq(DefaultCartService.class.getName() + ".cart"), any());

        servlet.doPost(request, response);

        verify(response).sendRedirect(contains("overview"));
        assertFalse(orders.isEmpty());
        assertTrue(sessionReset.get());
    }

    @Test
    public void testDoPostWithErrors() throws ServletException, IOException {
        when(request.getParameter(eq("firstName"))).thenReturn(FIRST_NAME);
        when(request.getParameter(eq("lastName"))).thenReturn(LAST_NAME);
        when(request.getParameter(eq("deliveryAddress"))).thenReturn(DELIVERY_ADDRESS);
        when(request.getParameter(eq("deliveryDate"))).thenReturn(DELIVERY_DATE_INCORRECT.toString());
        when(request.getParameter(eq("paymentMethod"))).thenReturn(PAYMENT_METHOD.toString());

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any(Map.class));
        verify(request).setAttribute(eq("order"), any(Order.class));
        verify(request).setAttribute(eq("deliveryDateInterval"), any(DateInterval.class));
        verify(request).setAttribute(eq("paymentMethods"), any(List.class));
        verify(requestDispatcher).forward(any(), any());
    }
}
