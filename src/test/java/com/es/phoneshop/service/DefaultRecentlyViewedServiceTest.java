package com.es.phoneshop.service;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.cartservice.CartService;
import com.es.phoneshop.service.cartservice.DefaultCartService;
import com.es.phoneshop.service.recentlyviewedservice.DefaultRecentlyViewedService;
import com.es.phoneshop.service.recentlyviewedservice.RecentlyViewedService;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.es.phoneshop.dao.ArrayListProductDao.USD_CURRENCY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultRecentlyViewedServiceTest {

    @Mock
    private HttpSession session;

    private List<Product> recentlyViewed;

    @Mock
    private ProductDao productDao;

    private RecentlyViewedService recentlyViewedService;

    @Before
    public void setup() {
        recentlyViewedService = DefaultRecentlyViewedService.getInstance();
        productDao = ArrayListProductDao.getInstance();
        recentlyViewed = new ArrayList<>();

        productDao.save(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), USD_CURRENCY, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        productDao.save(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), USD_CURRENCY, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        productDao.save(new Product("iphone", "Apple iPhone", new BigDecimal(200), USD_CURRENCY, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        productDao.save(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), USD_CURRENCY, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        productDao.save(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), USD_CURRENCY, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));

        when(session.getAttribute(eq(DefaultRecentlyViewedService.class.getName() + ".viewed"))).thenReturn(recentlyViewed);
    }

    @Test
    public void testGetRecentlyViewed() {
        assertNotNull(recentlyViewedService.getRecentlyViewedProducts(session));
    }

    @Test
    public void testAddToRecentlyViewed() {
        recentlyViewedService.addRecentlyViewedProduct(session, 1L);

        assertEquals(1, recentlyViewed.size());
    }

    @Test
    public void testAddLIFOOrder() {
        recentlyViewedService.addRecentlyViewedProduct(session, 1L);
        recentlyViewedService.addRecentlyViewedProduct(session, 2L);

        assertEquals(2L, (long) recentlyViewed.get(0).getId());
    }

    @Test
    public void testAddingTheSameMergesAndToTheFirst() {
        recentlyViewedService.addRecentlyViewedProduct(session, 1L);
        recentlyViewedService.addRecentlyViewedProduct(session, 2L);
        recentlyViewedService.addRecentlyViewedProduct(session, 1L);

        assertEquals(2, recentlyViewed.size());
        assertEquals(1L, (long) recentlyViewed.get(0).getId());
    }

    @Test
    public void testMaxLength() {
        recentlyViewedService.addRecentlyViewedProduct(session, 1L);
        recentlyViewedService.addRecentlyViewedProduct(session, 2L);
        recentlyViewedService.addRecentlyViewedProduct(session, 3L);
        recentlyViewedService.addRecentlyViewedProduct(session, 4L);
        recentlyViewedService.addRecentlyViewedProduct(session, 5L);

        assertEquals(3, recentlyViewed.size());
    }
}
