package com.example.semaj.mymemoapp;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.Calendar;

public class Utils {
    public static long createKey(){
        Calendar c = Calendar.getInstance();
        return c.getTimeInMillis();
    }
    public static String getDateString(){
        //todo change format
        return Calendar.getInstance().toString();
    }
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,@NonNull Fragment fragment,@NonNull int frameId){
        fragmentManager.beginTransaction()
                .add(frameId, fragment)
                .commit();
    }
}
