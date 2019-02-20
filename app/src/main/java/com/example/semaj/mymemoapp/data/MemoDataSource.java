package com.example.semaj.mymemoapp.data;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * domain 계층에서 data를 handling할 수 있도록 하는 model 계층 인터페이스
 * 콜백 리스너는 전부 RxJava로 대체하여 콜백 수행 - 스레드 전환도 손쉽게 가능
 * memo data를 저장하고 조회하고 삭제 등을 수행
 * source는 local과 remote 둘 다 구현 가능
 */
public interface MemoDataSource {
    //Rxjava로 대체
    interface DataListCallback{
        void onListLoaded(List<Memo> memoList);

        void onError();
    }
    interface DataCallback{
        void onLoaded(Memo memo);

        void onError();
    }

    Flowable<List<Memo>> getMemoList();

    Flowable<Memo> getMemo(@NonNull Long memoId);

    Single<Memo> saveMemo(Memo memo);

    Completable deleteMemo(Long memoId);

    Completable deleteAllMemo();

    Completable deleteMemos(ArrayList<Long> ids);
}
