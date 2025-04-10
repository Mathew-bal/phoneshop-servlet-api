package com.es.phoneshop.dao.implementations;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

public class ArrayListOrderDao extends ArrayListGenericDao<Order> implements OrderDao {

    private static OrderDao instance;

    public static synchronized OrderDao getInstance() {
        if (instance == null) {
            instance = new ArrayListOrderDao();
        }
        return instance;
    }

    private ArrayListOrderDao() {
        super(OrderNotFoundException::new);
    }

    @Override
    public Order getOrderBySecureId(String secureId) throws OrderNotFoundException {
        return extendedReadWriteLock.readSafe(() -> items.stream().
                filter(item -> secureId.equals(item.getSecureId())).
                findAny().orElseThrow(OrderNotFoundException::new));
    }
}
