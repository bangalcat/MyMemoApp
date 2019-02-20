package com.example.semaj.mymemoapp.memolist;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.semaj.mymemoapp.R;
import com.example.semaj.mymemoapp.Utils;
import com.example.semaj.mymemoapp.data.Memo;

import java.util.List;


/**
 * ListAdapter : RecyclerView.Adapter의 SubClass. 더 진화된 형태?
 * 현재 문제점 : view를 선택한 상태를 model에 반영하기는 쉬운데, 전체 선택처럼
 *      model의 상태를 다시 view에 반영하기가 어려움
 *      Memo 객체가 select 상태를 가지고 있어야 할까?
 */
public class MemoListAdapter extends ListAdapter<Memo, MemoListAdapter.MemoViewHolder>{

    private ItemClickListener<Memo> mClickListener;
    private ItemClickListener<Memo> mSelectListener;
    private boolean selectable = false;
    private boolean[] selectItems;

    MemoListAdapter(ItemClickListener<Memo> clickListener, ItemClickListener<Memo> selectListener) {
        this(new DiffUtil.ItemCallback<Memo>() {
            @Override
            public boolean areItemsTheSame(@NonNull Memo t, @NonNull Memo t1) {
                return t.getId().equals(t1.getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Memo t, @NonNull Memo t1) {
                return t == t1;
            }
        });
        mClickListener = clickListener;
        mSelectListener = selectListener;
    }

//    void submitNewList(@Nullable List<Memo> list) {
//        List<SelectableMemo> convList = new ArrayList<>();
//        for(Memo m : list) convList.add(new SelectableMemo(m));
//        super.submitList(convList);
//    }

    private MemoListAdapter(@NonNull DiffUtil.ItemCallback<Memo> diffCallback) {
        super(diffCallback);
    }

    @Override
    public void submitList(@Nullable List<Memo> list) {
        selectItems = new boolean[list.size()];
        super.submitList(list);
    }

    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_memo_card, viewGroup, false);
        return new MemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder memoViewHolder, int i) {
        memoViewHolder.bind(getItem(i),selectable? mSelectListener :mClickListener, selectable, i);
    }

    void setSelectable(boolean selectable) {
        this.selectable = selectable;
        if(!selectable)
            for(int i = 0; i< selectItems.length; ++i)
                selectItems[i] = false;
    }
    public void setAllItemSelect(boolean select){
        for(int i = 0; i< selectItems.length; ++i)
            selectItems[i] = select;
    }

    //ViewHolder Class
    class MemoViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTitle;
        private TextView tvContent;
        private TextView tvDate;
        private CheckBox ckBox;
        private View root;

        public MemoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.title);
            tvContent = itemView.findViewById(R.id.content);
            tvDate = itemView.findViewById(R.id.date);
            ckBox = itemView.findViewById(R.id.checkBox);
            root = itemView;
        }
        public void bind(Memo memo, ItemClickListener<Memo> clickListener, boolean selectable, int pos){
            tvTitle.setText(memo.getTitle());
            tvContent.setText(memo.getContent());
            tvDate.setText(Utils.getDateString(memo.getDate()).substring(5));
            if(selectable) {
                ckBox.setVisibility(View.VISIBLE);
            }else {
                ckBox.setVisibility(View.GONE);
            }
            ckBox.setChecked(selectItems[pos]);
            root.setOnClickListener(v -> {
                clickListener.onClick(memo);
                if(selectable){
                    ckBox.setChecked(!ckBox.isChecked());
                    selectItems[pos] = !selectItems[pos];
                }
            });
            root.setOnLongClickListener(v -> {
                ckBox.setChecked(true);
                selectItems[pos] = true;
                clickListener.onLongClick(memo);
                return true;
            });
        }
    }
}
