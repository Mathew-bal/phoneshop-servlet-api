package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;


public class ArrayListProductDaoTest
{
    private ProductDao productDao;

    private final Currency usd = Currency.getInstance("USD");

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testFindProductsHasResults() throws ProductNotFoundException {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testSaveProduct() throws ProductNotFoundException{
        Product testproduct = new Product("testproduct", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        assertTrue(productDao.save(testproduct));

        Product found = productDao.getProduct(testproduct.getId());

        assertEquals("testproduct", found.getCode());
    }

    @Test(expected = ProductNotFoundException.class)
    public void testDeleteProduct() throws ProductNotFoundException {
        Product testproduct = new Product("testproduct", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        productDao.save(testproduct);

        Product found = productDao.getProduct(testproduct.getId());

        assertEquals("testproduct", found.getCode());

        assertTrue(productDao.delete(found.getId()));

        found = productDao.getProduct(testproduct.getId());

        assertNull(found);
    }


    @Test(expected = ProductNotFoundException.class)
    public void testDeleteNonExistingProduct()throws ProductNotFoundException {
        Product testproduct = new Product("testproduct", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        productDao.save(testproduct);

        Product found = productDao.getProduct(testproduct.getId());

        assertEquals("testproduct", found.getCode());

        assertTrue(productDao.delete(found.getId()));

        found = productDao.getProduct(testproduct.getId());

        assertFalse(productDao.delete(testproduct.getId()));
    }
}
