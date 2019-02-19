package com.example.semaj.mymemoapp.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import com.example.semaj.mymemoapp.Utils;
import com.example.semaj.mymemoapp.data.Memo;
import com.example.semaj.mymemoapp.data.MemoDataSource;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
// Dao로 구현할까 했지만 그냥 SqlLiteOpenHelper 방식
//Singleton
public class LocalMemoDataSource implements MemoDataSource {

    private static LocalMemoDataSource INSTANCE = null;

    private MemoDbHelper mDbHelper; //db 헬퍼

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
    public Flowable<List<Memo>> getMemoList() {
        String[] projection = {
                MemoDbContract.Entry.ENTRY_ID,
                MemoDbContract.Entry.COLUMN_NAME_TITLE,
                MemoDbContract.Entry.COLUMN_NAME_CONTENT,
                MemoDbContract.Entry.COLUMN_NAME_DATE,
        };

        String sortOrder = MemoDbContract.Entry.COLUMN_NAME_DATE + " DESC";
        Cursor cursor = mDb.query(
                MemoDbContract.Entry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder);
        List<Memo> memoList = new ArrayList<>();
        while(cursor.moveToNext()){
            Long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(MemoDbContract.Entry.ENTRY_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MemoDbContract.Entry.COLUMN_NAME_TITLE));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(MemoDbContract.Entry.COLUMN_NAME_CONTENT));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(MemoDbContract.Entry.COLUMN_NAME_DATE));
            try {
                memoList.add(new Memo(itemId, title, content, Utils.parseDate(date)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return Flowable.fromIterable(memoList)
                .toList().toFlowable();
    }

    @Override
    public Flowable<Memo> getMemo(@NonNull Long memoId) {
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

        if(cursor.moveToNext()) {
            Long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(MemoDbContract.Entry._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MemoDbContract.Entry.COLUMN_NAME_TITLE));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(MemoDbContract.Entry.COLUMN_NAME_CONTENT));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(MemoDbContract.Entry.COLUMN_NAME_DATE));

            Memo memo;
            try {
                memo = new Memo(itemId, title, content, Utils.parseDate(date));
            } catch (ParseException e) {
                e.printStackTrace();
                return Flowable.error(Exception::new);
            }

            cursor.close();
            return Flowable.just(memo);
        }
        return Flowable.error(Exception::new);
    }

    @Override
    public Single<Memo> saveMemo(Memo memo) {
        ContentValues values = new ContentValues();
        values.put(MemoDbContract.Entry.ENTRY_ID, memo.getId());
        values.put(MemoDbContract.Entry.COLUMN_NAME_TITLE, memo.getTitle());
        values.put(MemoDbContract.Entry.COLUMN_NAME_CONTENT, memo.getContent());
        values.put(MemoDbContract.Entry.COLUMN_NAME_DATE, Utils.getDateString(memo.getDate()));
        long rowId = mDb.insertWithOnConflict(MemoDbContract.Entry.TABLE_NAME,null, values, SQLiteDatabase.CONFLICT_IGNORE);
        if(rowId == -1)
            mDb.update(MemoDbContract.Entry.TABLE_NAME, values, MemoDbContract.Entry.ENTRY_ID+"=?", new String[]{String.valueOf(memo.getId())});

        return Single.just(memo);
    }

    @Override
    public void deleteMemo(Long memoId) {
        String selection = MemoDbContract.Entry.ENTRY_ID + " LIKE ?";
        String[] args = {String.valueOf(memoId)};
        int deletedRows = mDb.delete(MemoDbContract.Entry.TABLE_NAME, selection, args);
    }

    @Override
    public Completable deleteAllMemo() {
        int deleteRows = mDb.delete(MemoDbContract.Entry.TABLE_NAME, null, null);
        return Completable.complete();
    }

    @Override
    public Completable deleteMemos(long[] ids, int cnt) {
        if(cnt <= 0) return Completable.error(IndexOutOfBoundsException::new);

        String selection = MemoDbContract.Entry.ENTRY_ID + " IN ("+new String(new char[cnt-1]).replace("\0","?,")+"?)";
        String[] args = new String[cnt];
        for(int i=0;i<cnt;++i)
            args[i] = String.valueOf(ids[i]);
        int deleteRows = mDb.delete(
            MemoDbContract.Entry.TABLE_NAME,
            selection,
            args);
        return Completable.complete();
    }
}
