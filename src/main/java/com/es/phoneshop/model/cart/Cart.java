package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.cartitem.CartItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {

    private ArrayList<CartItem> items;

    private int totalQuantity;

    private BigDecimal totalPrice;

    public Cart() {
        items = new ArrayList<>();
    }

    public List<CartItem> getCartItems() {
        return items;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Items in cart: ");
        items.forEach(item -> stringBuilder.append(item.toString()).append(';'));
        return stringBuilder.toString();
    }
}
