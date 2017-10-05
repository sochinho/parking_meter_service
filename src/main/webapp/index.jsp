<%--
  Created by IntelliJ IDEA.
  User: sochinho
  Date: 30.09.2017
  Time: 23:56
  To change this template use File | Settings | File Templates.
--%>
<%@include file="includes/header.jsp" %>

    <div class="driver">
        <h1>Driver</h1>
        <h3>Start and stop parking meter.</h3>
        <form name="driver" method="post">

            <p><label for="fname"><input name="fname" type="text" placeholder="First name"/></label></p>
            <p><label for="lname"><input name="lname" type="text" placeholder="Last name"/></label></p>
            <p><label for="vehicle"><input name="vehicle" placeholder="Vehicle ID"type="text"/></label></p>
            <p><label for="radio"><input type="radio" name="tdriver" value="REGULAR" checked>REGULAR</label>
                <label for="radio2"><input type="radio" name="tdriver" value="VIP">VIP</label></p>
            <p><label for="submit"><input name="start" type="submit" value="Start" onclick="form.action='rest/parkings/start_meter.go'"/>
                <input name="stop" type="submit" value="Stop" onclick="form.action='rest/parkings/stop_meter.go'"/></label>
                <input name="check" type="submit" value="Check" onclick="form.action='rest/parkings/driver_check.go'"/></p>

        </form>
    </div>

    <hr/>

    <div class="operator">
        <h1>Operator</h1>
        <h3>Check vehicle has started parking meter.</h3>
        <form name="operator" action="rest/parkings/operator_check.go" method="post">

            <label for="vehicle"><input name="vehicle" type="text" placeholder="Vehicle ID"/></label></br>
            <label for="submit"><input name="check" type="submit" value="Check"/></label>

        </form>
    </div>

    <hr/>

    <div class="owner">
        <h1>Owner</h1>
        <h3>Check earned money for given day</h3>
        <form name="owner" action="rest/parkings/owner_earnings.go" method="post">

            <label for="password"><input name="date" type="date" placeholder="Select day"/></label></br>
            <label for="submit"><input name="check" type="submit" value="Check"/></label>

        </form>
    </div>

<%@include file="includes/footer.jsp" %>