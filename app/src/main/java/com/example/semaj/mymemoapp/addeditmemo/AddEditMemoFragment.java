package com.example.semaj.mymemoapp.addeditmemo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.semaj.mymemoapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEditMemoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEditMemoFragment extends Fragment implements AddEditContract.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_MEMO_ID = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private TextView mTitle;
    private TextView mContent;


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
    public void showMemoList() {

    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void setContent(String content) {

    }

    @Override
    public void setPresenter(AddEditContract.Presenter presenter) {

    }
}
