package com.es.phoneshop.utils.comparators;

import com.es.phoneshop.enums.SortBy;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.querymatcher.QueryMatcher;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ProductComparator implements Comparator<Product> {

    private SortOrder sortOrder;

    private SortBy sortBy;

    private List<String> keyWords;

    private QueryMatcher queryMatcher;

    public ProductComparator(List<String> keyWords, SortBy sortBy, SortOrder sortOrder, QueryMatcher queryMatcher) {
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
        this.keyWords = Optional.ofNullable(keyWords).orElse(new ArrayList<>());
        this.queryMatcher = queryMatcher;
    }

    @Override
    public int compare(Product o1, Product o2) {
        double o1Match = queryMatcher.calculateMatchValue(o1, keyWords);
        double o2Match = queryMatcher.calculateMatchValue(o2, keyWords);

        if (o1Match == o2Match) {
            int orderSign = SortOrder.DESC == sortOrder ? -1 : 1;
            if (SortBy.DESCRIPTION == sortBy) {
                return orderSign * o1.getDescription().compareTo(o2.getDescription());
            } else {
                return orderSign * o1.getPrice().compareTo(o2.getPrice());
            }
        } else {
            return Double.compare(o2Match, o1Match);
        }
    }
}
