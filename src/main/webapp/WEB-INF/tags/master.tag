<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>
<%@ attribute name="bodyClass" required="true" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

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
  <main>
    <jsp:doBody/>
  </main>
</body>
<tags:footer/>
</html>
