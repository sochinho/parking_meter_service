package pl.touk.validation;

import pl.touk.types.DriverType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InputDataValidator {

    private static InputDataValidator instance = new InputDataValidator();

    private InputDataValidator() {}

    public static InputDataValidator getInstance()   {
        return instance;
    }

    public boolean isValidDriverFormInput(String fname, String lname, String vid)   {
        return isValidNames(fname) && isValidNames(lname) && isValidVid(vid);
    }

    public boolean isValidDriverFormInput(String fname, String lname, String vid, String tdriver)   {
        return isValidNames(fname) && isValidNames(lname) && isValidVid(vid) && isValidType(tdriver);
    }

    public boolean isValidNames(String name)    {


        if(name == null)
            return false;

        name = name.trim();

        if(name.length() < 2)  {
            return false;
        }

        if(name.length() > 40) {
            return false;
        }

        return true;

    }

    public boolean isValidVid(String vid)    {


        if(vid == null)
            return false;

        vid = vid.trim();

        if(vid.length() < 3)  {
            return false;
        }

        if(vid.length() > 10) {
            return false;
        }

        return true;

    }

    public boolean isValidDate(String date, String dateFormat)    {

        if(date == null)
            return false;

        SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
        sf.setLenient(false);

        try {
            sf.parse(date);
        } catch (ParseException e) {
            return false;
        }

        return true;

    }

    public boolean isValidType(String tdriver)  {

        if(tdriver == null)
            return false;

        try {
            if (DriverType.valueOf(tdriver) == null)
                return false;
        } catch(IllegalArgumentException e) {
            return false;
        }

        return true;
    }

}
