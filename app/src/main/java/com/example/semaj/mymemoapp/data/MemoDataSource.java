package com.example.semaj.mymemoapp.data;

import android.support.annotation.NonNull;

import java.util.List;

public interface MemoDataSource {
    interface DataListCallback{
        void onListLoaded(List<Memo> memoList);

        void onError();
    }
    interface DataCallback{
        void onLoaded(Memo memo);

        void onError();
    }
    void getMemoList(@NonNull DataListCallback callback);

    void getMemo(@NonNull Long memoId, @NonNull DataCallback callback);

    void saveMemo(Memo memo);

    void deleteMemo(Long memoId);
}
