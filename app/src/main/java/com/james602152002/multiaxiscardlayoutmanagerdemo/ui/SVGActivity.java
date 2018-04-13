package com.james602152002.multiaxiscardlayoutmanagerdemo.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.james602152002.multiaxiscardlayoutmanagerdemo.R;
import com.james602152002.multiaxiscardlayoutmanagerdemo.util.IPhone6ScreenResizeUtil;
import com.jaredrummler.android.widget.AnimatedSvgView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SVGActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.google_animated_svg_view)
    AnimatedSvgView googleSvgView;
    @BindView(R.id.ailinklaw_animated_svg_view)
    AnimatedSvgView ailinklawSvgView;
    @BindView(R.id.logo_animated_svg_view)
    AnimatedSvgView logoSvgView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg);
        ButterKnife.bind(this);
        initToolBar();
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

    @Override
    @OnClick({R.id.google_animated_svg_view, R.id.ailinklaw_animated_svg_view, R.id.logo_animated_svg_view})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_animated_svg_view:
                googleSvgView.start();
                break;
            case R.id.ailinklaw_animated_svg_view:
                ailinklawSvgView.start();
                break;
            case R.id.logo_animated_svg_view:
                logoSvgView.start();
                break;
        }
    }
}
