<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Price history" bodyClass="product-price-history">
  <h2>
    ${product.description}
  </h2>
  <p>
    Price history
  </p>
     <table>
       <thead>
         <tr>
           <td>Start date</td>
           <td>Price</td>
         </tr>
       </thead>
       <c:forEach var="priceChange" items="${product.priceChanges}">
         <tr>
           <td>
             <fmt:formatDate value="${priceChange.startDate}" pattern="dd-MM-yyyy" />
           </td>
           <td class="price">
             <fmt:formatNumber value="${priceChange.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
           </td>
         </tr>
       </c:forEach>
     </table>
</tags:master>
