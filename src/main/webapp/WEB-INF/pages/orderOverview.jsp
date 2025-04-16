<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Order overview" bodyClass="order-overview">
<c:if test="${not empty order}">
  <h1>Order overview</h1>
  <c:if test="${not empty param.message}">
      <p class="success" style="font-weight: bold; border: 1px black; border-radius: 3px;">
        ${param.message}
      </p>
  </c:if>
  <h2>Items</h2>
  <table>
      <thead>
        <tr>
          <td>Image</td>
          <td>
              Description
          </td>
          <td>Quantity</td>
          <td class="price">
              Price
          </td>
        </tr>
      </thead>
      <c:forEach var="item" items="${order.cartItems}" varStatus="status">
        <tr>
          <td>
              <img class="product-tile" src="${item.product.imageUrl}">
          </td>
          <td>
              <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
              ${item.product.description}
              </a>
          </td>
          <td>
              ${item.quantity}
          </td>
          <td class="price">
              <a href="${pageContext.servletContext.contextPath}/products/pricehistory/${item.product.id}">
                  <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/>
              </a>
          </td>
        </tr>
      </c:forEach>
    </table>

      <p class="orderinfo">
        <div>
        Products in order: ${order.totalQuantity}
        </div>
        <div>
        Subtotal: <fmt:formatNumber value="${order.subTotal}" type="currency" currencySymbol="${order.cartItems[0].product.currency.symbol}"/>
        </div>
        <div>
        Delivery cost: <fmt:formatNumber value="${order.deliveryCost}" type="currency" currencySymbol="${order.cartItems[0].product.currency.symbol}"/>
        </div>
        <p>
          Total: <fmt:formatNumber value="${order.totalPrice}" type="currency" currencySymbol="${order.cartItems[0].product.currency.symbol}"/>
        </p>
      </p>

    <h2>Your details</h2>
    <table>
        <tags:orderOverviewRow name="firstName" label="First Name" order="${order}" />
        <tags:orderOverviewRow name="lastName" label="Last Name" order="${order}" />
        <tags:orderOverviewRow name="phone" label="Phone" order="${order}" />
        <tags:orderOverviewRow name="deliveryAddress" label="Delivery Address" order="${order}" />
        <tags:orderOverviewRow name="deliveryDate" label="Delivery Date" order="${order}" />
        <tags:orderOverviewRow name="paymentMethod" label="Payment method" order="${order}" />
    </table>
</c:if>
</tags:master>
