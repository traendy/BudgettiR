package com.example.peter.budgetti.Classes;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Main Class defines an Expense by its name, price/amount and an index to sort.
 */

public class Expense {


    private static int index;
    private String name;
    private float amount;
    private Moment moment;

    public Expense(String name, float amount, int index) {
        this.name = name;
        this.amount = amount;
        Expense.index = index;
        moment = new Moment();
    }

    public Expense() {
        moment = new Moment();
    }

    public Expense(String name, float amount, int index, Moment moment) {
        this.name = name;
        this.amount = amount;
        Expense.index = index;
        this.moment = moment;
    }

    public static int getIndex() {
        return index;
    }

    private int indexAutoIncrement() {
        return index++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Moment getMoment() {
        return moment;
    }

    public void setMoment(Moment moment) {
        this.moment = moment;
    }
}
