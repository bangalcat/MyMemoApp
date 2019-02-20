package com.example.semaj.mymemoapp.addeditmemo;

import com.example.semaj.mymemoapp.BasePresenter;
import com.example.semaj.mymemoapp.BaseView;

public interface AddEditContract {
    interface View extends BaseView<Presenter>{
        void showMemoList();

        void setTitle(String title);

        void setContent(String content);

        void toggleEditMode(boolean editable);

        void showMessage(String message);

        void showDate(String date);

        void showMemoListAndMessage(String message);
    }
    interface Presenter extends BasePresenter{

        void saveMemo(String title, String content);

        void onClickEditMode();

        boolean isNewMemo();

        void deleteMemo();

        void saveMemoAndClose(String title, String content);

        void setChanged(boolean changed);

        boolean isChanged();
    }
}
