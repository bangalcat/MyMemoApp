package com.example.semaj.mymemoapp.data;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Singleton
public class MemoRepository implements MemoDataSource {

    private Map<Long, Memo> mCachedMemoList;

    private MemoDataSource mLocalDataSource;

    private static MemoRepository INSTANCE = null;

    private MemoRepository(MemoDataSource localDataSource){
        mLocalDataSource = localDataSource;
    }

    public static MemoRepository getInstance(MemoDataSource localDataSource){
        if(INSTANCE == null){
            INSTANCE = new MemoRepository(localDataSource);
        }

        return INSTANCE;
    }

    @Override
    public void getMemoList(@NonNull final DataListCallback callback) {
        if(mCachedMemoList != null){
            callback.onListLoaded(new ArrayList<>(mCachedMemoList.values()));
            return;
        }

        mLocalDataSource.getMemoList(new DataListCallback() {
            @Override
            public void onListLoaded(List<Memo> memoList) {
                refreshMemoList(memoList);
                callback.onListLoaded(memoList);
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    @Override
    public void getMemo(@NonNull Long memoId, @NonNull final DataCallback callback) {
        final Memo cachedMemo = findMemoById(memoId);
        if(cachedMemo != null){
            callback.onLoaded(cachedMemo);
            return;
        }
        mLocalDataSource.getMemo(memoId, new DataCallback() {
            @Override
            public void onLoaded(Memo memo) {
                cachedMemoListNotNull().put(memo.getId(), memo);

                callback.onLoaded(memo);
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    private Memo findMemoById(@NonNull Long id){
        return cachedMemoListNotNull().get(id);
    }

    private void refreshMemoList(List<Memo> memoList){
        cachedMemoListNotNull().clear();

        for(Memo memo : memoList)
            mCachedMemoList.put(memo.getId(), memo);
    }

    @Override
    public void saveMemo(Memo memo) {
        mLocalDataSource.saveMemo(memo);
        cachedMemoListNotNull().put(memo.getId(), memo);
    }

    private Map<Long, Memo> cachedMemoListNotNull(){
        if(mCachedMemoList == null)
            mCachedMemoList = new HashMap<>();
        return mCachedMemoList;
    }

    @Override
    public void deleteMemo(Long memoId) {
        mLocalDataSource.deleteMemo(memoId);
        cachedMemoListNotNull().remove(memoId);
    }
}
