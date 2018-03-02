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
import com.james602152002.multiaxiscardlayoutmanagerdemo.adapter.CardAdapter;
import com.james602152002.multiaxiscardlayoutmanagerdemo.bean.BeanHorizontalCards;
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

        recyclerView.setAdapter(new CardAdapter(this, fetchData(), R.layout.card_cell, R.layout.horizontal_card));

        FloatingActionButton actionButton = findViewById(R.id.action_btn);
        actionButton.setOnClickListener(this);
    }

    private SparseArray<Object> fetchData() {
        SparseArray<Object> data = new SparseArray<>();
        int j = 0;
        for (int i = 0; i < 1000; i++) {
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
                            data.put(i, "Notification");
                            break;
                        default:
                            data.put(i, "News");
                            break;
                    }
                    j++;
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
