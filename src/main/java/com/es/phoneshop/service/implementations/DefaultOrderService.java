package com.es.phoneshop.service.implementations;

import com.es.phoneshop.dao.implementations.ArrayListOrderDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cartitem.CartItem;
import com.es.phoneshop.model.dateinterval.DateInterval;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultOrderService implements OrderService {

    private static final long DELIVERY_WINDOW_START_ADD = 3;

    private static final long DELIVERY_WINDOW_END_ADD = 10;

    private static OrderService instance;

    private OrderDao orderDao;

    public static synchronized OrderService getInstance() {
        if (instance == null) {
            instance = new DefaultOrderService();
        }
        return instance;
    }

    private DefaultOrderService() {
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    public Order getOrder(Cart cart) {
        Order order = new Order();
        order.setItems(cart.getCartItems().stream().
                map(item -> {
                    try {
                        return (CartItem) item.clone();
                    } catch (CloneNotSupportedException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toCollection(ArrayList::new)));
        order.setTotalQuantity(cart.getTotalQuantity());
        order.setSubTotal(cart.getTotalPrice());
        order.setDeliveryCost(calculateDeliveryCost());
        order.setTotalPrice(order.getSubTotal().add(order.getDeliveryCost()));

        return order;
    }

    @Override
    public DateInterval calculateDeliveryDateInterval(Order order) {
        return new DateInterval(LocalDate.now().plusDays(DELIVERY_WINDOW_START_ADD), LocalDate.now().plusDays(DELIVERY_WINDOW_END_ADD));
    }

    @Override
    public void placeOrder(Order order) {
        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }

    private BigDecimal calculateDeliveryCost() {
        return new BigDecimal(5);
    }
}
