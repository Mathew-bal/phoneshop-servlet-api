package com.es.phoneshop.web;

import com.es.phoneshop.dao.implementations.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.enums.SortBy;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cartitem.CartItem;
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

public class ProductListPageServlet extends HttpServlet {

    private ProductDao productDao;

    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchQuery = request.getParameter("searchQuery");
        String sortBy = request.getParameter("sort");
        String sortOrder = request.getParameter("order");

        if (sortBy != null && sortOrder != null) {
            request.setAttribute("products", productDao.findProducts(searchQuery, SortBy.valueOf(sortBy.toUpperCase()), SortOrder.valueOf(sortOrder.toUpperCase())));
            request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
        } else {
            request.setAttribute("products", productDao.findProducts(searchQuery, SortBy.PRICE, SortOrder.DESC));
            request.setAttribute("sortedByDefault", true);
            request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String quantityString = request.getParameter("quantity");
        Long addedProductId = getAddedProductId(request);
        int quantity = parseProductQuantity(request, quantityString);

        try {
            if (quantity > 0) {
                cartService.add(request.getSession(), addedProductId, quantity);
            } else {
                request.setAttribute("error", "Not a valid number");
            }
        } catch (OutOfStockException outOfStockException) {
            request.setAttribute("error", "Out of stock, only available: " + outOfStockException.getStockAvailable());
        }

        if (request.getAttribute("error") == null) {
            CartItem addedItem = cartService.getCartItem(request.getSession(), addedProductId).get();
            response.sendRedirect(request.getContextPath() + "/products" + "?message=Product " + addedItem.getProduct().getDescription() + " (x" + addedItem.getQuantity() + ")" + " added to cart");
        } else {
            response.sendRedirect(request.getContextPath() + "/products?error=" + request.getAttribute("error") + "&previousInput=" + quantityString + "&addedId=" + addedProductId);
        }
    }

    private int parseProductQuantity(HttpServletRequest request, String quantityString) {
        try {
            NumberFormat numberFormat = NumberFormat.getInstance(request.getLocale());
            return numberFormat.parse(quantityString).intValue();
        } catch (ParseException parseException) {
            return -1;
        }
    }

    private static long getAddedProductId(HttpServletRequest request) {
        return Long.parseLong(request.getParameter("productAddedId"));
    }
}
