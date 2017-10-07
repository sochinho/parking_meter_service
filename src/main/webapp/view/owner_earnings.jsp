<%--
  Created by IntelliJ IDEA.
  User: sochinho
  Date: 05.10.2017
  Time: 15:16
  To change this template use File | Settings | File Templates.
--%>
<%@include file="../includes/header.jsp" %>

<c:if test="${not empty error}">
    <p class="error">
        Error: ${error}
    </p>
</c:if>
<c:if test="${empty errors}">
    <c:if test="${not empty date}">
        <p class="info">
            The parking during ${date} earned ${earnings} ${symbol}
        </p>
    </c:if>
    <c:if test="${empty date}">
        <p class="error">
            Something was wrong. Try again.
        </p>
    </c:if>
</c:if>

<%@include file="../includes/footer.jsp" %>
