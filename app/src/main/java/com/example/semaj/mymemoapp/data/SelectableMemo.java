package com.example.semaj.mymemoapp.data;

import android.support.annotation.Nullable;

import java.util.Date;

public class SelectableMemo extends Memo {
    private boolean isSelect = false;

    public SelectableMemo(Memo m){
        super(m.getId(),m.getTitle(),m.getContent(),m.getDate());
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
