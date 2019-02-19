package com.example.semaj.mymemoapp.memolist;

import com.example.semaj.mymemoapp.data.Memo;
import com.example.semaj.mymemoapp.data.MemoRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * MemoListFragment에 대한 Presenter 구현체
 */
public class MemoListPresenter implements MainContract.Presenter {

    private MemoRepository mRepo;
    private MainContract.View mView;

    private boolean isFirstLoad;

    // disposable을 관리
    // clear해주면 알아서 구독 해제됨
    private CompositeDisposable mCompositeDisposable;

//    private long[] selectedIds;
    private TreeSet<Long> selectedIds;

    /**
     *  injection은 따로 하지않고 Activity에서 생성할때 해주는걸로
     * @param repository 데이터 불러올 repo
     * @param view  보여줄 view
     */
    MemoListPresenter(MemoRepository repository, MainContract.View view) {
        mRepo = repository;
        mView = view;
        //first load 시 캐시 삭제후 reload
        isFirstLoad = true;
        //넘겨받은 view에 자신을 presenter로 등록
        mView.setPresenter(this);
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void loadData(boolean forceUpdate) {
        //forceUpdate는 cache는 그대로인데 DB가 갱신되었을 때에 필요
        //현재 MemoRepository에 cache는 DB와 똑같이 갱신되므로 refresh 기능이 없음
        // 따라서 forceUpdate 불필요..
        // 사실은 cache가 변했을 때도 View에 넘겨주기 위해 forceUpdate해주어야한다
        // 결국 현재 forceUpdate는 불필요한 변수 - 무조건 true
        // MemoRepository에 refresh 기능 추가하면 바꾸도록
        if(forceUpdate)
            mCompositeDisposable.add(
                    mRepo.getMemoList()
                            .doOnNext(memoList -> {
                                Collections.sort(memoList, (o1, o2) -> o2.getDate().compareTo(o1.getDate()));
                                selectedIds = new TreeSet<>();
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(memoList -> {
                                        mView.showMemoList(memoList);
                                    },
                                    throwable -> mView.showMessage("메모 목록을 불러오는데 실패하였습니다."))
            );
        isFirstLoad = false;
    }

    @Override
    public void addNewMemo() {
        mView.showAddMemo();
    }

    @Override
    public void openMemoDetail(Memo item) {
        mView.showMemoDetail(item.getId());
    }

    // 선택된 메모 삭제 후 view에 message 출력, list 갱신
    @Override
    public void onClickDeleteSelectedMemos() {
        int cnt = selectedIds.size();
        mCompositeDisposable.add(
                mRepo.deleteMemos(new ArrayList<Long>(selectedIds))
                        .subscribeOn(Schedulers.io())
                        .andThen(mRepo.getMemoList())
                        .doOnNext(memoList -> {
                            Collections.sort(memoList, (o1, o2) -> o2.getDate().compareTo(o1.getDate()));
                            selectedIds = new TreeSet<>();
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(memoList ->{
                            mView.showMemoList(memoList);
                            mView.showMessage("선택된 "+cnt+"개 메모가 삭제되었습니다");
                        },throwable -> {
                            if(throwable instanceof IndexOutOfBoundsException)
                                mView.showMessage("선택된 메시지가 없습니다");
                            else
                                mView.showMessage("메시지 삭제 오류");
                        })
        );
    }

    // longclick하면 select mode로 전환
    // item select는 view에서 또 따로 처리
    @Override
    public void onLongClickMemo(Memo item) {
        mView.toggleSelectMode(true);
    }

    //선택모드에서 아이템 하나 선택
    //todo 구조 개선 : selectedIds를 TreeSet으로
    @Override
    public void selectOne(Memo item) {
        if(selectedIds.contains(item.getId()))
            selectedIds.remove(item.getId());
        else
            selectedIds.add(item.getId());
    }

    //모든 선택 취소 및 선택창 토글
    @Override
    public void onClickSelectCancel() {
        mView.toggleSelectMode(false);
    }

    /**
     *  repo에서 memo list를 일단 전부 불러 온 뒤, 그 중 query를 포함한 Memo만
     *  view에 show한다
     * @param query text is filtered by query
     */
    @Override
    public void filter(String query) {
        mCompositeDisposable.add(
                mRepo.getMemoList()
                        .subscribeOn(Schedulers.computation())
                        .flatMap(Flowable::fromIterable) //Flowable<List<Memo>>를 Flowable<Memo>로 개별 방출하도록
                        .filter(memo -> memo.getTitle().contains(query) || memo.getContent().contains(query)) // 개별 방출한것중 query포함한것만 필터링
                        .toList() // 다시 Flowable<List<>>로
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(memoList -> mView.showMemoList(memoList),
                                throwable -> {
                                    throwable.printStackTrace();
                                })
        );
    }

    @Override
    public void selectAll() {
        //add memo to ids
        mCompositeDisposable.add(
                mRepo.getMemoList()
                .subscribeOn(Schedulers.computation())
                .flatMap(memoList -> Flowable.fromIterable(memoList))
                .subscribe(item -> {
                    //todo
                    selectedIds.add(item.getId());
                },Throwable::printStackTrace)
        );
    }

    @Override
    public void subscribe() {
        loadData(isFirstLoad);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
