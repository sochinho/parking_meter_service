package pl.touk.entities;

import java.math.BigDecimal;

public class Currency {

    protected String name;
    protected String symbol;

    protected BigDecimal coefficient;

    protected Currency()	{
        name = "";
        symbol = "";
        coefficient = new BigDecimal(1.00);
    }

    public String getName()	{
        return name;
    }

    public String getSymbol()	{
        return symbol;
    }

    public BigDecimal getMoney(BigDecimal m)	{
        return m.multiply(coefficient).setScale(2, BigDecimal.ROUND_UP);
    }


}
