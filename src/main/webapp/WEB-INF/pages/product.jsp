<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product Details">
  <p>
    Details
  </p>
  <p>
    ${product.description}
  </p>
  <p>
    Code: ${product.code}
  </p>
  <p>
    <img src="${product.imageUrl}">
  </p>
  <p>
    <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
  </p>
  <p>
    Stock: ${product.stock}
  </p>
</tags:master>
