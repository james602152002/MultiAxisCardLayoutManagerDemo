package com.james602152002.multiaxiscardlayoutmanagerdemo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.james602152002.multiaxiscardlayoutmanagerdemo.R;
import com.james602152002.multiaxiscardlayoutmanagerdemo.bean.BeanSvgCard;
import com.james602152002.multiaxiscardlayoutmanagerdemo.recyclerview.item_touch_helper.ItemMoveAdapter;
import com.james602152002.multiaxiscardlayoutmanagerdemo.util.IPhone6ScreenResizeUtil;
import com.jaredrummler.android.widget.AnimatedSvgView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SvgCardAdapter extends RecyclerView.Adapter<SvgCardAdapter.SvgCardViewHolder> implements ItemMoveAdapter {

    private final LayoutInflater inflater;
    private final List<BeanSvgCard> items;
    private final Context mContext;

    public SvgCardAdapter(Context context, List<BeanSvgCard> items) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        this.items = items;
    }

    @NonNull
    @Override
    public SvgCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SvgCardViewHolder(inflater.inflate(R.layout.cell_svg_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SvgCardViewHolder holder, int position) {
        holder.initView(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class SvgCardViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.svg_view)
        AnimatedSvgView svgView;

        public SvgCardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(SvgCardViewHolder.this, itemView);
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            lp.height = IPhone6ScreenResizeUtil.getPxValue(400);
        }

        public void initView(int position) {
            final BeanSvgCard item = items.get(position);
            final int view_port_size = position == 0 ? 400 : 1000;
            svgView.setViewportSize(view_port_size, view_port_size);
            svgView.setGlyphStrings(mContext.getResources().getStringArray(item.getSvg_str_arr_id()));
            svgView.setFillColors(mContext.getResources().getIntArray(item.getSvg_color_arr_id()));
            svgView.setTraceResidueColor(Color.argb(50, 0, 0, 0));
            svgView.setTraceColor(Color.BLACK);
        }

        @OnClick(R.id.svg_view)
        public void onClick(View v) {
            ((AnimatedSvgView) v).start();
        }

    }

    @Override
    public void move(int fromPos, int toPos) {
        Collections.swap(items, fromPos, toPos);
        notifyItemMoved(fromPos, toPos);
    }

    @Override
    public void swipe(int pos) {

    }
}
