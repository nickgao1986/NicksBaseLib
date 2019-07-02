package com.scroll.view;

import android.content.Context;
import android.util.AttributeSet;

import com.base.reddots.R;


public class PedoScrollableRefreshLayout extends BaseRefreshScrollLayout<BaseScrollableLayout> {

    public PedoScrollableRefreshLayout(Context context) {
        super(context);
    }

    public PedoScrollableRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.base_scroll_layout;
    }

}
