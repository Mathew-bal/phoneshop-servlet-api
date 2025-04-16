package com.es.phoneshop.web;

import com.es.phoneshop.dao.implementations.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.implementations.DefaultCartService;
import com.es.phoneshop.service.implementations.DefaultRecentlyViewedService;
import com.es.phoneshop.service.RecentlyViewedService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

public class ProductDetailsPageServlet extends HttpServlet {

    private static final int ID_SUBSTRING_PREFIX_LENGTH = 1;

    private ProductDao productDao;

    private CartService cartService;

    private RecentlyViewedService recentlyViewedService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        recentlyViewedService = DefaultRecentlyViewedService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("product", productDao.get(getProductId(request)));
        request.setAttribute("alreadyInCartQuantity", cartService.getProductQuantity(request.getSession(), getProductId(request)));
        request.setAttribute("error", request.getParameter("error"));
        request.setAttribute("previousInput", request.getParameter("previousInput"));
        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
        recentlyViewedService.addRecentlyViewedProduct(request.getSession(), getProductId(request));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String quantityString = request.getParameter("quantity");
        Long productId = getProductId(request);
        int quantity = parseProductQuantity(request, quantityString);

        try {
            if (quantity > 0) {
                cartService.add(request.getSession(), productId, quantity);
            } else {
                request.setAttribute("error", "Not a valid number");
            }
        } catch (OutOfStockException outOfStockException) {
            request.setAttribute("error", "Out of stock, only available: " + outOfStockException.getStockAvailable());
        }

        if (request.getAttribute("error") == null) {
            response.sendRedirect(request.getContextPath() + "/products/" + productId + "?message=Product added to cart");
        } else {
            response.sendRedirect(request.getContextPath() + "/products/" + productId + "?error=" + request.getAttribute("error") + "&previousInput=" + quantityString);
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

    private static long getProductId(HttpServletRequest request) {
        return Long.parseLong(request.getPathInfo().substring(ID_SUBSTRING_PREFIX_LENGTH));
    }
}
