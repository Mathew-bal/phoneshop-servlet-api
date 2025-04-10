package com.es.phoneshop.utils;

import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.order.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutParamsSetterUtilTest {
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String PHONE = "phone";
    private static final String DELIVERY_ADDRESS = "deliveryAddress";
    private static final String PAYMENT_METHOD = "paymentMethod";
    private static final String PREVIOUS_PAYMENT_METHOD = "previousPaymentMethod";
    private static final String DELIVERY_DATE = "deliveryDate";
    private static final String PREVIOUS_DELIVERY_DATE = "previousDeliveryDate";
    private static final String FIRST_NAME_VALUE = "AAAAA";
    private static final String LAST_NAME_VALUE = "AAAAA";
    private static final String DELIVERY_ADDRESS_VALUE = "AAAAA";
    private static final String PHONE_VALUE = "+375 29 111 22 33";
    private static final String PHONE_INCORRECT = "911";
    private static final String PAYMENT_METHOD_VALUE = PaymentMethod.CASH.toString();
    private static final String PAYMENT_METHOD_INCORRECT = "BITCOIN";
    private static final String DELIVERY_DATE_VALUE = LocalDate.now().plusDays(4).toString();
    private static final String DELIVERY_DATE_INCORRECT = "november 2 2022";
    private static final String DELIVERY_DATE_INVALID = LocalDate.now().plusDays(12).toString();
    private static final String MSG_VALUE_REQUIRED = "Value is required";
    private static final String MSG_PHONE_NOT_PARSED = "Not a phone number";
    private static final String MSG_DATE_NOT_IN_INTERVAL = "Delivery date outside of feasible interval";
    private static final String MSG_DATE_NOT_PARSED = "Unable to parse date";
    private static final String MSG_PAYMENT_METHOD_NOT_PARSED = "Unable to parse date";

    @Mock
    private HttpServletRequest request;

    private Order order;

    private Map<String, String> errors;

    private CheckoutParamsSetterUtil paramsSetterUtil = CheckoutParamsSetterUtil.getInstance();

    @Before
    public void setup() throws ServletException, OutOfStockException, NoSuchFieldException, IllegalAccessException {
        order = new Order();
        errors = new HashMap<>();
    }

    @Test
    public void testCorrectParams() {
        when(request.getParameter(eq(FIRST_NAME))).thenReturn(FIRST_NAME_VALUE);
        when(request.getParameter(eq(LAST_NAME))).thenReturn(LAST_NAME_VALUE);
        when(request.getParameter(eq(PHONE))).thenReturn(PHONE_VALUE);
        when(request.getParameter(eq(DELIVERY_ADDRESS))).thenReturn(DELIVERY_ADDRESS_VALUE);
        when(request.getParameter(eq(DELIVERY_DATE))).thenReturn(DELIVERY_DATE_VALUE);
        when(request.getParameter(eq(PAYMENT_METHOD))).thenReturn(PAYMENT_METHOD_VALUE);

        paramsSetterUtil.setCheckoutParams(order, request, errors);

        assertTrue(errors.isEmpty());
        verify(request).setAttribute(eq(PREVIOUS_DELIVERY_DATE), any(LocalDate.class));
        verify(request).setAttribute(eq(PREVIOUS_PAYMENT_METHOD), any(PaymentMethod.class));
    }

    @Test
    public void testEmptyParamsErrorOnAll() {
        when(request.getParameter(eq(FIRST_NAME))).thenReturn(null);
        when(request.getParameter(eq(LAST_NAME))).thenReturn(null);
        when(request.getParameter(eq(PHONE))).thenReturn(null);
        when(request.getParameter(eq(DELIVERY_ADDRESS))).thenReturn(null);
        when(request.getParameter(eq(DELIVERY_DATE))).thenReturn(null);
        when(request.getParameter(eq(PAYMENT_METHOD))).thenReturn(null);

        paramsSetterUtil.setCheckoutParams(order, request, errors);

        assertEquals(6, errors.values().size());
        errors.forEach((stringParam, stringError) -> assertEquals(MSG_VALUE_REQUIRED, stringError));
    }

    @Test
    public void testNotAPhoneNumber() {
        when(request.getParameter(eq(PHONE))).thenReturn(PHONE_INCORRECT);

        paramsSetterUtil.setCheckoutParams(order, request, errors);

        assertEquals(MSG_PHONE_NOT_PARSED, errors.get(PHONE));
    }

    @Test
    public void testNotAPaymentMethod() {
        when(request.getParameter(eq(PAYMENT_METHOD))).thenReturn(PAYMENT_METHOD_INCORRECT);

        paramsSetterUtil.setCheckoutParams(order, request, errors);

        assertEquals(MSG_PAYMENT_METHOD_NOT_PARSED, errors.get(PAYMENT_METHOD));
    }

    @Test
    public void testNotADate() {
        when(request.getParameter(eq(DELIVERY_DATE))).thenReturn(DELIVERY_DATE_INCORRECT);

        paramsSetterUtil.setCheckoutParams(order, request, errors);

        assertEquals(MSG_DATE_NOT_PARSED, errors.get(DELIVERY_DATE));
    }

    @Test
    public void testDateNotWithinTheInterval() {
        when(request.getParameter(eq(DELIVERY_DATE))).thenReturn(DELIVERY_DATE_INVALID);

        paramsSetterUtil.setCheckoutParams(order, request, errors);

        assertEquals(MSG_DATE_NOT_IN_INTERVAL, errors.get(DELIVERY_DATE));
    }
}
