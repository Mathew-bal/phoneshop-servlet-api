package com.es.phoneshop.dao;

import com.es.phoneshop.comparator.ProductComparator;
import com.es.phoneshop.enums.SortBy;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.locker.ExtendedReadWriteLock;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.querymatcher.DescriptionQueryMatcher;
import com.es.phoneshop.querymatcher.QueryMatcher;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {

    private static final String CURRENCY = "USD";

    public static final Currency USD_CURRENCY = Currency.getInstance(CURRENCY);

    private static final boolean SEARCH_CASE_SENSITIVE = true;

    private static ProductDao instance;

    public static synchronized ProductDao getInstance() {
        if (instance == null) {
            instance = new ArrayListProductDao();
        }
        return instance;
    }

    private List<Product> products;

    private long maxId = 1;

    private ExtendedReadWriteLock extendedReadWriteLock;

    private ArrayListProductDao() {
        extendedReadWriteLock = new ExtendedReadWriteLock();

        products = new ArrayList<>();
    }

    @Override
    public Product getProduct(Long id) {
        return extendedReadWriteLock.readSafe(() -> products.stream().
                filter(product -> id.equals(product.getId())).
                findAny().orElseThrow(ProductNotFoundException::new));
    }

    private boolean filterHasPriceHasStock(Product product) {
        return product.getPrice() != null && product.getStock() > 0;
    }

    @Override
    public List<Product> findProducts() {
        return extendedReadWriteLock.readSafe(() -> {
            return new ArrayList<>(products);
        });
    }

    @Override
    public List<Product> findProducts(String searchQuery, SortBy sortBy, SortOrder sortOrder) {
        List<String> keyWords = List.of(Optional.ofNullable(searchQuery).orElse("").split(" "));
        QueryMatcher queryMatcher = new DescriptionQueryMatcher(SEARCH_CASE_SENSITIVE);
        Comparator<Product> sortComparator = new ProductComparator(keyWords, sortBy, sortOrder, queryMatcher);

        return extendedReadWriteLock.readSafe(() -> {
            return products.stream().
                    filter(this::filterHasPriceHasStock).
                    filter(product -> Optional.ofNullable(searchQuery).orElse("").isBlank() || queryMatcher.calculateMatchValue(product, keyWords) > 0).
                    sorted(sortComparator).
                    collect(Collectors.toList());
        });
    }

    @Override
    public List<Product> filterProducts() {
        return extendedReadWriteLock.readSafe(() -> {
            return products.stream().
                    filter(this::filterHasPriceHasStock).
                    collect(Collectors.toList());
        });
    }

    @Override
    public void save(Product product) {
        extendedReadWriteLock.writeSafe((saveProduct) -> {
            if (saveProduct.getId() == null) {
                saveProduct.setId(maxId++);
                products.add(saveProduct);
            } else {
                products = products.stream().map((oldProduct) ->
                {
                    if (saveProduct.getId().equals(oldProduct.getId()))
                        return saveProduct;
                    else
                        return oldProduct;
                }).collect(Collectors.toList());
            }
        }, product);
    }

    @Override
    public void delete(Long id) throws ProductNotFoundException {
        extendedReadWriteLock.writeSafe((deleteId) -> {
            products.removeIf(product -> deleteId.equals(product.getId()));
        }, id);
    }
}
