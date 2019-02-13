package com.example.semaj.mymemoapp;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private MainContract.Presenter mPresenter;
    private FloatingActionButton mAddBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MemoListFragment fragment = (MemoListFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        //create fragment if null
        if(fragment == null){
            fragment = MemoListFragment.newInstance();
            Utils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.content_frame);
        }

        //create new presenter


        //fab id
        mAddBtn = findViewById(R.id.fab_add);
    }
}
