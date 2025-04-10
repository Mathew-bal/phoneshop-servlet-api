package com.es.phoneshop.service;

import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.dateinterval.DateInterval;
import com.es.phoneshop.model.order.Order;

import java.util.List;

public interface OrderService {
    Order getOrder(Cart cart);
    DateInterval calculateDeliveryDateInterval(Order order);
    List<PaymentMethod> getPaymentMethods();
    void placeOrder(Order order);
}
