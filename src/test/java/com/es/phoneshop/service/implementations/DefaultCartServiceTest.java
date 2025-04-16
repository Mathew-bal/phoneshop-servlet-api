package com.es.phoneshop.service.implementations;

import com.es.phoneshop.dao.implementations.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.es.phoneshop.dao.implementations.ArrayListProductDao.USD_CURRENCY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCartServiceTest {

    private static final long PRODUCT_TEST_ID = 3;

    private static final long PRODUCT_TEST_ID_2 = 1;

    private static final int TEST_QUANTITY = 3;

    private static final int TEST_QUANTITY_2 = 1;

    private static final int TEST_QUANTITY_ADD = 1;

    private static final int TEST_QUANTITY_OVER_STOCK = 300;

    @Mock
    private HttpSession session;

    private Cart sessionCart;

    private ProductDao productDao;

    private CartService cartService;

    @Before
    public void setup() {
        cartService = DefaultCartService.getInstance();
        productDao = ArrayListProductDao.getInstance();
        productDao.save(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), USD_CURRENCY, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        productDao.save(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), USD_CURRENCY, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        sessionCart = new Cart();

        when(session.getAttribute(eq(DefaultCartService.class.getName() + ".cart"))).thenReturn(sessionCart);
    }

    @Test
    public void testGetNewCart() {
        assertNotNull(cartService.getCart(session));
    }

    @Test
    public void testAddToCart() throws OutOfStockException {
        cartService.add(session, PRODUCT_TEST_ID, TEST_QUANTITY);

        assertFalse(sessionCart.getCartItems().isEmpty());
        assertEquals(TEST_QUANTITY, sessionCart.getCartItems().get(0).getQuantity());
    }

    @Test
    public void testCartSummary() throws OutOfStockException {
        cartService.add(session, PRODUCT_TEST_ID, TEST_QUANTITY);
        cartService.add(session, PRODUCT_TEST_ID_2, TEST_QUANTITY_2);

        assertEquals(TEST_QUANTITY + TEST_QUANTITY_2, cartService.getCart(session).getTotalQuantity());
        assertEquals( productDao.get(PRODUCT_TEST_ID).getPrice().multiply(new BigDecimal(TEST_QUANTITY))
                        .add(productDao.get(PRODUCT_TEST_ID_2).getPrice().multiply(new BigDecimal(TEST_QUANTITY_2))),
                cartService.getCart(session).getTotalPrice());
    }

    @Test
    public void testAddTheSameToCart() throws OutOfStockException {
        cartService.add(session, PRODUCT_TEST_ID, TEST_QUANTITY);
        cartService.add(session, PRODUCT_TEST_ID, TEST_QUANTITY_ADD);

        assertEquals(1, sessionCart.getCartItems().size());
        assertEquals(TEST_QUANTITY + TEST_QUANTITY_ADD, sessionCart.getCartItems().get(0).getQuantity());
    }

    @Test(expected = OutOfStockException.class)
    public void testAddOverStock() throws OutOfStockException {
        cartService.add(session, PRODUCT_TEST_ID, TEST_QUANTITY);
        cartService.add(session, PRODUCT_TEST_ID, TEST_QUANTITY_OVER_STOCK);
    }

    @Test
    public void testUpdateCart() throws OutOfStockException {
        cartService.add(session, PRODUCT_TEST_ID, TEST_QUANTITY);
        cartService.update(session, PRODUCT_TEST_ID, TEST_QUANTITY + TEST_QUANTITY_ADD);

        assertFalse(sessionCart.getCartItems().isEmpty());
        assertEquals(TEST_QUANTITY + TEST_QUANTITY_ADD, sessionCart.getCartItems().get(0).getQuantity());
    }

    @Test(expected = OutOfStockException.class)
    public void testUpdateOverStock() throws OutOfStockException {
        cartService.add(session, PRODUCT_TEST_ID, TEST_QUANTITY);
        cartService.update(session, PRODUCT_TEST_ID, TEST_QUANTITY_OVER_STOCK);
    }

    @Test
    public void testGetCartItem() throws OutOfStockException {
        cartService.add(session, PRODUCT_TEST_ID, TEST_QUANTITY);

        assertTrue(cartService.getCartItem(session, PRODUCT_TEST_ID).isPresent());
        assertTrue(cartService.getCartItem(session, PRODUCT_TEST_ID + 1).isEmpty());
    }

    @Test
    public void testDeleteCartItem() throws OutOfStockException {
        cartService.add(session, PRODUCT_TEST_ID, TEST_QUANTITY);
        cartService.delete(session, PRODUCT_TEST_ID);

        assertTrue(cartService.getCartItem(session, PRODUCT_TEST_ID).isEmpty());
    }

    @Test
    public void testDeleteCartItemOnZeroQuantity() throws OutOfStockException {
        cartService.add(session, PRODUCT_TEST_ID, TEST_QUANTITY);
        cartService.update(session, PRODUCT_TEST_ID, 0);

        assertTrue(cartService.getCartItem(session, PRODUCT_TEST_ID).isEmpty());
    }

    @Test
    public void testClearCart() throws OutOfStockException {
        cartService.add(session, PRODUCT_TEST_ID, TEST_QUANTITY);
        cartService.add(session, PRODUCT_TEST_ID_2, TEST_QUANTITY_2);
        AtomicBoolean sessionReset = new AtomicBoolean(false);

        doAnswer(invocationOnMock -> {
            if (invocationOnMock.getArgument(1) == null) {
                sessionReset.set(true);
            }
            return null;
        }).when(session).setAttribute(eq(DefaultCartService.class.getName() + ".cart"), any());

        cartService.clearCart(session);

        assertTrue(sessionReset.get());
    }
}
