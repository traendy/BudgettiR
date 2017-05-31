package com.example.peter.budgetti.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.peter.budgetti.Activities.MainActivity;
import com.example.peter.budgetti.Classes.Expense;
import com.example.peter.budgetti.R;

import java.util.List;


/**
 * Custom Adapter that manages the list View of expenses in the MainActivity
 */

public class CustomAdapter extends ArrayAdapter<Expense> {
    private static final String CURRENCY = " EURO";

    public CustomAdapter(Context context, int resource, List<Expense> objects) {
        super(context, resource, objects);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.complex_list_item, null);

        }
        TextView name_view = (TextView) v.findViewById(R.id.name_view);
        TextView amount_view = (TextView) v.findViewById(R.id.amount_view);
        name_view.setText(MainActivity.expense_list.get(position).getName());
        String str = MainActivity.expense_list.get(position).getAmount() + CURRENCY;
        amount_view.setText(str);

        return v;
    }

    /**
     * adds a expense and notifies List view
     *
     * @param e the new expense
     */
    public void addExpense(final Expense e) {
        MainActivity.expense_list.add(e);
        MainActivity.updateBudget();
        notifyDataSetChanged();
    }

    /**
     * removes a expense defined by its index in the list
     *
     * @param position index of the list item
     */
    public void remove(final int position) {
        MainActivity.expense_list.remove(position);
        notifyDataSetChanged();
    }

}
