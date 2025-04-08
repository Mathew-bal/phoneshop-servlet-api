package com.es.phoneshop.web;

import com.es.phoneshop.service.recentlyviewedservice.DefaultRecentlyViewedService;
import com.es.phoneshop.service.recentlyviewedservice.RecentlyViewedService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class RecentlyViewedServlet extends HttpServlet {

    private RecentlyViewedService recentlyViewedService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        recentlyViewedService = DefaultRecentlyViewedService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("recentlyViewedProducts", recentlyViewedService.getRecentlyViewedProducts(request.getSession()));
        request.getRequestDispatcher("/WEB-INF/pages/recentlyViewed.jsp").include(request, response);
    }
}
