package com.example.semaj.mymemoapp.view.addeditmemo;

import android.text.TextUtils;

import com.example.semaj.mymemoapp.Utils;
import com.example.semaj.mymemoapp.data.Memo;
import com.example.semaj.mymemoapp.data.MemoDataSource;
import com.example.semaj.mymemoapp.data.MemoRepository;

import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AddEditMemoPresenter implements AddEditContract.Presenter {

    private long mId;
    private MemoDataSource mRepo;
    private AddEditContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private boolean changed;

    public AddEditMemoPresenter(long memoId, MemoRepository repo, AddEditContract.View view) {
        mId = memoId;
        mRepo = repo;
        mView = view;
        mView.setPresenter(this);
        changed = false;
        mCompositeDisposable = new CompositeDisposable();
    }

    //메모 저장
    @Override
    public void saveMemo(String title, String content, boolean shouldClose) {
        Memo memo;
        //제목은 없어도 되지만 내용은 있어야됨
        if(TextUtils.isEmpty(content)){
            mView.showMessage("내용을 입력해야 합니다");
            return;
        }
        boolean isNew = isNewMemo();
        if(isNew){
            //key는 생성됨
            memo = new Memo(title,content, Calendar.getInstance().getTime());
        }else{
            memo = new Memo(mId,title,content, Calendar.getInstance().getTime());
        }
        mCompositeDisposable.add(
                mRepo.saveMemo(memo)//db저장
                        .subscribeOn(Schedulers.io())
                        //this memo is saved, so state is change
                        .doOnSuccess(memo1 -> {
                            mId = memo1.getId();
                            changed = false;
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(memo1 -> {
                            if(shouldClose){
                                mView.showMemoListPageAndMessage("메모가 저장되었습니다.");
                            }else {
                                mView.toggleEditMode(false);
                                mView.showDate(Utils.getDateString(memo.getDate()) + "에 마지막으로 수정됨");
                                if (isNew) {
                                    mView.showMessage("새 메모가 저장되었습니다");
                                } else
                                    mView.showMessage("메모가 수정되었습니다");
                            }
                        }, throwable -> {
                            mView.showMessage("Error : Fail to Save Memo");
                        }));
    }

    @Override
    public void onClickEditMode() {
        //change to edit mode
        mView.toggleEditMode(true);
    }

    @Override
    public void subscribe() {
        if(!isNewMemo())
            mCompositeDisposable.add(
                    mRepo.getMemo(mId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(memo -> {
                                showMemo(memo);
                                mView.toggleEditMode(false);
                            },throwable -> {
                                throwable.printStackTrace();
//                                Log.d(getClass().toString(),"");
                                mView.showMessage("Fail to Load Message");
                            })
            );
        else
            mView.toggleEditMode(true);

    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    private void showMemo(Memo memo){
        mView.setTitle(memo.getTitle());
        mView.setContent(memo.getContent());
        mView.showDate(Utils.getDateString(memo.getDate())+"에 마지막으로 수정됨");
    }

    public boolean isNewMemo(){
        return mId == -1;
    }

    //메모 삭제하고 리스트 화면으로
    @Override
    public void deleteMemo() {
        mCompositeDisposable.add(
                mRepo.deleteMemo(mId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> mView.showMemoListPageAndMessage("메모가 삭제되었습니다")));
    }

    @Override
    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    @Override
    public boolean isChanged() {
        return changed;
    }

    @Override
    public void onClickBackButton() {
        if(isChanged()){
            mView.showChangeAlert();
        }else
            mView.showMemoListPage();
    }
}
