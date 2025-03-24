<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<footer class="footer">
<c:if test="${not empty recentlyViewedProducts and recentlyViewedProducts.size() > 0}">
<h2>
  Recently viewed:
</h2>
<span class="recently-viewed">
<c:forEach items="${recentlyViewedProducts}" var="product">
  <div>
    <img src="${product.imageUrl}">
    <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
      ${product.description}
    </a>
    <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
  </div>
</c:forEach>
</c:if>
</span>
<c:if test="${not empty cart and not empty cart.cartItems and cart.cartItems.size() > 0}">
<h2>
  Cart: <fmt:formatNumber value="${cartPrice}" type="currency" currencySymbol="${cart.cartItems[0].product.currency.symbol}"/>
</h2>
<span class="cart-footer">
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
<p>
  (c) Expert Soft
</p>
</footer>
