package com.es.phoneshop.dao;

import com.es.phoneshop.enums.SearchMethod;
import com.es.phoneshop.enums.SortBy;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.model.product.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao extends GenericDao<Product> {
    List<Product> findProducts();
    List<Product> findProducts(String searchQuery, SortBy sortBy, SortOrder sortOrder);
    public List<Product> findProductsAdvanced(String searchQuery, SearchMethod searchMethod, SortBy sortBy, SortOrder sortOrder,
                                      BigDecimal minPrice, BigDecimal maxPrice);
    List<Product> filterProducts();
}
