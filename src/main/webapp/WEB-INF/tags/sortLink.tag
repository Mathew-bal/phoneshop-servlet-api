<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="sortBy" required="true" %>
<%@ attribute name="orderBy" required="true" %>

<a href="?sort=${sortBy}&order=${orderBy}&searchQuery=${param.searchQuery}"
    style="${(sortedByDefault == true and (sortBy eq 'price' and orderBy eq 'desc')) or sortBy eq param.sort and orderBy eq param.order ? 'font-weight: bold' : ' '}">
${orderBy}</a>
