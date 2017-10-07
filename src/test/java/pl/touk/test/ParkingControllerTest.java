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

    @Override
    protected AppDescriptor configure() {
        return new WebAppDescriptor.Builder().build();
    }

    @Test
    public void testGetAllStops()   {
        assertNotNull("SingleParkingStop list return", controllerResource.path("/parkings").get(List.class));
        assertEquals(200, controllerResource.path("/parkings").get(ClientResponse.class).getStatus());
    }

    @Test
    public void testNullData()   {
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        assertEquals(422, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(422, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(422, controllerResource.path("/parkings/driver_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(422, controllerResource.path("/parkings/operator_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(422, controllerResource.path("/parkings/owner_earnings.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testEmptyData()   {
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        driverFormData.add("fname", "");
        driverFormData.add("lname", "");
        driverFormData.add("vehicle", "");
        driverFormData.add("tdriver", "");
        assertEquals(422, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(422, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(422, controllerResource.path("/parkings/driver_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testShortData()   {
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        driverFormData.add("fname", "A");
        driverFormData.add("lname", "B");
        driverFormData.add("vehicle", "CCC");
        driverFormData.add("tdriver", "REGULAR");
        assertEquals(422, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(422, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(422, controllerResource.path("/parkings/driver_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testLongFname()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        driverFormData.add("fname", StringUtils.repeat("F", 41));
        driverFormData.add("lname", "L" + testId);
        driverFormData.add("vehicle", "V" + testId);
        driverFormData.add("tdriver", "REGULAR");
        assertEquals(422, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(422, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(422, controllerResource.path("/parkings/driver_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testShortVid()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap formData = new MultivaluedMapImpl();
        formData.add("fname", "F" + testId);
        formData.add("lname", "L" + testId);
        formData.add("vehicle", StringUtils.repeat("V", 2));
        formData.add("tdriver", "REGULAR");
        assertEquals(422, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
        assertEquals(422, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
        assertEquals(422, controllerResource.path("/parkings/driver_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
        assertEquals(422, controllerResource.path("/parkings/operator_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testLongVid()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap formData = new MultivaluedMapImpl();
        formData.add("fname", "F" + testId);
        formData.add("lname", "L" + testId);
        formData.add("vehicle", StringUtils.repeat("V", 11));
        formData.add("tdriver", "REGULAR");
        assertEquals(422, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
        assertEquals(422, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
        assertEquals(422, controllerResource.path("/parkings/driver_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
        assertEquals(422, controllerResource.path("/parkings/operator_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testEmptyFname()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        driverFormData.add("fname", "");
        driverFormData.add("lname", "L" + testId);
        driverFormData.add("vehicle", "V" + testId);
        driverFormData.add("tdriver", "REGULAR");
        assertEquals(422, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(422, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(422, controllerResource.path("/parkings/driver_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testEmptyLname()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        driverFormData.add("fname", "F" + testId);
        driverFormData.add("lname", "");
        driverFormData.add("vehicle", "V" + testId);
        driverFormData.add("tdriver", "REGULAR");
        assertEquals(422, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(422, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(422, controllerResource.path("/parkings/driver_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testEmptyVehicle()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        driverFormData.add("fname", "F" + testId);
        driverFormData.add("lname", "L" + testId);
        driverFormData.add("vehicle", "");
        driverFormData.add("tdriver", "REGULAR");
        assertEquals(422, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(422, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(422, controllerResource.path("/parkings/driver_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testEmptyTdriverStartMeter()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        driverFormData.add("fname", "F" + testId);
        driverFormData.add("lname", "L" + testId);
        driverFormData.add("vehicle", "V" + testId);
        driverFormData.add("tdriver", "");
        assertEquals(422, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testUnknownTdriverStartMeter()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        driverFormData.add("fname", "F" + testId);
        driverFormData.add("lname", "L" + testId);
        driverFormData.add("vehicle", "V" + testId);
        driverFormData.add("tdriver", UUID.randomUUID().toString().substring(0, 8));
        assertEquals(422, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testNotExistingDataStopCheckMeter()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        driverFormData.add("fname", "F" + testId);
        driverFormData.add("lname", "L" + testId);
        driverFormData.add("vehicle", "V" + testId);
        driverFormData.add("tdriver", "REGULAR");
        assertEquals(400, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(400, controllerResource.path("/parkings/driver_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testDuplicatedDataStartMeter()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        driverFormData.add("fname", "F" + testId);
        driverFormData.add("lname", "L" + testId);
        driverFormData.add("vehicle", "V" + testId);
        driverFormData.add("tdriver", "REGULAR");
        assertEquals(200, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(400, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(200, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testValidDataStopMeter()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        driverFormData.add("fname", "F" + testId);
        driverFormData.add("lname", "L" + testId);
        driverFormData.add("vehicle", "V" + testId);
        driverFormData.add("tdriver", "REGULAR");
        assertEquals(200, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(200, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testValidDataCheckMeter()   {
        testId = UUID.randomUUID().toString().substring(0, 8);
        MultivaluedMap driverFormData = new MultivaluedMapImpl();
        driverFormData.add("fname", "F" + testId);
        driverFormData.add("lname", "L" + testId);
        driverFormData.add("vehicle", "V" + testId);
        driverFormData.add("tdriver", "REGULAR");
        assertEquals(200, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(200, controllerResource.path("/parkings/driver_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(200, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(200, controllerResource.path("/parkings/driver_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(200, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(200, controllerResource.path("/parkings/driver_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(200, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
        assertEquals(200, controllerResource.path("/parkings/driver_check.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, driverFormData).getStatus());
    }

    @Test
    public void testValidDataOwnerCheck()   {
        MultivaluedMap dateFormData = new MultivaluedMapImpl();
        dateFormData.add("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        assertEquals(200, controllerResource.path("/parkings/owner_earnings.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, dateFormData).getStatus());
    }

    @Test
    public void testInvalidDataOwnerCheck()   {
        MultivaluedMap dateFormData = new MultivaluedMapImpl();
        dateFormData.add("date", new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        assertEquals(422, controllerResource.path("/parkings/owner_earnings.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, dateFormData).getStatus());
    }

}
