package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.OrderService;
import com.es.phoneshop.service.implementations.DefaultCartService;
import com.es.phoneshop.service.implementations.DefaultOrderService;
import com.es.phoneshop.utils.CheckoutParamsSetterUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CheckoutPageServlet extends HttpServlet {

    private OrderService orderService;

    private CartService cartService;

    private CheckoutParamsSetterUtil paramsSetterUtil;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        orderService = DefaultOrderService.getInstance();
        cartService = DefaultCartService.getInstance();
        paramsSetterUtil = CheckoutParamsSetterUtil.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("order", orderService.getOrder(cartService.getCart(request.getSession())));
        request.setAttribute("paymentMethods", orderService.getPaymentMethods());
        request.setAttribute("deliveryDateInterval", orderService.calculateDeliveryDateInterval((Order)request.getAttribute("order")));
        request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request.getSession());
        Order order = orderService.getOrder(cart);
        Map<String, String> errors = new HashMap<>();

        paramsSetterUtil.setCheckoutParams(order, request, errors);

        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            cartService.clearCart(request.getSession());
            response.sendRedirect(request.getContextPath() + "/order/overview/" + order.getSecureId());
        } else {
            request.setAttribute("errors", errors);
            request.setAttribute("order", order);
            request.setAttribute("deliveryDateInterval", orderService.calculateDeliveryDateInterval((Order)request.getAttribute("order")));
            request.setAttribute("paymentMethods", orderService.getPaymentMethods());
            request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
        }
    }
}
