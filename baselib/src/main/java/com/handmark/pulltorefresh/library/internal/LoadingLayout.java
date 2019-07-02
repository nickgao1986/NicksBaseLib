/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.library.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.base.reddots.R;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;

@SuppressLint("ViewConstructor")
public abstract class LoadingLayout extends FrameLayout implements ILoadingLayout {

	static final String LOG_TAG = "PullToRefresh-LoadingLayout";

	static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();

	private View mInnerLayout;

	protected ImageView mHeaderImage;
	protected ImageView mHeaderImageBody;
	protected ProgressBar mHeaderProgress;

	private boolean mUseIntrinsicAnimation;

	protected Mode mMode;
	protected Orientation mScrollDirection;

	protected boolean isSlideLayout;
	protected FrameLayout mRefreshBody;
	protected TextView mSlideText;

	public LoadingLayout(Context context, final Mode mode, final Orientation scrollDirection, TypedArray attrs, boolean isSlide) {
		super(context);
		init(context, mode, scrollDirection, attrs, isSlide);
	}

	protected void init(Context context, final Mode mode, final Orientation scrollDirection, TypedArray attrs, boolean isSlide) {
		this.mMode = mode;
		this.mScrollDirection = scrollDirection;
		this.isSlideLayout = isSlideLayout(isSlide, mode, scrollDirection);

		switch (scrollDirection) {
			case HORIZONTAL:
				LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header_horizontal, this);
				break;
			case VERTICAL:
			default:
				if (isSlideLayout) {
					LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_slide_header_vertical, this);
				} else {
					LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header_vertical, this);
				}
				break;
		}

		mInnerLayout = findViewById(R.id.fl_inner);
		mHeaderProgress = (ProgressBar) mInnerLayout.findViewById(R.id.pull_to_refresh_progress);
		mHeaderImage = (ImageView) mInnerLayout.findViewById(R.id.pull_to_refresh_image);
		mHeaderImageBody = (ImageView) mInnerLayout.findViewById(R.id.pull_to_refresh_body);

		if (isSlideLayout) {
			mRefreshBody = (FrameLayout) mInnerLayout.findViewById(R.id.refresh_header_body);
			mSlideText = (TextView) mInnerLayout.findViewById(R.id.refresh_slide_down_text);
		}
		LayoutParams lp = (LayoutParams) mInnerLayout.getLayoutParams();

		switch (mode) {
			case PULL_FROM_END:
				lp.gravity = scrollDirection == Orientation.VERTICAL ? Gravity.TOP : Gravity.LEFT;
				break;
			case PULL_FROM_START:
			default:
				lp.gravity = scrollDirection == Orientation.VERTICAL ? Gravity.BOTTOM : Gravity.RIGHT;
				break;
		}

		if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderBackground)) {
			Drawable background = attrs.getDrawable(R.styleable.PullToRefresh_ptrHeaderBackground);
			if (null != background) {
				ViewCompat.setBackground(this, background);
			}
		}else{
            setBackgroundColor(getResources().getColor(R.color.color_f5f5f5));
        }

		if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderTextAppearance)) {
			TypedValue styleID = new TypedValue();
			attrs.getValue(R.styleable.PullToRefresh_ptrHeaderTextAppearance, styleID);
			setTextAppearance(styleID.data);
		}
		if (attrs.hasValue(R.styleable.PullToRefresh_ptrSubHeaderTextAppearance)) {
			TypedValue styleID = new TypedValue();
			attrs.getValue(R.styleable.PullToRefresh_ptrSubHeaderTextAppearance, styleID);
			setSubTextAppearance(styleID.data);
		}

		// Text Color attrs need to be set after TextAppearance attrs
		if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderTextColor)) {
			ColorStateList colors = attrs.getColorStateList(R.styleable.PullToRefresh_ptrHeaderTextColor);
			if (null != colors) {
				setTextColor(colors);
			}
		}
		if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderSubTextColor)) {
			ColorStateList colors = attrs.getColorStateList(R.styleable.PullToRefresh_ptrHeaderSubTextColor);
			if (null != colors) {
				setSubTextColor(colors);
			}
		}

		if (isSlideLayout) {
			if (attrs.hasValue(R.styleable.PullToRefresh_ptrSlideText)) {
				String slideText = attrs.getString(R.styleable.PullToRefresh_ptrSlideText);
				if (null != slideText) {
					setSlideText(slideText);
				}
			}
		}

		// Try and get defined drawable from Attrs
		Drawable pullDrawable = null;
		if (attrs.hasValue(R.styleable.PullToRefresh_ptrPullDrawable)) {
			pullDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrPullDrawable);
		}

		Drawable loadingDrawable = null;
		if (attrs.hasValue(R.styleable.PullToRefresh_ptrLoadingDrawable)) {
			loadingDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrLoadingDrawable);
		}

		// Check Specific Drawable from Attrs, these overrite the generic
		// drawable attr above
		switch (mode) {
		case PULL_FROM_START:
		default:
			if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableStart)) {
				pullDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableStart);
			}
			break;

		case PULL_FROM_END:
			if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableEnd)) {
				pullDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableEnd);
			}
			break;
		}

		// If we don't have a user defined drawable, load the default
		if (null == pullDrawable) {
			pullDrawable = context.getResources().getDrawable(getDefaultDrawableResId());
		}

        //  If we don't have a user defined drawable, do not set
        if (null != loadingDrawable) {
            setLoadingDrawable(loadingDrawable);
        }

		// Set Drawable, and save width/height
		setPullDrawable(pullDrawable);

		reset();
	}

	public void setHeight(int height) {
		ViewGroup.LayoutParams lp = getLayoutParams();
		lp.height = height;
		requestLayout();
	}

	public void setWidth(int width) {
		ViewGroup.LayoutParams lp = getLayoutParams();
		lp.width = width;
		requestLayout();
	}

	public int getContentSize() {
		switch (mScrollDirection) {
		case HORIZONTAL:
			return mInnerLayout.getWidth();
		case VERTICAL:
		default:
			return mInnerLayout.getHeight();
		}
	}

	public void hideAllViews() {
		if (View.VISIBLE == mHeaderProgress.getVisibility()) {
			mHeaderProgress.setVisibility(View.INVISIBLE);
		}
		if (View.VISIBLE == mHeaderImage.getVisibility()) {
			mHeaderImage.setVisibility(View.INVISIBLE);
		}
		if (View.VISIBLE == mHeaderImageBody.getVisibility()) {
			mHeaderImageBody.setVisibility(View.INVISIBLE);
		}
	}

	public void onPull(float scaleOfLayout) {
		if (!mUseIntrinsicAnimation) {
			onPullImpl(scaleOfLayout);
		}
	}

	public void pullToRefresh() {
		if (isSlideLayout && mSlideText != null) {
			mSlideText.setVisibility(INVISIBLE);
		}

		// Now call the callback
		pullToRefreshImpl();
	}

	public void refreshing() {
		if (isSlideLayout && mSlideText != null) {
			mSlideText.setVisibility(INVISIBLE);
		}
		if (mUseIntrinsicAnimation) {
			((AnimationDrawable) mHeaderImage.getDrawable()).start();
		} else {
			// Now call the callback
			refreshingImpl();
		}
	}

	public void releaseToRefresh() {
		if (isSlideLayout && mSlideText != null) {
			mSlideText.setVisibility(VISIBLE);
		}
		// Now call the callback
		releaseToRefreshImpl();
	}

	public void reset() {
		mHeaderImage.setVisibility(View.VISIBLE);
		mHeaderImageBody.setVisibility(View.VISIBLE);

		if (mUseIntrinsicAnimation) {
			((AnimationDrawable) mHeaderImage.getDrawable()).stop();
		} else {
			// Now call the callback
			resetImpl();
		}
	}

	public void setPullDrawable(Drawable imageDrawable) {
		// Set Drawable
		mHeaderImage.setImageDrawable(imageDrawable);
		mUseIntrinsicAnimation = (imageDrawable instanceof AnimationDrawable);

		// Now call the callback
		onPullDrawableSet(imageDrawable);
	}

	public void setLoadingDrawable(Drawable drawable) {
		mHeaderProgress.setIndeterminateDrawable(drawable);
	}

	public void setPullLabel(CharSequence pullLabel) {
	}

	public void setRefreshingLabel(CharSequence refreshingLabel) {
	}

	public void setReleaseLabel(CharSequence releaseLabel) {
	}

	@Override
	public void setLastUpdatedLabel(CharSequence label) {
		setSubHeaderText(label);
	}

	@Override
	public void setTextTypeface(Typeface tf) {
	}

	@Override
	public void setTextSize(float size) {
	}

	@Override
	public void setTextColor(ColorStateList color) {
	}

	public void setSubHeaderText(CharSequence label) {
	}

	public void setSubTextAppearance(int value) {
	}

	public void setSubTextColor(ColorStateList color) {
	}

	public void setTextAppearance(int value) {
	}

	public void showInvisibleViews() {
		if (View.INVISIBLE == mHeaderProgress.getVisibility()) {
			mHeaderProgress.setVisibility(View.VISIBLE);
		}
		if (View.INVISIBLE == mHeaderImage.getVisibility()) {
			mHeaderImage.setVisibility(View.VISIBLE);
		}
		if (View.INVISIBLE == mHeaderImageBody.getVisibility()) {
			mHeaderImageBody.setVisibility(View.VISIBLE);
		}
	}
	protected boolean isSlideLayout(boolean isSlide, final Mode mode, final Orientation scrollDirection) {
		return isSlide && mode == PullToRefreshBase.Mode.PULL_FROM_START && scrollDirection == PullToRefreshBase.Orientation.VERTICAL;
	}

	public void releaseToSlide() {
		if (isSlideLayout) {
			// do something
			releaseToSlideImpl();
		}
	}

	public int getRefreshHeight() {
		if (isSlideLayout && mRefreshBody != null) {
			return mRefreshBody.getHeight();
		} else {
			return getContentSize();
		}
	}

	public void onPullSlide(float scaleOfLayout) {
		if (isSlideLayout && !mUseIntrinsicAnimation) {
			onPullSlideImpl(scaleOfLayout);
		}
	}

	public void setSlideText(String test) {
		if (null != mSlideText) {
			mSlideText.setText(test);
		}
	}

	public void setPullAnimationEnabled(boolean animation) {
		// TODO NO OP
	}

	/**
	 * Callbacks for derivative Layouts
	 */
	protected abstract void onPullSlideImpl(float scaleOfLayout);

	protected abstract void releaseToSlideImpl();

	protected abstract int getDefaultDrawableResId();

	protected abstract void onPullDrawableSet(Drawable imageDrawable);

	protected abstract void onPullImpl(float scaleOfLayout);

	protected abstract void pullToRefreshImpl();

	protected abstract void refreshingImpl();

	protected abstract void releaseToRefreshImpl();

	protected abstract void resetImpl();

}
