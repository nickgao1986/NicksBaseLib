package com.scroll.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.scroll.util.ScrollableHelper;
import com.util.LogUtil;

public class ScrollableLayout extends LinearLayout {
    private final String tag = "cp:scrollableLayout";
    private float mDownX;
    private float mDownY;
    private float mLastY;
    private int topOffset = 0;
    private int minY = 0;
    private int maxY = 0;
    private int mHeadHeight;
    private int mExpandHeight;
    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private DIRECTION mDirection;
    private int mCurY;
    private int mLastScrollerY;
    private boolean needCheckUpdown;
    private boolean updown;
    private boolean mDisallowIntercept;
    private boolean isClickHead;
    private boolean isClickHeadExpand;
    private View mHeadView;
    private ViewPager childViewPager;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private OnScrollListener onScrollListener;
    private ScrollableHelper mHelper;
    static Handler mHandler = new Handler();
    Runnable runnable;
    private Rect mNotScrollArea = new Rect();

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public ScrollableHelper getHelper() {
        return this.mHelper;
    }

    public ScrollableLayout(Context context) {
        super(context);
        class NamelessClass_1 implements Runnable {
            NamelessClass_1() {
            }

            public void run() {
                if (ScrollableLayout.this.onScrollListener != null) {
                    ScrollableLayout.this.onScrollListener.onScrollStop();
                }

            }
        }
        this.runnable = new NamelessClass_1();
        this.init(context);
    }

    public ScrollableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        class NamelessClass_1 implements Runnable {
            NamelessClass_1() {
            }

            public void run() {
                if (ScrollableLayout.this.onScrollListener != null) {
                    ScrollableLayout.this.onScrollListener.onScrollStop();
                }

            }
        }
        this.runnable = new NamelessClass_1();
        this.init(context);
    }



    @TargetApi(21)
    public ScrollableLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        class NamelessClass_1 implements Runnable {
            NamelessClass_1() {
            }

            public void run() {
                if (ScrollableLayout.this.onScrollListener != null) {
                    ScrollableLayout.this.onScrollListener.onScrollStop();
                }

            }
        }

        this.runnable = new NamelessClass_1();
        this.init(context);
    }

    private void init(Context context) {
        this.mHelper = new ScrollableHelper();
        this.mScroller = new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        this.mTouchSlop = configuration.getScaledTouchSlop();
        this.mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    public void setNotScrollArea(Rect r) {
        mNotScrollArea = r;
    }

    public boolean isSticked() {
        return this.mCurY == this.maxY;
    }

    public void setClickHeadExpand(int expandHeight) {
        this.mExpandHeight = expandHeight;
    }

    public int getMaxY() {
        return this.maxY;
    }

    public boolean isHeadTop() {
        return this.mCurY == this.minY;
    }

    public boolean canPtr() {
        return this.getScrollY() == 0 && this.mCurY == this.minY && this.mHelper.isTop();
    }

    public void requestScrollableLayoutDisallowInterceptTouchEvent(boolean disallowIntercept) {
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
        this.mDisallowIntercept = disallowIntercept;
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        float currentX = ev.getX();
        float currentY = ev.getY();
        int shiftX = (int)Math.abs(currentX - this.mDownX);
        int shiftY = (int)Math.abs(currentY - this.mDownY);
        switch(ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(mNotScrollArea.contains((int)currentX,(int)currentY) && isHeadTop()) {
                    this.mDisallowIntercept = true;
                }else{
                    this.mDisallowIntercept = false;
                }

                this.needCheckUpdown = true;
                this.updown = true;
                this.mDownX = currentX;
                this.mDownY = currentY;
                this.mLastY = currentY;
                this.checkIsClickHead((int)currentY, this.mHeadHeight, this.getScrollY());
                this.checkIsClickHeadExpand((int)currentY, this.mHeadHeight, this.getScrollY());
                this.initOrResetVelocityTracker();
                this.mVelocityTracker.addMovement(ev);
                this.mScroller.forceFinished(true);
                break;
            case MotionEvent.ACTION_UP:
                LogUtil.d("TAG","<<<<scroll action up1");
                if (!this.updown || shiftY <= shiftX || shiftY <= this.mTouchSlop) {
                    break;
                }
                LogUtil.d("TAG","<<<<scroll action up2");
                this.mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
                float yVelocity = -this.mVelocityTracker.getYVelocity();
                boolean dislowChild = false;
                if (Math.abs(yVelocity) > (float)this.mMinimumVelocity) {
                    this.mDirection = yVelocity > 0.0F ? DIRECTION.UP : DIRECTION.DOWN;
                    if ((this.mDirection != DIRECTION.UP || !this.isSticked()) && (this.isSticked() || this.getScrollY() != 0 || this.mDirection != DIRECTION.DOWN)) {
                        this.mScroller.fling(0, this.getScrollY(), 0, (int)yVelocity, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
                        this.mScroller.computeScrollOffset();
                        this.mLastScrollerY = this.getScrollY();
                        this.invalidate();
                    } else {
                        dislowChild = true;
                    }
                }

                if (!dislowChild && (this.isClickHead || !this.isSticked())) {
                    int action = ev.getAction();
                    ev.setAction(3);
                    boolean dispathResult = super.dispatchTouchEvent(ev);
                    ev.setAction(action);
                    return dispathResult;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!this.mDisallowIntercept) {
                    this.initVelocityTrackerIfNotExists();
                    this.mVelocityTracker.addMovement(ev);
                    float deltaY = this.mLastY - currentY;
                    if (this.needCheckUpdown) {
                        if (shiftX > this.mTouchSlop && shiftX > shiftY) {
                            this.needCheckUpdown = false;
                            this.updown = false;
                        } else if (shiftY > this.mTouchSlop && shiftY > shiftX) {
                            this.needCheckUpdown = false;
                            this.updown = true;
                        }
                    }

                    if (this.updown && shiftY > this.mTouchSlop && shiftY > shiftX && (!this.isSticked() || this.mHelper.isTop() || this.isClickHeadExpand)) {
                        if (this.childViewPager == null) {
                            this.childViewPager = this.mHelper.getViewPager();
                        }

                        if (this.childViewPager != null) {
                            this.childViewPager.requestDisallowInterceptTouchEvent(true);
                        }

                        this.scrollBy(0, (int)((double)deltaY + 0.5D));
                    }

                    this.mLastY = currentY;
                }
        }

        super.dispatchTouchEvent(ev);
        return true;
    }

    @TargetApi(14)
    private int getScrollerVelocity(int distance, int duration) {
        if (this.mScroller == null) {
            return 0;
        } else {
            return VERSION.SDK_INT >= 14 ? (int)this.mScroller.getCurrVelocity() : distance / duration;
        }
    }

    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            int currY = this.mScroller.getCurrY();
            int deltaY;
            int toY;
            if (this.mDirection == DIRECTION.UP) {
                if (this.isSticked()) {
                    deltaY = this.mScroller.getFinalY() - currY;
                    toY = this.calcDuration(this.mScroller.getDuration(), this.mScroller.timePassed());
                    this.mHelper.smoothScrollBy(this.getScrollerVelocity(deltaY, toY), deltaY, toY);
                    this.mScroller.forceFinished(true);
                    return;
                }

                this.scrollTo(0, currY);
                this.invalidate();
            } else {
                if (this.mHelper.isTop() || this.isClickHeadExpand) {
                    deltaY = currY - this.mLastScrollerY;
                    toY = this.getScrollY() + deltaY;
                    this.scrollTo(0, toY);
                    if (this.mCurY <= this.minY) {
                        this.mScroller.forceFinished(true);
                        return;
                    }
                }

                this.invalidate();
            }

            this.mLastScrollerY = currY;
        }

    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!this.isSticked()) {
            this.scrollBy(0, 0);
        } else {
            if (this.mCurY == 0) {
                return;
            }

            this.scrollTo(0, this.maxY);
        }

    }

    public void scrollBy(int x, int y) {
        int scrollY = this.getScrollY();
        int toY = scrollY + y;
        if (toY >= this.maxY) {
            toY = this.maxY;
        } else if (toY <= this.minY) {
            toY = this.minY;
        }

        y = toY - scrollY;
        super.scrollBy(x, y);
    }

    public void scrollTo(int x, int y) {
        if (y >= this.maxY) {
            y = this.maxY;
        } else if (y <= this.minY) {
            y = this.minY;
        }

        this.mCurY = y;
        super.scrollTo(x, y);
    }

    private void initOrResetVelocityTracker() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            this.mVelocityTracker.clear();
        }

    }

    private void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }

    }

    private void recycleVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }

    }

    private void checkIsClickHead(int downY, int headHeight, int scrollY) {
        this.isClickHead = downY + scrollY <= headHeight;
    }

    private void checkIsClickHeadExpand(int downY, int headHeight, int scrollY) {
        if (this.mExpandHeight <= 0) {
            this.isClickHeadExpand = false;
        }

        this.isClickHeadExpand = downY + scrollY <= headHeight + this.mExpandHeight;
    }

    private int calcDuration(int duration, int timepass) {
        return duration - timepass;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.mHeadView = this.getChildAt(0);
        this.measureChildWithMargins(this.mHeadView, widthMeasureSpec, 0, 0, 0);
        boolean isSticked = this.isSticked();
        boolean isHeadTop = this.isHeadTop();
        this.maxY = this.mHeadView.getMeasuredHeight() - this.topOffset;
        if (this.maxY < 0) {
            this.maxY = 0;
        }

        if (!isHeadTop && isSticked) {
            this.mCurY = this.maxY;
        }

        this.mHeadHeight = this.mHeadView.getMeasuredHeight();
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) + this.maxY, MeasureSpec.EXACTLY));
    }

    protected void onFinishInflate() {
        if (this.mHeadView != null && !this.mHeadView.isClickable()) {
            this.mHeadView.setClickable(true);
        }

        int childCount = this.getChildCount();

        for(int i = 0; i < childCount; ++i) {
            View child = this.getChildAt(i);
            if (child instanceof ViewPager) {
                this.childViewPager = (ViewPager)child;
            }
        }

        super.onFinishInflate();
    }

    public void setTopOffset(int topOffset) {
        this.topOffset = topOffset;
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (this.onScrollListener != null) {
            this.onScrollListener.onScroll(t, this.maxY);
        }

        mHandler.removeCallbacks(this.runnable);
        if (!this.isSticked()) {
            mHandler.postDelayed(this.runnable, 100L);
        }

    }

    public interface OnScrollListener {
        void onScroll(int var1, int var2);

        void onScrollStop();
    }

    static enum DIRECTION {
        UP,
        DOWN;

        private DIRECTION() {
        }
    }
}
