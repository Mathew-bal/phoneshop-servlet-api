<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<div>
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
</span>
</c:if>
</div>
