package pl.touk;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.FormParam;

import com.sun.jersey.api.view.Viewable;

import pl.touk.entities.*;
import pl.touk.dao.SingleParkingStopDao;
import pl.touk.exceptions.DuplicateRowException;
import pl.touk.exceptions.InvalidInputException;
import pl.touk.exceptions.NoRowException;
import pl.touk.types.DriverType;
import pl.touk.utilis.FinancialCalculator;
import pl.touk.validation.InputDataValidator;

@Path("/parkings")
public class ParkingController {
    @Context
    UriInfo uriInfo;

    InputDataValidator dataValidator= InputDataValidator.getInstance();
    List<String> errors = new ArrayList<String>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<SingleParkingStop> getAkkParkingStops() {
        SingleParkingStopDao spsDao = SingleParkingStopDao.getInstance();
        return spsDao.getParkingAllStops();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SingleParkingStop getParkingStopById(@PathParam("id") int id) {
        SingleParkingStopDao spsDao = SingleParkingStopDao.getInstance();
        return spsDao.getParkingStopById(id);
    }

    @POST
    @Path("start_meter.go")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response startMeter(@FormParam("fname") String fname,
                               @FormParam("lname") String lname,
                               @FormParam("vehicle") String vid,
                               @FormParam("tdriver") String tdriver,
                               @Context HttpServletRequest request,
                               @Context HttpServletResponse response) throws InvalidInputException {
        try {
            if(!(dataValidator.isValidNames(fname) && dataValidator.isValidNames(lname) && dataValidator.isValidVid(vid) && dataValidator.isValidType(tdriver))) throw new InvalidInputException("Invalid input data.");
            SingleParkingStopDao spsDao = SingleParkingStopDao.getInstance();
            SingleParkingStop sps = new SingleParkingStop(new Driver(fname, lname, DriverType.valueOf(tdriver)), new Vehicle(vid));
            sps.startMeter();
            spsDao.insertParkingStop(sps);
            request.setAttribute("sps", sps);
            return Response.status(200).entity(new Viewable("/view/start_meter.jsp", null)).build();
        } catch (InvalidInputException e)   {
            errors.add("Input data is invalid. The minimum text length is 2, maximum 50.");
            request.setAttribute("error", errors);
            return Response.status(400).entity(new Viewable("/view/start_meter.jsp", null)).build();
        } catch (DuplicateRowException e) {
            errors.add("The driver: " + fname + " " + lname + " with vehicle " + vid + " has already started parking meter.");
            request.setAttribute("error", errors);
            return Response.status(400).entity(new Viewable("/view/start_meter.jsp", null)).build();
        }
    }

    @POST
    @Path("stop_meter.go")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response stopMeter(@FormParam("fname") String fname,
                              @FormParam("lname") String lname,
                              @FormParam("vehicle") String vid,
                              @Context HttpServletRequest request,
                              @Context HttpServletResponse response) throws InvalidInputException {
        try {
            if(!(dataValidator.isValidNames(fname) && dataValidator.isValidNames(lname) && dataValidator.isValidVid(vid))) throw new InvalidInputException("Invalid input data.");
            SingleParkingStopDao spsDao = SingleParkingStopDao.getInstance();
            SingleParkingStop sps = null;
            sps = spsDao.stopParkingStop(vid, fname, lname);
            FinancialCalculator fc = FinancialCalculator.getInstance();
            BigDecimal payment = fc.calculateStopPayment(sps);
            request.setAttribute("sps", sps);
            request.setAttribute("payment", payment);
            request.setAttribute("symbol", fc.getCurrency().getSymbol());
            return Response.status(200).entity(new Viewable("/view/stop_meter.jsp", null)).build();
        } catch (InvalidInputException e)   {
            errors.add("Input data is invalid. The minimum text length is 2, maximum 50.");
            request.setAttribute("error", errors);
            return Response.status(400).entity(new Viewable("/view/stop_meter.jsp", null)).build();
        } catch(NoRowException e) {
            errors.add("The driver: " + fname + " " + lname + " with vehicle " + vid + " has not started parking meter");
            request.setAttribute("error", errors);
            return Response.status(400).entity(new Viewable("/view/stop_meter.jsp", null)).build();
        }
    }

    @POST
    @Path("driver_check.go")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Response driverCheckStop(@FormParam("fname") String fname,
                                    @FormParam("lname") String lname,
                                    @FormParam("vehicle") String vid,
                                    @Context HttpServletRequest request,
                                    @Context HttpServletResponse response) throws InvalidInputException {
        try {
            if(!(dataValidator.isValidNames(fname) && dataValidator.isValidNames(lname) && dataValidator.isValidVid(vid))) throw new InvalidInputException("Invalid input data.");
            SingleParkingStopDao spsDao = SingleParkingStopDao.getInstance();
            FinancialCalculator fc = FinancialCalculator.getInstance();
            SingleParkingStop sps = spsDao.getParkingStop(vid, fname, lname);
            BigDecimal payment = fc.calculateStopPayment(sps);
            request.setAttribute("sps", sps);
            request.setAttribute("payment", payment);
            request.setAttribute("symbol", fc.getCurrency().getSymbol());
            return Response.status(200).entity(new Viewable("/view/driver_check.jsp", null)).build();
        } catch (InvalidInputException e) {
            errors.add("Input data is invalid.");
            request.setAttribute("error", errors);
            return Response.status(400).entity(new Viewable("/view/driver_check.jsp", null)).build();
        } catch (NoRowException e) {
            errors.add("The driver: " + fname + " " + lname + " with vehicle " + vid + " does not exist.");
            request.setAttribute("error", errors);
            return Response.status(400).entity(new Viewable("/view/driver_check.jsp", null)).build();
        }
    }

    @POST
    @Path("operator_check.go")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response operatorCheckStop(@FormParam("vehicle") String vid,
                                      @Context HttpServletRequest request,
                                      @Context HttpServletResponse response) throws InvalidInputException{
        request.setAttribute("vid", vid);
        try {
            if(!dataValidator.isValidVid(vid)) throw new InvalidInputException("Invalid input data.");
            SingleParkingStopDao spsDao = SingleParkingStopDao.getInstance();
            SingleParkingStop sps = spsDao.getParkingStop(vid, true);
            request.setAttribute("sps", sps);
            return Response.status(200).entity(new Viewable("/view/operator_check.jsp", null)).build();
        } catch (InvalidInputException e) {
            errors.add("Input data is invalid.");
            request.setAttribute("error", errors);
            return Response.status(400).entity(new Viewable("/view/operator_check.jsp", null)).build();
        } catch (NoRowException e)  {
            errors.add("The vehicle: " + vid + " has not started meter");
            request.setAttribute("error", errors);
            return Response.status(400).entity(new Viewable("/view/operator_check.jsp", null)).build();
        }
    }

    @POST
    @Path("owner_earnings.go")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response checkOwnerBrowser(@FormParam("date") String date,
                                      @Context HttpServletRequest request,
                                      @Context HttpServletResponse response) throws InvalidInputException{
        try {
            if(!dataValidator.isValidDate(date, "yyyy-MM-dd")) throw new InvalidInputException("Invalid date format from input");
            SingleParkingStopDao spsDao = SingleParkingStopDao.getInstance();
            List<SingleParkingStop> spsList = spsDao.getParkingStopsForDay(date);
            FinancialCalculator fc = FinancialCalculator.getInstance();
            BigDecimal earnings = fc.calculateDayEarnings(spsList, date);
            request.setAttribute("date", date);
            request.setAttribute("earnings", earnings);
            request.setAttribute("symbol", fc.getCurrency().getSymbol());
            return Response.status(200).entity(new Viewable("/view/owner_earnings.jsp", null)).build();
        } catch(InvalidInputException e) {
            errors.add("Input date format is invalid. There is one acceptable date format: yyyy-MM-dd.");
            request.setAttribute("error", errors);
            return Response.status(400).entity(new Viewable("/view/owner_earnings.jsp", null)).build();
        }
    }

}
