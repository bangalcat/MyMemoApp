package com.example.semaj.mymemoapp.addeditmemo;

import com.example.semaj.mymemoapp.Utils;
import com.example.semaj.mymemoapp.data.Memo;
import com.example.semaj.mymemoapp.data.MemoRepository;

import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AddEditMemoPresenter implements AddEditContract.Presenter {

    private long mId;
    private MemoRepository mRepo;
    private AddEditContract.View mView;
    private boolean mShouldLoadData;
    private CompositeDisposable mCompositeDisposable;

    public AddEditMemoPresenter(long memoId, MemoRepository repo, AddEditContract.View view, boolean shouldLoadData) {
        mId = memoId;
        mRepo = repo;
        mView = view;
        mView.setPresenter(this);
        mShouldLoadData = shouldLoadData;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void saveMemo(String title, String content) {
        Memo memo;
        boolean isNew = isNewMemo();
        if(isNew){
            memo = new Memo(title,content, Calendar.getInstance().getTime());
        }else{
            memo = new Memo(mId,title,content, Calendar.getInstance().getTime());
        }
        mCompositeDisposable.add(
                mRepo.saveMemo(memo)
                        .subscribeOn(Schedulers.io())
                        //this memo is saved, so state is change
                        .doOnSuccess(memo1 -> mId = memo1.getId())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(memo1 -> {
                            mView.toggleEditMode(false);
                            if(isNew) {
                                mView.showMessage("새 메모가 저장되었습니다");
                            }else
                                mView.showMessage("메모가 수정되었습니다");
                        }, throwable -> {
                            mView.showMessage("Error : Save Fail");
                        }));
    }

    @Override
    public void onClickEditMode() {
        //change to edit mode
        mView.toggleEditMode(true);
    }

    @Override
    public void subscribe() {
        if(mShouldLoadData && !isNewMemo())
            mCompositeDisposable.add(
                    mRepo.getMemo(mId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(memo -> {
                                showMemo(memo);
                                mView.toggleEditMode(false);
                            },throwable -> mView.showMessage("Fail to Load Message"))
            );

    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    private void showMemo(Memo memo){
        mView.setTitle(memo.getTitle());
        mView.setContent(memo.getContent());
    }

    private boolean isNewMemo(){
        return mId == -1;
    }
}
