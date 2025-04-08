<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List" bodyClass="product-list">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <form>
    <input name="searchQuery" placeholder="Search query here" value="${param.searchQuery}">
    <button>Search</button>
  </form>
  <c:if test="${not empty param.message}">
    <div class="success">
      ${param.message}
    </div>
  </c:if>
  <c:if test="${not empty param.error}">
    <div class="error">
      Error in adding to cart
    </div>
  </c:if>
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
        <td>Add to cart</td>
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
        <td>
         <form method="post" style="display: inline-grid; grid-template-columns: 2fr 0.05fr 1fr 0.01fr; grid-template-rows: 1fr;">
             <input style="width: 65px;" name="quantity" id="quantity" value="${not empty param.error and product.id eq param.addedId ? param.previousInput : 1}">
             <span> </span>
             <button>Add</button>
             <input type="hidden" name="productAddedId" value="${product.id}">
         </form>
           <c:if test="${not empty param.error and product.id eq param.addedId}">
              <p class="error">
                ${error}
              </p>
           </c:if>
        </td>
      </tr>
    </c:forEach>
  </table>
</tags:master>
