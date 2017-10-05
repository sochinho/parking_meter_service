package pl.touk.entities;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class User	{

    @NotNull(message = "First name cannot be empty")
    @Size(min = 2, max = 50)
    private String fname;
    @NotNull(message = "Last name cannot be empty")
    @Size(min = 2, max = 50)
    private String lname;

    public User()	{
        super();
    }

    public User(String f, String l) {
        super();
        this.fname = f;
        this.lname = l;
    }

    public void setFirstName(String f)	{
        fname = f;
    }

    public void setLastName(String l)	{
        lname = l;
    }

    public String getFirstName() {
        return fname;
    }

    public String getLastName() {
        return lname;
    }

}
