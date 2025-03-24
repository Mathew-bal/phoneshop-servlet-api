<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>
<%@ attribute name="bodyClass" required="true" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
  <title>${pageTitle}</title>
  <link href='http://fonts.googleapis.com/css?family=Lobster+Two' rel='stylesheet' type='text/css'>
  <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/main.css">
</head>

<body class="${bodyClass}">
  <header>
    <a href="${pageContext.servletContext.contextPath}">
      <img src="${pageContext.servletContext.contextPath}/images/logo.svg"/>
      PhoneShop
    </a>
  </header>
<c:if test="${not empty cart and not empty cart.cartItems and cart.cartItems.size() > 0}">
<p>
  Cart: <fmt:formatNumber value="${cartPrice}" type="currency" currencySymbol="${cart.cartItems[0].product.currency.symbol}"/>
  <br>
  Products in cart: ${cart.cartItems.size()}
  <br>
</p>
<p class="cart-preview-hover">
  Hover to preview
</p>
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
  <main>
    <jsp:doBody/>
  </main>
</body>
<tags:footer/>
</html>
