package com.example.peter.budgetti.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.peter.budgetti.Classes.Expense;
import com.example.peter.budgetti.Classes.Moment;
import com.example.peter.budgetti.Classes.Score;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peter on 21.06.2017.
 */

public class DatabaseSource {

    public static final String DATABASE_SOURCE_LOG_TAG = "DATABASE_SOURCE";

    private SQLiteDatabase database;
    private SQLiteDatabase dayDatabase;
    //private SQLiteDatabase highscoreDatabase;
    private SQLiteDatabase momentDatabase;

    private DatabaseHelper dbHelper;

    private String[] expensesColumns = {
            DatabaseHelper.ID_COLUMN,
            DatabaseHelper.NAME_COLUMN,
            DatabaseHelper.AMOUNT_COLUMN,
            DatabaseHelper.DATE_COLUMN,
            DatabaseHelper.TIME_COLUMN
    };

    private String[] scoreColumns = {
            DatabaseHelper.ID_COLUMN2,
            DatabaseHelper.MONTH_SUM_COLUMN,
            DatabaseHelper.MONTH_COLUMN,
            DatabaseHelper.JEAR_COLUMN
    };

    public DatabaseSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }


    public void open(){
        database = dbHelper.getWritableDatabase();
        //dayDatabase = dbHelper.getWritableDatabase();
        //highscoreDatabase = dbHelper.getWritableDatabase();
        //
    }


    public void close(){
        dbHelper.close();
    }

    public Expense createExpense(String name, float amount, Moment moment){
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.NAME_COLUMN, name);
        contentValues.put(dbHelper.AMOUNT_COLUMN, amount);
        contentValues.put(dbHelper.DATE_COLUMN, moment.getDate());
        contentValues.put(dbHelper.TIME_COLUMN, moment.getTime());

        long insertId = database.insert(dbHelper.EXPENSES_TABLE_NAME, null, contentValues);

        Cursor cursor = database.query(dbHelper.EXPENSES_TABLE_NAME, expensesColumns,
                dbHelper.ID_COLUMN + "=" + insertId, null, null, null, null);
        cursor.moveToFirst();
        Expense expense = cursorToExpenses(cursor);
        cursor.close();


        return expense;
    }

    public Score createScore(String month, float sum, int jear){
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.MONTH_SUM_COLUMN, sum);
        contentValues.put(dbHelper.MONTH_COLUMN, month);
        contentValues.put(dbHelper.JEAR_COLUMN, jear);

        long insertId = database.insert(dbHelper.MONTH_HIGHSCORE_TABLE_NAME, null, contentValues);

        Cursor cursor = database.query(dbHelper.MONTH_HIGHSCORE_TABLE_NAME, scoreColumns,
                dbHelper.ID_COLUMN2 + "=" + insertId, null, null, null, null);
        cursor.moveToFirst();
        Score score = cursorToScore(cursor);
        cursor.close();
        return score;
    }

    private Expense cursorToExpenses(Cursor cursor){

        int idIndex = cursor.getColumnIndex(dbHelper.ID_COLUMN);
        int idName = cursor.getColumnIndex(dbHelper.NAME_COLUMN);
        int idAmount = cursor.getColumnIndex(dbHelper.AMOUNT_COLUMN);
        int idDate = cursor.getColumnIndex(dbHelper.DATE_COLUMN);
        int idTime = cursor.getColumnIndex(dbHelper.TIME_COLUMN);

        String name = cursor.getString(idName);
        float amount = cursor.getFloat(idAmount);
        String date = cursor.getString(idDate);
        String time = cursor.getString(idTime);
        int id = cursor.getInt(idIndex);

        Expense expense = new Expense(name, amount, id, new Moment(date, time));
        return expense;
    }

    private Score cursorToScore(Cursor cursor){

        int idIndex = cursor.getColumnIndex(dbHelper.ID_COLUMN);
        int idMonth = cursor.getColumnIndex(dbHelper.MONTH_COLUMN);
        int idAmount = cursor.getColumnIndex(dbHelper.MONTH_SUM_COLUMN);
        int idJear  = cursor.getColumnIndex(dbHelper.JEAR_COLUMN);

        int id = cursor.getInt(idIndex);
        String month = cursor.getString(idMonth);
        float amount = cursor.getFloat(idAmount);
        int jear = cursor.getInt(idJear);

        Score score = new Score(month, amount, jear, id);
        return score;

    }

    public List<Expense> getAllExpenses(){
        List<Expense> expenseList = new ArrayList<>();

        Cursor cursor = database.query(dbHelper.EXPENSES_TABLE_NAME, expensesColumns, null, null, null, null, null);

        cursor.moveToFirst();

        Expense expense;

        while (!cursor.isAfterLast()){
            expense = cursorToExpenses(cursor);
            expenseList.add(expense);
            cursor.moveToNext();
        }
        cursor.close();
        return expenseList;
    }

    public List<Score> getAllScores(){
        List<Score> scoreList = new ArrayList<>();

        Cursor cursor = database.query(dbHelper.MONTH_HIGHSCORE_TABLE_NAME, scoreColumns, null, null, null, null, null);

        cursor.moveToFirst();

        Score score;

        while (!cursor.isAfterLast()){
            score = cursorToScore(cursor);
            scoreList.add(score);
            cursor.moveToNext();
        }
        cursor.close();
        return scoreList;
    }

    public void deleteExpense(Expense expense){
        long id = expense.getIndex();
        database.delete(dbHelper.EXPENSES_TABLE_NAME, dbHelper.ID_COLUMN + "=" +id, null);
    }


    public void deleteScore(Score score){
        long id = score.getId();
        database.delete(dbHelper.MONTH_HIGHSCORE_TABLE_NAME, dbHelper.ID_COLUMN + "=" +id, null);
    }

    public void dropTable(String tableName){
        ArrayList<String> arrTblNames = new ArrayList<String>();
        Cursor c = dbHelper.getWritableDatabase().rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                arrTblNames.add( c.getString( c.getColumnIndex("name")) );
                c.moveToNext();
            }
        }
        Log.e("SQLite", arrTblNames.toString() );
        dbHelper.dropTable(dbHelper.getWritableDatabase(), tableName);
    }


}
