package com.example.semaj.mymemoapp.addeditmemo;

import com.example.semaj.mymemoapp.Utils;
import com.example.semaj.mymemoapp.data.Memo;
import com.example.semaj.mymemoapp.data.MemoDataSource;
import com.example.semaj.mymemoapp.data.MemoRepository;

public class AddEditMemoPresenter implements AddEditContract.Presenter {

    private long mId;
    private MemoRepository mRepo;
    private AddEditContract.View mView;
    private boolean mShouldLoadData;

    public AddEditMemoPresenter(long memoId, MemoRepository repo, AddEditContract.View view, boolean shouldLoadData) {
        mId = memoId;
        mRepo = repo;
        mView = view;
        mView.setPresenter(this);
        mShouldLoadData = shouldLoadData;
    }

    @Override
    public void saveMemo(String title, String content) {
        if(isNewMemo()){
            Memo memo = new Memo(title,content,Utils.getDateString());
            mRepo.saveMemo(memo);
            //need callback
            mView.showMemoList();
        }else{
            Memo memo = new Memo(mId,title,content,Utils.getDateString());
            mRepo.saveMemo(memo);
            mView.showMemoList();
        }

    }

    @Override
    public void onClickEditMode() {
        //change to edit mode
    }

    @Override
    public void subscribe() {
        if(mShouldLoadData && !isNewMemo())
            mRepo.getMemo(mId, new MemoDataSource.DataCallback() {
                @Override
                public void onLoaded(Memo memo) {
                    showMemo(memo);
                }

                @Override
                public void onError() {
                }
            });
    }

    @Override
    public void unsubscribe() {

    }

    private void showMemo(Memo memo){
        mView.setTitle(memo.getTitle());
        mView.setContent(memo.getContent());
    }

    private boolean isNewMemo(){
        return mId == -1;
    }
}
