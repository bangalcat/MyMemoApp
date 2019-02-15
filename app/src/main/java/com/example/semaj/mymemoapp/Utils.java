package com.example.semaj.mymemoapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {
    public static long createKey(){
        Calendar c = Calendar.getInstance();
        return c.getTimeInMillis();
    }
    static SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm");
    public static String getDateString(Date date){
        return format.format(date);
    }
    public static Date parseDate(String date) throws ParseException {
        return format.parse(date);
    }
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,@NonNull Fragment fragment,@NonNull int frameId){
        fragmentManager.beginTransaction()
                .add(frameId, fragment)
                .commit();
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
