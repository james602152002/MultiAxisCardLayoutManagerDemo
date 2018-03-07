package com.james602152002.multiaxiscardlayoutmanagerdemo.util;

import android.support.v7.util.DiffUtil;
import android.util.SparseArray;

/**
 * Created by shiki60215 on 18-3-7.
 */

public class DiffCallBackUtil extends DiffUtil.Callback {

    private SparseArray<Object> oldData;
    private SparseArray<Object> newData;

    public DiffCallBackUtil(SparseArray<Object> oldData, SparseArray<Object> newData) {
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
