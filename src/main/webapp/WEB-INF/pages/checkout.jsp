<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Checkout" bodyClass="checkout">
<c:if test="${not empty order}">
  <c:if test="${not empty errors}">
      <p class="error">
        Error in placing order
      </p>
  </c:if>
  <c:if test="${not empty param.message}">
      <p class="success" style="font-weight: bold; border: 1px black; border-radius: 3px;">
        ${param.message}
      </p>
  </c:if>
  <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
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
        <tags:orderFormRow name="firstName" label="First Name" order="${order}" errors="${errors}" />
        <tags:orderFormRow name="lastName" label="Last Name" order="${order}" errors="${errors}" />
        <tags:orderFormRow name="phone" label="Phone" order="${order}" errors="${errors}" />
        <tags:orderFormRow name="deliveryAddress" label="Delivery Address" order="${order}" errors="${errors}" />

        <tr>
          <td>
            Delivery date
          </td>
          <td>
            <c:set var="error" value="${errors['deliveryDate']}" />
            <input
              type="date"
              id="deliveryDate"
              name="deliveryDate"
              value="${not empty previousDeliveryDate ? previousDeliveryDate : order.deliveryDate}"
              min="${deliveryDateInterval.start}"
              max="${deliveryDateInterval.end}" />
            <c:if test="${not empty error}">
              <div class="error">
                ${error}
              </div>
            </c:if>
          </td>
        </tr>

        <tr>
        <td>
          Payment method
        </td>
          <td>
            <c:set var="error" value="${errors['paymentMethod']}" />
            <select name="paymentMethod" >
              <option></option>
                <c:forEach var="method" items="${paymentMethods}">
                  <option ${not empty previousPaymentMethod and previousPaymentMethod eq method ? 'selected' : ''}>${method}</option>
              </c:forEach>
            </select>
            <c:if test="${not empty error}">
              <div class="error">
                ${error}
              </div>
            </c:if>
          </td>
        </tr>
    </table>

    <p>
      <button>Place order</button>
    </p>
    </form>
</c:if>
</tags:master>
