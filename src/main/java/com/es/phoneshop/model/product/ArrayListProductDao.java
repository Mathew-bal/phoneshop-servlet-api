package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {

    private static final String CURRENCY = "USD";

    private static final Currency USD_CURRENCY = Currency.getInstance(CURRENCY);

    private List<Product> products;

    private long maxId = 1;

    private ExtendedReadWriteLock extendedReadWriteLock;

    public ArrayListProductDao() {
        extendedReadWriteLock = new ExtendedReadWriteLock();

        products = new ArrayList<>();
        saveSampleProducts();
    }

    @Override
    public Product getProduct(Long id){
        return extendedReadWriteLock.readSafe(() -> products.stream().
                filter(product -> id.equals(product.getId())).
                findAny().orElseThrow(ProductNotFoundException::new));
    }

    @Override
    public List<Product> findProducts(){
        return extendedReadWriteLock.readSafe(() ->
        {
            return new ArrayList<>(products);
        });
    }

    private static boolean filterHasPriceHasStock(Product product){
        return product.getPrice() != null && product.getStock() > 0;
    }

    public List<Product> filterProducts(){
        return extendedReadWriteLock.readSafe(() -> {
            return products.stream().
                    filter(ArrayListProductDao::filterHasPriceHasStock).
                    collect(Collectors.toList());
        });
    }

    @Override
    public void save(Product product){
        extendedReadWriteLock.writeSafe((saveProduct) ->{
            if (saveProduct.getId() == null){
                saveProduct.setId(maxId++);
                products.add(saveProduct);
            }else {
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
    public void delete(Long id) throws ProductNotFoundException{
        extendedReadWriteLock.writeSafe((deleteId) -> {
            products.removeIf(product -> deleteId.equals(product.getId()));
       }, id);
    }

    private void saveSampleProducts(){
        save(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        save(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), USD_CURRENCY, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        save(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), USD_CURRENCY, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        save(new Product("iphone", "Apple iPhone", new BigDecimal(200), USD_CURRENCY, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        save(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), USD_CURRENCY, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        save(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), USD_CURRENCY, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));
        save(new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), USD_CURRENCY, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        save(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(120), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg"));
        save(new Product("nokia3310", "Nokia 3310", new BigDecimal(70), USD_CURRENCY, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));
        save(new Product("palmp", "Palm Pixi", new BigDecimal(170), USD_CURRENCY, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        save(new Product("simc56", "Siemens C56", new BigDecimal(70), USD_CURRENCY, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
        save(new Product("simc61", "Siemens C61", new BigDecimal(80), USD_CURRENCY, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        save(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), USD_CURRENCY, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));
    }
}
