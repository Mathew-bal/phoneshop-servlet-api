<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:if test="${not empty cart and not empty cart.cartItems and cart.cartItems.size() > 0}">
<p>
  Cart: <fmt:formatNumber value="${cart.totalPrice}" type="currency" currencySymbol="${cart.cartItems[0].product.currency.symbol}"/>
  <br>
  Products in cart: ${cart.totalQuantity}
  <br>
</p>
<a href="${pageContext.servletContext.contextPath}/cart" class="cart-preview-hover">
  <p>
    Go to cart (Hover to preview)
  </p>
</a>
<span class="cart-preview">
<c:forEach items="${cart.cartItems}" var="item" begin="0" end="2">
  <div>
    <img src="${item.product.imageUrl}">
    <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
      ${item.product.description}
    </a>
    <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/>
    <span class="cartinfo">
      x${item.quantity}
    </span>
  </div>
</c:forEach>
<c:if test="${cart.cartItems.size() > 3}">
  <div>
    <p>
    And more...
    </p>
  </div>
</c:if>
</c:if>
</span>
