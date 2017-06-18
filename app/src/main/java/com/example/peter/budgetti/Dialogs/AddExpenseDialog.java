package com.example.peter.budgetti.Dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.peter.budgetti.Activities.MainActivity;
import com.example.peter.budgetti.Classes.Expense;
import com.example.peter.budgetti.R;

/**
 * Dialog class that constructs a window, in witch the user can add an expense to his list
 */

public class AddExpenseDialog extends DialogFragment {

    EditText amount_edit, name_edit;
    Button abort_btn, add_btn;
    private int mNum;

    public static AddExpenseDialog newInstance(int num) {
        AddExpenseDialog f = new AddExpenseDialog();

        // Supply num input as an argument.
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
        View rootView = inflater.inflate(R.layout.add_expense_dialog, container, false);
        getDialog().setTitle("New Expense");

        amount_edit = (EditText) rootView.findViewById(R.id.amount_edit);
        name_edit = (EditText) rootView.findViewById(R.id.name_edit);
        abort_btn = (Button) rootView.findViewById(R.id.abort_btn);
        add_btn = (Button) rootView.findViewById(R.id.add_btn);

        abort_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Expense e = new Expense();
                e.setName(name_edit.getText().toString());
                e.setAmount(Float.parseFloat(amount_edit.getText().toString()));
                MainActivity.adapter.addExpense(e);
                MainActivity.adapter.notifyDataSetChanged();
                dismiss();
            }
        });
        amount_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount_edit.selectAll();
            }
        });
        name_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name_edit.selectAll();
            }
        });

        return rootView;
    }
}
