package com.example.semaj.mymemoapp;

public interface MainContract {
    interface View extends BaseView<Presenter>{
    }

    interface Presenter extends BasePresenter{
        void addNewMemo();

        void openMemoDetail();
    }
}
