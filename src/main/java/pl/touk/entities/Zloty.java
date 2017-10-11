package pl.touk.entities;

import java.math.BigDecimal;

public class Zloty extends Currency{

    public Zloty()	{
        super();
        name = "Zloty";
        symbol = "ZL";
        coefficient = BigDecimal.valueOf(1.00);
    }

}
