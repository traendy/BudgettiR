package com.example.peter.budgetti.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by peter on 20.06.2017.
 *
 * https://developer.android.com/guide/topics/data/data-storage.html#db
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME ="BUDGETTI";

    private static final int DATABASE_VERSION = 1;

    private static final String EXPENSES_TABLE_CREATE = "EXPENSES_TABLE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EXPENSES_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
