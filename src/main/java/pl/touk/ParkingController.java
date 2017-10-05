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
import pl.touk.exceptions.NoRowException;
import pl.touk.types.DriverType;
import pl.touk.utilis.FinancialCalculator;
import pl.touk.validation.ValidString;

@Path("/parkings")
public class ParkingController {
    @Context
    UriInfo uriInfo;

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
    public Viewable startMeter(@Valid @ValidString @FormParam("fname") String fname,
                               @Valid @ValidString @FormParam("lname") String lname,
                               @Valid @ValidString @FormParam("vehicle") String vid,
                               @FormParam("tdriver") String tdriver,
                               @Context HttpServletRequest request,
                               @Context HttpServletResponse response)	{
        SingleParkingStopDao spsDao = SingleParkingStopDao.getInstance();
        SingleParkingStop sps = new SingleParkingStop(new Driver(fname, lname, DriverType.valueOf(tdriver)), new Vehicle(vid));
        sps.startMeter();
        try {
            spsDao.insertParkingStop(sps);
        } catch (DuplicateRowException e)   {
            errors.add("The driver: " + fname + " " + lname + " with vehicle " + vid + " has already started parking meter");
            request.setAttribute("error", errors);
            return new Viewable("/start_meter.jsp", null);
        }
        request.setAttribute("sps", sps);
        return new Viewable("/start_meter.jsp", null);
    }

    @POST
    @Path("stop_meter.go")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Viewable stopMeter(@Valid @ValidString @FormParam("fname") String fname,
                              @Valid @ValidString @FormParam("lname") String lname,
                              @Valid @ValidString @FormParam("vehicle") String vid,
                              @Context HttpServletRequest request,
                              @Context HttpServletResponse response)	{
        SingleParkingStopDao spsDao = SingleParkingStopDao.getInstance();
        SingleParkingStop sps = null;
        try {
            spsDao.stopParkingStop(vid, fname, lname);
        } catch(NoRowException e) {
            errors.add("The driver: " + fname + " " + lname + " with vehicle " + vid + " has not started parking meter");
            request.setAttribute("error", errors);
            return new Viewable("/stop_meter.jsp", null);
        }
        request.setAttribute("sps", sps);
        return new Viewable("/stop_meter.jsp", null);
    }

    @POST
    @Path("driver_check.go")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String driverCheckStop(@NotNull @Size(min = 2, max = 50) @FormParam("fname") String fname,
                                  @NotNull @Size(min = 2, max = 50) @FormParam("lname") String lname,
                                  @NotNull @Size(min = 2, max = 50) @FormParam("vehicle") String vid)	{
        SingleParkingStopDao spsDao = SingleParkingStopDao.getInstance();
        SingleParkingStop sps = spsDao.getParkingStop(vid, fname, lname);
        FinancialCalculator fc = FinancialCalculator.getInstance();
        double payment = fc.calculateStopPayment(sps);
        String msg = "Driver: " + sps.getDriver().getFirstName() + " " + sps.getDriver().getLastName()
                + " Vehicle: " + sps.getVehicle().getIdentifier() + " Start time - " + sps.getStartDate()
                + " Current payment - " + payment + " " + fc.getCurrency().getSymbol();
        return msg;
    }

    @POST
    @Path("operator_check.go")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Viewable operatorCheckStop(@NotNull @Size(min = 2, max = 50) @FormParam("vehicle") String vid,
                                            @Context HttpServletRequest request,
                                            @Context HttpServletResponse response) {
        request.setAttribute("vid", vid);
        SingleParkingStopDao spsDao = SingleParkingStopDao.getInstance();
        SingleParkingStop sps = spsDao.getParkingStop(vid, true);
        String msg;
        if(sps != null)
            msg = "Vehicle: " + sps.getVehicle().getIdentifier() + " has started parking meter from " + sps.getStartDate();
        else
            msg = "Vehicle: " + vid + " has not started parking meter";
        request.setAttribute("sps", sps);
        return new Viewable("/operator_check.jsp", null);
    }

    @POST
    @Path("owner_earnings.go")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String checkOwnerBrowser(@NotNull @FormParam("date") String date)   {
        SingleParkingStopDao spsDao = SingleParkingStopDao.getInstance();
        List<SingleParkingStop> spsList = spsDao.getParkingStopsForDay(date);
        FinancialCalculator fc = FinancialCalculator.getInstance();
        double earnings = fc.calculateDayEarnings(spsList, date);
        return "earnings: " + earnings;
    }

}
