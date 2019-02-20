package com.example.semaj.mymemoapp.addeditmemo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.semaj.mymemoapp.R;
import com.example.semaj.mymemoapp.Utils;
import com.example.semaj.mymemoapp.memolist.MemoListFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEditMemoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEditMemoFragment extends Fragment implements AddEditContract.View {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_MEMO_ID = "memo_id";

    //components
    private EditText mTitle; //제목입력란
    private EditText mContent; //내용입력란
    private TextView mSaveBtn; // 저장 버튼 - 현재 우측상단
    private FloatingActionButton mEditBtn; // 편집버튼 - float button

    private AddEditContract.Presenter mPresenter;
    private TextView mSavedDate;

    public AddEditMemoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddEditMemoFragment.
     */
    public static AddEditMemoFragment newInstance(long memoId) {
        AddEditMemoFragment fragment = new AddEditMemoFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_MEMO_ID, memoId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_add_edit_memo, container, false);
        mTitle = root.findViewById(R.id.etTitle);
        mContent = root.findViewById(R.id.etContent);
        mSaveBtn = getActivity().findViewById(R.id.toolbar_right);
        setHasOptionsMenu(true);
        mEditBtn = getActivity().findViewById(R.id.fab_edit);
        mEditBtn.setOnClickListener(v -> mPresenter.onClickEditMode());
        mSavedDate = root.findViewById(R.id.tv_saved_date);

        mSaveBtn.setOnClickListener(v -> {
            mPresenter.saveMemo(mTitle.getText().toString(), mContent.getText().toString());
            Utils.hideKeyboard(getActivity());
        });
        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPresenter.setChanged(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPresenter.setChanged(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_menu_delete: //현재 메모 삭제
                mPresenter.deleteMemo();
                break;
            case android.R.id.home: // 뒤로가기
                mPresenter.onClickBackButton();
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(!mPresenter.isNewMemo())
            inflater.inflate(R.menu.edit_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //메모 리스트 화면으로 돌아가기
    @Override
    public void showMemoList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public void setContent(String content) {
        mContent.setText(content);
    }

    //toggle editable
    @Override
    public void toggleEditMode(boolean editable) {
        mTitle.setFocusable(editable);
        mTitle.setFocusableInTouchMode(editable);
        mContent.setFocusable(editable);
        mContent.setFocusableInTouchMode(editable);
        if(editable) {
            mSaveBtn.setText("SAVE");
            mSaveBtn.setVisibility(View.VISIBLE);
            mTitle.setOnClickListener(null);
            mContent.setOnClickListener(null);
            mEditBtn.hide();
        } else {
            getActivity().invalidateOptionsMenu();
            mSaveBtn.setVisibility(View.GONE);
            // 입력란 클릭해도 편집모드로 들어가도록
            mTitle.setOnClickListener(v -> mPresenter.onClickEditMode());
            mContent.setOnClickListener(v -> mPresenter.onClickEditMode());
            mEditBtn.show();
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
//        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showDate(String date) {
        mSavedDate.setText(date);
    }

    @Override
    public void showMemoListAndMessage(String message) {
        Intent data = new Intent();
        data.putExtra("MESSAGE", message);
        getActivity().setResult(MemoListFragment.RESULT_CODE_MESSAGE,data);
        getActivity().finish();
    }

    @Override
    public void showChangeAlert() {
        Utils.getSaveAlertDialog(getContext(),"","저장하시겠습니까?",(dialog, which) -> {
            mPresenter.saveMemoAndClose(mTitle.getText().toString(), mContent.getText().toString());
        },(dialog, which) -> {
            showMemoList();
        }).show();
    }

    @Override
    public void setPresenter(AddEditContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
