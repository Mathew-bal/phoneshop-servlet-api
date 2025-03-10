package com.es.phoneshop.model.product;

import java.util.List;

public interface ProductDao {
    Product getProduct(Long id) throws ProductNotFoundException;
    List<Product> findProducts() throws ProductNotFoundException;
    List<Product> filterProducts() throws ProductNotFoundException;
    boolean save(Product product) throws ProductNotFoundException;
    boolean delete(Long id) throws ProductNotFoundException;
}
