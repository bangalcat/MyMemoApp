package com.example.semaj.mymemoapp.addeditmemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.semaj.mymemoapp.R;
import com.example.semaj.mymemoapp.data.MemoRepository;
import com.example.semaj.mymemoapp.data.local.LocalMemoDataSource;

public class AddEditMemoActivity extends AppCompatActivity {

    private AddEditMemoPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_memo);

        AddEditMemoFragment fragment = (AddEditMemoFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);

        long memoId = getIntent().getLongExtra(AddEditMemoFragment.ARG_MEMO_ID, -1);

        if(fragment == null){
            fragment = AddEditMemoFragment.newInstance(memoId);
        }

        mPresenter = new AddEditMemoPresenter(
                memoId,
                MemoRepository.getInstance(LocalMemoDataSource.getInstance(getApplicationContext())),
                fragment,
                false
        );

    }
}
