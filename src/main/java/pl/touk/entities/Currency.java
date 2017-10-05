package pl.touk.entities;

import java.math.BigDecimal;

public class Currency {

    protected String name;
    protected String symbol;

    protected double coefficient;

    protected Currency()	{
        name = "";
        symbol = "";
        coefficient = 1.00;
    }

    public String getName()	{
        return name;
    }

    public String getSymbol()	{
        return symbol;
    }

    public double getMoney(double m)	{
        return m * coefficient;
    }


}
