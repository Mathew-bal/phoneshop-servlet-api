package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.cartitem.CartItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {

    private ArrayList<CartItem> items;

    public Cart() {
        items = new ArrayList<>();
    }

    public List<CartItem> getCartItems() {
        return items;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Items in cart: ");
        items.forEach(item -> stringBuilder.append(item.toString()).append(';'));
        return stringBuilder.toString();
    }
}
