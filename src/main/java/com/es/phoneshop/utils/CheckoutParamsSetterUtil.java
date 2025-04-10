package com.es.phoneshop.utils;

import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.model.dateinterval.DateInterval;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.OrderService;
import com.es.phoneshop.service.implementations.DefaultOrderService;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.function.Consumer;

public class CheckoutParamsSetterUtil {
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String PHONE = "phone";
    private static final String DELIVERY_ADDRESS = "deliveryAddress";
    private static final String PAYMENT_METHOD = "paymentMethod";
    private static final String PREVIOUS_PAYMENT_METHOD = "previousPaymentMethod";
    private static final String DELIVERY_DATE = "deliveryDate";
    private static final String PREVIOUS_DELIVERY_DATE = "previousDeliveryDate";
    private static final String PHONE_REGEXP = "^\\+375\\d{9}$";
    private static final LocalDate DATE_INCORRECT = LocalDate.EPOCH;


    private static final String MSG_VALUE_REQUIRED = "Value is required";
    private static final String MSG_PHONE_NOT_PARSED = "Not a phone number";
    private static final String MSG_DATE_NOT_IN_INTERVAL = "Delivery date outside of feasible interval";
    private static final String MSG_DATE_NOT_PARSED = "Unable to parse date";
    private static final String MSG_PAYMENT_METHOD_NOT_PARSED = "Unable to parse date";

    private OrderService orderService;

    private static CheckoutParamsSetterUtil instance;

    public static synchronized CheckoutParamsSetterUtil getInstance() {
        if (instance == null) {
            instance = new CheckoutParamsSetterUtil();
        }
        return instance;
    }

    private CheckoutParamsSetterUtil() {
        orderService = DefaultOrderService.getInstance();
    }

    public void setCheckoutParams(Order order, HttpServletRequest request, Map<String, String> errors) {
        setFirstNameParam(order, request, errors);
        setLastNameParam(order, request, errors);
        setDeliveryAddressParam(order, request, errors);
        setPhoneParam(order, request, errors);
        setPaymentMethodParam(order, request, errors);
        setDeliveryDateParam(order, request, errors);
    }

    private void setFirstNameParam(Order order, HttpServletRequest request, Map<String, String> errors) {
        setRequiredParameter(request, errors, FIRST_NAME, order::setFirstName);
    }

    private void setLastNameParam(Order order, HttpServletRequest request, Map<String, String> errors) {
        setRequiredParameter(request, errors, LAST_NAME, order::setLastName);
    }

    private void setDeliveryAddressParam(Order order, HttpServletRequest request, Map<String, String> errors) {
        setRequiredParameter(request, errors, DELIVERY_ADDRESS, order::setDeliveryAddress);
    }

    private void setPhoneParam(Order order, HttpServletRequest request, Map<String, String> errors) {
        setRequiredParameter(request, errors, PHONE, phoneString ->{
            if (validatePhoneNumber(phoneString)) {
                order.setPhone(phoneString);
            } else {
                errors.put(PHONE, MSG_PHONE_NOT_PARSED);
            }
        });
    }

    private String removeSpaces(String string) {
        return string.replaceAll("\\s", "");
    }

    private boolean validatePhoneNumber(String phoneString) {
        return removeSpaces(phoneString).matches(PHONE_REGEXP);
    }

    private void setPaymentMethodParam(Order order, HttpServletRequest request, Map<String, String> errors) {
        setRequiredParameter(request, errors, PAYMENT_METHOD, paymentMethodString ->{
            if (validatePaymentMethod(paymentMethodString)) {
                PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentMethodString);
                order.setPaymentMethod(paymentMethod);
                request.setAttribute(PREVIOUS_PAYMENT_METHOD, paymentMethod);
            } else {
                errors.put(PAYMENT_METHOD, MSG_PAYMENT_METHOD_NOT_PARSED);
            }
        });
    }

    private boolean validatePaymentMethod(String paymentMethodString) {
        return orderService.getPaymentMethods().stream().
                map(PaymentMethod::toString).
                anyMatch(paymentMethodString::equals);
    }

    private void setDeliveryDateParam(Order order, HttpServletRequest request, Map<String, String> errors) {
        setRequiredParameter(request, errors, DELIVERY_DATE, (dateString) -> {
            LocalDate deliveryDate = parseDate(dateString);
            if (deliveryDate.equals(DATE_INCORRECT)){
                errors.put(DELIVERY_DATE, MSG_DATE_NOT_PARSED);
                return;
            }

            if (validateDeliveryDate(deliveryDate, order)) {
                order.setDeliveryDate(deliveryDate);
                request.setAttribute(PREVIOUS_DELIVERY_DATE, deliveryDate);
            } else {
                errors.put(DELIVERY_DATE, MSG_DATE_NOT_IN_INTERVAL);
            }
        });
    }

    private LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString);
        } catch (DateTimeParseException dateTimeParseException) {
            return DATE_INCORRECT;
        }
    }

    private boolean validateDeliveryDate(LocalDate deliveryDate, Order order) {
        DateInterval deliveryDateInterval = orderService.calculateDeliveryDateInterval(order);
        return deliveryDate.isBefore(deliveryDateInterval.getEnd().plusDays(1))
                && deliveryDate.isAfter(deliveryDateInterval.getStart().minusDays(1));
    }


    private void setRequiredParameter(HttpServletRequest request, Map<String, String> errors, String parameter,
                                      Consumer<String> consumer) {
        String value = request.getParameter(parameter);
        if (value == null || value.isEmpty()) {
            errors.put(parameter, MSG_VALUE_REQUIRED);
        } else {
            consumer.accept(value);
        }
    }
}
