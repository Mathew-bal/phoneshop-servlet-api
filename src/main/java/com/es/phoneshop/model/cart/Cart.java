package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.baseentity.BaseEntity;
import com.es.phoneshop.model.cartitem.CartItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Cart extends BaseEntity<Long> implements Serializable {

    private ArrayList<CartItem> items;

    private int totalQuantity;

    private BigDecimal totalPrice;

    public Cart() {
        items = new ArrayList<>();
    }

    public ArrayList<CartItem> getCartItems() {
        return items;
    }

    public void setItems(ArrayList<CartItem> items) {
        this.items = items;
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
