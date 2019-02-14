package com.example.semaj.mymemoapp.memolist;

import com.example.semaj.mymemoapp.data.Memo;

public interface ItemClickListener<T> {
    void onClick(T item);
}
