package com.james602152002.multiaxiscardlayoutmanagerdemo.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.james602152002.multiaxiscardlayoutmanagerdemo.R;
import com.james602152002.multiaxiscardlayoutmanagerdemo.bean.BeanCardDetailListItems;
import com.james602152002.multiaxiscardlayoutmanagerdemo.recyclerview.item_touch_helper.ItemMoveAdapter;
import com.james602152002.multiaxiscardlayoutmanagerdemo.util.DOMUtil;
import com.james602152002.multiaxiscardlayoutmanagerdemo.util.IPhone6ScreenResizeUtil;
import com.yalantis.ucrop.UCrop;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shiki60215 on 18-3-7.
 */

public class CardDetailAdapter extends RecyclerView.Adapter<CardDetailAdapter.CardViewHolder> implements ItemMoveAdapter, View.OnClickListener {

    private final Context context;
    private final LayoutInflater inflater;
    private List<BeanCardDetailListItems> items;
    private boolean show_anim = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

    public CardDetailAdapter(Context context, List<BeanCardDetailListItems> data) {
        this.context = context;
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

        @BindView(R.id.vertical_divider)
        View v_divider;
        @BindView(R.id.title)
        AppCompatTextView title;
        @BindView(R.id.content)
        WebView content;
        @BindView(R.id.photo_card)
        CardView photoCard;
        @BindView(R.id.photo)
        SimpleDraweeView photo;

        public CardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ((ConstraintLayout.LayoutParams) v_divider.getLayoutParams()).width = IPhone6ScreenResizeUtil.getPxValue(50);

            final int h_margin = IPhone6ScreenResizeUtil.getPxValue(40);
            final int v_margin = IPhone6ScreenResizeUtil.getPxValue(30);
            ConstraintLayout.LayoutParams titleParams = (ConstraintLayout.LayoutParams) title.getLayoutParams();
            titleParams.setMargins(h_margin, v_margin, h_margin, v_margin >> 1);

            ConstraintLayout.LayoutParams contentParams = (ConstraintLayout.LayoutParams) content.getLayoutParams();
            contentParams.setMargins(h_margin, v_margin >> 1, h_margin, v_margin);

            ConstraintLayout.LayoutParams photoParams = (ConstraintLayout.LayoutParams) photoCard.getLayoutParams();
            photoParams.setMargins(h_margin, v_margin >> 1, h_margin, v_margin >> 1);

            IPhone6ScreenResizeUtil.adjustTextSize(title, 32);
        }

        public void initView(int position) {
            final BeanCardDetailListItems item = items.get(position);
            title.setText(item.getTitle());

            final String uriStr = item.getUrl();
            if (!TextUtils.isEmpty(uriStr)) {
                Uri uri = Uri.parse(uriStr);
                final ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                        .setProgressiveRenderingEnabled(true)
                        .build();
                final DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(photo.getController())
                        .build();
                photo.setController(controller);
                photo.setTag(request);
            }
            content.loadData(DOMUtil.fetchJustifyTextDOM(item.getContent(), IPhone6ScreenResizeUtil.getPT_TextSize(10), 2), "text/html; charset=utf-8", "utf-8");
            photo.setOnClickListener(CardDetailAdapter.this);
        }
    }

    @Override
    public void move(int fromPos, int toPos) {
        Collections.swap(items, fromPos, toPos);
        notifyItemMoved(fromPos, toPos);
    }

    @Override
    public void swipe(int pos) {
        items.remove(pos);
        notifyItemRemoved(pos);
    }

    @Override
    public void onClick(final View v) {
        Log.i("","");
        ImageRequest imageRequest = (ImageRequest) v.getTag();
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                .getEncodedCacheKey(imageRequest, null);
        BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache()
                .getResource(cacheKey);
        final File file = ((FileBinaryResource) resource).getFile();
        AndPermission.with(context).permission(Manifest.permission.WRITE_EXTERNAL_STORAGE).onGranted(new Action() {
            @Override
            public void onAction(List<String> permissions) {
                Uri destinationUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "SampleCropImage.jpeg"));
                UCrop.of(Uri.fromFile(file), destinationUri)
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(200, 200)
                        .start((Activity) context);
            }
        }).start();
    }
}
