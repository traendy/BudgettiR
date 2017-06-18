package com.example.peter.budgetti.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peter.budgetti.Activities.MainActivity;
import com.example.peter.budgetti.Classes.Expense;
import com.example.peter.budgetti.R;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by peter on 18.06.2017.
 */

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<Expense>> expandableListDetail;
    //private DecimalFormat format = new DecimalFormat("#0.00");

    public CustomExpandableListAdapter(Context context, List<String> expandableListTitle,
                                       HashMap<String, List<Expense>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final Expense expandedListText = (Expense) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.complex_list_item, null);
        }
        TextView nameView = (TextView) convertView
                .findViewById(R.id.name_view);

        TextView amountView = (TextView) convertView
                .findViewById(R.id.amount_view);

        nameView.setText(expandedListText.getName());
        amountView.setText(String.valueOf(expandedListText.getAmount())+" €");
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, final ViewGroup parent) {
        String keyDate = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.custom_expandable_item, null);
        }
        TextView dateView = (TextView) convertView
                .findViewById(R.id.date_today_view);
        TextView sumView = (TextView) convertView.findViewById(R.id.spent_today_view);

        //Button btn = (Button)convertView.findViewById(R.id.drop_button);

        dateView.setText(keyDate);

        sumView.setText(calculateSum(keyDate)+" €");

        return convertView;
    }


    private String calculateSum(String key){
        float sum =0.0f;
        for(Expense e: expandableListDetail.get(key)){
            sum+=Float.valueOf(e.getAmount());
        }
       // return format.format(String.valueOf(sum));
        return String.valueOf(sum);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    public static void addExpense(Expense expense){
        String date = expense.getMoment().getDate();
        if(!MainActivity.keyDates.contains(date)){
            MainActivity.keyDates.add(date);
            List<Expense> list= new ArrayList<>();
            list.add(expense);
            MainActivity.expenseMap.put(date, list);
        }else{
            MainActivity.expenseMap.get(date).add(expense);
        }
        MainActivity.updateBudget();
        //ggf notifyDataSetChanged

    }

}
