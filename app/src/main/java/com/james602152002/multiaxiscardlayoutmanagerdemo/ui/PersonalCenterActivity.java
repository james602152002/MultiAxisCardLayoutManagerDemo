package com.james602152002.multiaxiscardlayoutmanagerdemo.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.james602152002.multiaxiscardlayoutmanagerdemo.adapter.SvgCardAdapter;
import com.james602152002.multiaxiscardlayoutmanagerdemo.bean.BeanSvgCard;
import com.james602152002.multiaxiscardlayoutmanagerdemo.recyclerview.item_decoration.SvgCardDecoration;
import com.james602152002.multiaxiscardlayoutmanagerdemo.recyclerview.item_touch_helper.CardDetailItemTouchHelperCallBack;
import com.james602152002.multiaxiscardlayoutmanagerdemo.util.IPhone6ScreenResizeUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yalantis.ucrop.UCrop;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonalCenterActivity extends TranslucentActivity implements View.OnClickListener {

    @BindView(R.id.toolbar_image)
    SimpleDraweeView image;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.avatar)
    SimpleDraweeView avatar;
    @BindView(R.id.avatar_card)
    CardView avatarCard;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.edit)
    AppCompatTextView edit;
    @BindView(R.id.photo)
    AppCompatTextView photo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        ButterKnife.bind(this);
        initToolBar();
        initView();
    }

    private void initToolBar() {
        CollapsingToolbarLayout mCToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        mCToolbarLayout.setCollapsedTitleTextColor(0);
        mCToolbarLayout.setExpandedTitleColor(0);
        // Set the support action bar
        initToolBar(mToolbar);

        LinearLayout view = new LinearLayout(this);
        view.setGravity(Gravity.CENTER_VERTICAL);
        TextView text_view = new TextView(this);
        text_view.setTextColor(Color.WHITE);
        text_view.setText(getIntent().getStringExtra("title"));
        text_view.setGravity(Gravity.CENTER_VERTICAL);
        IPhone6ScreenResizeUtil.adjustTextSize(text_view, 34);
        view.addView(text_view);
        mToolbar.addView(view);

        CollapsingToolbarLayout.LayoutParams imgParams = (CollapsingToolbarLayout.LayoutParams) image.getLayoutParams();
        imgParams.height = IPhone6ScreenResizeUtil.getPxValue(350);
        image.setImageURI(getIntent().getStringExtra("uri"));
    }

    private void initView() {
        CoordinatorLayout.LayoutParams imgParams = (CoordinatorLayout.LayoutParams) avatarCard.getLayoutParams();
        imgParams.height = IPhone6ScreenResizeUtil.getPxValue(400);
        imgParams.width = IPhone6ScreenResizeUtil.getPxValue(400);
        imgParams.topMargin = IPhone6ScreenResizeUtil.getPxValue(150);

        final ImageRequest request = ImageRequestBuilder.newBuilderWithSource((Uri) getIntent().getParcelableExtra("avatar"))
                .setProgressiveRenderingEnabled(true)
                .build();
        final DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(avatar.getController())
                .build();
        avatar.setController(controller);
        avatar.setTag(request);

        final int text_height = IPhone6ScreenResizeUtil.getPxValue(80);
        ((ConstraintLayout.LayoutParams)edit.getLayoutParams()).height = text_height;
        ((ConstraintLayout.LayoutParams)photo.getLayoutParams()).height = text_height;


//        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
////                refreshData();
//            }
//        });
//        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
////                fetchData();
//            }
//        });
//        smartRefreshLayout.autoRefresh();
        List<BeanSvgCard> items = new ArrayList<>();
        BeanSvgCard item = new BeanSvgCard(R.array.google_glyph_strings, R.array.google_glyph_colors);
        items.add(item);
        item = new BeanSvgCard(R.array.ailinklaw_glyph_strings, R.array.ailinklaw_glyph_colors);
        items.add(item);
        item = new BeanSvgCard(R.array.logo_glyph_strings, R.array.logo_glyph_colors);
        items.add(item);
        SvgCardAdapter adapter = new SvgCardAdapter(this, items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new SvgCardDecoration());
        recyclerView.setAdapter(adapter);
        CardDetailItemTouchHelperCallBack callBack = new CardDetailItemTouchHelperCallBack(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callBack);
        touchHelper.attachToRecyclerView(recyclerView);
    }


    @OnClick(R.id.avatar)
    @Override
    public void onClick(View v) {
        ImageRequest imageRequest = (ImageRequest) v.getTag();
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                .getEncodedCacheKey(imageRequest, null);
        BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache()
                .getResource(cacheKey);
        final File file = ((FileBinaryResource) resource).getFile();
        AndPermission.with(this).permission(Manifest.permission.WRITE_EXTERNAL_STORAGE).onGranted(new Action() {
            @Override
            public void onAction(List<String> permissions) {
                File storageFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/crop");
                try {
                    if (!storageFolder.exists())
                        storageFolder.mkdir();
                } catch (Exception e) {

                }

                Uri destinationUri = Uri.fromFile(new File(storageFolder, new StringBuilder().append(System.currentTimeMillis()).append(".jpeg").toString()));
                UCrop.of(Uri.fromFile(file), destinationUri)
                        .start(PersonalCenterActivity.this);
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri resultUri = UCrop.getOutput(data);
            avatar.setImageURI(resultUri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                avatar.setTransitionName(null);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Snackbar.make(recyclerView, cropError.getMessage(), Toast.LENGTH_SHORT);
        }
    }
}
