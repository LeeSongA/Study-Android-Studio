package com.example.ms.work;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by ms on 2018-02-01.
 */

public class Util {

    public static int getdpi(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    public static int covertDptoPx(Context context, int dp) {
        return dp * (getdpi(context) / 160);
    }
    public static int covertPxtoDp(Context context, int px) {
        return px / (getdpi(context) / 160);
    }

}
