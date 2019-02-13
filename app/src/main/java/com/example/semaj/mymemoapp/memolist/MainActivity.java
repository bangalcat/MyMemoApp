package com.example.semaj.mymemoapp.memolist;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.semaj.mymemoapp.R;
import com.example.semaj.mymemoapp.Utils;
import com.example.semaj.mymemoapp.data.MemoRepository;
import com.example.semaj.mymemoapp.data.local.LocalMemoDataSource;

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
        mPresenter = new MemoListPresenter(MemoRepository.getInstance(LocalMemoDataSource.getInstance(this)), fragment);

        //fab id
        mAddBtn = findViewById(R.id.fab_add);
    }
}
