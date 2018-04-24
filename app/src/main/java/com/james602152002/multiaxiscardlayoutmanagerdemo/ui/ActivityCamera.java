package com.james602152002.multiaxiscardlayoutmanagerdemo.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hluhovskyi.camerabutton.CameraButton;
import com.james602152002.multiaxiscardlayoutmanagerdemo.R;
import com.james602152002.multiaxiscardlayoutmanagerdemo.adapter.CameraGalleryAdapter;
import com.james602152002.multiaxiscardlayoutmanagerdemo.fragment.CameraCropFragmentDialog;
import com.james602152002.multiaxiscardlayoutmanagerdemo.interfaces.CameraCropListener;
import com.james602152002.multiaxiscardlayoutmanagerdemo.recyclerview.item_decoration.CameraGalleryDecoration;
import com.james602152002.multiaxiscardlayoutmanagerdemo.util.IPhone6ScreenResizeUtil;
import com.james602152002.multiaxiscardlayoutmanagerdemo.util.SmoothScrollUtil;
import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.io.File;
import java.io.FileOutputStream;

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

    @BindView(R.id.img_camera_rotate)
    AppCompatImageButton imgCameraRotate;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.camera_header)
    View cameraHeader;
    @BindView(R.id.camera_btn)
    CameraButton cameraButton;
    @BindView(R.id.content)
    CameraView cameraView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.action_btn)
    FloatingActionButton floatingActionButton;
    private boolean usingCamera = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

        initToolBar();
        initView();
    }

    private void initToolBar() {
        CollapsingToolbarLayout mCToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
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

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (usingCamera) {
                    return;
                }

                final int total_scroll_range = appBarLayout.getTotalScrollRange();
                final int dy = verticalOffset + total_scroll_range;
                int alpha = ((int) (0xFF * (-(float) verticalOffset / total_scroll_range)) << 24) + 0xFFFFFF;
                int color = ContextCompat.getColor(appBarLayout.getContext(), R.color.colorPrimary);
                color = color & alpha;
                appBarLayout.setBackgroundColor(color);
                cameraView.setAlpha(dy / total_scroll_range);
                cameraButton.cancel();
                if (dy == 0) {
                    if (cameraView.getVisibility() == View.VISIBLE) {
                        cameraView.setVisibility(View.GONE);
                        cameraView.stop();
                        cameraButton.setVisibility(View.GONE);
                    }
                } else if (verticalOffset == 0 && cameraView.getVisibility() == View.GONE) {
                    cameraView.setVisibility(View.VISIBLE);
                    cameraView.start();
                    cameraButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private void initView() {
        initCameraView();
        initRecyclerView();
    }

    private void initCameraView() {
        CollapsingToolbarLayout.LayoutParams cameraHeaderParams = (CollapsingToolbarLayout.LayoutParams) cameraHeader.getLayoutParams();
        cameraHeaderParams.width = IPhone6ScreenResizeUtil.getCurrentScreenWidth();
        cameraHeaderParams.height = IPhone6ScreenResizeUtil.getCurrentScreenHeight();

        cameraView.setMethod(CameraKit.Constants.METHOD_STILL);
        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(final CameraKitImage cameraKitImage) {
//                cameraButton.setVisibility(View.GONE);
                cameraView.stop();
                final CompositeDisposable compositeDisposable = new CompositeDisposable();
                Observable<Uri> observable = Observable.create(new ObservableOnSubscribe<Uri>() {
                    @Override
                    public void subscribe(ObservableEmitter<Uri> emitter) throws Exception {
                        FileOutputStream stream = null;
                        Bitmap bitmap = cameraKitImage.getBitmap();
                        File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpeg");
                        stream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        Uri uri = Uri.fromFile(file);
                        MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), "crop_photos");
                        stream.flush(); // Not really required
                        stream.close();
                        emitter.onNext(uri);
                        emitter.onComplete();
                    }
                });

                observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Uri>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                compositeDisposable.add(d);
                            }

                            @Override
                            public void onNext(Uri uri) {
                                cameraView.setTag(uri);
                            }

                            @Override
                            public void onError(Throwable e) {
                                compositeDisposable.dispose();
                                compositeDisposable.clear();
                            }

                            @Override
                            public void onComplete() {
                                showPhoto();
                            }
                        });
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {
                usingCamera = true;
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(cameraKitVideo.getVideoFile())));
            }
        });
        cameraButton.setOnPhotoEventListener(new CameraButton.OnPhotoEventListener() {
            @Override
            public void onClick() {
                usingCamera = true;
                cameraView.captureImage();
            }
        });

        cameraButton.setOnVideoEventListener(new CameraButton.OnVideoEventListener() {
            @Override
            public void onStart() {
                cameraView.captureVideo();
            }

            @Override
            public void onFinish() {
                cameraView.stopVideo();
            }

            @Override
            public void onCancel() {
                cameraView.stopVideo();
            }
        });


        imgCameraRotate.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        final int tool_bar_height = getToolBarHeight();
        final int status_bar_height = getStatusBarHeight();
        final int img_width = IPhone6ScreenResizeUtil.getPxValue(40);
        final int img_margin = (tool_bar_height - img_width) >> 1;
        ConstraintLayout.LayoutParams camera_rotate_params = (ConstraintLayout.LayoutParams) imgCameraRotate.getLayoutParams();
        camera_rotate_params.height = img_width + (img_margin << 1);
        camera_rotate_params.width = img_width + (img_margin << 1);
        camera_rotate_params.setMargins(IPhone6ScreenResizeUtil.getPxValue(30),
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? status_bar_height : 0),
                IPhone6ScreenResizeUtil.getPxValue(10), 0);
        imgCameraRotate.setPadding(img_margin, img_margin, img_margin, img_margin);

        cameraButton.setProgressArcColors(new int[]{ContextCompat.getColor(this, R.color.colorPrimary), Color.BLUE});
    }

    private void initRecyclerView() {
        final CompositeDisposable compositeDisposable = new CompositeDisposable();
        Observable<Cursor> observable = Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(ObservableEmitter<Cursor> emitter) {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = getContentResolver();

                String[] projection = new String[]{MediaStore.Images.Media.MIME_TYPE,
                        MediaStore.Images.Media.DATE_MODIFIED, MediaStore.Images.Media.DATA};
                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, projection,
                        MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_ADDED + " DESC");
//                    MediaStore.Images.Media.DATE_ADDED + " DESC"
                if (mCursor != null) {
                    emitter.onNext(mCursor);
                    emitter.onComplete();
                } else {
                    emitter.onError(new Throwable());
                }
            }
        });

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new CameraGalleryDecoration());
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Cursor>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Cursor cursor) {
                        CameraGalleryAdapter adapter = new CameraGalleryAdapter(ActivityCamera.this, cursor);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        compositeDisposable.dispose();
                        compositeDisposable.clear();
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.dispose();
                        compositeDisposable.clear();
                    }
                });
    }

    private void showPhoto() {
        CameraCropFragmentDialog dialogFragment = new CameraCropFragmentDialog();
        dialogFragment.setAction(null, null, null, new CameraCropListener() {
            @Override
            public void onCrop() {
                Intent uriIntent = new Intent();
                uriIntent.putExtra("type", "crop");
                uriIntent.putExtra("uri", (Uri) cameraView.getTag());
                setResult(RESULT_OK, uriIntent);
                onBackPressed();
            }

            @Override
            public void onSend() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    cameraView.setTransitionName("avatar");
                Intent uriIntent = new Intent();
                uriIntent.putExtra("uri", (Uri) cameraView.getTag());
                uriIntent.putExtra("type", "send");
                setResult(RESULT_OK, uriIntent);
                onBackPressed();
            }

            @Override
            public void onDismiss() {
                cameraView.start();
                cameraButton.setVisibility(View.VISIBLE);
                usingCamera = false;
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "show_crop_dialog");
    }

    @OnClick({R.id.img_camera_rotate, R.id.action_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_camera_rotate:
                switch (cameraView.getFacing()) {
                    case CameraKit.Constants.FACING_BACK:
                        cameraView.setFacing(CameraKit.Constants.FACING_FRONT);
                        break;
                    case CameraKit.Constants.FACING_FRONT:
                        cameraView.setFacing(CameraKit.Constants.FACING_BACK);
                        break;
                }
                break;
            case R.id.action_btn:
                SmoothScrollUtil.smoothScrollToTop(recyclerView);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }
}
