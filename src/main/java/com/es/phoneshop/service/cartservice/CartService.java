package com.es.phoneshop.service.cartservice;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cartitem.CartItem;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.Optional;

public interface CartService {
    Cart getCart(HttpSession session);
    Optional<CartItem> getCartItem(HttpSession session, Long productId);
    void add(HttpSession session, Long productId, int quantity) throws OutOfStockException;
    void update(HttpSession session, Long productId, int quantity) throws OutOfStockException;
    void delete(HttpSession session, Long productId);
    int getProductQuantity(HttpSession session, Long productId);
}
