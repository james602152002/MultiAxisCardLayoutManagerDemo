package com.james602152002.multiaxiscardlayoutmanagerdemo.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.james602152002.multiaxiscardlayoutmanagerdemo.R;
import com.james602152002.multiaxiscardlayoutmanagerdemo.adapter.CardDetailAdapter;
import com.james602152002.multiaxiscardlayoutmanagerdemo.bean.BeanCardDetailListItems;
import com.james602152002.multiaxiscardlayoutmanagerdemo.recyclerview.item_decoration.CardDetailDecoration;
import com.james602152002.multiaxiscardlayoutmanagerdemo.recyclerview.item_touch_helper.CardDetailItemTouchHelperCallBack;
import com.james602152002.multiaxiscardlayoutmanagerdemo.recyclerview.item_touch_helper.ItemMoveAdapter;
import com.james602152002.multiaxiscardlayoutmanagerdemo.util.DiffListCallBackUtil;
import com.james602152002.multiaxiscardlayoutmanagerdemo.util.IPhone6ScreenResizeUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
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
 * Created by shiki60215 on 18-3-2.
 */

public class ActivityCardDetail extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.toolbar_image)
    SimpleDraweeView image;
    private final List<BeanCardDetailListItems> data = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);
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
        imgParams.height = IPhone6ScreenResizeUtil.getPxValue(400);
        image.setImageURI(getIntent().getStringExtra("uri"));
    }

    private void initView() {
//        smartRefreshLayout.setHeaderHeight(DensityUtil.px2dp(100) )
//        ((CollapsingToolBarMaterialHeader) findViewById(R.id.header)).setAppBarLayout((AppBarLayout) findViewById(R.id.appbar));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshData();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                fetchData();
            }
        });
        smartRefreshLayout.autoRefresh();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new CardDetailDecoration());
    }

    private void refreshData() {
        final CompositeDisposable disposable = new CompositeDisposable();
        Observable.interval(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        data.clear();
                        List<BeanCardDetailListItems> items = new ArrayList<>();
                        for (int i = 0; i < 5; i++) {
                            BeanCardDetailListItems item = new BeanCardDetailListItems();
                            item.setTitle(new StringBuilder("Card Detail Title ").append(i + 1).toString());
                            items.add(i, item);
                        }
                        data.addAll(items);
                        smartRefreshLayout.finishRefresh();

                        if (recyclerView.getAdapter() != null) {
                            recyclerView.getAdapter().notifyDataSetChanged();
                        } else {
                            recyclerView.setAdapter(new CardDetailAdapter(ActivityCardDetail.this, data));
                            CardDetailItemTouchHelperCallBack callBack = new CardDetailItemTouchHelperCallBack((ItemMoveAdapter) recyclerView.getAdapter());
                            ItemTouchHelper touchHelper = new ItemTouchHelper(callBack);
                            touchHelper.attachToRecyclerView(recyclerView);
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
    }

    private void fetchData() {
        final CompositeDisposable disposable = new CompositeDisposable();
        Observable.interval(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        List<BeanCardDetailListItems> oldData = new ArrayList<>();
                        oldData.addAll(data);
                        List<BeanCardDetailListItems> items = new ArrayList<>();
                        for (int i = 0; i < 5; i++) {
                            BeanCardDetailListItems item = new BeanCardDetailListItems();
                            item.setTitle(new StringBuilder("Card Detail Title ").append(data.size() + i + 1).toString());
                            items.add(i, item);
                        }
                        data.addAll(items);
                        smartRefreshLayout.finishLoadMore();
                        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffListCallBackUtil(oldData, data), true);
                        diffResult.dispatchUpdatesTo(recyclerView.getAdapter());
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
    }
}
