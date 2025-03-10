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
    public void testSaveProduct(){
        Currency usd = Currency.getInstance("USD");
        Product testproduct = new Product("testproduct", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        try {
            productDao.save(testproduct);
        }catch (Exception exception){
            fail("Unable to save product");
        }

        Product found = null;
        try {
            found = productDao.getProduct(testproduct.getId());
        }catch (ProductNotFoundException productNotFoundException){
            fail("Product was not added");
        }

        assertEquals("testproduct", found.getCode());
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

        Product found = null;
        try {
            found = productDao.getProduct(testproduct.getId());
        }catch (ProductNotFoundException productNotFoundException){
            fail("Product was not added");
        }

        assertEquals("testproduct", found.getCode());

        try {
            productDao.delete(found.getId());
        }catch (ProductNotFoundException productNotFoundException){
            fail("Unable to find the product for deletion");
        }


        found = null;
        try {
            found = productDao.getProduct(testproduct.getId());
        }catch (ProductNotFoundException productNotFoundException){
            found = null;
        }

        assertNull(found);
    }
}
