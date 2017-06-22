package com.example.peter.budgetti.Classes;

/**
 * Created by peter on 21.06.2017.
 */

public class Score {

    private String month;
    private float sum;
    private int jear;
    private static int id;

    public Score(String month, float sum, int jear, int id) {
        this.month = month;
        this.sum = sum;
        this.jear = jear;
        Score.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public int getJear() {
        return jear;
    }

    public void setJear(int jear) {
        this.jear = jear;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
