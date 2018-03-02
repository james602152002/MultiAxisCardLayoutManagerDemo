package com.james602152002.multiaxiscardlayoutmanagerdemo.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.james602152002.multiaxiscardlayoutmanager.adapter.MultiAxisCardAdapter;
import com.james602152002.multiaxiscardlayoutmanager.viewholder.BaseCardViewHolder;
import com.james602152002.multiaxiscardlayoutmanager.viewholder.HorizontalCardViewHolder;
import com.james602152002.multiaxiscardlayoutmanager.viewholder.VerticalCardViewHolder;
import com.james602152002.multiaxiscardlayoutmanagerdemo.R;
import com.james602152002.multiaxiscardlayoutmanagerdemo.bean.BeanHorizontalCards;

import java.util.List;

/**
 * Created by shiki60215 on 18-3-1.
 */

public class CardAdapter extends MultiAxisCardAdapter {

    public CardAdapter(Context context, SparseArray<Object> items, int vertical_view_id, int horizontal_view_id) {
        super(context, items, vertical_view_id, horizontal_view_id);
    }

    @Override
    public BaseCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_VERTICAL:
                return new VerticalCardViewHolder(inflater.inflate(vertical_view_id, parent, false));
            case TYPE_HORIZONTAL:
                return new H_PhotoCardViewHolder(inflater.inflate(horizontal_view_id, parent, false));
        }
        return new VerticalCardViewHolder(inflater.inflate(vertical_view_id, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseCardViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    class H_PhotoCardViewHolder extends HorizontalCardViewHolder {

        SimpleDraweeView photo;
        AppCompatTextView title;

        public H_PhotoCardViewHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);
            title = itemView.findViewById(R.id.title);
        }

        @Override
        public void initView(int v_card_position, int h_card_position) {
            final List<BeanHorizontalCards> h_items = (List<BeanHorizontalCards>) items.get(v_card_position);
            final BeanHorizontalCards item =  h_items.get(h_card_position);
            photo.setImageURI(item.getUri());
            title.setText(item.getTitle());
        }
    }
}
