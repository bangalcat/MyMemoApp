package com.example.semaj.mymemoapp.memolist;

import com.example.semaj.mymemoapp.data.Memo;
import com.example.semaj.mymemoapp.data.MemoDataSource;
import com.example.semaj.mymemoapp.data.MemoRepository;

import java.util.List;

public class MemoListPresenter implements MainContract.Presenter {

    private MemoRepository mRepo;
    private MainContract.View mView;
    private boolean isFirstLoad;

    /**
     * Memo Repository, Memo List View
    */
    MemoListPresenter(MemoRepository repository, MainContract.View view) {
        mRepo = repository;
        mView = view;
        isFirstLoad = true;
        mView.setPresenter(this);
    }

    @Override
    public void loadData(boolean forceUpdate) {
        if(forceUpdate)
        mRepo.getMemoList(new MemoDataSource.DataListCallback() {
            @Override
            public void onListLoaded(List<Memo> memoList) {
                mView.showMemoList(memoList);
            }

            @Override
            public void onError() {
                mView.showMessage("메모 목록을 불러오는데 실패하였습니다.");
            }
        });
        isFirstLoad = false;
    }

    @Override
    public void addNewMemo() {
        mView.showAddMemo();
    }

    @Override
    public void openMemoDetail(Memo item) {
        mView.showMemoDetail(item.getId());
    }

    @Override
    public void onResume() {
        loadData(isFirstLoad);
    }
}
