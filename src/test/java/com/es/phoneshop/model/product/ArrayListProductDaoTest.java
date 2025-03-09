package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testFindProductsHasResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testSaveNewProduct(){
        Currency usd = Currency.getInstance("USD");
        Product testproduct = new Product("testproduct", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        try {
            productDao.save(testproduct);
        }catch (Exception exception){
            fail("Unable to save product");
        }

        assertNotNull(testproduct.getId());
        Product added = null;
        try {
            added = productDao.getProduct(testproduct.getId());
        }catch (Exception exception)
        {
            fail("Unable to get saved product");
        }

        assertNotNull(added);
        assertEquals("testproduct", testproduct.getCode());
    }

    @Test
    public void testDeleteProduct(){
        Currency usd = Currency.getInstance("USD");
        Product testproduct = new Product("testproduct", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        try {
            productDao.save(testproduct);
        }catch (Exception exception){
            fail("Unable to save product");
        }

        assertNotNull(testproduct.getId());
        Product added = null;
        try {
            added = productDao.getProduct(testproduct.getId());
        }catch (Exception exception)
        {
            fail("Unable to get saved product");
        }

        assertNotNull(added);
        assertEquals("testproduct", testproduct.getCode());

        try{
            productDao.delete(testproduct.getId());
        }catch (Exception exception){
            fail("Unable to delete product");
        }

        added = null;
        boolean NotFound = false;
        try {
            added = productDao.getProduct(testproduct.getId());
        }catch (Exception exception)
        {
            NotFound = true;
        }

        assertTrue(NotFound);
    }

}
