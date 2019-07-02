package com.scroll.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

public abstract class BaseRefreshScrollLayout<T extends ScrollableLayout> extends PullToRefreshBase<T> {
    public BaseRefreshScrollLayout(Context context) {
        super(context);
    }

    public BaseRefreshScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected T createRefreshableView(Context context, AttributeSet attrs) {
        T scrollableLayout = (T)LayoutInflater.from(context).inflate(this.getLayoutResId(), (ViewGroup)null);
        this.initView(scrollableLayout);
        return scrollableLayout;
    }

    public Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    protected boolean isReadyForPullEnd() {
        return false;
    }

    protected boolean isReadyForPullStart() {
        return ((ScrollableLayout)this.getRefreshableView()).canPtr();
    }

    public void initView(T view) {
    }

    public abstract int getLayoutResId();
}
