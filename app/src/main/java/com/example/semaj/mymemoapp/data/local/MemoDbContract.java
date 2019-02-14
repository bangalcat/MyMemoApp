package com.example.semaj.mymemoapp.data.local;

import android.provider.BaseColumns;

public final class MemoDbContract {
    private MemoDbContract(){}
    public static class Entry implements BaseColumns{
        public static final String TABLE_NAME = "entry";
        public static final String ENTRY_ID = "entry_id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_DATE = "date";
    }
}
