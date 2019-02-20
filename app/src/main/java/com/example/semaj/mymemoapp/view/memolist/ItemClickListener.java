package com.example.semaj.mymemoapp.view.memolist;

public interface ItemClickListener<T> {
    void onClick(T item);

    void onLongClick(T item);
}
