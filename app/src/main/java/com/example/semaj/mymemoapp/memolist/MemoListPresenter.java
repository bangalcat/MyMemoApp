package com.example.semaj.mymemoapp.memolist;

import com.example.semaj.mymemoapp.data.MemoRepository;

public class MemoListPresenter implements MainContract.Presenter {

    private MemoRepository mRepo;
    private MainContract.View mView;

    /**
     * Memo Repository, Memo List View
    */
    public MemoListPresenter(MemoRepository repository, MainContract.View view) {
        mRepo = repository;
        mView = view;
    }

    @Override
    public void addNewMemo() {

    }

    @Override
    public void openMemoDetail() {

    }

    @Override
    public void onResume() {

    }
}
