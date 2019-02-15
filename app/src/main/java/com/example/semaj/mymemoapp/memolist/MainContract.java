package com.example.semaj.mymemoapp.memolist;

import com.example.semaj.mymemoapp.BasePresenter;
import com.example.semaj.mymemoapp.BaseView;
import com.example.semaj.mymemoapp.data.Memo;

import java.util.List;

public interface MainContract {
    interface View extends BaseView<Presenter> {
        void showMemoDetail(long id);

        void showAddMemo();

        void showMemoList(List<Memo> itemList);

        void showMessage(String message);

        void toggleSelectMode(boolean selectMode);
    }

    interface Presenter extends BasePresenter {
        void loadData(boolean forceUpdate);

        void addNewMemo();

        void openMemoDetail(Memo item);

        void onClickDeleteAllMemo();

        void onLongClickMemo(Memo item);
    }
}
