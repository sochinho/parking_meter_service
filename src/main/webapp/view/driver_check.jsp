<%--
  Created by IntelliJ IDEA.
  User: sochinho
  Date: 05.10.2017
  Time: 14:39
  To change this template use File | Settings | File Templates.
--%>
<%@include file="../includes/header.jsp" %>

<c:if test="${not empty error}">
    <p class="error">
        Error: ${error}
    </p>
</c:if>
<c:if test="${empty error}">
    <c:if test="${not empty sps}">
    <p class="info">
        Driver: ${sps.driver.firstName} ${sps.driver.lastName} <br/>
        Vehicle: ${sps.vehicle.identifier} <br/>
        Start time: ${sps.startDate} <br/>
        End date: ${sps.stopDate} <br/>
        Current payment - ${payment} ${symbol}
    </p>
    </c:if>
    <c:if test="${empty sps}">
        <p class="error">
            Something was wrong. Try again.
        </p>
    </c:if>
</c:if>

<%@include file="../includes/footer.jsp" %>
