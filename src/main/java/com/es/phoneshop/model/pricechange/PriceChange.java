package com.es.phoneshop.model.pricechange;

import java.math.BigDecimal;
import java.util.Date;

public class PriceChange {

    private BigDecimal price;

    private Date startDate;

    public PriceChange(BigDecimal price, Date startDate) {
        this.price = price;
        this.startDate = startDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
