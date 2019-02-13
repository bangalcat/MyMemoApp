package com.example.semaj.mymemoapp.memolist;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.semaj.mymemoapp.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemoListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemoListFragment extends Fragment implements MainContract.View {

    private MainContract.Presenter mPresenter;

    //components
    private FloatingActionButton mAddBtn;
    private RecyclerView mRcvMemoList;

    public MemoListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MemoListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MemoListFragment newInstance() {
        MemoListFragment fragment = new MemoListFragment();
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
        View root =  inflater.inflate(R.layout.fragment_memo_list, container, false);

        mAddBtn = getActivity().findViewById(R.id.fab_add);
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewMemo();
            }
        });

        return root;
    }


    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
