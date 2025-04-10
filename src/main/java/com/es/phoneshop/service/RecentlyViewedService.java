package com.es.phoneshop.service;

import com.es.phoneshop.model.product.Product;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface RecentlyViewedService {
    List<Product> getRecentlyViewedProducts(HttpSession session);
    void addRecentlyViewedProduct(HttpSession session, Long productId);
}
