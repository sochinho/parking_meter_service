package pl.touk.dao;

import java.sql.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import pl.touk.exceptions.InternalServerException;
import pl.touk.exceptions.JdbcConnectException;
import pl.touk.exceptions.NoRowException;
import pl.touk.types.DriverType;

public class SingleParkingStopDao {

    private static Logger logger;

    final static String NAMING_CONTEXT = "java:/comp/env/jdbc/parking_meter";

    private static SingleParkingStopDao instance = new SingleParkingStopDao();

    private SingleParkingStopDao()	{
        super();
        logger = Logger.getLogger(this.getClass().getName());
    }

    public static SingleParkingStopDao getInstance()	{
        return instance;
    }

    public SingleParkingStop getParkingStop(String vid, String fname, String lname) throws NoRowException, InternalServerException{
        SingleParkingStop sps = null;
        try(Connection con = getConnection()) {
            if(con == null)
                throw new JdbcConnectException("Cannot connect to JDBC database");
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
            throw new InternalServerException("Database problem has occured");
        } catch (JdbcConnectException e) {
            throw new InternalServerException("Problem with connecting to JDBC database");
        } catch(ParseException e) {
            throw new InternalServerException("Parsing date problem");
        } catch (NamingException e) {
            throw new InternalServerException("Problem with getting JDBC resource");
        }
        return sps;
    }

    public SingleParkingStop getParkingStop(String vid, boolean started) throws NoRowException, InternalServerException{
        SingleParkingStop sps = null;
        try(Connection con = getConnection()) {
            if(con == null)
                throw new JdbcConnectException("Cannot connect to JDBC database");
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
        } catch (SQLException e) {
            throw new InternalServerException("Database problem has occured");
        } catch (JdbcConnectException e) {
            throw new InternalServerException("Problem with connecting to JDBC database");
        } catch (ParseException e)	{
            throw new InternalServerException("Parsing date problem");
        } catch (NamingException e) {
            throw new InternalServerException("Problem with getting JDBC resource");
        } catch (NoRowException e) {
            throw e;
        }
        return sps;
    }

    public List<SingleParkingStop> getParkingStopsForDay(String date) throws InternalServerException {
        logger.info(logger.getName() + " - getParkingStopsForDay, date " + date);
        List<SingleParkingStop> spsList = new ArrayList<SingleParkingStop>();
        String start_date, end_date;

        try {
            String formattedDate = new SimpleDateFormat("yyyy/MM/dd").format(new SimpleDateFormat("yyyy-MM-dd").parse(date));
            start_date = "TO_TIMESTAMP('" + formattedDate + " 23:59', 'YYYY/MM/DD HH:MI')";
            end_date = "TO_TIMESTAMP('" + formattedDate + " 00:00', 'YYYY/MM/DD HH:MI')";
        } catch (ParseException e)	{
            throw new InternalServerException("Parsing date problem");
        }

        try(Connection con = getConnection();
            ResultSet rs = con.createStatement().executeQuery(
                    "select * from parking_stops where start_date < " + start_date + " and end_date > " + end_date + " " +
                        "union select * from parking_stops where start_date < " + start_date + " and end_date is null and started_meter=true"))
        {
            if(con == null)
                throw new JdbcConnectException("Cannot connect to JDBC database");
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
        } catch (SQLException e) {
            throw new InternalServerException("Database problem has occured");
        } catch (JdbcConnectException e) {
            throw new InternalServerException("Problem with connecting to JDBC database");
        } catch (ParseException e)	{
            throw new InternalServerException("Parsing date problem");
        } catch (NamingException e) {
            throw new InternalServerException("Problem with getting JDBC resource");
        }
        return spsList;
    }

    public void insertParkingStop(SingleParkingStop sps) throws DuplicateRowException, InternalServerException{
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
        String fname = "NULL", lname = "NULL", vid = "NULL";
        String start_date = "NULL", end_date = "NULL";
        int driverTypeNum;

        try(Connection con = getConnection();
            Statement stm = con.createStatement())
        {
            if(con == null)
                throw new JdbcConnectException("Cannot connect to JDBC database");

            if(checkParkingStopStarted(sps)) throw new DuplicateRowException("Record already exists in DB");
            if(sps.getDriver().getFirstName() != null)	fname = sps.getDriver().getFirstName();
            if(sps.getDriver().getLastName() != null)	lname = sps.getDriver().getLastName();
            if(sps.getVehicle().getIdentifier() != null) vid = sps.getVehicle().getIdentifier();
            if(sps.getStartDate() != null)
                start_date = "TO_TIMESTAMP('" + df2.format(df.parse(sps.getStartDate())) + "', 'YYYY-MM-DD HH.MI.SS')";
            if(sps.getStopDate() != null)
                end_date = "TO_TIMESTAMP('" + df2.format(df.parse(sps.getStopDate())) + "', 'YYYY-MM-DD HH.MI.SS')";
            driverTypeNum = sps.getDriver().getType() == DriverType.VIP ? 1 : 0;

            stm.executeUpdate(
                    "insert into parking_stops(vehicle_identity, start_date, end_date, driver_fname, driver_lname, started_meter, driver_type) values('" + vid + "', " + start_date  + ", " + end_date + ", '" + fname + "', '" + lname + "', " + sps.isStarted() + ", " + driverTypeNum + ")");

        } catch (SQLException e) {
            throw new InternalServerException("Database problem has occured");
        } catch (JdbcConnectException e) {
            throw new InternalServerException("Problem with connecting to JDBC database");
        } catch (NamingException e) {
            throw new InternalServerException("Problem with getting JDBC resource");
        } catch (ParseException e) {
            throw new InternalServerException("Parsing date problem");
        } catch (DuplicateRowException e) {
            throw e;
        }
    }

    public SingleParkingStop stopParkingStop(String vid, String fname, String lname) throws NoRowException, InternalServerException{
        SingleParkingStop sps = new SingleParkingStop(new Driver(fname, lname), new Vehicle(vid));
        try(Connection con = getConnection();
            Statement stm = con.createStatement())
        {
            if(con == null)
                throw new JdbcConnectException("Cannot connect to JDBC database");

            if(!checkParkingStopStarted(sps)) throw new NoRowException("No records in DB");
            String end_date = "TO_TIMESTAMP('" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()) + "', 'YYYY-MM-DD HH.MI.SS')";

            stm.executeUpdate(
                    "update parking_stops set end_date=" + end_date + ", started_meter=0 where vehicle_identity='" + vid + "' and driver_fname='" + fname + "' and driver_lname='" + lname + "' and started_meter=true limit 1");

            sps = getParkingStop(vid, fname, lname);

        } catch(SQLException e) {
            throw new InternalServerException("Database problem has occured");
        } catch (JdbcConnectException e) {
            throw new InternalServerException("Problem with connecting to JDBC database");
        } catch (NamingException e) {
            throw new InternalServerException("Problem with getting JDBC resource");
        } catch (NoRowException e) {
            throw e;
        }
        return sps;
    }

    private boolean checkParkingStopStarted(SingleParkingStop sps) throws SQLException, JdbcConnectException, NamingException{
        String fname = "NULL", lname = "NULL", vid = "NULL";
        if(sps.getVehicle().getIdentifier() != null) vid = sps.getVehicle().getIdentifier();
        if(sps.getDriver().getFirstName() != null)	fname = sps.getDriver().getFirstName();
        if(sps.getDriver().getLastName() != null)	lname = sps.getDriver().getLastName();

        try(Connection con = getConnection();
            ResultSet rs = con.createStatement().executeQuery(
                    "select count(idp) as num from parking_stops where vehicle_identity='" + vid + "' and driver_fname='" + fname + "' and driver_lname='" + lname + "' and started_meter=true"))
        {
            if(con == null)
                throw new JdbcConnectException("Cannot connect to JDBC database");

            if(rs.next())   {
                if(rs.getInt("num") > 0)  return true;
            }

        } catch (SQLException e) {
            throw e;
        } catch (NamingException e) {
            throw e;
        }

        return false;
    }

    private Connection getConnection() throws SQLException, NamingException{
        try {
            Context initContext = new InitialContext();
            DataSource ds = (DataSource) initContext
                    .lookup(NAMING_CONTEXT);
            return ds.getConnection();
        } catch (SQLException e) {
            throw e;
        } catch (NamingException e) {
            throw e;
        }
    }

}
