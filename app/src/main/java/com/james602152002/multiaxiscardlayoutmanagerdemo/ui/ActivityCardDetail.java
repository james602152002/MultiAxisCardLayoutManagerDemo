package com.james602152002.multiaxiscardlayoutmanagerdemo.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.james602152002.multiaxiscardlayoutmanagerdemo.R;
import com.james602152002.multiaxiscardlayoutmanagerdemo.adapter.CardDetailAdapter;
import com.james602152002.multiaxiscardlayoutmanagerdemo.bean.BeanCardDetailListItems;
import com.james602152002.multiaxiscardlayoutmanagerdemo.item_decoration.CardDetailDecoration;

/**
 * Created by shiki60215 on 18-3-2.
 */

public class ActivityCardDetail extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

        initToolBar();
        initView();
    }

    private void initToolBar() {
        CollapsingToolbarLayout mCToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        mCToolbarLayout.setCollapsedTitleTextColor(0);
        mCToolbarLayout.setExpandedTitleColor(0);
        // Set the support action bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setSupportActionBar(mToolbar);
        else
            getSupportActionBar().hide();

        LinearLayout view = new LinearLayout(this);
        view.setGravity(Gravity.CENTER_VERTICAL);
        TextView text_view = new TextView(this);
        text_view.setTextColor(Color.WHITE);
        text_view.setText(getIntent().getStringExtra("title"));
        text_view.setGravity(Gravity.CENTER_VERTICAL);
        view.addView(text_view);
        mToolbar.addView(view);
        SimpleDraweeView image = findViewById(R.id.toolbar_image);
        image.setImageURI(getIntent().getStringExtra("uri"));
    }

    private void initView() {
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new CardDetailDecoration());
//        recyclerView.setItemAnimator(new CardDetailItemAnimator());
        final short duration = 1000;
        AnimationSet anim = new AnimationSet(true);
        TranslateAnimation translateAnim = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
        translateAnim.setDuration(duration);
        RotateAnimation rotateAnim = new RotateAnimation(10, 0);
        rotateAnim.setDuration(duration);
        anim.addAnimation(translateAnim);
        anim.addAnimation(rotateAnim);

        recyclerView.setLayoutAnimation(new LayoutAnimationController(anim, 0));
        final SparseArray<BeanCardDetailListItems> data = new SparseArray<>();
        SparseArray<BeanCardDetailListItems> fetch_data = fetchData();
        for (int i = 0; i < fetch_data.size(); i++) {
            data.put(data.size(), fetch_data.get(i));
        }
        recyclerView.setAdapter(new CardDetailAdapter(this, data));
    }

    private SparseArray<BeanCardDetailListItems> fetchData() {
        SparseArray<BeanCardDetailListItems> items = new SparseArray<>();
        for (int i = 0; i < 10; i++) {
            BeanCardDetailListItems item = new BeanCardDetailListItems();
            item.setTitle(new StringBuilder("Card Detail Title ").append(i + 1).toString());
            items.put(i, item);
        }
        return items;
    }
}
