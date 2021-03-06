package com.james602152002.multiaxiscardlayoutmanagerdemo.adapter;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeTransition;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.james602152002.multiaxiscardlayoutmanagerdemo.R;
import com.james602152002.multiaxiscardlayoutmanagerdemo.bean.BeanCardDetailListItems;
import com.james602152002.multiaxiscardlayoutmanagerdemo.recyclerview.item_touch_helper.ItemMoveAdapter;
import com.james602152002.multiaxiscardlayoutmanagerdemo.ui.ActivityPersonalCenter;
import com.james602152002.multiaxiscardlayoutmanagerdemo.util.DOMUtil;
import com.james602152002.multiaxiscardlayoutmanagerdemo.util.IPhone6ScreenResizeUtil;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

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
        ImageRequest imageRequest = (ImageRequest) v.getTag();
//        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
//                .getEncodedCacheKey(imageRequest, null);
//        BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache()
//                .getResource(cacheKey);
//        final File file = ((FileBinaryResource) resource).getFile();
//        AndPermission.with(context).permission(Manifest.permission.WRITE_EXTERNAL_STORAGE).onGranted(new Action() {
//            @Override
//            public void onAction(List<String> permissions) {
//                File storageFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/crop");
//                try {
//                    if (!storageFolder.exists())
//                        storageFolder.mkdir();
//                } catch (Exception e) {
//
//                }
//
//                Uri destinationUri = Uri.fromFile(new File(storageFolder, new StringBuilder().append(System.currentTimeMillis()).append(".jpeg").toString()));
//                UCrop.of(Uri.fromFile(file), destinationUri)
//                        .start((Activity) context);
//            }
//        }).start();
        final Intent destIntent = new Intent(context, ActivityPersonalCenter.class);
        final Intent currentActivityIntent = ((AppCompatActivity) context).getIntent();
        destIntent.putExtra("uri", currentActivityIntent.getStringExtra("uri"));
        destIntent.putExtra("avatar", imageRequest.getSourceUri());
        destIntent.putExtra("title", currentActivityIntent.getStringExtra("title"));
        View itemView = (View) v.getParent().getParent().getParent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (itemView.getTop() < 0) {
                final RecyclerView recyclerView = (RecyclerView) itemView.getParent();
                recyclerView.smoothScrollToPosition(recyclerView.getChildAdapterPosition(itemView));
                final CompositeDisposable disposable = new CompositeDisposable();
                Observable.interval(500, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                disposable.add(d);
                            }

                            @Override
                            public void onNext(Long aLong) {
                                if (v != null && context != null && destIntent != null) {
                                    startActivityTransition(v, (AppCompatActivity) context, destIntent);
                                }
                                disposable.dispose();
                                disposable.clear();
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            } else {
                startActivityTransition(v, (AppCompatActivity) context, destIntent);
            }
        } else {
            ContextCompat.startActivity(context, destIntent, null);
        }
    }

    @TargetApi(21)
    private void startActivityTransition(final View avatar, AppCompatActivity activity, Intent destIntent) {
        Window window = activity.getWindow();
        window.setSharedElementEnterTransition(DraweeTransition.createTransitionSet(
                ScalingUtils.ScaleType.CENTER_CROP, ScalingUtils.ScaleType.CENTER_CROP));
        window.setSharedElementExitTransition(DraweeTransition.createTransitionSet(
                ScalingUtils.ScaleType.CENTER_CROP, ScalingUtils.ScaleType.CENTER_CROP));
        activity.setExitSharedElementCallback(new SharedElementCallback() {

            @Override
            public void onSharedElementEnd(List<String> sharedElementNames,
                                           List<View> sharedElements,
                                           List<View> sharedElementSnapshots) {

                super.onSharedElementEnd(sharedElementNames, sharedElements,
                        sharedElementSnapshots);

                for (View view : sharedElements) {
                    if (view == avatar) {
                        view.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
//        photo.setTransitionName("photo");
        ContextCompat.startActivity(context, destIntent, ActivityOptions.makeSceneTransitionAnimation(activity, avatar, "avatar").toBundle());
    }
}
