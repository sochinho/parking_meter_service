package pl.touk.test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class ParkingControllerTest extends JerseyTest {

    static final String testId = RandomStringUtils.random(8);
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
    public void testEmptyDataStartMeter()   {
        MultivaluedMap formData = new MultivaluedMapImpl();
        assertEquals(400, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testNullDataStartMeter()   {
        MultivaluedMap formData = new MultivaluedMapImpl();
        formData.add("fname", "");
        formData.add("lname", "");
        formData.add("vehicle", "");
        formData.add("tdriver", "");
        assertEquals(400, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testShortDataStartMeter()   {
        MultivaluedMap formData = new MultivaluedMapImpl();
        formData.add("fname", "A");
        formData.add("lname", "B");
        formData.add("vehicle", "C");
        formData.add("tdriver", "REGULAR");
        assertEquals(400, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testLongFnameStartMeter()   {
        MultivaluedMap formData = new MultivaluedMapImpl();
        formData.add("fname", StringUtils.repeat("F", 41));
        formData.add("lname", "L" + testId);
        formData.add("vehicle", "V" + testId);
        formData.add("tdriver", "REGULAR");
        assertEquals(400, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testShortVidStartMeter()   {
        MultivaluedMap formData = new MultivaluedMapImpl();
        formData.add("fname", "F" + testId);
        formData.add("lname", "L" + testId);
        formData.add("vehicle", StringUtils.repeat("V", 2));
        formData.add("tdriver", "REGULAR");
        assertEquals(400, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testNullFnameStartMeter()   {
        MultivaluedMap formData = new MultivaluedMapImpl();
        formData.add("fname", "");
        formData.add("lname", "L" + testId);
        formData.add("vehicle", "V" + testId);
        formData.add("tdriver", "REGULAR");
        assertEquals(400, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testNullLnameStartMeter()   {
        MultivaluedMap formData = new MultivaluedMapImpl();
        formData.add("fname", "F" + testId);
        formData.add("lname", "");
        formData.add("vehicle", "V" + testId);
        formData.add("tdriver", "REGULAR");
        assertEquals(400, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testNullVehicleStartMeter()   {
        MultivaluedMap formData = new MultivaluedMapImpl();
        formData.add("fname", "F" + testId);
        formData.add("lname", "L" + testId);
        formData.add("vehicle", "");
        formData.add("tdriver", "REGULAR");
        assertEquals(400, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testNullTdriverStartMeter()   {
        MultivaluedMap formData = new MultivaluedMapImpl();
        formData.add("fname", "F" + testId);
        formData.add("lname", "L" + testId);
        formData.add("vehicle", "V" + testId);
        formData.add("tdriver", "");
        assertEquals(400, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testNotExistingDataStopMeter()   {
        MultivaluedMap formData = new MultivaluedMapImpl();
        formData.add("fname", "F" + testId);
        formData.add("lname", "L" + testId);
        formData.add("vehicle", "V" + testId);
        formData.add("tdriver", "REGULAR");
        assertEquals(400, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testValidDataStartMeter()   {
        MultivaluedMap formData = new MultivaluedMapImpl();
        formData.add("fname", "F" + testId);
        formData.add("lname", "L" + testId);
        formData.add("vehicle", "V" + testId);
        formData.add("tdriver", "REGULAR");
        assertEquals(200, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testSameDataStartMeter()   {
        MultivaluedMap formData = new MultivaluedMapImpl();
        formData.add("fname", "F" + testId);
        formData.add("lname", "L" + testId);
        formData.add("vehicle", "V" + testId);
        formData.add("tdriver", "REGULAR");
        assertEquals(400, controllerResource.path("/parkings/start_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

    @Test
    public void testValidDataStopMeter()   {
        MultivaluedMap formData = new MultivaluedMapImpl();
        formData.add("fname", "F" + testId);
        formData.add("lname", "L" + testId);
        formData.add("vehicle", "V" + testId);
        formData.add("tdriver", "REGULAR");
        assertEquals(200, controllerResource.path("/parkings/stop_meter.go").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData).getStatus());
    }

}
