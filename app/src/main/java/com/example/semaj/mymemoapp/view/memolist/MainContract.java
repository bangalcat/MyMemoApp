package com.example.semaj.mymemoapp.view.memolist;

import com.example.semaj.mymemoapp.BasePresenter;
import com.example.semaj.mymemoapp.BaseView;
import com.example.semaj.mymemoapp.data.Memo;

import java.util.List;

public interface MainContract {
    interface View extends BaseView<Presenter> {
        String EXTRA_MESSAGE = "MESSAGE";
        //
        void showMemoDetailPage(long id);

        void showAddMemoPage();

        void showMemoList(List<Memo> itemList);

        void showMessage(String message);

        void toggleSelectMode(boolean selectMode);

        void scrollUp();
    }

    interface Presenter extends BasePresenter {
        void loadData(boolean forceUpdate);

        void onClickAddButton();

        void onClickMemo(Memo item);

        void onClickDeleteSelectedMemos();

        void onLongClickMemo(Memo item);

        //select mode에서 메모 하나 선택 혹은 제거
        void selectOne(Memo item);

        //선택 모드 취소
        void onClickSelectCancel();

        //검색 쿼리 전달
        void filter(String s);

        void selectAll(CharSequence query);
    }
}
