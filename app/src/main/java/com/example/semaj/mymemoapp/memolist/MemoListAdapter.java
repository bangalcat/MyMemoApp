package com.example.semaj.mymemoapp.memolist;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.semaj.mymemoapp.R;
import com.example.semaj.mymemoapp.Utils;
import com.example.semaj.mymemoapp.data.Memo;


public class MemoListAdapter extends ListAdapter<Memo, MemoListAdapter.MemoViewHolder> {

    private ItemClickListener<Memo> mListener;

    public MemoListAdapter(ItemClickListener<Memo> clickListener) {
        this(new DiffUtil.ItemCallback<Memo>() {
            @Override
            public boolean areItemsTheSame(@NonNull Memo t, @NonNull Memo t1) {
                return t.getId() == t1.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Memo t, @NonNull Memo t1) {
                return t == t1;
            }
        });
        mListener = clickListener;
    }

    private MemoListAdapter(@NonNull DiffUtil.ItemCallback<Memo> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_memo, viewGroup, false);
        return new MemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder memoViewHolder, int i) {
        memoViewHolder.bind(getItem(i),mListener);
    }

    //ViewHolder Class
    class MemoViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTitle;
        private TextView tvContent;
        private TextView tvDate;
        private View root;

        public MemoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.title);
            tvContent = itemView.findViewById(R.id.content);
            tvDate = itemView.findViewById(R.id.date);
            root = itemView;
        }
        public void bind(Memo memo, ItemClickListener<Memo> clickListener){
            tvTitle.setText(memo.getTitle());
            tvContent.setText(memo.getContent());
            tvDate.setText(Utils.getDateString(memo.getDate()));
            root.setOnClickListener(v -> clickListener.onClick(memo));
        }
    }
}
