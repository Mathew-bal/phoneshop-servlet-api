package com.es.phoneshop.service.cartservice;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cartitem.CartItem;
import com.es.phoneshop.model.product.Product;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class DefaultCartService implements CartService {

    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";

    private ProductDao productDao;

    private static CartService instance;

    public static synchronized CartService getInstance() {
        if (instance == null) {
            instance = new DefaultCartService();
        }
        return instance;
    }

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute(CART_SESSION_ATTRIBUTE);
        if (cart == null) {
            cart = new Cart();
            session.setAttribute(CART_SESSION_ATTRIBUTE, cart);
        }
        return cart;
    }

    @Override
    public void add(HttpSession session, Long productId, int quantity) throws OutOfStockException {
        Cart cart = getCart(session);
        Product product = productDao.getProduct(productId);
        if (product.getStock() < quantity + getProductQuantity(session, productId)) {
            throw new OutOfStockException(product, quantity + getProductQuantity(session, productId), product.getStock());
        } else {
            cart.getCartItems().stream().
                    filter(cartItem -> productId.equals(cartItem.getProduct().getId())).
                    findAny().
                    ifPresentOrElse(cartItem -> cartItem.setQuantity(cartItem.getQuantity() + quantity),
                            () -> cart.getCartItems().add(new CartItem(product, quantity)));
        }
    }

    @Override
    public int getProductQuantity(HttpSession session, Long productId) {
        Cart cart = getCart(session);
        synchronized (cart) {
            return cart.getCartItems().stream().
                    filter(item -> productId.equals(item.getProduct().getId())).
                    findAny().map(CartItem::getQuantity).orElse(0);
        }
    }

    @Override
    public BigDecimal getCartPrice(HttpSession session) {
        BigDecimal result = new BigDecimal(0);
        Cart cart = getCart(session);
        synchronized (cart) {
            for (BigDecimal productPrice : cart.getCartItems().stream().
                    map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))).
                    toList()) {
                result = result.add(productPrice);
            }
            return result;
        }
    }
}
