package com.james602152002.multiaxiscardlayoutmanagerdemo.item_animator;

import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

/**
 * Created by shiki60215 on 18-3-7.
 */

public abstract class BaseItemAnimator extends SimpleItemAnimator {


    //业务控制是否执行该viewHolder的动画  比如通讯录列表，判断只有联
    //系人的ViewHolder执行动画，如果是分组头部ViewHolder则不执行动画
    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        // 计算 holder的动画参数 如：x偏移量 y轴偏移量 透明度等等
        // 保存 viewHolder 以及 动画参数
        //  业务控制是否执行该viewHolder的动画
        //      比如通讯录列表，判断只有联系人的ViewHolder执行动画，
        //      如果是分组头部ViewHolder则不执行动画
        return false;
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        // 计算 holder的动画参数 如：x偏移量 y轴偏移量 透明度等等
        // 保存 viewHolder 以及 动画参数
        //  业务控制是否执行该viewHolder的动画
        return false;
    }


    //用于控制添加，移动更新时，其它Item的动画执行
    @Override
    public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        // 计算 holder的动画参数 如：x偏移量 y轴偏移量 透明度等等
        // 保存 viewHolder 以及 动画参数
        //  业务控制是否执行该viewHolder的动画
        return false;
    }

    //Item更新回调
    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, int toLeft, int toTop) {
        // 计算 holder的动画参数 如：x偏移量 y轴偏移量 透明度等等
        // 保存 viewHolder 以及 动画参数
        //  业务控制是否执行该viewHolder的动画
        return false;
    }

    //真正控制执行动画的地方
    @Override
    public void runPendingAnimations() {
        // 根据保存的 viewHolder 以及 animInfo   执行动画
    }

    //停止某个Item的动画
    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {

    }

    //停止所有动画
    @Override
    public void endAnimations() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }


    /**
     * 执行移除动画
     * @param holder 被移除的ViewHolder
     * @param animator 被移动的ViewHolder对应动画对象
     */
    public abstract void setRemoveAnimation(RecyclerView.ViewHolder holder, ViewPropertyAnimatorCompat animator);
    /**
     * 执行移除动画结束，执行还原，因为该ViewHolder会被复用
     * @param holder 被移除的ViewHolder
     */
    public abstract void removeAnimationEnd(RecyclerView.ViewHolder holder);
    /**
     * 执行添加动画初始化 这里设置透明为0添加时就会有渐变效果当然你可以在执行动画代码之前执行
     * @param holder 添加的ViewHolder
     */
    public abstract void addAnimationInit(RecyclerView.ViewHolder holder);
    /**
     * 执行添加动画
     * @param holder 添加的ViewHolder
     * @param animator 添加的ViewHolder对应动画对象
     */
    public abstract void setAddAnimation(RecyclerView.ViewHolder holder, ViewPropertyAnimatorCompat animator);
    /**
     * 取消添加还原状态以复用
     * @param holder 添加的ViewHolder
     */
    public abstract void addAnimationCancel(RecyclerView.ViewHolder holder);
    /**
     * 更新时旧的ViewHolder动画
     * @param holder 旧的ViewHolder
     * @param animator ViewHolder对应动画对象
     */
    public abstract void setOldChangeAnimation(RecyclerView.ViewHolder holder, ViewPropertyAnimatorCompat animator);
    /**
     * 更新时旧的ViewHolder动画结束，执行还原
     * @param holder
     */
    public abstract void oldChangeAnimationEnd(RecyclerView.ViewHolder holder);
    /**
     * 更新时新的ViewHolder初始化
     * @param holder 更新时新的ViewHolder
     */
    public abstract void newChangeAnimationInit(RecyclerView.ViewHolder holder);
    /**
     * 更新时新的ViewHolder动画
     * @param holder 新的ViewHolder
     * @param animator ViewHolder对应动画对象
     */
    public abstract void setNewChangeAnimation(RecyclerView.ViewHolder holder, ViewPropertyAnimatorCompat animator);
    /**
     * 更新时新的ViewHolder动画结束，执行还原
     * @param holder
     */
    public abstract void newChangeAnimationEnd(RecyclerView.ViewHolder holder);
}
