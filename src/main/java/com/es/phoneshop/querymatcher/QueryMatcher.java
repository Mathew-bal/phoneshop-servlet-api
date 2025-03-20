package com.es.phoneshop.querymatcher;

import com.es.phoneshop.model.product.Product;

import java.util.List;

public interface QueryMatcher {
    double calculateMatchValue(Product product, List<String> keyWords);
}
