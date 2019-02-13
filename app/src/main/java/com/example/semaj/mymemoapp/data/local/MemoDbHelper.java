package com.example.semaj.mymemoapp.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MemoDbHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DbName = "Memo.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MemoDbContract.Entry.TABLE_NAME + " (" +
                    MemoDbContract.Entry._ID + " INTEGER PRIMARY KEY," +
                    MemoDbContract.Entry.COLUMN_NAME_TITLE + " TEXT," +
                    MemoDbContract.Entry.COLUMN_NAME_CONTENT + " TEXT," +
                    MemoDbContract.Entry.COLUMN_NAME_DATE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MemoDbContract.Entry.TABLE_NAME;
    public MemoDbHelper(Context context) {
        super(context, DbName, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
