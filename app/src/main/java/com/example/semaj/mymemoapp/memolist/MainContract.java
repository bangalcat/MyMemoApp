package com.example.semaj.mymemoapp.memolist;

import com.example.semaj.mymemoapp.BasePresenter;
import com.example.semaj.mymemoapp.BaseView;
import com.example.semaj.mymemoapp.data.Memo;

import java.util.List;

public interface MainContract {
    interface View extends BaseView<Presenter> {
        //
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

        void onClickDeleteSelectedMemos();

        void onLongClickMemo(Memo item);

        //select mode에서 메모 하나 선택
        void selectOne(Memo item);
        //선택 모드 취소
        void onClickSelectCancel();

        void filter(String s);

        void selectAll();
    }
}
