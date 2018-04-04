package com.james602152002.multiaxiscardlayoutmanagerdemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

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
        if (show_anim) {
            final short duration = 1000;
            AnimationSet anim = new AnimationSet(true);
            TranslateAnimation translateAnim = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
            translateAnim.setDuration(duration);
            RotateAnimation rotateAnim = new RotateAnimation(10, 0);
            rotateAnim.setDuration(duration);
            anim.addAnimation(translateAnim);
            anim.addAnimation(rotateAnim);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    show_anim = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            holder.itemView.startAnimation(anim);
        }
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
