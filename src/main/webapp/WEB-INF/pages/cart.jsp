<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Cart" bodyClass="cart">
<c:if test="${not empty cart}">
  <p>
    Cart: <fmt:formatNumber value="${cart.totalPrice}" type="currency" currencySymbol="${cart.cartItems[0].product.currency.symbol}"/>
    <br>
    Products in cart: ${cart.totalQuantity}
    <br>
  </p>
  <c:if test="${not empty errors}">
      <p class="error">
        Error in adding products to cart
      </p>
  </c:if>
  <c:if test="${not empty param.message}">
        <p class="success" style="font-weight: bold; border: 1px black; border-radius: 3px;">
          ${param.message}
        </p>
  </c:if>
  <form method="post" action="${pageContext.servletContext.contextPath}/cart">
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
          <td></td>
        </tr>
      </thead>
      <c:forEach var="item" items="${cart.cartItems}" varStatus="status">
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
              <fmt:formatNumber value="${item.quantity}" var="quantity"/>
              <c:set var="error" value="${errors[item.product.id]}" />
              <c:set var="message" value="${messages[item.product.id]}" />
              <input name="quantity" value="${not empty error ? paramValues['quantity'][status.index] : item.quantity}">
              <input type="hidden" name="productId" value="${item.product.id}" >
              <c:if test="${not empty error}">
                <div class="error">
                  ${error}
                </div>
              </c:if>
              <c:if test="${not empty message}">
                 <div class="success">
                   ${message}
                 </div>
              </c:if>
          </td>
          <td class="price">
              <a href="${pageContext.servletContext.contextPath}/products/pricehistory/${item.product.id}">
                  <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/>
              </a>
          </td>
          <td>
            <button form="deleteCartItem" formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${item.product.id}" >Delete</button>
          </td>
        </tr>
      </c:forEach>
    </table>
    <p>
      <button>Update</button>
     </p>
    </form>
    <form action="${pageContext.servletContext.contextPath}/checkout" method="GET">
       <button>Checkout</button>
    </form>
    <form id="deleteCartItem" method="post">
    </form>
</c:if>
</tags:master>
