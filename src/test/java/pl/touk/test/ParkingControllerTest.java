package pl.touk.test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class ParkingControllerTest extends JerseyTest {

    String testId = UUID.randomUUID().toString().substring(0, 8);
    final WebResource controllerResource = client().resource("http://localhost:8080/touk_parking_meter").path("/rest");

    public void initDriverFormData(String fname, String lname, String vehicle, String tdriver, MultivaluedMap form) {
        form.add("fname", fname);
        form.add("lname", lname);
        form.add("vehicle", vehicle);
        form.add("tdriver", tdriver);
    }

    public void initOperatorFormData(String vehicle, MultivaluedMap form) {
        form.add("vehicle", vehicle);
    }

    public void initOwnerFormData(String vehicle, MultivaluedMap form) {
        form.add("date", vehicle);
    }

    @Override
    protected AppDescriptor configure() {
        return new WebAppDescriptor.Builder().build();
    }

    @Test
    public void testNullDataStartMeter()   {
        MultivaluedMap formData = new MultivaluedMapImpl();
        assertEquals(422, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testNullDataStopMeter()   {
        MultivaluedMap formData = new MultivaluedMapImpl();
        assertEquals(422, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testNullDriverCheck()   {
        MultivaluedMap formData = new MultivaluedMapImpl();
        assertEquals(422, controllerResource.path("/parkings/driver_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testNullDataOperatorCheck()   {
        MultivaluedMap formData = new MultivaluedMapImpl();
        assertEquals(422, controllerResource.path("/parkings/operator_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testNullDataOwnerCheck()   {
        MultivaluedMap formData = new MultivaluedMapImpl();
        assertEquals(422, controllerResource.path("/parkings/owner_earnings.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testEmptyDataStartMeter()   {
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        initDriverFormData("", "", "", "", driverFormData);
        assertEquals(422, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testEmptyDataStopMeter()   {
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        initDriverFormData("", "", "", "", driverFormData);
        assertEquals(422, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testEmptyDataDriverCheck()   {
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        initDriverFormData("", "", "", "", driverFormData);
        assertEquals(422, controllerResource.path("/parkings/driver_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testEmptyDataOperatorCheck()   {
        MultivaluedMap operatorFormData = new MultivaluedMapImpl();
        initOperatorFormData("", operatorFormData);
        assertEquals(422, controllerResource.path("/parkings/operator_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, operatorFormData).getStatus());
    }

    @Test
    public void testEmptyDataOwnerCheck()   {
        MultivaluedMap ownerFormData = new MultivaluedMapImpl();
        initOwnerFormData("", ownerFormData);
        assertEquals(422, controllerResource.path("/parkings/owner_earnings.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, ownerFormData).getStatus());
    }

    @Test
    public void testShortDataStartMeter()   {
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        initDriverFormData("A", "B", "CC", "REGULAR", driverFormData);
        assertEquals(422, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testShortDataStopMeter()   {
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        initDriverFormData("A", "B", "CC", "REGULAR", driverFormData);
        assertEquals(422, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testShortDataDriverCheck()   {
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        initDriverFormData("A", "B", "CC", "REGULAR", driverFormData);
        assertEquals(422, controllerResource.path("/parkings/driver_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testLongFnameStartMeter()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        initDriverFormData(StringUtils.repeat("F", 41), "L" + testId, "V" + testId, "REGULAR", driverFormData);
        assertEquals(422, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testLongFnameStopMeter()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        initDriverFormData(StringUtils.repeat("F", 41), "L" + testId, "V" + testId, "REGULAR", driverFormData);
        assertEquals(422, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testLongFnameDriverCheck()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        initDriverFormData(StringUtils.repeat("F", 41), "L" + testId, "V" + testId, "REGULAR", driverFormData);
        assertEquals(422, controllerResource.path("/parkings/driver_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testShortVidStartMeter()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap formData = new MultivaluedMapImpl();
        initDriverFormData("F" + testId, "L" + testId, StringUtils.repeat("V", 2), "REGULAR", formData);
        assertEquals(422, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testShortVidStopMeter()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap formData = new MultivaluedMapImpl();
        initDriverFormData("F" + testId, "L" + testId, StringUtils.repeat("V", 2), "REGULAR", formData);
        assertEquals(422, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testShortVidDriverCheck()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap formData = new MultivaluedMapImpl();
        initDriverFormData("F" + testId, "L" + testId, StringUtils.repeat("V", 2), "REGULAR", formData);
        assertEquals(422, controllerResource.path("/parkings/driver_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testShortVidOperatorCheck()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap formData = new MultivaluedMapImpl();
        initOwnerFormData(StringUtils.repeat("V", 2), formData);
        assertEquals(422, controllerResource.path("/parkings/operator_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testLongVidStartMeter()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap formData = new MultivaluedMapImpl();
        initDriverFormData("F" + testId, "L" + testId, StringUtils.repeat("V", 11), "REGULAR", formData);
        assertEquals(422, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testLongVidStopMeter()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap formData = new MultivaluedMapImpl();
        initDriverFormData("F" + testId, "L" + testId, StringUtils.repeat("V", 11), "REGULAR", formData);
        assertEquals(422, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testLongVidDriverCheck()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap formData = new MultivaluedMapImpl();
        initDriverFormData("F" + testId, "L" + testId, StringUtils.repeat("V", 11), "REGULAR", formData);
        assertEquals(422, controllerResource.path("/parkings/driver_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testLongVidOperatorCheck()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap formData = new MultivaluedMapImpl();
        initOwnerFormData(StringUtils.repeat("V", 11), formData);
        assertEquals(422, controllerResource.path("/parkings/operator_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testEmptyFnameStartMeter()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap formData = new MultivaluedMapImpl();
        initDriverFormData("", "L" + testId, "V" + testId, "REGULAR", formData);
        assertEquals(422, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testEmptyFnameStopMeter()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap formData = new MultivaluedMapImpl();
        initDriverFormData("", "L" + testId, "V" + testId, "REGULAR", formData);
        assertEquals(422, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testEmptyFnameDriverCheck()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap formData = new MultivaluedMapImpl();
        initDriverFormData("", "L" + testId, "V" + testId, "REGULAR", formData);
        assertEquals(422, controllerResource.path("/parkings/driver_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testEmptyLnameStartMeter()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap formData = new MultivaluedMapImpl();
        initDriverFormData("F" + testId, "", "V" + testId, "REGULAR", formData);
        assertEquals(422, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testEmptyLnameStopMeter()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap formData = new MultivaluedMapImpl();
        initDriverFormData("F" + testId, "", "V" + testId, "REGULAR", formData);
        assertEquals(422, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testEmptyLnameDriverCheck()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap formData = new MultivaluedMapImpl();
        initDriverFormData("F" + testId, "", "V" + testId, "REGULAR", formData);
        assertEquals(422, controllerResource.path("/parkings/driver_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testEmptyVehicleStartMeter()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        initDriverFormData("F" + testId, "L" + testId, "", "REGULAR", driverFormData);
        assertEquals(422, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testEmptyVehicleStopMeter()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        initDriverFormData("F" + testId, "L" + testId, "", "REGULAR", driverFormData);
        assertEquals(422, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testEmptyVehicleDriverCheck()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        initDriverFormData("F" + testId, "L" + testId, "", "REGULAR", driverFormData);
        assertEquals(422, controllerResource.path("/parkings/driver_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testEmptyTdriverStartMeter()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        initDriverFormData("F" + testId, "L" + testId, "V" + testId, "", driverFormData);
        assertEquals(422, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testUnknownTdriverStartMeter()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        initDriverFormData("F" + testId, "L" + testId, "V" + testId, UUID.randomUUID().toString().substring(0, 8), driverFormData);
        assertEquals(422, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testNotExistingDataStopMeter()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        initDriverFormData("F" + testId, "L" + testId, "V" + testId, "REGULAR", driverFormData);
        assertEquals(400, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testNotExistingDataDriverCheck()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        initDriverFormData("F" + testId, "L" + testId, "V" + testId, "REGULAR", driverFormData);
        assertEquals(400, controllerResource.path("/parkings/driver_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testDuplicatedDataStartMeter()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        initDriverFormData("F" + testId, "L" + testId, "V" + testId, "REGULAR", driverFormData);
        assertEquals(200, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(400, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(200, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testValidDataStopMeter()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        initDriverFormData("F" + testId, "L" + testId, "V" + testId, "REGULAR", driverFormData);
        assertEquals(200, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(200, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testValidDataStartStopCheckMeter()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        initDriverFormData("F" + testId, "L" + testId, "V" + testId, "REGULAR", driverFormData);
        assertEquals(200, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(200, controllerResource.path("/parkings/driver_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(200, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(200, controllerResource.path("/parkings/driver_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testValidDateFormatOwnerCheck()   {
        MultivaluedMap dateFormData = new MultivaluedMapImpl();
        dateFormData.add("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        assertEquals(200, controllerResource.path("/parkings/owner_earnings.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, dateFormData).getStatus());
    }

    @Test
    public void testInvalidDateFormatOwnerCheck()   {
        MultivaluedMap dateFormData = new MultivaluedMapImpl();
        dateFormData.add("date", new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        assertEquals(422, controllerResource.path("/parkings/owner_earnings.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, dateFormData).getStatus());
    }

}
