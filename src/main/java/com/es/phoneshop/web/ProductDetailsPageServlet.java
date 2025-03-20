package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ProductDetailsPageServlet extends HttpServlet {

    private static final int ID_SUBSTRING_PREFIX_LENGTH = 1;

    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("product", productDao.getProduct(getProductId(request)));
        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
    }

    private static long getProductId(HttpServletRequest request) {
        return Long.parseLong(request.getPathInfo().substring(ID_SUBSTRING_PREFIX_LENGTH));
    }
}
