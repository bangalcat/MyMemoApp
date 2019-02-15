package com.example.semaj.mymemoapp.memolist;

import com.example.semaj.mymemoapp.data.Memo;
import com.example.semaj.mymemoapp.data.MemoDataSource;
import com.example.semaj.mymemoapp.data.MemoRepository;

import java.util.Collections;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MemoListPresenter implements MainContract.Presenter {

    private MemoRepository mRepo;
    private MainContract.View mView;
    private boolean isFirstLoad;

    private CompositeDisposable mCompositeDisposable;

    /**
     * Memo Repository, Memo List View
    */
    MemoListPresenter(MemoRepository repository, MainContract.View view) {
        mRepo = repository;
        mView = view;
        isFirstLoad = true;
        mView.setPresenter(this);
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void loadData(boolean forceUpdate) {
        if(forceUpdate)
            mCompositeDisposable.add(
                    mRepo.getMemoList()
                            .doOnNext(memoList -> Collections.sort(memoList, (o1, o2) -> o2.getDate().compareTo(o1.getDate())))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(memoList -> mView.showMemoList(memoList),
                                    throwable -> mView.showMessage("메모 목록을 불러오는데 실패하였습니다."))
            );
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
    public void onClickDeleteAllMemo() {
        mCompositeDisposable.add(
                mRepo.deleteAllMemo()
                        .subscribeOn(Schedulers.io())
                        .doOnComplete(() -> loadData(true))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() ->{
                            mView.showMessage("모든 메모가 삭제되었습니다");
                        })
        );
    }

    @Override
    public void onLongClickMemo(Memo item) {
        mView.toggleSelectMode(true);
    }

    @Override
    public void selectOne(Memo item) {

    }

    @Override
    public void subscribe() {
        loadData(isFirstLoad);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
