package com.es.phoneshop.comparator;

import com.es.phoneshop.enums.SortBy;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.querymatcher.DescriptionQueryMatcher;
import com.es.phoneshop.querymatcher.QueryMatcher;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ProductComparatorTest
{

    private static final String CURRENCY = "USD";

    private static final Currency USD_CURRENCY = Currency.getInstance(CURRENCY);

    private ProductComparator productComparator;

    private QueryMatcher queryMatcher = new DescriptionQueryMatcher(true);

    @Test
    public void testNoTextQuery() {
        productComparator = new ProductComparator(null, SortBy.PRICE, SortOrder.ASC, queryMatcher);
        Product product1 = new Product("iphone", "Apple iPhone", new BigDecimal(200), USD_CURRENCY, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        Product product2 = new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), USD_CURRENCY, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg");

        assertTrue(productComparator.compare(product1, product2) < 0);
    }

    @Test
    public void testWithTextQuery() {
        productComparator = new ProductComparator(List.of("Apple"), SortBy.PRICE, SortOrder.ASC, queryMatcher);
        Product product1 = new Product("iphone", "Apple iPhone", new BigDecimal(200), USD_CURRENCY, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        Product product2 = new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), USD_CURRENCY, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg");

        assertTrue(productComparator.compare(product1, product2) < 0);
    }

    @Test
    public void testTextQuerySameMatching() {
        productComparator = new ProductComparator(List.of("Apple iPhone".split(" ")), SortBy.PRICE, SortOrder.ASC, queryMatcher);
        Product product1 = new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), USD_CURRENCY, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg");
        Product product2 = new Product("iphone", "Apple iPhone", new BigDecimal(200), USD_CURRENCY, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");

        assertTrue(productComparator.compare(product1, product2) > 0);
    }

    @Test
    public void testWithComplexTextQuery() {
        productComparator = new ProductComparator(List.of("Samsung S".split(" ")), SortBy.PRICE, SortOrder.ASC, queryMatcher);
        Product product1 = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product product2 = new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), USD_CURRENCY, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg");

        assertTrue(productComparator.compare(product1, product2) < 0);
    }
}
