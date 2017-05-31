package com.example.peter.budgetti.Classes;

/**
 * Main Class defines an Expense by its name, price/amount and an index to sort.
 */

public class Expense {


    private static int index;
    private String name;
    private float amount;


    public Expense(String name, float amount, int index) {
        this.name = name;
        this.amount = amount;
        Expense.index = index;
    }

    public Expense() {
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
}
