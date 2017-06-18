package com.example.peter.budgetti.Dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.peter.budgetti.Activities.MainActivity;
import com.example.peter.budgetti.R;

/**
 * Dialog class that constructs a Window to change the Budget the User want to spent.
 */

public class ChangeBudgetDialog extends DialogFragment {

    private final String CURRENCY = " EURO";
    TextView old_budget;
    EditText new_budget;
    Button abort_btn, change_btn;
    int mNum;

    public static ChangeBudgetDialog newInstance(int num) {
        ChangeBudgetDialog f = new ChangeBudgetDialog();
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments().getInt("num");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.change_budget_dialog, container, false);
        getDialog().setTitle("New Task");
        MainActivity.recentAlert = true;
        old_budget = (TextView) rootView.findViewById(R.id.old_budget_view);
        setOldBudget();
        new_budget = (EditText) rootView.findViewById(R.id.new_budget_edit);
        abort_btn = (Button) rootView.findViewById(R.id.abort_btn);
        change_btn = (Button) rootView.findViewById(R.id.change_btn);
        abort_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!new_budget.getText().toString().isEmpty()) {
                    MainActivity.Budget = Float.parseFloat(new_budget.getText().toString());
                    MainActivity.monthlyBudget = MainActivity.Budget;
                    MainActivity.keyDates.clear();
                    MainActivity.expenseMap.clear();
                    //TODO abfrage ob daten gespeichert werden sollen
                    MainActivity.adapter.notifyDataSetChanged();
                    MainActivity.updateBudget();

                    dismiss();
                }
            }
        });
        new_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_budget.selectAll();
            }
        });
        return rootView;
    }

    /**
     * sets an old budget in the old budget view
     */
    private void setOldBudget() {
        String str = String.valueOf(MainActivity.monthlyBudget) + CURRENCY;
        old_budget.setText(str);
    }
}
