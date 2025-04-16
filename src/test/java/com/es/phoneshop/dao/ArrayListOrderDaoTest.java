package com.es.phoneshop.dao;

import com.es.phoneshop.dao.implementations.ArrayListGenericDao;
import com.es.phoneshop.dao.implementations.ArrayListOrderDao;
import com.es.phoneshop.dao.implementations.ArrayListProductDao;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cartitem.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.implementations.DefaultOrderService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ArrayListOrderDaoTest {


    private static final String CURRENCY = "USD";

    private static final Currency USD_CURRENCY = Currency.getInstance(CURRENCY);

    private static final String FIRST_NAME = "Mathew1";

    private static ProductDao productDao;

    private static Cart testCart;

    private OrderDao orderDao;

    private List<Order> orders;

    @BeforeClass
    public static void initialSetup() {
        productDao = ArrayListProductDao.getInstance();
        saveSampleProducts();
        testCart = new Cart();
        testCart.getCartItems().add(new CartItem(productDao.get(1L), 1));
        testCart.getCartItems().add(new CartItem(productDao.get(2L), 1));
        testCart.setTotalQuantity(testCart.getCartItems().stream().
                map(CartItem::getQuantity).
                mapToInt(q -> q).sum());
        testCart.setTotalPrice(testCart.getCartItems().stream().
                map(cartItem -> cartItem.getProduct().getPrice().multiply(new BigDecimal(cartItem.getQuantity()))).
                reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private static void saveSampleProducts() {
        productDao.save(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), USD_CURRENCY, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
    }

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        resetOrderDao();
        orderDao = ArrayListOrderDao.getInstance();
        Field field = ArrayListGenericDao.class.getDeclaredField("items");
        field.setAccessible(true);
        orders = (List<Order>) field.get(orderDao);
        field.setAccessible(false);
    }

    public void resetOrderDao() {
        Field instance;
        try {
            instance = ArrayListOrderDao.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(null, null);
            instance.setAccessible(false);
            orderDao = ArrayListOrderDao.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Order getTestOrder() {
        Order order = DefaultOrderService.getInstance().getOrder(testCart);
        order.setFirstName(FIRST_NAME);
        return order;
    }

    @Test
    public void testGetOrder() {
        orders.add(getTestOrder());
        orders.get(0).setId(1L);

        assertEquals(FIRST_NAME, orderDao.get(1L).getFirstName());
    }

    @Test(expected = OrderNotFoundException.class)
    public void testGetNonExistingOrder() {
        orderDao.get(10L);
    }

    @Test
    public void testSaveOrder() {
        Order testorder = getTestOrder();
        orderDao.save(testorder);

        assertTrue(orders.stream().
                anyMatch(order -> order.getFirstName().equals(FIRST_NAME)));
    }

    @Test
    public void testDeleteOrder() {
        Order testorder = getTestOrder();
        orderDao.save(testorder);

        orderDao.delete(testorder.getId());

        assertFalse(orders.stream().
                anyMatch(order -> order.getFirstName().equals(FIRST_NAME)));
    }

    @Test
    public void testGetOrderBySecureId() {
        Order testorder = getTestOrder();
        String secureId = UUID.randomUUID().toString();
        testorder.setSecureId(secureId);
        orderDao.save(testorder);

        Order testOrderResult = orderDao.getOrderBySecureId(secureId);

        assertEquals(FIRST_NAME, testOrderResult.getFirstName());
    }
}
