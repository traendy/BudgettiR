package com.example.peter.budgetti.Helper;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

import com.example.peter.budgetti.Activities.MainActivity;
import com.example.peter.budgetti.R;


/**
 * Changes the Statusbar to @colot.background blue for all activities
 */

public class StatusBarHelper {
    public void invoke(Context context, MainActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(ContextCompat.getColor(context, R.color.statusbar));
            //
        }
    }

}