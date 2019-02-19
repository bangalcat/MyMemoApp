package com.example.semaj.mymemoapp.memolist;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.semaj.mymemoapp.R;
import com.example.semaj.mymemoapp.Utils;
import com.example.semaj.mymemoapp.data.MemoRepository;
import com.example.semaj.mymemoapp.data.local.LocalMemoDataSource;

public class MainActivity extends AppCompatActivity {

    // 현재는 딱히 가지고 있을 필요 없는 변수
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
        //presenter 생성시 fragment를 view로 넘겨주면, presenter 안에서 view에 자신을 presenter를 등록
        mPresenter = new MemoListPresenter(MemoRepository.getInstance(LocalMemoDataSource.getInstance(this)), fragment);

        //fab id
        mAddBtn = findViewById(R.id.fab_add);
    }

}
