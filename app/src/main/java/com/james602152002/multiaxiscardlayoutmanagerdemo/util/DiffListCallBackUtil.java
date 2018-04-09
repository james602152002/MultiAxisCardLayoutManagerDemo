package com.james602152002.multiaxiscardlayoutmanagerdemo.util;

import android.support.v7.util.DiffUtil;

import java.util.List;

public class DiffListCallBackUtil extends DiffUtil.Callback {

    private List<? extends Object> oldData;
    private List<? extends Object> newData;

    public DiffListCallBackUtil(List<? extends Object>  oldData, List<? extends Object>  newData) {
        this.oldData = oldData;
        this.newData = newData;
    }

    @Override
    public int getOldListSize() {
        return oldData.size();
    }

    @Override
    public int getNewListSize() {
        return newData.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldData.get(oldItemPosition) == newData.get(newItemPosition);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return true;
    }
}
