package com.example.semaj.mymemoapp.addeditmemo;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.semaj.mymemoapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEditMemoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEditMemoFragment extends Fragment implements AddEditContract.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_MEMO_ID = "memo_id";

    private EditText mTitle;
    private EditText mContent;
    private AddEditContract.Presenter mPresenter;

    public AddEditMemoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddEditMemoFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        //mPresenter.subscribe();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_add_edit_memo, container, false);
        mTitle = root.findViewById(R.id.etTitle);
        mContent = root.findViewById(R.id.etContent);
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.addedit_memo_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

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
        mTitle.setFocusableInTouchMode(true);
        mContent.setFocusable(editable);
        mContent.setFocusableInTouchMode(true);
    }

    @Override
    public void setPresenter(AddEditContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
