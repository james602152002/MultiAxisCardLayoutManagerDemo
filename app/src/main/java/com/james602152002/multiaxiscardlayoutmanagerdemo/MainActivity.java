package com.james602152002.multiaxiscardlayoutmanagerdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.james602152002.multiaxiscardlayoutmanager.ui.CardRecyclerView;
import com.james602152002.multiaxiscardlayoutmanagerdemo.adapter.HomepageCardAdapter;
import com.james602152002.multiaxiscardlayoutmanagerdemo.bean.BeanHorizontalCards;
import com.james602152002.multiaxiscardlayoutmanagerdemo.item_decoration.MultiAxisCardDecoration;
import com.james602152002.multiaxiscardlayoutmanagerdemo.ui.BaseActivity;
import com.james602152002.multiaxiscardlayoutmanagerdemo.util.DiffCallBackUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private CardRecyclerView recyclerView;
    private SparseArray<Object> mData = new SparseArray<>();
    private HomepageCardAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        text_view.setText(R.string.app_name);
        text_view.setGravity(Gravity.CENTER_VERTICAL);
        view.addView(text_view);
        mToolbar.addView(view);
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new MultiAxisCardDecoration());
        fetchData();
        mAdapter = new HomepageCardAdapter(this, mData, R.layout.card_cell, R.layout.horizontal_card);
        recyclerView.setAdapter(mAdapter);

        FloatingActionButton actionButton = findViewById(R.id.action_btn);
        actionButton.setOnClickListener(this);
    }

    private void fetchData() {
        SparseArray<Object> data = new SparseArray<>();
        int j = 0;
        int title_position = (mData.size() >> 2) + 1;
        for (int i = 0; i < 4; i++) {
            switch (i % 2) {
                case 1:
                    List<BeanHorizontalCards> list = new ArrayList<>();
                    BeanHorizontalCards item = new BeanHorizontalCards();
                    item.setUri("http://img4.imgtn.bdimg.com/it/u=3750011819,3893667393&fm=27&gp=0.jpg");
                    item.setTitle("Nier Automata Photo 1");
                    list.add(item);
                    item = new BeanHorizontalCards();
                    item.setUri("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2485596028,71439047&fm=27&gp=0.jpg");
                    item.setTitle("Nier Automata Photo 2");
                    list.add(item);
                    item = new BeanHorizontalCards();
                    item.setUri("http://img3.imgtn.bdimg.com/it/u=624862191,3362962092&fm=27&gp=0.jpg");
                    item.setTitle("Nier Automata Photo 3");
                    list.add(item);
                    item = new BeanHorizontalCards();
                    item.setUri("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2602427892,3990550605&fm=27&gp=0.jpg");
                    item.setTitle("Nier Automata Photo 4");
                    list.add(item);
                    data.put(i, list);
                    break;
                default:
                    switch (j % 2) {
                        case 1:
                            data.put(i, new StringBuilder("Notification").append(title_position).toString());
                            title_position++;
                            break;
                        default:
                            data.put(i, new StringBuilder("News").append(title_position).toString());
                            break;
                    }
                    j++;
                    break;
            }
        }
        for (int i = 0; i < data.size(); i++) {
            mData.put(mData.size(), data.get(i));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_btn:
                final SparseArray<Object> oldData = new SparseArray<>();
                for (int i = 0; i < mData.size(); i++) {
                    oldData.put(i, mData.get(i));
                }
                fetchData();

                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallBackUtil(oldData, mData), true);
                diffResult.dispatchUpdatesTo(mAdapter);
                break;
        }
    }
}
