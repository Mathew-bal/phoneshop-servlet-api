package com.es.phoneshop.service.cartservice;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cartitem.CartItem;
import com.es.phoneshop.model.product.Product;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
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
        updateProductQuantity(session, productId, quantity + getProductQuantity(session, productId));
    }

    @Override
    public void update(HttpSession session, Long productId, int quantity) throws OutOfStockException {
        updateProductQuantity(session, productId, quantity);
    }

    public void updateProductQuantity(HttpSession session, Long productId, int quantity) throws OutOfStockException {
        Cart cart = getCart(session);
        Product product = productDao.getProduct(productId);
        Optional<CartItem> cartItem = getCartItem(session, productId);
        int previousQuantity = cartItem.map(CartItem::getQuantity).orElse(0);

        if (product.getStock() < quantity) {
            throw new OutOfStockException(product, quantity + getProductQuantity(session, productId), product.getStock());
        } else {
            if (previousQuantity > 0 && quantity == 0) {
                delete(session, productId);
            } else {
                if (quantity > 0) {
                    cartItem.ifPresentOrElse(
                            item -> item.setQuantity(quantity),
                            () -> cart.getCartItems().add(new CartItem(product, quantity)));
                }
            }
        }
        recalculateCart(session);
    }

    @Override
    public void delete(HttpSession session, Long productId) {
        getCart(session).getCartItems().removeIf(item -> productId.equals(item.getProduct().getId()));
        recalculateCart(session);
    }

    @Override
    public Optional<CartItem> getCartItem(HttpSession session, Long productId) {
        return getCart(session).getCartItems().stream().
                filter(cartItem -> productId.equals(cartItem.getProduct().getId())).
                findAny();
    }


    @Override
    public int getProductQuantity(HttpSession session, Long productId) {
        Cart cart = getCart(session);
        synchronized (cart) {
            return getCartItem(session, productId).
                    map(CartItem::getQuantity).orElse(0);
        }
    }

    private void recalculateCart(HttpSession session) {
        Cart cart = getCart(session);

        cart.setTotalQuantity(cart.getCartItems().stream().
                map(CartItem::getQuantity).
                mapToInt(q -> q).sum());
        cart.setTotalPrice(cart.getCartItems().stream().
                map(cartItem -> cartItem.getProduct().getPrice().multiply(new BigDecimal(cartItem.getQuantity()))).
                reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}
