package com.example.peter.budgetti.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by peter on 20.06.2017.
 *
 * https://developer.android.com/guide/topics/data/data-storage.html#db
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME ="BUDGETTI3";

    private static final int DATABASE_VERSION = 3;

    public static final String EXPENSES_TABLE_NAME = "EXPENSES_TABLE";
    public static final String DAY_TABLE_NAME = "DAY_TABLE";
    public static final String MOMENT_TABLE_NAME = "MOMENT_TABLE";
    public static final String MONTH_HIGHSCORE_TABLE_NAME = "HIGH_SCORE_TABLE";

    public static final String ID_COLUMN = "ID";
    public static final String ID_COLUMN2 = "ID";
    public static final String AMOUNT_COLUMN = "AMOUNT";
    public static final String NAME_COLUMN = "NAME";

    public static final String DATE_COLUMN = "DATE";
    public static final String TIME_COLUMN = "TIME";

    public static final String DAY_SUM_COLUMN = "SUM";

    public static final String MONTH_SUM_COLUMN = "MONTH_SUM";
    public static final String MONTH_COLUMN = "MONTH";
    public static final String JEAR_COLUMN = "JEAR";





    private static final String EXPENSES_TABLE_CREATE = "CREATE TABLE " + EXPENSES_TABLE_NAME
                                                        + " (" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                        + NAME_COLUMN + " TEXT NOT NULL, "
                                                        + AMOUNT_COLUMN + " REAL NOT NULL, "
                                                        + DATE_COLUMN + " TEXT NOT NULL, "
                                                        + TIME_COLUMN + " TEXT NOT NULL);";

    private static final String DAY_TABLE_CREATE = "CREATE TABLE " + DAY_TABLE_NAME
                                                        + " (" +DATE_COLUMN + " TEXT, "
                                                        + DAY_SUM_COLUMN + "REAL, "
                                                        + "FOREIGN KEY(" + DATE_COLUMN + ") REFERENCES "
                                                        + EXPENSES_TABLE_NAME +" ("
                                                        + DATE_COLUMN +"));";

    private static final String MOMENT_TABLE_CREATE = "CREATE TABLE " + MOMENT_TABLE_NAME
                                                        + " (" + DATE_COLUMN + " TEXT, "
                                                        + TIME_COLUMN + " TEXT, "
                                                        + "FOREIGN KEY(" + DATE_COLUMN +", " + TIME_COLUMN
                                                        + ") REFERENCES "+ EXPENSES_TABLE_NAME
                                                        + "("+ DATE_COLUMN+", "+ TIME_COLUMN +"));";

    private static final String MONTH_HIGHSCORE_CREATE = "CREATE TABLE " + MONTH_HIGHSCORE_TABLE_NAME
                                                        + " (" + ID_COLUMN2 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                        + MONTH_SUM_COLUMN + " REAL NOT NULL, "
                                                        + MONTH_COLUMN + " TEXT NOT NULL, "
                                                        + JEAR_COLUMN + " INT NOT NULL);";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MONTH_HIGHSCORE_CREATE);
        db.execSQL(EXPENSES_TABLE_CREATE);
        //db.execSQL(DAY_TABLE_CREATE);
        //db.execSQL(MOMENT_TABLE_CREATE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void dropTable(SQLiteDatabase db, String tableName){
        try {
            db.execSQL("DROP TABLE " + tableName);
        }catch(SQLiteException e){
            Log.e(DatabaseSource.DATABASE_SOURCE_LOG_TAG, e.getMessage());
        }
    }
}
