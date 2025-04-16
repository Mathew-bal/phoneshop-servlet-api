package com.es.phoneshop.dao;

import com.es.phoneshop.dao.GenericDao;
import com.es.phoneshop.enums.SortBy;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.model.product.Product;

import java.util.List;

public interface ProductDao extends GenericDao<Product> {
    List<Product> findProducts();
    List<Product> findProducts(String searchQuery, SortBy sortBy, SortOrder sortOrder);
    List<Product> filterProducts();
}
