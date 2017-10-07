<%--
  Created by IntelliJ IDEA.
  User: sochinho
  Date: 04.10.2017
  Time: 02:45
  To change this template use File | Settings | File Templates.
--%>

<%@include file="../includes/header.jsp" %>

<c:if test="${not empty sps}">
    <p class="info">
        Vehicle: ${sps.vehicle.identifier} has started parking meter from ${sps.startDate}
    </p>
</c:if>
<c:if test="${empty sps}">
    <p class="info">
        Vehicle: ${vid} has not started parking meter
    </p>
</c:if>

<%@include file="../includes/footer.jsp" %>