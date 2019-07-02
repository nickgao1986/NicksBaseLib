package com.scroll.view;

import android.support.v4.view.ViewPager;
import android.view.View;

public interface ScrollableContainer {
    View getScrollableView();

    ViewPager getViewPager();

    void scrollToTop();

    void setCanScroll(boolean var1);

    void setOnScrollStateChangeListener(OnScrollStateChangeListener var1);
}
