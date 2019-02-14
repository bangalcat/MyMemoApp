package com.example.semaj.mymemoapp.addeditmemo;

import com.example.semaj.mymemoapp.BasePresenter;
import com.example.semaj.mymemoapp.BaseView;

public interface AddEditContract {
    interface View extends BaseView<Presenter>{
        void showMemoList();

        void setTitle(String title);

        void setContent(String content);

        void toggleEditMode(boolean editable);
    }
    interface Presenter extends BasePresenter{

        void saveMemo(String title, String content);

        void onClickEditMode();

    }
}
