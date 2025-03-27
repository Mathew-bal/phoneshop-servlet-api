package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.service.cartservice.CartService;
import com.es.phoneshop.service.cartservice.DefaultCartService;
import com.es.phoneshop.service.recentlyviewedservice.DefaultRecentlyViewedService;
import com.es.phoneshop.service.recentlyviewedservice.RecentlyViewedService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ProductPriceHistoryPageServlet extends HttpServlet {

    private static final int ID_SUBSTRING_PREFIX_LENGTH = 1;

    private ProductDao productDao;

    private RecentlyViewedService recentlyViewedService;

    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        recentlyViewedService = DefaultRecentlyViewedService.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("product", productDao.getProduct(getProductId(request)));
        request.setAttribute("recentlyViewedProducts", recentlyViewedService.getRecentlyViewedProducts(request.getSession()));
        request.setAttribute("cart", cartService.getCart(request.getSession()));
        request.setAttribute("cartPrice", cartService.getCartPrice(request.getSession()));
        request.getRequestDispatcher("/WEB-INF/pages/productPriceHistory.jsp").forward(request, response);
    }

    private static long getProductId(HttpServletRequest request) {
        return Long.parseLong(request.getPathInfo().substring(ID_SUBSTRING_PREFIX_LENGTH));
    }
}
