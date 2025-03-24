package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.enums.SortBy;
import com.es.phoneshop.enums.SortOrder;
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

public class ProductListPageServlet extends HttpServlet {

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
        String searchQuery = request.getParameter("searchQuery");
        String sortBy = request.getParameter("sort");
        String sortOrder = request.getParameter("order");

        request.setAttribute("recentlyViewedProducts", recentlyViewedService.getRecentlyViewedProducts(request.getSession()));
        request.setAttribute("cart", cartService.getCart(request.getSession()));
        request.setAttribute("cartPrice", cartService.getCartPrice(request.getSession()));

        if (sortBy != null && sortOrder != null) {
            request.setAttribute("products", productDao.findProducts(searchQuery, SortBy.valueOf(sortBy.toUpperCase()), SortOrder.valueOf(sortOrder.toUpperCase())));
            request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
        } else {
            request.setAttribute("products", productDao.findProducts(searchQuery, SortBy.PRICE, SortOrder.DESC));
            request.setAttribute("sortedByDefault", true);
            request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
        }
    }
}
