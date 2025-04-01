package com.es.phoneshop.web;

import com.es.phoneshop.service.cartservice.CartService;
import com.es.phoneshop.service.cartservice.DefaultCartService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DeleteCartItemServlet extends HttpServlet {

    private static final int ID_SUBSTRING_PREFIX_LENGTH = 1;

    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = getProductId(request);

        cartService.delete(request.getSession(), productId);

        response.sendRedirect(request.getContextPath() + "/cart?Cart item removed successfully");
    }

    private static long getProductId(HttpServletRequest request) {
        return Long.parseLong(request.getPathInfo().substring(ID_SUBSTRING_PREFIX_LENGTH));
    }
}
