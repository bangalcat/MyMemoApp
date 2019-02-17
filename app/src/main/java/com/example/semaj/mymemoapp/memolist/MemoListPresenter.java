package com.example.semaj.mymemoapp.memolist;

import com.example.semaj.mymemoapp.data.Memo;
import com.example.semaj.mymemoapp.data.MemoRepository;

import java.util.Collections;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MemoListPresenter implements MainContract.Presenter {

    private MemoRepository mRepo;
    private MainContract.View mView;
    private boolean isFirstLoad;

    private CompositeDisposable mCompositeDisposable;

    private long[] selectedIds;
    private int selectedCnt;

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
                            .doOnNext(memoList -> {
                                Collections.sort(memoList, (o1, o2) -> o2.getDate().compareTo(o1.getDate()));
                                selectedIds = new long[memoList.size()];
                                selectedCnt = 0;
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(memoList -> {
                                    mView.showMemoList(memoList);
                                },
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
    public void onClickDeleteSelectedMemos() {
        int cnt = selectedCnt;
        mCompositeDisposable.add(
                mRepo.deleteMemos(selectedIds, selectedCnt)
                        .subscribeOn(Schedulers.io())
                        .doOnComplete(() -> {
                            loadData(true);
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() ->{
                            mView.showMessage("선택된 "+cnt+"개 메모가 삭제되었습니다");
                        },throwable -> {
                            if(throwable instanceof IndexOutOfBoundsException)
                                mView.showMessage("선택된 메시지가 없습니다");
                            else
                                mView.showMessage("메시지 삭제 오류");
                        })
        );
    }

    @Override
    public void onLongClickMemo(Memo item) {
        mView.toggleSelectMode(true);
    }

    //선택모드에서 아이템 하나 선택
    @Override
    public void selectOne(Memo item) {
        for(int i=0;i<selectedCnt;++i)
            if(selectedIds[i]==item.getId()){
                selectedIds[i] = selectedIds[--selectedCnt];
            }
        selectedIds[selectedCnt++] = item.getId();
    }

    //모든 선택 취소 및 선택창 토글
    @Override
    public void onClickSelectCancel() {
        selectedCnt = 0;
        mView.toggleSelectMode(false);
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
