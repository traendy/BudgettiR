package com.example.peter.budgetti.Classes;

/**
 * Created by peter on 21.06.2017.
 */

public class DayEypense {

    private String date;
    private float daysum;

    public DayEypense(String date, float daysum) {
        this.date = date;
        this.daysum = daysum;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getDaysum() {
        return daysum;
    }

    public void setDaysum(float daysum) {
        this.daysum = daysum;
    }
}
