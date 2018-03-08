package com.james602152002.multiaxiscardlayoutmanagerdemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.james602152002.multiaxiscardlayoutmanagerdemo.R;
import com.james602152002.multiaxiscardlayoutmanagerdemo.bean.BeanCardDetailListItems;

/**
 * Created by shiki60215 on 18-3-7.
 */

public class CardDetailAdapter extends RecyclerView.Adapter<CardDetailAdapter.CardViewHolder> {

    private final LayoutInflater inflater;
    private SparseArray<BeanCardDetailListItems> items;
    private boolean show_anim = true;

    public CardDetailAdapter(Context context, SparseArray<BeanCardDetailListItems> data) {
        inflater = LayoutInflater.from(context);
        this.items = data;
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull CardViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardViewHolder holder = new CardViewHolder(inflater.inflate(R.layout.cell_activity_card_detail, parent, false));
//        Log.i("", "create view holder");
//        if (show_anim) {
//            TranslateAnimation anim =  new TranslateAnimation(
//                    Animation.RELATIVE_TO_PARENT, 0f,
//                    Animation.RELATIVE_TO_PARENT, 0f,
//                    Animation.RELATIVE_TO_PARENT, 1f,
//                    Animation.RELATIVE_TO_PARENT, 0f);
//            anim.setDuration(5000);
//            holder.itemView.startAnimation(anim);
//            show_anim = false;
//        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.initView(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        public CardViewHolder(View itemView) {
            super(itemView);
        }

        public void initView(int position) {
            final BeanCardDetailListItems item = items.get(position);
        }
    }
}
