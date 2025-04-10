package com.es.phoneshop.service.implementations;

import com.es.phoneshop.dao.implementations.ArrayListOrderDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.implementations.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cartitem.CartItem;
import com.es.phoneshop.model.dateinterval.DateInterval;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.OrderService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.es.phoneshop.dao.implementations.ArrayListProductDao.USD_CURRENCY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DefaultOrderServiceTest {

    private static final long PRODUCT_TEST_ID = 3;

    private static final long PRODUCT_TEST_ID_2 = 1;

    private static final int TEST_QUANTITY = 3;

    private static final int TEST_QUANTITY_2 = 1;

    private static final long DELIVERY_WINDOW_START_ADD = 3;

    private static final long DELIVERY_WINDOW_END_ADD = 10;

    private static final String FIRST_NAME = "DefaultOrder";

    private static final BigDecimal DELIVERY_COST = new BigDecimal(5);

    private static Cart testCart;

    private static ProductDao productDao;

    private static OrderDao orderDao;

    private OrderService orderService;

    @BeforeClass
    public static void initialSetup() {
        orderDao = ArrayListOrderDao.getInstance();
        productDao = ArrayListProductDao.getInstance();
        productDao.save(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), USD_CURRENCY, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        productDao.save(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), USD_CURRENCY, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        testCart = new Cart();
        testCart.getCartItems().add(new CartItem(productDao.get(PRODUCT_TEST_ID), TEST_QUANTITY));
        testCart.getCartItems().add(new CartItem(productDao.get(PRODUCT_TEST_ID_2), TEST_QUANTITY_2));
        testCart.setTotalQuantity(testCart.getCartItems().stream().
                map(CartItem::getQuantity).
                mapToInt(q -> q).sum());
        testCart.setTotalPrice(testCart.getCartItems().stream().
                map(cartItem -> cartItem.getProduct().getPrice().multiply(new BigDecimal(cartItem.getQuantity()))).
                reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    @Before
    public void setup() {
        orderService = DefaultOrderService.getInstance();
    }

    @Test
    public void testGetOrder() {
        Order order = orderService.getOrder(testCart);

        assertEquals(testCart.getTotalPrice(), order.getSubTotal());
        assertEquals(testCart.getTotalQuantity(), order.getTotalQuantity());
        assertEquals(testCart.getTotalPrice().add(DELIVERY_COST), order.getTotalPrice());
    }

    @Test
    public void testPlaceOrder() {
        Order order = orderService.getOrder(testCart);
        order.setFirstName(FIRST_NAME);

        orderService.placeOrder(order);

        assertEquals(FIRST_NAME, orderDao.get(1L).getFirstName());
        assertNotNull(order.getSecureId());
    }

    @Test
    public void testDeliveryDateInterval() {
        Order order = orderService.getOrder(testCart);
        DateInterval interval = orderService.calculateDeliveryDateInterval(order);

        assertEquals(LocalDate.now().plusDays(DELIVERY_WINDOW_START_ADD), interval.getStart());
        assertEquals(LocalDate.now().plusDays(DELIVERY_WINDOW_END_ADD), interval.getEnd());
    }
}
