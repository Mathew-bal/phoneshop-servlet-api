package com.es.phoneshop.dao;

import com.es.phoneshop.dao.GenericDao;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

public interface OrderDao extends GenericDao<Order> {
    Order getOrderBySecureId(String secureId) throws OrderNotFoundException;
}
