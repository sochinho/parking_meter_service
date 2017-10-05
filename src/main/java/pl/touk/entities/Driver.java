package pl.touk.entities;

import pl.touk.types.DriverType;

public class Driver extends User{

    DriverType type;

    public Driver(String f, String l)	{
        super(f, l);
        type = DriverType.REGULAR;
    }

    public Driver(String f, String l, DriverType dt)	{
        super(f, l);
        type = dt;
    }

    public DriverType getType() {
        return type;
    }

    public void setType(DriverType type) {
        this.type = type;
    }
}

