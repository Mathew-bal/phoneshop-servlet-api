package com.es.phoneshop.service.cartservice;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;

public interface CartService {
    Cart getCart(HttpSession session);
    void add(HttpSession session, Long productId, int quantity) throws OutOfStockException;
    int getProductQuantity(HttpSession session, Long productId);
    BigDecimal getCartPrice(HttpSession session);
}
