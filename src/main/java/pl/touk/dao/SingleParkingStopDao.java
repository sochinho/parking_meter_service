package pl.touk.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import pl.touk.entities.Driver;
import pl.touk.entities.Vehicle;
import pl.touk.entities.SingleParkingStop;
import pl.touk.exceptions.DuplicateRowException;
import pl.touk.exceptions.NoRowException;
import pl.touk.types.DriverType;

public class SingleParkingStopDao {

    private static Logger log;

    final static String NAMING_CONTEXT = "java:/comp/env/jdbc/parking_meter";

    private static SingleParkingStopDao instance = new SingleParkingStopDao();

    private SingleParkingStopDao()	{
        super();
        log = Logger.getLogger(this.getClass().getName());
    }

    public static SingleParkingStopDao getInstance()	{
        return instance;
    }

    public ArrayList<SingleParkingStop> getParkingAllStops() {
        ArrayList<SingleParkingStop> spsList = new ArrayList<SingleParkingStop>();
        try(Connection con = getConnection()) {
            ResultSet rs = con.createStatement().executeQuery(
                    "select * from parking_stops");
            while (rs.next()) {
                Driver d = new Driver("","");
                Vehicle v = new Vehicle(rs.getString("vehicle_identity"));
                SingleParkingStop sps = new SingleParkingStop(d, v);
                DateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss.SSS");
                if(rs.getString("start_date") != null)
                    sps.setStartDate(df.parse(rs.getString("start_date")));
                if(rs.getString("end_date") != null)
                    sps.setStopDate(df.parse(rs.getString("end_date")));
                if((rs.getString("driver_fname") != null) && (rs.getString("driver_lname") != null)) {
                    d.setFirstName(rs.getString("driver_fname"));
                    d.setLastName(rs.getString("driver_lname"));
                    d.setType(rs.getInt("driver_type") == 1 ? DriverType.VIP : DriverType.REGULAR);
                    sps.setDriver(d);
                }
                spsList.add(sps);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch(ParseException e)	{
            e.printStackTrace();
        }
        return spsList;
    }

    public SingleParkingStop getParkingStopById(int idp) {
        SingleParkingStop sps = null;
        try(Connection con = getConnection()) {
            ResultSet rs = con.createStatement().executeQuery(
                    "select * from parking_stops where idp=" + idp);
            if (rs.next()) {
                Driver d = new Driver("","");
                Vehicle v = new Vehicle(rs.getString("vehicle_identity"));
                sps = new SingleParkingStop(d, v);
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                if(rs.getString("start_date") != null)
                    sps.setStartDate(df.parse(rs.getString("start_date")));
                if(rs.getString("end_date") != null)
                    sps.setStopDate(df.parse(rs.getString("end_date")));
                if((rs.getString("driver_fname") != null) && (rs.getString("driver_lname") != null)) {
                    d.setFirstName(rs.getString("driver_fname"));
                    d.setLastName(rs.getString("driver_lname"));
                    d.setType(rs.getInt("driver_type") == 1 ? DriverType.VIP : DriverType.REGULAR);
                    sps.setDriver(d);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch(ParseException e)	{
            e.printStackTrace();
        }
        return sps;
    }

    public SingleParkingStop getParkingStop(String vid, String fname, String lname) throws NoRowException{
        SingleParkingStop sps = null;
        try(Connection con = getConnection()) {
            ResultSet rs = con.createStatement().executeQuery(
                    "select * from parking_stops where vehicle_identity='" + vid + "' and driver_fname='" + fname + "' and driver_lname='" + lname + "' order by start_date desc limit 1");
            if (rs.next()) {
                Vehicle v = new Vehicle(rs.getString("vehicle_identity"));
                Driver d = new Driver("","");
                if((rs.getString("driver_fname") != null) && (rs.getString("driver_lname") != null)) {
                    d.setFirstName(rs.getString("driver_fname"));
                    d.setLastName(rs.getString("driver_lname"));
                    d.setType(rs.getInt("driver_type") == 1 ? DriverType.VIP : DriverType.REGULAR);
                }
                sps = new SingleParkingStop(d, v);
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                if(rs.getString("start_date") != null)
                    sps.setStartDate(df.parse(rs.getString("start_date")));
                if(rs.getString("end_date") != null)
                    sps.setStopDate(df.parse(rs.getString("end_date")));
                if(rs.getString("started_meter") != null)
                    sps.setStarted(rs.getBoolean("started_meter"));
            }
            else throw new NoRowException("No records in DB");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch(ParseException e)	{
            e.printStackTrace();
        }
        return sps;
    }

    public SingleParkingStop getParkingStop(String vid, boolean started) throws NoRowException{
        SingleParkingStop sps = null;
        try(Connection con = getConnection()) {
            ResultSet rs = con.createStatement().executeQuery(
                    "select * from parking_stops where vehicle_identity='" + vid + "' and started_meter=" + started);
            if(rs.next())   {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                Vehicle v = new Vehicle(rs.getString("vehicle_identity"));
                Driver d = new Driver("", "");
                sps = new SingleParkingStop(d, v);
                if((rs.getString("driver_fname") != null) && (rs.getString("driver_lname") != null)) {
                    d.setFirstName(rs.getString("driver_fname"));
                    d.setLastName(rs.getString("driver_lname"));
                    d.setType(rs.getInt("driver_type") == 1 ? DriverType.VIP : DriverType.REGULAR);
                    sps.setDriver(d);
                }
                if(rs.getString("start_date") != null)
                    sps.setStartDate(df.parse(rs.getString("start_date")));
                if(rs.getString("end_date") != null)
                    sps.setStopDate(df.parse(rs.getString("end_date")));
                if(rs.getString("started_meter") != null)
                    sps.setStarted(rs.getBoolean("started_meter"));
            }
            else throw new NoRowException("No records in DB");
        } catch(SQLException e) {
            e.printStackTrace();
        } catch(ParseException e)	{
            e.printStackTrace();
        }
        return sps;
    }

    public List<SingleParkingStop> getParkingStopsForDay(String date) {
        log.info(this.getClass().getName() + " - getParkingStopsForDay, date " + date);
        List<SingleParkingStop> spsList = new ArrayList<SingleParkingStop>();
        try(Connection con = getConnection()) {
            String formattedDate = new SimpleDateFormat("yyyy/MM/dd").format(new SimpleDateFormat("yyyy-MM-dd").parse(date));
            String start_date = "TO_TIMESTAMP('" + formattedDate + " 23:59', 'YYYY/MM/DD HH:MI')";
            String end_date = "TO_TIMESTAMP('" + formattedDate + " 00:00', 'YYYY/MM/DD HH:MI')";
            ResultSet rs = con.createStatement().executeQuery(
                    "select * from parking_stops where start_date < " + start_date + " and end_date > " + end_date + " " +
                            "union select * from parking_stops where start_date < " + start_date + " and end_date is null and started_meter=true");
            while(rs.next())    {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                Vehicle v = new Vehicle(rs.getString("vehicle_identity"));
                Driver driver = new Driver("", "");
                if((rs.getString("driver_fname") != null) && (rs.getString("driver_lname") != null)) {
                    driver.setFirstName(rs.getString("driver_fname"));
                    driver.setLastName(rs.getString("driver_lname"));
                    driver.setType(rs.getInt("driver_type") == 1 ? DriverType.VIP : DriverType.REGULAR);
                }
                SingleParkingStop sps = new SingleParkingStop(driver, v);
                if(rs.getString("start_date") != null)
                    sps.setStartDate(df.parse(rs.getString("start_date")));
                if(rs.getString("end_date") != null)
                    sps.setStopDate(df.parse(rs.getString("end_date")));
                if(rs.getString("started_meter") != null)
                    sps.setStarted(rs.getBoolean("started_meter"));
                spsList.add(sps);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return spsList;
    }

    public void insertParkingStop(SingleParkingStop sps) throws DuplicateRowException{
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
        if(checkParkingStopStarted(sps)) throw new DuplicateRowException("Record already exists in DB");
        try(Connection con = getConnection())	{
            String vid  = sps.getVehicle().getIdentifier();
            String start_date = "NULL", end_date = "NULL", fname = "NULL", lname = "NULL";
            if(sps.getStartDate() != null)
                start_date = "TO_TIMESTAMP('" + df2.format(df.parse(sps.getStartDate())) + "', 'YYYY-MM-DD HH.MI.SS')";
            if(sps.getStopDate() != null)
                end_date = "TO_TIMESTAMP('" + df2.format(df.parse(sps.getStopDate())) + "', 'YYYY-MM-DD HH.MI.SS')";
            if(sps.getDriver().getFirstName() != null)	fname = sps.getDriver().getFirstName();
            if(sps.getDriver().getLastName() != null)	lname = sps.getDriver().getLastName();
            int driverTypeNum = sps.getDriver().getType() == DriverType.VIP ? 1 : 0;

            con.createStatement().executeUpdate(
                    "insert into parking_stops(vehicle_identity, start_date, end_date, driver_fname, driver_lname, started_meter, driver_type) values('" + vid + "', " + start_date  + ", " + end_date + ", '" + fname + "', '" + lname + "', " + sps.isStarted() + ", " + driverTypeNum + ")");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch(ParseException e)	{
            e.printStackTrace();
        }
    }

    public SingleParkingStop stopParkingStop(String vid, String fname, String lname) throws NoRowException{
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SingleParkingStop sps = new SingleParkingStop(new Driver(fname, lname), new Vehicle(vid));
        if(!checkParkingStopStarted(sps)) throw new NoRowException("No records in DB");
        try(Connection con = getConnection())	{
            ResultSet rs = con.createStatement().executeQuery(
                    "select idp from parking_stops where vehicle_identity='" + vid + "' and driver_fname='" + fname + "' and driver_lname='" + lname + "' and started_meter=true order by start_date desc limit 1");
            if (rs.next()) {
                int idp = rs.getInt("idp");
                sps = getParkingStopById(idp);
                sps.stopMeter();
                String start_date = "NULL", end_date = "NULL";
                if(sps.getStartDate() != null)
                    start_date = "TO_TIMESTAMP('" + df2.format(df.parse(sps.getStartDate())) + "', 'YYYY-MM-DD HH.MI.SS')";
                if(sps.getStopDate() != null)
                    end_date = "TO_TIMESTAMP('" + df2.format(df.parse(sps.getStopDate())) + "', 'YYYY-MM-DD HH.MI.SS')";
                con.createStatement().executeUpdate(
                        "update parking_stops set start_date=" + start_date + ", end_date=" + end_date + ", started_meter=0 where idp=" + idp);
            }
            else throw new NoRowException("No records in DB");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch(ParseException e)	{
            e.printStackTrace();
        }
        return sps;
    }

    private boolean checkParkingStopStarted(SingleParkingStop sps)  {
        try(Connection con = getConnection()) {
            String vid  = sps.getVehicle().getIdentifier();
            String fname = "NULL", lname = "NULL";
            if(sps.getDriver().getFirstName() != null)	fname = sps.getDriver().getFirstName();
            if(sps.getDriver().getLastName() != null)	lname = sps.getDriver().getLastName();

            ResultSet rs = con.createStatement().executeQuery(
                    "select count(idp) as num from parking_stops where vehicle_identity='" + vid + "' and driver_fname='" + fname + "' and driver_lname='" + lname + "' and started_meter=true");

            if(rs.next())   {
                if(rs.getInt("num") > 0)  return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Connection getConnection() {
        try {
            Context initContext = new InitialContext();
            DataSource ds = (DataSource) initContext
                    .lookup(NAMING_CONTEXT);
            return ds.getConnection();
        } catch (SQLException ec) {
            System.out.println(ec.getMessage());
        } catch (NamingException ne) {
            System.out.println(ne.getMessage());
        }
        return null;
    }

}
