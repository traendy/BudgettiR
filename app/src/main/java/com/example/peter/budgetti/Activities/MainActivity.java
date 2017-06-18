package com.example.peter.budgetti.Activities;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peter.budgetti.Adapter.CustomExpandableListAdapter;
import com.example.peter.budgetti.Classes.Expense;
import com.example.peter.budgetti.Classes.Moment;
import com.example.peter.budgetti.Dialogs.AddExpenseDialog;
import com.example.peter.budgetti.Dialogs.CustomAlertDialog;
import com.example.peter.budgetti.Helper.StatusBarHelper;
import com.example.peter.budgetti.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private static final String BUDGET_NAME = "BUDGET";
    private static final String MONTHLY_BUDGET_NAME = "MONTHLYBUDGET";
    private static final String EXPENSE_LIST_NAME = "EXPENSELIST";
    private static final String EXPENSE_INDEX = "INDEX";
    private static final String EXPENSE_NAME = "NAME";
    private static final String EXPENSE_AMOUNT = "Amount";
    private static final String CURRENCY = " EURO";
    private static final String DATES_LIST_NAME = "DATES1.0";
    private static final String DATE_NAME = "DATE1.0";
    private static final String EXPENSE_MAP_NAME = "EXPENSE_MAP1.0";
    private static final String MOMENT_DATE_NAME = "MOMENT_DATE";
    private static final String MOMENT_TIME_NAME = "MOMENT_TIME";
    private static final String EXPENSE_MOMENT = "Moment";

    public static CustomExpandableListAdapter adapter;

    public static float Budget;
    public static float monthlyBudget;
    public static boolean recentAlert = false;
    public static int mStackLevel = 0;
    static SharedPreferences prefs;
    private static TextView remains_view;
    public static ExpandableListView list_view;
    public static HashMap<String,List<Expense>> expenseMap;
    public static List<String> keyDates;
    private static FloatingActionButton indicatorBtn;
    Button reset_btn, new_btn;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * handels the removing of a list element
     *
     * @param i list element index
     */
    public static void removeIndex(int i) {
        String temp = keyDates.get(i);
        expenseMap.remove(temp);
        keyDates.remove(i);
        adapter.notifyDataSetChanged();
        updateBudget();
    }

    /**
     * saves the List to shared prefs
     */
    private static void saveArrayList() {
        JSONArray jsonArray = new JSONArray();

        for (String str : keyDates) {
            JSONObject object = new JSONObject();
            try {
                object.put(DATE_NAME, str);
            } catch (JSONException ex) {
                Log.e("JSON ERROR", ex.getMessage());
            }
            jsonArray.put(object);

        }

        JSONArray jsonMap = new JSONArray();

        for(String key: keyDates){

            for(Expense expense: expenseMap.get(key)){
                JSONObject object = new JSONObject();
                JSONObject momentObject = new JSONObject();
                try{
                    momentObject.put(MOMENT_DATE_NAME, expense.getMoment().getDate());
                    momentObject.put(MOMENT_TIME_NAME, expense.getMoment().getTime());
                    object.put(EXPENSE_INDEX, expense.getIndex());
                    object.put(EXPENSE_NAME, expense.getName());
                    object.put(EXPENSE_AMOUNT, expense.getAmount());
                    object.put(EXPENSE_MOMENT, momentObject);
                    jsonMap.put(object);
                }catch (JSONException e){
                    Log.e("JSON ERROR", e.getMessage());
                }


            }
        }


        prefs.edit().putString(DATES_LIST_NAME, jsonArray.toString()).apply();
        prefs.edit().putString(EXPENSE_MAP_NAME, jsonMap.toString()).apply();
    }

    /**
     * sets the budget to shared prefs
     */
    private static void setBudget() {
        prefs.edit().putString(BUDGET_NAME, String.valueOf(Budget)).apply();
        prefs.edit().putString(MONTHLY_BUDGET_NAME, String.valueOf(monthlyBudget)).apply();
    }

    /**
     * updates the Budget text view and the budget itsself whenever it is changed
     * by a new expense or a delete
     */
    public static void updateBudget() {
        Budget = monthlyBudget;
        for (String str: keyDates) {
            for (Expense expense: expenseMap.get(str)){
                Budget -= expense.getAmount();
            }

        }
        String str = String.valueOf(Budget) + CURRENCY;
        remains_view.setText(str);
        saveArrayList();
        setBudget();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        new StatusBarHelper().invoke(this.getApplicationContext(), this);
        expenseMap = new HashMap<>();
        keyDates = new ArrayList<>();

        getSavedData();

        initElements();

        setListeners();

        PreferenceManagerInit();

        String str = String.valueOf(Budget) + CURRENCY;
        remains_view.setText(str);
        adapter.notifyDataSetChanged();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menulayout, menu);
        return true;

    }

    /**
     * In this part all the Listeners for the List, and buttons etc are initiated
     */
    private void setListeners() {
        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });

        new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewExpense();
            }
        });


        adapter = new CustomExpandableListAdapter(this, keyDates, expenseMap);
        list_view.setAdapter(adapter);

        /**
         * TODO
         * Wenn ich ein button in der expandable list view habe kann ich die view nicht expandieren
         * kann das mit recycle view gemacht werden?
         * wie kann ich sonst den indicator setzen?
         *
         */

        list_view.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(), "onGroupExpand", Toast.LENGTH_SHORT).show();
                indicatorBtn.setImageResource(R.drawable.arrow_minus);
            }
        });

        list_view.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(), "onGroupCollaps", Toast.LENGTH_SHORT).show();
                indicatorBtn.setImageResource(R.drawable.arrow_plus);
            }
        });
        //funktioniert
        list_view.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getApplicationContext(), "onchildclick", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    /**
     * initiation of the elements of the main activity
     */
    private void initElements() {
        list_view = (ExpandableListView) findViewById(R.id.list_view);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        indicatorBtn = (FloatingActionButton)findViewById(R.id.floatbtn);
        list_view.setIndicatorBounds(GetPixelFromDips(0), GetPixelFromDips(0));


        ViewCompat.setNestedScrollingEnabled(list_view,true);

        remains_view = (TextView) findViewById(R.id.remains_view);
        reset_btn = (Button) findViewById(R.id.reset_btn);
        new_btn = (Button) findViewById(R.id.new_btn);
    }
    private int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }


    /**
     * gets the List from the shared preferences
     * use it whenever u need the saved data
     */
    private void getSavedData() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        getArrayList();
        getBudget();

    }

    /**
     * function contains listeners to autosave the list if the app closes or stops
     */
    private void PreferenceManagerInit() {
        PreferenceManager.OnActivityDestroyListener destroyListener = new PreferenceManager.OnActivityDestroyListener() {
            @Override
            public void onActivityDestroy() {
                saveArrayList();
                setBudget();
            }
        };
        destroyListener.onActivityDestroy();
        PreferenceManager.OnActivityStopListener onActivityStopListener = new PreferenceManager.OnActivityStopListener() {
            @Override
            public void onActivityStop() {
                saveArrayList();
                setBudget();
            }
        };
        onActivityStopListener.onActivityStop();
    }

    /**
     * opens a dialog to reset and or set a new Budget
     */
    private void reset() {
        mStackLevel++;
        String warning = "Do you really want to delete your list and define a new Budget?";
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        CustomAlertDialog newAlert = CustomAlertDialog.newInstance(mStackLevel, 0, warning, 2);
        newAlert.show(ft, "dialog");
        // manage mStacklevel

        adapter.notifyDataSetChanged();

    }

    /**
     * opens a Dialog to add a new Expense to your list
     */
    private void addNewExpense() {
        mStackLevel++;

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        AddExpenseDialog newFragment = AddExpenseDialog.newInstance(mStackLevel);
        newFragment.show(ft, "dialog");

    }

    /**
     * opens a Dialog to ask if the klicked element shall be deleted and deletes it if wanted
     *
     * @param i index of the list element that shall be deleted
     */
    private void deleteEntry(final int i) {
        mStackLevel++;
        String warning = "Do you really want to delete this entry?";
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        CustomAlertDialog newAlert = CustomAlertDialog.newInstance(mStackLevel, i, warning, 1);
        newAlert.show(ft, "dialog");


    }

    /**
     * get the list of expeneses form the shared prefs
     */
    private void getArrayList() {

        String jsonDatesString = prefs.getString(DATES_LIST_NAME, "");
        keyDates = new ArrayList<>();
        try{
            JSONArray jsonDatesArray = new JSONArray(jsonDatesString);
            for(int i =0; i<jsonDatesArray.length(); i++){
                JSONObject j = jsonDatesArray.getJSONObject(i);
                keyDates.add(j.getString(DATE_NAME));
            }
        }catch (JSONException e){
            Log.e("JSON ERROR", "Could not decode keyDates: "+ e.getMessage());
        }


        //TODO fill map

        String jsonExpensMapString = prefs.getString(EXPENSE_MAP_NAME, "");
        expenseMap = new HashMap<>();

        try {
            JSONArray jsonMapArray = new JSONArray(jsonExpensMapString);
            for(int i = 0; i<jsonMapArray.length(); i++){
                JSONObject element = jsonMapArray.getJSONObject(i);
                JSONObject intern =  element.getJSONObject(EXPENSE_MOMENT);
                Moment moment = new Moment(intern.getString(MOMENT_DATE_NAME),
                        intern.getString(MOMENT_TIME_NAME));
                String dateString = moment.getDate();
                Expense tempExpense = new Expense(element.getString(EXPENSE_NAME),
                        Float.valueOf(element.getString(EXPENSE_AMOUNT)),
                        Integer.valueOf(element.getString(EXPENSE_INDEX)), moment);
                if(!expenseMap.containsKey(dateString)){
                    List<Expense> exList= new ArrayList<>();
                    exList.add(tempExpense);
                    expenseMap.put(dateString,exList);
                }else{
                    expenseMap.get(dateString).add(tempExpense);
                }
            }

        }catch (JSONException e){
            Log.e("JSON ERROR", e.getMessage());
        }

    }

    /**
     * gets the budget form shared Prefs
     */
    private void getBudget() {
        monthlyBudget = Float.parseFloat(prefs.getString(MONTHLY_BUDGET_NAME, "0"));
        Budget = Float.parseFloat(prefs.getString(BUDGET_NAME, "0"));
    }

    /**
     * I dont trust the on destroy listenrs so this will use the life cycle model to save the list
     * and the budget
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveArrayList();
        setBudget();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Budgetti")
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://www.traendy-eistee.de"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
