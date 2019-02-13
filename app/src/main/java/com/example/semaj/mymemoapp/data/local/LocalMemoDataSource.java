package com.example.semaj.mymemoapp.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import com.example.semaj.mymemoapp.data.Memo;
import com.example.semaj.mymemoapp.data.MemoDataSource;

import java.util.ArrayList;
import java.util.List;

//Singleton
public class LocalMemoDataSource implements MemoDataSource {

    private static LocalMemoDataSource INSTANCE = null;

    private MemoDbHelper mDbHelper;

    private SQLiteDatabase mDb;

    private LocalMemoDataSource(Context context) {
        this.mDbHelper = new MemoDbHelper(context.getApplicationContext());
        mDb = mDbHelper.getWritableDatabase();
    }

    public static LocalMemoDataSource getInstance(Context context){
        if (INSTANCE == null)
            INSTANCE = new LocalMemoDataSource(context);

        return INSTANCE;
    }

    @Override
    public void getMemoList(@NonNull DataListCallback callback) {
        String[] projection = {
                BaseColumns._ID,
                MemoDbContract.Entry.COLUMN_NAME_TITLE,
                MemoDbContract.Entry.COLUMN_NAME_CONTENT,
                MemoDbContract.Entry.COLUMN_NAME_DATE,
        };
        String selection = "*";

        String sortOrder = MemoDbContract.Entry.COLUMN_NAME_DATE + " DESC";
        Cursor cursor = mDb.query(
                MemoDbContract.Entry.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                sortOrder);
        List<Memo> memoList = new ArrayList<>();
        while(cursor.moveToNext()){
            Long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(MemoDbContract.Entry._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MemoDbContract.Entry.COLUMN_NAME_TITLE));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(MemoDbContract.Entry.COLUMN_NAME_CONTENT));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(MemoDbContract.Entry.COLUMN_NAME_DATE));
            memoList.add(new Memo(itemId, title, content, date));
        }
        cursor.close();
        callback.onListLoaded(memoList);
    }

    @Override
    public void getMemo(@NonNull Long memoId, @NonNull DataCallback callback) {
        String[] projection = {
                BaseColumns._ID,
                MemoDbContract.Entry.COLUMN_NAME_TITLE,
                MemoDbContract.Entry.COLUMN_NAME_CONTENT,
                MemoDbContract.Entry.COLUMN_NAME_DATE,
        };
        String selection = MemoDbContract.Entry._ID + " = ?";
        String []selectionArgs = {"" +memoId};

        Cursor cursor = mDb.query(
                MemoDbContract.Entry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        while(cursor.moveToNext()) {
            Long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(MemoDbContract.Entry._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MemoDbContract.Entry.COLUMN_NAME_TITLE));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(MemoDbContract.Entry.COLUMN_NAME_CONTENT));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(MemoDbContract.Entry.COLUMN_NAME_DATE));

            Memo memo = new Memo(itemId, title, content, date);

            cursor.close();
            callback.onLoaded(memo);
            return;
        }
        callback.onError();
    }

    @Override
    public void saveMemo(Memo memo) {
        ContentValues values = new ContentValues();
        values.put(MemoDbContract.Entry.COLUMN_NAME_TITLE, memo.getTitle());
        values.put(MemoDbContract.Entry.COLUMN_NAME_CONTENT, memo.getContent());
        values.put(MemoDbContract.Entry.COLUMN_NAME_DATE, memo.getDate());
        long newId = mDb.insert(MemoDbContract.Entry.TABLE_NAME, null, values);
        memo.setId(newId);
    }

    @Override
    public void deleteMemo(Long memoId) {
        String selection = MemoDbContract.Entry._ID + " LIKE ?";
        String[] args = {String.valueOf(memoId)};
        int deletedRows = mDb.delete(MemoDbContract.Entry.TABLE_NAME, selection, args);
    }
}
