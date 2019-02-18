package com.example.semaj.mymemoapp.memolist;

import android.text.TextUtils;

import com.example.semaj.mymemoapp.data.Memo;
import com.example.semaj.mymemoapp.data.MemoRepository;

import java.util.Collections;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MemoListPresenter implements MainContract.Presenter {

    private MemoRepository mRepo;
    private MainContract.View mView;
    private boolean isFirstLoad;

    // disposable을 관리
    // clear해주면 알아서 구독 해제됨
    private CompositeDisposable mCompositeDisposable;

    private long[] selectedIds;
    private int selectedCnt;

    /**
     *  injection은 따로 하지않고 Activity에서 생성할때 해주는걸로
     * @param repository 데이터 불러올 repo
     * @param view  보여줄 view
     */
    MemoListPresenter(MemoRepository repository, MainContract.View view) {
        mRepo = repository;
        mView = view;
        //first load 시 캐시 삭제후 reload
        isFirstLoad = true;
        //넘겨받은 view에 자신을 presenter로 등록
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

    // longclick하면 select mode로 전환
    // item select는 view에서 또 따로 처리
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
                return;
            }
        selectedIds[selectedCnt++] = item.getId();
    }

    //모든 선택 취소 및 선택창 토글
    @Override
    public void onClickSelectCancel() {
        selectedCnt = 0;
        mView.toggleSelectMode(false);
    }

    /**
     *  repo에서 memo list를 일단 전부 불러 온 뒤, 그 중 query를 포함한 Memo만
     *  view에 show한다
     * @param query text is filtered by query
     */
    @Override
    public void filter(String query) {
        mCompositeDisposable.add(
                mRepo.getMemoList()
                        .subscribeOn(Schedulers.computation())
                        .flatMap(Flowable::fromIterable)
                        .filter(memo -> memo.getTitle().contains(query) || memo.getContent().contains(query))
                        .toList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(memoList -> mView.showMemoList(memoList),
                                throwable -> {
                                    throwable.printStackTrace();
                                })
        );
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
