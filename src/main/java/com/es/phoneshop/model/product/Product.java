package com.es.phoneshop.model.product;

import com.es.phoneshop.model.pricechange.PriceChange;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

public class Product implements Serializable {
    private Long id;
    private String code;
    private String description;
    /**
     * null means there is no price because the product is outdated or new
     */
    private BigDecimal price;
    /**
     * can be null if the price is null
     */
    private Currency currency;
    private int stock;
    private String imageUrl;
    private List<PriceChange> priceChanges;

    public Product() {
    }

    public Product(Long id, String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl, List<PriceChange> priceChanges) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.stock = stock;
        this.imageUrl = imageUrl;

        if (priceChanges == null) {
            this.priceChanges = new ArrayList<>();
            this.priceChanges.add(new PriceChange(price, new Date()));
        } else {
            this.priceChanges = priceChanges;
        }
    }

    public Product(String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl) {
        this(null, code, description, price, currency, stock, imageUrl, null);
    }

    public Product(String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl, List<PriceChange> priceChanges) {
        this(null, code, description, price, currency, stock, imageUrl, priceChanges);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
        priceChanges.add(new PriceChange(price, new Date()));
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<PriceChange> getPriceChanges() {
        return priceChanges;
    }

    public void setPriceChanges(List<PriceChange> priceChanges) {
        this.priceChanges = priceChanges;
    }
}
