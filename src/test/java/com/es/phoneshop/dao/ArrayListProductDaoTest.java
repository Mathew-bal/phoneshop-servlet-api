package com.es.phoneshop.dao;

import com.es.phoneshop.dao.implementations.ArrayListGenericDao;
import com.es.phoneshop.dao.implementations.ArrayListProductDao;
import com.es.phoneshop.enums.SearchMethod;
import com.es.phoneshop.enums.SortBy;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ArrayListProductDaoTest {
    private ProductDao productDao;

    private static final String CURRENCY = "USD";

    private static final Currency USD_CURRENCY = Currency.getInstance(CURRENCY);

    private List<Product> products;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        resetProductDao();
        productDao = ArrayListProductDao.getInstance();
        Field field = ArrayListGenericDao.class.getDeclaredField("items");
        field.setAccessible(true);
        products = (List<Product>) field.get(productDao);
        field.setAccessible(false);
    }

    private void saveSampleProducts() {
        productDao.save(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), USD_CURRENCY, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        productDao.save(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), USD_CURRENCY, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        productDao.save(new Product("iphone", "Apple iPhone", new BigDecimal(200), USD_CURRENCY, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        productDao.save(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), USD_CURRENCY, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        productDao.save(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), USD_CURRENCY, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));
        productDao.save(new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), USD_CURRENCY, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        productDao.save(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(120), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg"));
        productDao.save(new Product("nokia3310", "Nokia 3310", new BigDecimal(70), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));
        productDao.save(new Product("palmp", "Palm Pixi", new BigDecimal(170), USD_CURRENCY, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        productDao.save(new Product("simc56", "Siemens C56", new BigDecimal(70), USD_CURRENCY, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
        productDao.save(new Product("simc61", "Siemens C61", new BigDecimal(80), USD_CURRENCY, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        productDao.save(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), USD_CURRENCY, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));
    }

    public void resetProductDao() {
        Field instance;
        try {
            instance = ArrayListProductDao.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(null, null);
            instance.setAccessible(false);
            productDao = ArrayListProductDao.getInstance();
            saveSampleProducts();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFindProductsHasResults() {
        assertFalse(productDao.findProducts(null, SortBy.DESCRIPTION, SortOrder.ASC).isEmpty());
    }

    @Test
    public void testGetProduct() {
        assertEquals("sgs", productDao.get(1L).getCode());
    }

    @Test(expected = ProductNotFoundException.class)
    public void testGetNonExistingProduct() {
        long productsCount = products.size();

        productDao.get(productsCount + 2);
    }

    @Test
    public void testSaveProduct() {
        Product testproduct = new Product("testproduct", "Samsung Galaxy S", new BigDecimal(100), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        productDao.save(testproduct);

        assertTrue(products.stream().
                anyMatch(product -> product.getCode().equals("testproduct")));
    }

    @Test(expected = ProductNotFoundException.class)
    public void testDeleteProduct() {
        Product testproduct = new Product("testproduct", "Samsung Galaxy S", new BigDecimal(100), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(testproduct);

        productDao.delete(testproduct.getId());

        assertFalse(products.stream().
                anyMatch(product -> product.getCode().equals("testproduct")));
        productDao.get(testproduct.getId());
    }

    @Test
    public void testSort() {
        List<Product> sortResults = productDao.findProducts(null, SortBy.DESCRIPTION, SortOrder.ASC);
        Iterator<Product> productIterator = sortResults.iterator();
        Product previousProduct = productIterator.next();
        Product currentProduct;

        while (productIterator.hasNext()) {
            currentProduct = productIterator.next();
            assertTrue(currentProduct.getDescription().compareTo(previousProduct.getDescription()) > 0);
            previousProduct = currentProduct;
        }
    }

    @Test
    public void testTextSearchContainsAllMatching() {
        String searchQuery = "Samsung";
        List<Product> searchResults = productDao.findProducts(searchQuery, SortBy.DESCRIPTION, SortOrder.ASC);
        List<Product> allExpectedProducts = List.of(productDao.get(1L), productDao.get(3L));

        assertTrue(searchResults.containsAll(allExpectedProducts));
    }

    @Test
    public void testTextSearchContainsNoZeroStock() {
        String searchQuery = "Samsung";
        List<Product> searchResults = productDao.findProducts(searchQuery, SortBy.DESCRIPTION, SortOrder.ASC);

        assertFalse(searchResults.contains(productDao.get(2L)));
    }

    @Test
    public void testTextSearchContainsOnlyMatching() {
        String searchQuery = "Samsung S";
        List<String> keyWords = new ArrayList<>(List.of(searchQuery.split(" ")));
        List<Product> searchResults = productDao.findProducts(searchQuery, SortBy.DESCRIPTION, SortOrder.ASC);

        searchResults.forEach(product -> {
            assertTrue(product.getDescription().contains(keyWords.get(0)) || product.getDescription().contains(keyWords.get(1)));
        });
    }

    @Test
    public void testAdvancedEmptySearch() {
        String searchQuery = "";
        List<Product> searchResults = productDao.findProductsAdvanced(searchQuery, SearchMethod.ALL_WORDS,  SortBy.DESCRIPTION, SortOrder.ASC, null, null);

        assertTrue(searchResults.isEmpty());
    }

    @Test
    public void testAdvancedAllWords() {
        String searchQuery = "Samsung S";
        List<String> keyWords = new ArrayList<>(List.of(searchQuery.split(" ")));
        List<Product> searchResults = productDao.findProductsAdvanced(searchQuery, SearchMethod.ALL_WORDS,  SortBy.DESCRIPTION, SortOrder.ASC, null, null);

        searchResults.forEach(product -> {
            assertTrue(product.getDescription().contains(keyWords.get(0)) && product.getDescription().contains(keyWords.get(1)));
        });
    }

    @Test
    public void testAdvancedAnyWord() {
        String searchQuery = "Samsung S";
        List<String> keyWords = new ArrayList<>(List.of(searchQuery.split(" ")));
        List<Product> searchResults = productDao.findProductsAdvanced(searchQuery, SearchMethod.ALL_WORDS,  SortBy.DESCRIPTION, SortOrder.ASC, null, null);

        searchResults.forEach(product -> {
            assertTrue(product.getDescription().contains(keyWords.get(0)) || product.getDescription().contains(keyWords.get(1)));
        });
    }

    @Test
    public void testAdvancedAllWordsPriceRange() {
        String searchQuery = "Samsung S";
        BigDecimal minPrice = new BigDecimal(100);
        BigDecimal maxPrice = new BigDecimal(200);
        List<String> keyWords = new ArrayList<>(List.of(searchQuery.split(" ")));
        List<Product> searchResults = productDao.findProductsAdvanced(searchQuery, SearchMethod.ALL_WORDS,  SortBy.DESCRIPTION, SortOrder.ASC, minPrice, maxPrice);

        searchResults.forEach(product -> {
            assertTrue(product.getDescription().contains(keyWords.get(0)) && product.getDescription().contains(keyWords.get(1)));
            assertTrue(product.getPrice().compareTo(minPrice) >= 0);
            assertTrue(product.getPrice().compareTo(maxPrice) <= 0);
        });
    }

    @Test
    public void testAdvancedAnyWordPriceRange() {
        String searchQuery = "Samsung S";
        BigDecimal minPrice = new BigDecimal(100);
        BigDecimal maxPrice = new BigDecimal(200);
        List<String> keyWords = new ArrayList<>(List.of(searchQuery.split(" ")));
        List<Product> searchResults = productDao.findProductsAdvanced(searchQuery, SearchMethod.ALL_WORDS,  SortBy.DESCRIPTION, SortOrder.ASC, minPrice, maxPrice);

        searchResults.forEach(product -> {
            assertTrue(product.getDescription().contains(keyWords.get(0)) || product.getDescription().contains(keyWords.get(1)));
            assertTrue(product.getPrice().compareTo(minPrice) >= 0);
            assertTrue(product.getPrice().compareTo(maxPrice) <= 0);
        });
    }
}
