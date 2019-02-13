package com.example.semaj.mymemoapp;

public interface BaseView<T extends BasePresenter> {
    void setPresenter(T presenter);
}
