package com.es.phoneshop.dao;

import com.es.phoneshop.enums.SortBy;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.model.product.Product;

import java.util.List;

public interface ProductDao {
    Product getProduct(Long id);
    List<Product> findProducts();
    List<Product> findProducts(String searchQuery, SortBy sortBy, SortOrder sortOrder);
    List<Product> filterProducts();
    void save(Product product);
    void delete(Long id);
}
