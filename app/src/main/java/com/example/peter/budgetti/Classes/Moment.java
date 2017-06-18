package com.example.peter.budgetti.Classes;



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by peter on 18.06.2017.
 */

public class Moment {

    Calendar calendar = Calendar.getInstance();
    private String date;
    private String time;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");


    public Moment(){

        date = dateFormat.format(calendar.getTime());
        time = timeFormat.format(calendar.getTime());
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
