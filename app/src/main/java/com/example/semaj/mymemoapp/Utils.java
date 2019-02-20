package com.example.semaj.mymemoapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {
    //그냥 시간을 밀리세컨으로 얻은것을 key로
    public static long createKey(){
        Calendar c = Calendar.getInstance();
        return c.getTimeInMillis();
    }
    static SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm",Locale.getDefault());
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

    public static void showToastMessage(Activity activity, String message){
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public static AlertDialog.Builder getSaveAlertDialog(Context context, String title, String message, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener){
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("저장",positiveListener)
                .setCancelable(true)
                .setNegativeButton("저장하지 않음", negativeListener);
    }
    public static AlertDialog.Builder getDeleteConfirmDialog(Context context, String title, String message, DialogInterface.OnClickListener confirmListener){
        return new AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(true)
            .setPositiveButton("삭제",confirmListener)
            .setNegativeButton("취소",(dialogInterface, i) -> dialogInterface.cancel());
    }
}
