package com.scroll.util;

import android.annotation.SuppressLint;
import android.os.Build.VERSION;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.scroll.view.ScrollableContainer;


public class ScrollableHelper {
    private ScrollableContainer mCurrentScrollableCainer;

    public ScrollableHelper() {
    }

    public void setCurrentScrollableContainer(ScrollableContainer scrollableContainer) {
        this.mCurrentScrollableCainer = scrollableContainer;
    }

    private View getScrollableView() {
        return this.mCurrentScrollableCainer == null ? null : this.mCurrentScrollableCainer.getScrollableView();
    }

    public void scrollToTop() {
        if (this.mCurrentScrollableCainer != null) {
            this.mCurrentScrollableCainer.scrollToTop();
        }

    }

    public ViewPager getViewPager() {
        return this.mCurrentScrollableCainer == null ? null : this.mCurrentScrollableCainer.getViewPager();
    }

    public boolean isTop() {
        View scrollableView = this.getScrollableView();
        if (scrollableView == null) {
            return true;
        } else if (scrollableView instanceof AdapterView) {
            return isAdapterViewTop((AdapterView)scrollableView);
        } else if (scrollableView instanceof ScrollView) {
            return isScrollViewTop((ScrollView)scrollableView);
        } else if (scrollableView instanceof RecyclerView) {
            return isRecyclerViewTop((RecyclerView)scrollableView);
        } else if (scrollableView instanceof WebView) {
            return isWebViewTop((WebView)scrollableView);
        } else {
            throw new IllegalStateException("scrollableView must be a instance of AdapterView|ScrollView|RecyclerView");
        }
    }

    private static boolean isRecyclerViewTop(RecyclerView recyclerView) {
        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                int firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();
                View childAt = recyclerView.getChildAt(0);
                if (childAt == null || firstVisibleItemPosition == 0 && layoutManager.getDecoratedTop(childAt) == 0) {
                    return true;
                }
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] positionsArrays = ((StaggeredGridLayoutManager)layoutManager).findFirstVisibleItemPositions((int[])null);
                if (positionsArrays.length > 0) {
                    int minPosition = positionsArrays[0];
                    int[] var4 = positionsArrays;
                    int var5 = positionsArrays.length;
                    int var6 = 0;

                    while(true) {
                        if (var6 >= var5) {
                            View childAt = recyclerView.getChildAt(0);
                            if (childAt == null || minPosition == 0 && layoutManager.getDecoratedTop(childAt) == 0) {
                                return true;
                            }
                            break;
                        }

                        int value = var4[var6];
                        if (value > -1 && value < minPosition) {
                            minPosition = value;
                        }

                        ++var6;
                    }
                }
            }
        }

        return false;
    }

    private static boolean isAdapterViewTop(AdapterView adapterView) {
        if (adapterView != null) {
            int firstVisiblePosition = adapterView.getFirstVisiblePosition();
            View childAt = adapterView.getChildAt(0);
            if (childAt == null || firstVisiblePosition == 0 && childAt.getTop() == 0) {
                return true;
            }
        }

        return false;
    }

    private static boolean isScrollViewTop(ScrollView scrollView) {
        if (scrollView != null) {
            int scrollViewY = scrollView.getScrollY();
            return scrollViewY <= 0;
        } else {
            return false;
        }
    }

    private static boolean isWebViewTop(WebView scrollView) {
        if (scrollView != null) {
            int scrollViewY = scrollView.getScrollY();
            return scrollViewY <= 0;
        } else {
            return false;
        }
    }

    @SuppressLint({"NewApi"})
    public void smoothScrollBy(int velocityY, int distance, int duration) {
        View scrollableView = this.getScrollableView();
        if (scrollableView instanceof AbsListView) {
            AbsListView absListView = (AbsListView)scrollableView;
            if (VERSION.SDK_INT >= 21) {
                absListView.fling(velocityY);
            } else {
                absListView.smoothScrollBy(distance, duration);
            }
        } else if (scrollableView instanceof ScrollView) {
            ((ScrollView)scrollableView).fling(velocityY);
        } else if (scrollableView instanceof RecyclerView) {
            ((RecyclerView)scrollableView).fling(0, velocityY);
        } else if (scrollableView instanceof WebView) {
            ((WebView)scrollableView).flingScroll(0, velocityY);
        }

    }
}
