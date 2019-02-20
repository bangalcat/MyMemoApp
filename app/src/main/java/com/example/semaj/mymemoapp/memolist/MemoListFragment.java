package com.example.semaj.mymemoapp.memolist;


import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
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

    public static final int RESULT_CODE_MESSAGE = 0x1;
    private MainContract.Presenter mPresenter;

    //components
    private FloatingActionButton mAddBtn;
    private RecyclerView mRcvMemoList;
    private TextView mCancelBtn;

    //menu items
    private MenuItem mDeleteMenuItem;
    private MenuItem mSelectMenuItem;
    private MenuItem mSelectAllMenuItem;
    private MenuItem mSearchMenuItem;
    private SearchView mSearchView;

    private MemoListAdapter mAdapter;
    // 선택 모드 아닐때 click listener
    private ItemClickListener<Memo> mClickListener = new ItemClickListener<Memo>() {
        @Override
        public void onClick(Memo item) {
            mPresenter.openMemoDetail(item);
        }

        @Override
        public void onLongClick(Memo item) {
            mPresenter.onLongClickMemo(item);
            mPresenter.selectOne(item);
        }
    };
    // 선택 모드일때 click listener
    private ItemClickListener<Memo> mSelectListener = new ItemClickListener<Memo>() {
        @Override
        public void onClick(Memo item) {
            mPresenter.selectOne(item);
        }

        @Override
        public void onLongClick(Memo item) {
            //do nothing
        }
    };

    public MemoListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MemoListFragment.
     */
    public static MemoListFragment newInstance() {
        return new MemoListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new MemoListAdapter(mClickListener, mSelectListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_memo_list, container, false);

        mRcvMemoList = root.findViewById(R.id.rcv_memo_list);
//        mRcvMemoList.setLayoutManager(new LinearLayoutManager(getContext()));
        StaggeredGridLayoutManager _sglm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        _sglm.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRcvMemoList.setLayoutManager(_sglm);
        mRcvMemoList.setAdapter(mAdapter);

        mAddBtn = getActivity().findViewById(R.id.fab_add);
        mAddBtn.setOnClickListener(v -> mPresenter.addNewMemo());
        mCancelBtn = root.findViewById(R.id.toolbar_left);
        mCancelBtn.setVisibility(View.GONE);
        mCancelBtn.setText("Cancel");
        mCancelBtn.setOnClickListener(view -> {
            mPresenter.onClickSelectCancel();
        });
        setHasOptionsMenu(true);

        Toolbar toolbar =  root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        TextView rightToolbar = root.findViewById(R.id.toolbar_right);
        rightToolbar.setText("");
        TextView middle = root.findViewById(R.id.toolbar_middle);
        middle.setText("");

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == AddEditMemoActivity.REQUEST_ADD_MEMO ||
                requestCode == AddEditMemoActivity.REQUEST_EDIT_MEMO){
            if(resultCode == Activity.RESULT_OK){
                mPresenter.loadData(true);
            }else if(resultCode == Activity.RESULT_CANCELED){
                mPresenter.loadData(true);
            }else if(resultCode == RESULT_CODE_MESSAGE){
                String message = data.getStringExtra("MESSAGE");
                showMessage(message);
                mPresenter.loadData(true);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        // menu item - select mode에서 보여주는 메뉴가 다름
        // 멤버로 가지고 있다가 visibility set
        mSelectMenuItem = menu.getItem(0);
        mSearchMenuItem = menu.getItem(1);
        mDeleteMenuItem = menu.getItem(2);
        mSelectAllMenuItem = menu.getItem(3);

        mDeleteMenuItem.setVisible(false);
        mSelectAllMenuItem.setVisible(false);

        //searchview
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) mSearchMenuItem.getActionView();
        if(mSearchView != null){
            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            mSearchView.setQueryHint("search text");
            //검색창 텍스트 입력을 자동 인식 - 매번 쿼리 전달
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    mPresenter.filter(s);
                    return true;
                }
            });
        }


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_delete: //선택 메모 삭제
                mPresenter.onClickDeleteSelectedMemos();
                break;
            case R.id.menu_select: // 선택 모드
                mSearchView.setQuery("",true);
                mSearchView.clearFocus();
                toggleSelectMode(true); //딱히 presenter 호출 안해도 될듯?
                break;
            case R.id.menu_search: // 검색 버튼 - 검색은 searchView에 로직
                toggleSelectMode(false);  //검색 시 다중선택모드 취소
                break;
            case R.id.menu_select_all:
                // Need to change process
                mAdapter.setAllItemSelect(true);
                mAdapter.notifyItemRangeChanged(0,mAdapter.getItemCount());
                mPresenter.selectAll();
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
        //add memo와 같은 activity 호출하지만, intent extra에 memo의 id를 넣음
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

    // 다중 선택 모드 toggle
    // 메뉴 item 보여지는게 다름
    // Adapter의 select mode를 바꾸고 view를 다시그리도록 notifyDatasetChanged 호출
    @Override
    public void toggleSelectMode(boolean selectMode) {
        mAdapter.setSelectable(selectMode);
        mAdapter.notifyItemRangeChanged(0,mAdapter.getItemCount());
        //다중 선택 모드에서 Cancel 버튼이 툴바 왼쪽에 나올 수 있도록
        mCancelBtn.setVisibility(selectMode?View.VISIBLE:View.GONE);
        if(selectMode) {
            mSelectAllMenuItem.setVisible(true);
            mDeleteMenuItem.setVisible(true);
            mSelectMenuItem.setVisible(false);
            mSearchMenuItem.setVisible(false);
            mAddBtn.hide();
        }
        else {
            mSelectAllMenuItem.setVisible(false);
            mDeleteMenuItem.setVisible(false);
            mSelectMenuItem.setVisible(true);
            mSearchMenuItem.setVisible(true);
            mAddBtn.show();
        }
    }
}
