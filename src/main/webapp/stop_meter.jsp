<%--
  Created by IntelliJ IDEA.
  User: sochinho
  Date: 05.10.2017
  Time: 12:44
  To change this template use File | Settings | File Templates.
--%>
<%@include file="includes/header.jsp" %>

<c:if test="${not empty error}">
    Error: ${error}
</c:if>
<c:if test="${empty errors}">
    <c:if test="${not empty sps}">
        Udalo sie zaczac
    </c:if>
    <c:if test="${empty sps}">
        Something was wrong. Try again.
    </c:if>
</c:if>

<%@include file="includes/footer.jsp" %>