package pl.touk.entities;

import java.util.Date;
import java.text.SimpleDateFormat;

public class SingleParkingStop {

    private SimpleDateFormat sf;
    private String startDate;
    private String endDate;
    private boolean started;
    private Driver driver;
    private Vehicle vehicle;

    public SingleParkingStop()	{
        super();
    }

    public SingleParkingStop(Driver d, Vehicle v)	{
        sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        driver = d;
        vehicle = v;
        started = false;
    }

    public void setDriver(Driver d)	{
        driver = d;
    }

    public Driver getDriver()	{
        return driver;
    }

    public void setVehicle(Vehicle v)	{
        vehicle = v;
    }

    public Vehicle getVehicle()	{
        return vehicle;
    }

    public void startMeter()	{
        startDate = sf.format(new Date());
        started = true;
    }

    public void stopMeter()	{
        endDate = sf.format(new Date());
        started = false;
    }

    public void setStartDate(Date d)	{
        startDate = sf.format(d);
    }

    public String getStartDate()	{
        return startDate;
    }

    public void setStopDate(Date d)	{
        endDate = sf.format(d);
    }

    public String getStopDate()	{
        return endDate;
    }

    public void setStarted(boolean s)   {
        started = s;
    }

    public boolean isStarted()  { return started; }

}
