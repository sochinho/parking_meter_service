package pl.touk;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Constraint;
import javax.validation.Valid;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.FormParam;
import javax.validation.constraints.*;

import com.sun.jersey.api.view.Viewable;

import pl.touk.entities.*;
import pl.touk.dao.SingleParkingStopDao;
import pl.touk.exceptions.DuplicateRowException;
import pl.touk.exceptions.InvalidInputException;
import pl.touk.exceptions.NoRowException;
import pl.touk.types.DriverType;
import pl.touk.utilis.FinancialCalculator;
import pl.touk.validation.InputDateValidator;
import pl.touk.validation.InputStringValidator;

@Path("/parkings")
public class ParkingController {
    @Context
    UriInfo uriInfo;

    InputStringValidator stringValidator = InputStringValidator.getInstance();
    InputDateValidator dateValidator= InputDateValidator.getInstance();
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
    public Viewable startMeter(@FormParam("fname") String fname,
                               @FormParam("lname") String lname,
                               @FormParam("vehicle") String vid,
                               @FormParam("tdriver") String tdriver,
                               @Context HttpServletRequest request,
                               @Context HttpServletResponse response) throws InvalidInputException {
        try {
            if(!(stringValidator.isValid(fname) && stringValidator.isValid(lname) && stringValidator.isValid(vid))) throw new InvalidInputException("Invalid input data.");
            SingleParkingStopDao spsDao = SingleParkingStopDao.getInstance();
            SingleParkingStop sps = new SingleParkingStop(new Driver(fname, lname, DriverType.valueOf(tdriver)), new Vehicle(vid));
            sps.startMeter();
            spsDao.insertParkingStop(sps);
            request.setAttribute("sps", sps);
        } catch (InvalidInputException e)   {
            errors.add("Input data is invalid. The minimum text length is 2, maximum 50.");
            request.setAttribute("error", errors);
        } catch (DuplicateRowException e) {
            errors.add("The driver: " + fname + " " + lname + " with vehicle " + vid + " has already started parking meter.");
            request.setAttribute("error", errors);
        } finally {
            return new Viewable("/view/start_meter.jsp", null);
        }
    }

    @POST
    @Path("stop_meter.go")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Viewable stopMeter(@FormParam("fname") String fname,
                              @FormParam("lname") String lname,
                              @FormParam("vehicle") String vid,
                              @Context HttpServletRequest request,
                              @Context HttpServletResponse response) throws InvalidInputException {
        try {
            if(!(stringValidator.isValid(fname) && stringValidator.isValid(lname) && stringValidator.isValid(vid))) throw new InvalidInputException("Invalid input data.");
            SingleParkingStopDao spsDao = SingleParkingStopDao.getInstance();
            SingleParkingStop sps = null;
            spsDao.stopParkingStop(vid, fname, lname);
            request.setAttribute("sps", sps);
        } catch (InvalidInputException e)   {
            errors.add("Input data is invalid. The minimum text length is 2, maximum 50.");
            request.setAttribute("error", errors);
        } catch(NoRowException e) {
            errors.add("The driver: " + fname + " " + lname + " with vehicle " + vid + " has not started parking meter");
            request.setAttribute("error", errors);
        } finally {
            return new Viewable("/view/stop_meter.jsp", null);
        }
    }

    @POST
    @Path("driver_check.go")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Viewable driverCheckStop(@FormParam("fname") String fname,
                                    @FormParam("lname") String lname,
                                    @FormParam("vehicle") String vid,
                                    @Context HttpServletRequest request,
                                    @Context HttpServletResponse response) throws InvalidInputException {
        try {
            if(!(stringValidator.isValid(fname) && stringValidator.isValid(lname) && stringValidator.isValid(vid))) throw new InvalidInputException("Invalid input data.");
            SingleParkingStopDao spsDao = SingleParkingStopDao.getInstance();
            SingleParkingStop sps = spsDao.getParkingStop(vid, fname, lname);
            FinancialCalculator fc = FinancialCalculator.getInstance();
            double payment = fc.calculateStopPayment(sps);
            request.setAttribute("sps", sps);
            request.setAttribute("payment", payment);
            request.setAttribute("symbol", fc.getCurrency().getSymbol());
        } catch (InvalidInputException e) {
            errors.add("Input data is invalid. The minimum text length is 2, maximum 50.");
            request.setAttribute("error", errors);
        } finally {
            return new Viewable("/view/driver_check.jsp", null);
        }
    }

    @POST
    @Path("operator_check.go")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Viewable operatorCheckStop(@FormParam("vehicle") String vid,
                                      @Context HttpServletRequest request,
                                      @Context HttpServletResponse response) throws InvalidInputException{
        request.setAttribute("vid", vid);
        try {
            if(!stringValidator.isValid(vid)) throw new InvalidInputException("Invalid input data.");
            SingleParkingStopDao spsDao = SingleParkingStopDao.getInstance();
            SingleParkingStop sps = spsDao.getParkingStop(vid, true);
            request.setAttribute("sps", sps);
        } catch (InvalidInputException e) {
            errors.add("Input data is invalid. The minimum text length is 2, maximum 50.");
            request.setAttribute("error", errors);
        } catch (NoRowException e)  {
            errors.add("The vehicle: " + vid + " has not started meter");
        } finally {
            return new Viewable("/view/operator_check.jsp", null);
        }
    }

    @POST
    @Path("owner_earnings.go")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Viewable checkOwnerBrowser(@FormParam("date") String date,
                                      @Context HttpServletRequest request,
                                      @Context HttpServletResponse response) throws InvalidInputException{
        try {
            if(!dateValidator.isValid(date, "yyyy-MM-dd")) throw new InvalidInputException("Invalid date format from input");
            SingleParkingStopDao spsDao = SingleParkingStopDao.getInstance();
            List<SingleParkingStop> spsList = spsDao.getParkingStopsForDay(date);
            FinancialCalculator fc = FinancialCalculator.getInstance();
            double earnings = fc.calculateDayEarnings(spsList, date);
            request.setAttribute("date", date);
            request.setAttribute("earnings", earnings);
            request.setAttribute("symbol", fc.getCurrency().getSymbol());
        } catch(InvalidInputException e) {
            errors.add("Input date format is invalid. There is one acceptable format: yyyy-MM-dd.");
            request.setAttribute("error", errors);
        } finally {
            return new Viewable("/view/owner_earnings.jsp", null);
        }
    }

}
