<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="order" required="true" type="com.es.phoneshop.model.order.Order" %>
<%@ attribute name="errors" required="true" type="java.util.Map"%>

<tr>
  <td>${label}<span style="color: red;">*</span></td>
  <c:set var="error" value="${errors[name]}" />
  <td>
  <input name="${name}" value="${not empty error ? param[name] : order[name]}" />
    <c:if test="${not empty error}">
      <div class="error">
        ${error}
      </div>
    </c:if>
  </td>
</tr>
