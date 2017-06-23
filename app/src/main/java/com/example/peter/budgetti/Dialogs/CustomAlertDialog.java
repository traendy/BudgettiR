package com.example.peter.budgetti.Dialogs;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.peter.budgetti.Activities.MainActivity;
import com.example.peter.budgetti.Helper.DatabaseSource;
import com.example.peter.budgetti.R;

/**
 * Class defines a Custom Alert Dialog, can be changed by a string as a message and altering
 * the "accept" method.
 */

public class CustomAlertDialog extends DialogFragment {

    private static int index;
    private static int functionIndex = 0;
    private static String warning = "danger!";
    private Button cancel_btn, accept_btn;
    private TextView alert_msg_view;
    private int mNum;

    public static CustomAlertDialog newInstance(int num, int i, String warningMsg, int whichFunction) {
        CustomAlertDialog f = new CustomAlertDialog();
        index = i;
        warning = warningMsg;
        functionIndex = whichFunction;

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
        View rootView = inflater.inflate(R.layout.custom_alert_dialog, container, false);
        getDialog().setTitle("Danger");

        alert_msg_view = (TextView) rootView.findViewById(R.id.ad_alert_view);
        alert_msg_view.setText(warning);
        cancel_btn = (Button) rootView.findViewById(R.id.ad_cancel_btn);
        accept_btn = (Button) rootView.findViewById(R.id.ad_accept_btn);


        accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accept();

            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (MainActivity.recentAlert) {
            MainActivity.recentAlert = false;

            dismiss();

        }

    }

    /**
     * this function handels the possibility of an accepted alert message by a given index
     */
    private void accept() {
        switch (functionIndex) {
            case 1:
                MainActivity.removeIndex(index);
               //TODO delete entryfrom db
                dismiss();
                break;
            case 2:
                MainActivity.keyDates.clear();
                MainActivity.expenseMap.clear();
                dismiss();

                MainActivity.mStackLevel++;

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                ChangeBudgetDialog newFragment = ChangeBudgetDialog.newInstance(MainActivity.mStackLevel);
                newFragment.show(ft, "dialog");
                dismiss();

                break;
            default:
                break;
        }

    }

}
