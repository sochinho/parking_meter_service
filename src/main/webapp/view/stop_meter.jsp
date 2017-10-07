<%--
  Created by IntelliJ IDEA.
  User: sochinho
  Date: 05.10.2017
  Time: 12:44
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
            Parking meter has been stopped <br/>
            Driver ${sps.driver.firstName} ${sps.driver.lastName} <br/>
            Vehicle ${sps.vehicle.identifier} <br/>
            Start time ${sps.startDate} <br/>
            End time ${sps.stopDate} <br/>
            Payment ${payment} ${symbol} <br/>
        </p>
    </c:if>
    <c:if test="${empty sps}">
        Something was wrong. Try again.
    </c:if>
</c:if>

<%@include file="../includes/footer.jsp" %>