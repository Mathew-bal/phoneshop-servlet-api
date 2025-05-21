package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.implementations.DefaultCartService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

public class CartPageServlet extends HttpServlet {

    private static final int ID_SUBSTRING_PREFIX_LENGTH = 1;

    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setAttribute("cart", cartService.getCart(request.getSession()));
        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productIdStrings = request.getParameterValues("productId");
        String[] quantityStrings = request.getParameterValues("quantity");
        if (productIdStrings == null || productIdStrings.length == 0 || quantityStrings == null || quantityStrings.length == 0) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }
        ListIterator<String> productIdIterator = Arrays.stream(productIdStrings).toList().listIterator();
        ListIterator<String> quantityIterator = Arrays.stream(quantityStrings).toList().listIterator();

        Map<Long, String> errors = new HashMap<>();
        Map<Long, String> messages = new HashMap<>();

        while (productIdIterator.hasNext() && quantityIterator.hasNext()) {
            Long productId = Long.parseLong(productIdIterator.next());
            int quantity = parseProductQuantity(request, quantityIterator.next());
            try {
                if (quantity >= 0) {
                    cartService.update(request.getSession(), productId, quantity);
                    if (cartService.getProductQuantity(request.getSession(), productId) != 0) {
                        messages.put(productId, "Quantity updated successfully");
                    }
                } else {
                    errors.put(productId, "Not a valid number");
                }
            } catch (OutOfStockException outOfStockException) {
                errors.put(productId, "Out of stock, only available: " + outOfStockException.getStockAvailable());
            }
        }

        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart?message=All changed items updated successfully");
            return;
        }
        request.setAttribute("errors", errors);
        request.setAttribute("messages", messages);
        doGet(request, response);
    }

    private int parseProductQuantity(HttpServletRequest request, String quantityString) {
        try {
            NumberFormat numberFormat = NumberFormat.getInstance(request.getLocale());
            return numberFormat.parse(quantityString).intValue();
        } catch (ParseException parseException) {
            return -1;
        }
    }
}
