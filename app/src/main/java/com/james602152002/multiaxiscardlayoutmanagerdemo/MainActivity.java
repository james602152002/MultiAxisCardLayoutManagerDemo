package com.james602152002.multiaxiscardlayoutmanagerdemo;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.james602152002.multiaxiscardlayoutmanager.MultiAxisCardLayoutManager;
import com.james602152002.multiaxiscardlayoutmanager.adapter.MultiAxisCardAdapter;
import com.james602152002.multiaxiscardlayoutmanager.interfaces.ViewHolderCallBack;
import com.james602152002.multiaxiscardlayoutmanagerdemo.item_decoration.CardDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        text_view.setText(R.string.app_name);
        text_view.setGravity(Gravity.CENTER_VERTICAL);
        view.addView(text_view);
        mToolbar.addView(view);
        initView();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new MultiAxisCardLayoutManager(recyclerView));
        recyclerView.addItemDecoration(new CardDecoration());
//        recyclerView.setAdapter(new CardAdapter(this));

        recyclerView.setAdapter(new MultiAxisCardAdapter(this, fetchData(), R.layout.card_cell, R.layout.horizontal_card, new ViewHolderCallBack() {
            @Override
            public void horizontalViewCallBack(int position) {

            }

            @Override
            public void verticalViewCallBack(int position) {

            }
        }));

        FloatingActionButton actionButton = findViewById(R.id.action_btn);
        actionButton.setOnClickListener(this);
    }

    private SparseArray<Object> fetchData() {
        SparseArray<Object> data = new SparseArray<>();
        for (int i = 0; i < 10; i++) {
            switch (i % 3) {
                case 2:
                    List<Object> list = new ArrayList<>();
                    list.add(new Object());
                    list.add(new Object());
                    list.add(new Object());
                    list.add(new Object());
                    data.put(i, list);
                    break;
                default:
                    data.put(i, new Object());
                    break;
            }

        }
        return data;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_btn:
                break;
        }
    }
}
