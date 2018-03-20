package com.james602152002.multiaxiscardlayoutmanagerdemo.adapter;

import android.app.ActivityOptions;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.DraweeTransition;
import com.facebook.drawee.view.SimpleDraweeView;
import com.james602152002.multiaxiscardlayoutmanager.adapter.MultiAxisCardAdapter;
import com.james602152002.multiaxiscardlayoutmanager.viewholder.BaseCardViewHolder;
import com.james602152002.multiaxiscardlayoutmanager.viewholder.HorizontalCardViewHolder;
import com.james602152002.multiaxiscardlayoutmanager.viewholder.VerticalCardViewHolder;
import com.james602152002.multiaxiscardlayoutmanagerdemo.R;
import com.james602152002.multiaxiscardlayoutmanagerdemo.bean.BeanHorizontalCards;
import com.james602152002.multiaxiscardlayoutmanagerdemo.ui.ActivityCardDetail;

import java.util.List;

/**
 * Created by shiki60215 on 18-3-1.
 */

public class HomepageCardAdapter extends MultiAxisCardAdapter implements View.OnClickListener {

    public HomepageCardAdapter(Context context, SparseArray<Object> items, int vertical_view_id, int horizontal_view_id) {
        super(context, items, vertical_view_id, horizontal_view_id);
    }

    @Override
    public BaseCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_VERTICAL:
                return new V_TitleViewHolder(inflater.inflate(vertical_view_id, parent, false));
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

        private SimpleDraweeView photo;
        private AppCompatTextView title;

        public H_PhotoCardViewHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);
            title = itemView.findViewById(R.id.title);
        }

        @Override
        public void initView(int v_card_position, int h_card_position) {
            final List<BeanHorizontalCards> h_items = (List<BeanHorizontalCards>) items.get(v_card_position);
            final BeanHorizontalCards item = h_items.get(h_card_position);
            photo.setImageURI(item.getUri());
            title.setText(item.getTitle());
            itemView.setTag(item);
            itemView.setOnClickListener(HomepageCardAdapter.this);
        }
    }

    class V_TitleViewHolder extends VerticalCardViewHolder {

        private AppCompatTextView title;

        public V_TitleViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }

        @Override
        public void initView(int v_card_position, int h_card_position) {
            title.setText((String) items.get(v_card_position));
        }
    }

    @Override
    public void onClick(View view) {
        Object item = view.getTag();
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        Intent destIntent;
        if (item instanceof BeanHorizontalCards) {
            destIntent = new Intent(activity, ActivityCardDetail.class);
            destIntent.putExtra("uri", ((BeanHorizontalCards) item).getUri());
            destIntent.putExtra("title", ((BeanHorizontalCards) item).getTitle());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.setSharedElementEnterTransition(DraweeTransition.createTransitionSet(
                        ScalingUtils.ScaleType.CENTER_CROP, ScalingUtils.ScaleType.CENTER_CROP));
                window.setSharedElementExitTransition(DraweeTransition.createTransitionSet(
                        ScalingUtils.ScaleType.CENTER_CROP, ScalingUtils.ScaleType.CENTER_CROP));
//                window.setSharedElementEnterTransition(DraweeTransition.createTransitionSet(
//                        ScalingUtils.ScaleType.CENTER_CROP, ScalingUtils.ScaleType.CENTER_CROP));
                final View photo = view.findViewById(R.id.photo);
                final View title = view.findViewById(R.id.title);
                title.setVisibility(View.INVISIBLE);
                activity.setExitSharedElementCallback(new SharedElementCallback() {

                    @Override
                    public void onSharedElementEnd(List<String> sharedElementNames,
                                                   List<View> sharedElements,
                                                   List<View> sharedElementSnapshots) {

                        super.onSharedElementEnd(sharedElementNames, sharedElements,
                                sharedElementSnapshots);

                        for (View view : sharedElements) {
                            if (view == photo) {
                                view.setVisibility(View.VISIBLE);
                                title.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
                photo.setTransitionName("photo");
                activity.startActivity(destIntent, ActivityOptions.makeSceneTransitionAnimation(activity, photo, "photo").toBundle());
            } else {
                activity.startActivity(destIntent);
            }
        }
    }
}
