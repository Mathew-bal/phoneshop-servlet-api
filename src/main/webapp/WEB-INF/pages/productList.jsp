<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <form>
    <input name="searchQuery" placeholder="Search query here" value="${param.searchQuery}">
    <button>Search</button>
  </form>
  <table>
    <thead>
      <tr>
        <td>Image</td>
        <td>
            Description
            <tags:sortLink sortBy="description" orderBy="asc" />
            <tags:sortLink sortBy="description" orderBy="desc" />
        </td>
        <td class="price">
            Price
            <tags:sortLink sortBy="price" orderBy="asc" />
            <tags:sortLink sortBy="price" orderBy="desc" />
        </td>
      </tr>
    </thead>
    <c:forEach var="product" items="${products}">
      <tr>
        <td>
            <img class="product-tile" src="${product.imageUrl}">
        </td>
        <td>
            <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
            ${product.description}
            </a>
        </td>
        <td class="price">
            <a href="${pageContext.servletContext.contextPath}/products/pricehistory/${product.id}">
                <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
            </a>
        </td>
      </tr>
    </c:forEach>
  </table>
</tags:master>
