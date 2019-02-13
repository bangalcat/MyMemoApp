package com.example.semaj.mymemoapp.memolist;

import com.example.semaj.mymemoapp.BasePresenter;
import com.example.semaj.mymemoapp.BaseView;

public interface MainContract {
    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {
        void addNewMemo();

        void openMemoDetail();
    }
}
