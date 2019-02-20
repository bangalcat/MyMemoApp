package com.example.semaj.mymemoapp.addeditmemo;

import com.example.semaj.mymemoapp.BasePresenter;
import com.example.semaj.mymemoapp.BaseView;

public interface AddEditContract {
    interface View extends BaseView<Presenter>{
        //메모 리스트 화면으로
        void showMemoListPage();
        //메모 리스트 화면으로 가서 해당 메시지 출력
        void showMemoListPageAndMessage(String message);

        void setTitle(String title);

        void setContent(String content);
        // 보기/편집 모드 토글
        void toggleEditMode(boolean editable);

        void showMessage(String message);

        void showDate(String date);

        // 변경 내용 저장할 것인지 확인하는 dialog 출력
        void showChangeAlert();
    }
    interface Presenter extends BasePresenter{

        // shouldClose : 메모를 저장하고 창을 닫아야 하는지 여부
        void saveMemo(String title, String content, boolean shouldClose);
        // 편집 모드 클릭
        void onClickEditMode();

        boolean isNewMemo();

        void deleteMemo();

        // 변경되었는지 여부
        void setChanged(boolean changed);

        boolean isChanged();

        // 뒤로가기 시 저장 여부를 판단해야함
        void onClickBackButton();
    }
}
