package pl.touk.entities;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Vehicle {

    @NotNull(message = "Vehicle id cannot be empty")
    @Size(min = 2, max = 50)
    private String identifier;

    public Vehicle(String id)	{
        identifier = id;
    }

    public String getIdentifier()	{
        return identifier;
    }

}
