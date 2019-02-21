package com.example.semaj.mymemoapp.view.addeditmemo;

import com.example.semaj.mymemoapp.BasePresenter;
import com.example.semaj.mymemoapp.BaseView;

public interface AddEditContract {
    interface View extends BaseView<Presenter>{
        void showMemoListPage();
        void showMemoListPageAndMessage(String message);
        void setTitle(String title);
        void setContent(String content);
        void toggleEditMode(boolean editable);
        void showMessage(String message);
        void showDate(String date);
        void showChangeAlert();
    }
    interface Presenter extends BasePresenter{
        void saveMemo(String title, String content, boolean shouldClose);
        void onClickEditMode();
        boolean isNewMemo();
        void deleteMemo();
        void setChanged(boolean changed);
        boolean isChanged();
        void onClickBackButton();
    }
}
