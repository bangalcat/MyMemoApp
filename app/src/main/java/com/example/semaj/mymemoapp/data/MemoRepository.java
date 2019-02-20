package com.example.semaj.mymemoapp.data;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * MemoDataSource를 구현한 종합 Repository
 * data를 cache하여 매번 local/remote에서 불러오지 않아도 되도록
 * 현재는 간단하게 local에서만 불러오도록 되어있어 cache dirty 처리가 단순하게 처리
 * Singleton 객체로 유지
 */
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
    public Flowable<List<Memo>> getMemoList() {
        if(mCachedMemoList != null){
            return Flowable.fromIterable(mCachedMemoList.values()).toList().toFlowable();
        }else
            mCachedMemoList = new LinkedHashMap<>();

        return getAndCacheLocalMemoList();
    }

    private Flowable<List<Memo>> getAndCacheLocalMemoList(){
        return mLocalDataSource.getMemoList()
                .flatMap(Flowable::fromIterable)
                .doOnNext(memo -> mCachedMemoList.put(memo.getId(),memo))
                .toList()
                .toFlowable();
    }

    @Override
    public Flowable<Memo> getMemo(@NonNull Long memoId) {
        final Memo cachedMemo = findMemoById(memoId);
        if(cachedMemo != null){
            return Flowable.just(cachedMemo);
        }
        return mLocalDataSource.getMemo(memoId)
                .doOnNext(memo -> cachedMemoListNotNull().put(memo.getId(), memo))
                .firstElement().toFlowable();
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
    public Single<Memo> saveMemo(Memo memo) {
        return mLocalDataSource.saveMemo(memo)
                .doOnSuccess(memo1 ->
                        cachedMemoListNotNull().put(memo.getId(), memo)
                );
    }

    private Map<Long, Memo> cachedMemoListNotNull(){
        if(mCachedMemoList == null)
            mCachedMemoList = new LinkedHashMap<>();
        return mCachedMemoList;
    }

    @Override
    public Completable deleteMemo(Long memoId) {
        return mLocalDataSource.deleteMemo(memoId)
                .doOnComplete(() ->
                        cachedMemoListNotNull().remove(memoId)
                );
    }

    @Override
    public Completable deleteAllMemo() {
        return mLocalDataSource.deleteAllMemo()
                .doOnComplete(() -> mCachedMemoList.clear());
    }

    @Override
    public Completable deleteMemos(ArrayList<Long> ids) {
        return mLocalDataSource.deleteMemos(ids)
                .doOnComplete(() -> {
                    for(Long id : ids)
                        mCachedMemoList.remove(id);
                });
    }
}
