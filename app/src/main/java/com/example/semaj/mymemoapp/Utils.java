package com.example.semaj.mymemoapp;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class Utils {
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,@NonNull Fragment fragment,@NonNull int frameId){
        fragmentManager.beginTransaction()
                .add(frameId, fragment)
                .commit();
    }
}
