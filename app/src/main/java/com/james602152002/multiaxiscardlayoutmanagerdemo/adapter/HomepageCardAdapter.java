package com.james602152002.multiaxiscardlayoutmanagerdemo.adapter;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeTransition;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.james602152002.multiaxiscardlayoutmanager.adapter.MultiAxisCardAdapter;
import com.james602152002.multiaxiscardlayoutmanager.interfaces.ScrollAnimatorObserver;
import com.james602152002.multiaxiscardlayoutmanager.ui.MultiAxisCardRecyclerView;
import com.james602152002.multiaxiscardlayoutmanager.viewholder.BaseCardViewHolder;
import com.james602152002.multiaxiscardlayoutmanager.viewholder.HorizontalCardViewHolder;
import com.james602152002.multiaxiscardlayoutmanager.viewholder.VerticalCardViewHolder;
import com.james602152002.multiaxiscardlayoutmanagerdemo.R;
import com.james602152002.multiaxiscardlayoutmanagerdemo.bean.BeanHorizontalCards;
import com.james602152002.multiaxiscardlayoutmanagerdemo.ui.ActivityCardDetail;
import com.james602152002.multiaxiscardlayoutmanagerdemo.ui.SVGActivity;
import com.james602152002.multiaxiscardlayoutmanagerdemo.util.IPhone6ScreenResizeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by shiki60215 on 18-3-1.
 */

public class HomepageCardAdapter extends MultiAxisCardAdapter {

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

    class H_PhotoCardViewHolder extends HorizontalCardViewHolder implements View.OnClickListener {

        @BindView(R.id.photo)
        SimpleDraweeView photo;
        @BindView(R.id.title)
        AppCompatTextView title;

        public H_PhotoCardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            RecyclerView.LayoutParams cardParams = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            cardParams.width = IPhone6ScreenResizeUtil.getPxValue(450);
            cardParams.height = IPhone6ScreenResizeUtil.getPxValue(600);

            IPhone6ScreenResizeUtil.adjustTextSize(title, 28);
        }

        @Override
        public void initView(int v_card_position, int h_card_position) {
            final List<BeanHorizontalCards> h_items = (List<BeanHorizontalCards>) items.get(v_card_position);
            final BeanHorizontalCards item = h_items.get(h_card_position);
            final String uriStr = item.getUri();

            if (!TextUtils.isEmpty(uriStr)) {
                Uri uri = Uri.parse(uriStr);
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                        .setProgressiveRenderingEnabled(true)
                        .build();
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(photo.getController())
                        .build();
                photo.setController(controller);
            }

//            photo.setImageURI(item.getUri());
            title.setText(item.getTitle());
            itemView.setTag(item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            Object item = view.getTag();
            final AppCompatActivity activity = (AppCompatActivity) view.getContext();
            if (item instanceof BeanHorizontalCards) {
                final Intent destIntent = new Intent(activity, ActivityCardDetail.class);
                destIntent.putExtra("uri", ((BeanHorizontalCards) item).getUri());
                destIntent.putExtra("title", ((BeanHorizontalCards) item).getTitle());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (view.getTop() < 0) {
                        final MultiAxisCardRecyclerView recyclerView = (MultiAxisCardRecyclerView) view.getParent();
                        recyclerView.smoothScrollToPosition(recyclerView.getChildAdapterPosition(view), new ScrollAnimatorObserver() {
                            @Override
                            public void end() {
                                if (view != null && activity != null && destIntent != null)
                                    startActivityTransition(view, activity, destIntent);
                            }
                        });
                    } else {
                        startActivityTransition(view, activity, destIntent);
                    }
                } else {
                    activity.startActivity(destIntent);
                }
            }
        }
    }

    class V_TitleViewHolder extends VerticalCardViewHolder {

        @BindView(R.id.title)
        AppCompatTextView title;
        @BindView(R.id.more)
        AppCompatTextView more;

        public V_TitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            IPhone6ScreenResizeUtil.adjustTextArrSize(32, title, more);
        }

        @Override
        public void initView(int v_card_position, int h_card_position) {
            title.setText((String) items.get(v_card_position));
        }

        @OnClick(R.id.more)
        public void onClick(View v) {
            final Context activity = v.getContext();
            final Intent destIntent = new Intent(activity, SVGActivity.class);
            ActivityCompat.startActivity(activity, destIntent, null);
        }
    }


    @TargetApi(21)
    private void startActivityTransition(View view, AppCompatActivity activity, Intent destIntent) {
        Window window = activity.getWindow();
        window.setSharedElementEnterTransition(DraweeTransition.createTransitionSet(
                ScalingUtils.ScaleType.CENTER_CROP, ScalingUtils.ScaleType.CENTER_CROP));
        window.setSharedElementExitTransition(DraweeTransition.createTransitionSet(
                ScalingUtils.ScaleType.CENTER_CROP, ScalingUtils.ScaleType.CENTER_CROP));
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
        activity.startActivity(destIntent, ActivityOptions.makeSceneTransitionAnimation(activity, photo, "photo").toBundle());
    }
}
