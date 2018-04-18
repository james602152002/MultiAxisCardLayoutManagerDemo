package com.james602152002.multiaxiscardlayoutmanagerdemo.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.james602152002.multiaxiscardlayoutmanagerdemo.R;
import com.james602152002.multiaxiscardlayoutmanagerdemo.adapter.SvgCardAdapter;
import com.james602152002.multiaxiscardlayoutmanagerdemo.bean.BeanSvgCard;
import com.james602152002.multiaxiscardlayoutmanagerdemo.recyclerview.item_decoration.SvgCardDecoration;
import com.james602152002.multiaxiscardlayoutmanagerdemo.recyclerview.item_touch_helper.CardDetailItemTouchHelperCallBack;
import com.james602152002.multiaxiscardlayoutmanagerdemo.util.IPhone6ScreenResizeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SVGActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg);
        ButterKnife.bind(this);
        initToolBar();
        initView();
    }

    private void initToolBar() {
        mToolbar.setTitle("");
        LinearLayout view = new LinearLayout(this);
        view.setGravity(Gravity.CENTER_VERTICAL);
        TextView text_view = new TextView(this);
        text_view.setTextColor(Color.WHITE);
        text_view.setText("SVG Activity");
        text_view.setGravity(Gravity.CENTER_VERTICAL);
        IPhone6ScreenResizeUtil.adjustTextSize(text_view, 34);
        view.addView(text_view);
        mToolbar.addView(view);
    }

    private void initView() {
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

}
