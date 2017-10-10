package pl.touk.utilis;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import pl.touk.entities.Currency;
import pl.touk.entities.Zloty;
import pl.touk.entities.SingleParkingStop;
import pl.touk.types.DriverType;

public class FinancialCalculator {

    private static Logger log;

    private static Currency currency;
    private static SimpleDateFormat sf;
    private static FinancialCalculator instance = null;

    private FinancialCalculator()	{
        currency = new Zloty();
        sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        log = Logger.getLogger(this.getClass().getName());
    }

    public static FinancialCalculator getInstance()	{
        if(instance == null)	instance = new FinancialCalculator();
        return instance;
    }

    public static Currency getCurrency()	{
        return currency;
    }

    public BigDecimal calculateStopPayment(SingleParkingStop sps)	{
        Date start;
        Date end;
        BigDecimal result = new BigDecimal(0);
        double payment = 0.00;
        if(sps != null)
            if(sps.getStartDate() != null)	{
                try	{
                    log.info(this.getClass().getName() + " calculateStopPayment, sps.getStartDate() - " + sps.getStartDate());
                    start = sf.parse(sps.getStartDate());
                    if(!sps.isStarted() && sps.getStopDate() != null)	{
                        end = sf.parse(sps.getStopDate());
                    }
                    else	{
                        end = new Date();
                    }
                    log.info(this.getClass().getName() + " calculateStopPayment, start - " + start);
                    log.info(this.getClass().getName() + " calculateStopPayment, end - " + end);
                    long millis = end.getTime() - start.getTime();
                    double hours = Math.ceil((double) millis/(1000*3600));
                    double startRate = 0.0;
                    double nextHourMultiplier = 0.0;
                    if(sps.getDriver().getType() == DriverType.VIP) {
                        hours--;
                        startRate = 2.00;
                        nextHourMultiplier = 1.5;
                    }
                    else    {
                        startRate = 1.00;
                        nextHourMultiplier = 2.0;
                    }
                    if(hours > 0)   payment = startRate*(1-Math.pow(nextHourMultiplier,hours))/(1-nextHourMultiplier);
					result = currency.getMoney(payment);
                } catch(ParseException e)	{
                    e.printStackTrace();
                }

            }
        return result;
    }

    public BigDecimal calculateDayEarnings(List<SingleParkingStop> spsList, String date)    {
        BigDecimal dayEarnings = new BigDecimal(0.00);
        try {
            Calendar calendar = Calendar.getInstance();
            Date startDay = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            calendar.setTime(startDay);
            calendar.add(Calendar.DATE, 1);
            Date endDay = calendar.getTime();
            log.info(this.getClass().getName() + " calculateDayEarnings, spsList.size() - " + spsList.size());
            for(SingleParkingStop sps : spsList)    {
                double payment = 0.00;
                Date startDate, endDate;
                if(sf.parse(sps.getStartDate()).before(startDay))   startDate = startDay;
                else    startDate = sf.parse(sps.getStartDate());
                if(!sps.isStarted() && sf.parse(sps.getStopDate()).before(endDay))  endDate = sf.parse(sps.getStopDate());
                else    endDate = endDay;
                if(endDate.after(new Date()))   endDate = new Date();
                long millis = endDate.getTime() - startDate.getTime();
                double hours = Math.ceil((double) millis/(1000*3600));
                double startRate = 0.0, nextHourMultiplier = 0.0;
                if(sps.getDriver().getType() == DriverType.VIP) {
                    hours--;
                    startRate = 2.00;
                    nextHourMultiplier = 1.5;
                }
                else    {
                    startRate = 1.00;
                    nextHourMultiplier = 2.0;
                }
                if(hours > 0)   payment = startRate*(1-Math.pow(nextHourMultiplier,hours))/(1-nextHourMultiplier);
                log.info(this.getClass().getName() + " calculateDayEarnings, payment - " + payment);
                dayEarnings = dayEarnings.add(currency.getMoney(payment));
            }
        } catch(ParseException e)   {
            e.printStackTrace();
        }
        return dayEarnings;
    }

}
