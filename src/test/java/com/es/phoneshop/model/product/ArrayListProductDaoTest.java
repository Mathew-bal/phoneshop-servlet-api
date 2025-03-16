package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;

    private static final String CURRENCY = "USD";

    private static final Currency USD_CURRENCY = Currency.getInstance(CURRENCY);

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testFindProductsHasResults(){
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testSaveProduct(){
        Product testproduct = new Product("testproduct", "Samsung Galaxy S", new BigDecimal(100), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        productDao.save(testproduct);

        assertEquals("testproduct", productDao.getProduct(testproduct.getId()).getCode());
    }

    @Test(expected = ProductNotFoundException.class)
    public void testGetNonExistingProduct(){
        long productsCount = productDao.filterProducts().size();

        productDao.getProduct(productsCount + 2);
    }

    @Test(expected = ProductNotFoundException.class)
    public void testDeleteProduct(){
        Product testproduct = new Product("testproduct", "Samsung Galaxy S", new BigDecimal(100), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(testproduct);

        productDao.delete(testproduct.getId());

        productDao.getProduct(testproduct.getId());
    }
}
