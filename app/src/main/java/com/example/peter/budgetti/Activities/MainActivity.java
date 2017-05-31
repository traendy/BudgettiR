package com.example.peter.budgetti.Activities;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.peter.budgetti.Adapter.CustomAdapter;
import com.example.peter.budgetti.Classes.Expense;
import com.example.peter.budgetti.Dialogs.AddExpenseDialog;
import com.example.peter.budgetti.Dialogs.CustomAlertDialog;
import com.example.peter.budgetti.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private static final String BUDGET_NAME = "BUDGET";
    private static final String MONTHLY_BUDGET_NAME = "MONTHLYBUDGET";
    private static final String EXPENSE_LIST_NAME = "EXPENSELIST";
    private static final String EXPENSE_INDEX = "INDEX";
    private static final String EXPENSE_NAME = "NAME";
    private static final String EXPENSE_AMOUNT = "Amount";
    private static final String CURRENCY = " EURO";
    public static CustomAdapter adapter;
    public static List<Expense> expense_list;
    public static float Budget;
    public static float monthlyBudget;
    public static boolean recentAlert = false;
    public static int mStackLevel = 0;
    static SharedPreferences prefs;
    private static TextView remains_view;
    ListView list_view;
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
        adapter.remove(i);
        adapter.notifyDataSetChanged();
        updateBudget();

    }

    /**
     * saves the List to shared prefs
     */
    private static void saveArrayList() {
        JSONArray jsonArray = new JSONArray();

        for (Expense e : expense_list) {
            JSONObject object = new JSONObject();
            try {
                object.put(EXPENSE_NAME, e.getName());
                object.put(EXPENSE_AMOUNT, String.valueOf(e.getAmount()));
                object.put(EXPENSE_INDEX, String.valueOf(Expense.getIndex()));

            } catch (JSONException ex) {
                Log.d("ERROR", ex.getMessage());
            }
            jsonArray.put(object);

        }

        prefs.edit().putString(EXPENSE_LIST_NAME, jsonArray.toString()).apply();
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
        for (int i = 0; i < expense_list.size(); i++) {
            Budget -= expense_list.get(i).getAmount();
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
        expense_list = new ArrayList<>();

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

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteEntry(i);
            }
        });

        adapter = new CustomAdapter(this, R.layout.complex_list_item, expense_list);
        list_view.setAdapter(adapter);
    }

    /**
     * initiation of the elements of the main activity
     */
    private void initElements() {
        list_view = (ListView) findViewById(R.id.list_view);
        remains_view = (TextView) findViewById(R.id.remains_view);
        reset_btn = (Button) findViewById(R.id.reset_btn);
        new_btn = (Button) findViewById(R.id.new_btn);
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

        String jsonString = prefs.getString(EXPENSE_LIST_NAME, "");
        expense_list = new ArrayList<>();
        try {
            JSONArray json = new JSONArray(jsonString);
            for (int i = 0; i < json.length(); i++) {
                JSONObject j = json.getJSONObject(i);
                Expense e = new Expense(j.getString(EXPENSE_NAME),
                        Float.parseFloat(j.getString(EXPENSE_AMOUNT)),
                        Integer.parseInt(j.getString(EXPENSE_INDEX)));
                expense_list.add(e);
            }
        } catch (JSONException e) {
            Log.d("ERROR", e.getMessage());
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
