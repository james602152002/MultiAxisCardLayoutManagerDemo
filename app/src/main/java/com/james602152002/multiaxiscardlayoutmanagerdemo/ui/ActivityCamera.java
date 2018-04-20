package com.james602152002.multiaxiscardlayoutmanagerdemo.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.florent37.camerafragment.CameraFragment;
import com.github.florent37.camerafragment.configuration.Configuration;
import com.github.florent37.camerafragment.listeners.CameraFragmentResultListener;
import com.hluhovskyi.camerabutton.CameraButton;
import com.james602152002.multiaxiscardlayoutmanagerdemo.R;
import com.james602152002.multiaxiscardlayoutmanagerdemo.adapter.SvgCardAdapter;
import com.james602152002.multiaxiscardlayoutmanagerdemo.bean.BeanSvgCard;
import com.james602152002.multiaxiscardlayoutmanagerdemo.recyclerview.item_decoration.SvgCardDecoration;
import com.james602152002.multiaxiscardlayoutmanagerdemo.recyclerview.item_touch_helper.CardDetailItemTouchHelperCallBack;
import com.james602152002.multiaxiscardlayoutmanagerdemo.util.IPhone6ScreenResizeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ActivityCamera extends ActivityTranslucent implements View.OnClickListener {

    private CameraFragment cameraFragment;
    @BindView(R.id.camera_header)
    View cameraHeader;
    @BindView(R.id.photo)
    SimpleDraweeView photo;
    @BindView(R.id.btn_group)
    View btnGroup;
    @BindView(R.id.camera_btn)
    CameraButton cameraButton;
    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.crop)
    View crop;
    @BindView(R.id.sure)
    View sure;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            cameraFragment = CameraFragment.newInstance(new Configuration.Builder().build());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, cameraFragment, null)
                    .commit();
        }

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
        text_view.setText("title");
        text_view.setGravity(Gravity.CENTER_VERTICAL);
        IPhone6ScreenResizeUtil.adjustTextSize(text_view, 34);
        view.addView(text_view);
        mToolbar.addView(view);
    }


    private void initView() {
        CollapsingToolbarLayout.LayoutParams cameraHeaderParams = (CollapsingToolbarLayout.LayoutParams) cameraHeader.getLayoutParams();
        cameraHeaderParams.width = IPhone6ScreenResizeUtil.getCurrentScreenWidth();
        cameraHeaderParams.height = IPhone6ScreenResizeUtil.getCurrentScreenHeight();

        cameraButton.setOnPhotoEventListener(new CameraButton.OnPhotoEventListener() {
            @Override
            public void onClick() {
                cameraFragment.takePhotoOrCaptureVideo(new CameraFragmentResultListener() {
                    @Override
                    public void onVideoRecorded(String filePath) {

                    }

                    @Override
                    public void onPhotoTaken(byte[] bytes, String filePath) {
                        content.setVisibility(View.GONE);
                        cameraButton.setVisibility(View.GONE);
                        getSupportFragmentManager().beginTransaction().detach(cameraFragment).commit();
                        File file = new File(filePath);
                        Uri uri = Uri.fromFile(file);
                        photo.setImageURI(uri);
                        photo.setTag(uri);
                        showPhoto();
                    }
                }, null, null);
            }
        });

        cameraButton.setProgressArcColors(new int[]{ContextCompat.getColor(this, R.color.colorPrimary), Color.BLUE});



//        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        ContentResolver mContentResolver = getContentResolver();
//
//        String[] projection = new String[]{MediaStore.Images.Media.MIME_TYPE,
//                MediaStore.Images.Media.DATE_MODIFIED, MediaStore.Images.Media.DATA};
//        // 只查询jpeg和png的图片
//        Cursor mCursor = mContentResolver.query(mImageUri, projection,
//                MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
//                new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
        final CompositeDisposable disposable = new CompositeDisposable();
        Observable<Cursor> observable = Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(ObservableEmitter<Cursor> emitter) throws Exception {

            }
        });
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Cursor>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(Cursor cursor) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        disposable.dispose();
                        disposable.clear();
                    }

                    @Override
                    public void onComplete() {
                        disposable.dispose();
                        disposable.clear();
                    }
                });
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

    private void showPhoto() {
        final int duration = 500;
//        ScaleAnimation anim = new ScaleAnimation(0, 1, 0, 1,
//                Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
//        anim.setDuration(duration);
//        photo.startAnimation(anim);

        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        translateAnimation.setDuration(duration);
        btnGroup.startAnimation(translateAnimation);
    }

    @OnClick({R.id.crop, R.id.sure})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.crop:
                Intent uriIntent = new Intent();
                uriIntent.putExtra("uri", (Uri) photo.getTag());
                setResult(RESULT_OK, uriIntent);
                finish();
                break;
            case R.id.sure:
                break;
        }
    }
}
