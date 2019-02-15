package com.example.semaj.mymemoapp.memolist;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.semaj.mymemoapp.R;
import com.example.semaj.mymemoapp.addeditmemo.AddEditMemoActivity;
import com.example.semaj.mymemoapp.addeditmemo.AddEditMemoFragment;
import com.example.semaj.mymemoapp.data.Memo;

import java.util.List;


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

    private ItemClickListener<Memo> mClickListener = new ItemClickListener<Memo>() {
        @Override
        public void onClick(Memo item) {
            mPresenter.openMemoDetail(item);
        }

        @Override
        public void onLongClick(Memo item) {
            mPresenter.onLongClickMemo(item);
        }
    };

    private MemoListAdapter mAdapter;

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
        return new MemoListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new MemoListAdapter(mClickListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_memo_list, container, false);

        mRcvMemoList = root.findViewById(R.id.rcv_memo_list);
        mRcvMemoList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRcvMemoList.setAdapter(mAdapter);

        mAddBtn = getActivity().findViewById(R.id.fab_add);
        mAddBtn.setOnClickListener(v -> mPresenter.addNewMemo());
        setHasOptionsMenu(true);

        Toolbar toolbar =  root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        TextView leftToolbar = root.findViewById(R.id.toolbar_left);
        leftToolbar.setText("");
        TextView rightToolbar = root.findViewById(R.id.toolbar_right);
        rightToolbar.setText("");
        TextView middle = root.findViewById(R.id.toolbar_middle);
        middle.setText("메모장");

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AddEditMemoActivity.REQUEST_ADD_MEMO ||
                requestCode == AddEditMemoActivity.REQUEST_EDIT_MEMO){
            if(resultCode == Activity.RESULT_OK){
                mPresenter.loadData(true);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_delete_all:
                mPresenter.onClickDeleteAllMemo();
                break;
            case R.id.menu_select:
                break;
        }
        return true;
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
    public void setPresenter(MainContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showMemoDetail(long id) {
        //create new activity
        Intent intent = new Intent(getContext(), AddEditMemoActivity.class);
        intent.putExtra(AddEditMemoFragment.ARG_MEMO_ID,id);
        startActivityForResult(intent, AddEditMemoActivity.REQUEST_EDIT_MEMO);
    }

    @Override
    public void showAddMemo() {
        //create new activity
        Intent intent = new Intent(getContext(), AddEditMemoActivity.class);
        startActivityForResult(intent, AddEditMemoActivity.REQUEST_ADD_MEMO);
    }

    @Override
    public void showMemoList(List<Memo> itemList) {
        mAdapter.submitList(itemList);
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void toggleSelectMode(boolean selectMode) {
        mAdapter.setSelectable(selectMode);
        //todo select mode back button add
        //if(selectMode)
        //
    }
}
