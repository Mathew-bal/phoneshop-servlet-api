<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product Details" bodyClass="product-details">
  <p>
    Details
  </p>
  <c:if test="${not empty alreadyInCartQuantity and alreadyInCartQuantity > 0}">
    <p class="cartinfo">
      In cart: ${alreadyInCartQuantity}
    </p>
  </c:if>
  <p>
    ${product.description}
  </p>
  <c:if test="${not empty error}">
    <p class="error">
      Error in adding product to cart
    </p>
  </c:if>
   <c:if test="${not empty param.message}">
      <p class="success">
        Product added successfully
      </p>
   </c:if>
  <p>
    Code: ${product.code}
  </p>
  <p>
    <img src="${product.imageUrl}">
  </p>
  <p>
     <a href="${pageContext.servletContext.contextPath}/products/pricehistory/${product.id}">
       <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
     </a>
  </p>
  <p>
    Stock: ${product.stock}
  </p>
  <c:if test="${not empty error}">
    <p class="error">
      ${error}
    </p>
  </c:if>
  <form method="post" style="display: inline-grid; grid-template-columns: 1fr; grid-template-rows: 1fr 1fr 1fr; border: solid 1px; padding: 10px;">
    <label for="quantity">Quantity</label>
    <input name="quantity" id="quantity" value="${not empty error ? previousInput : 1}">
    <br>
    <button>Add to cart</button>
  </form>
</tags:master>
