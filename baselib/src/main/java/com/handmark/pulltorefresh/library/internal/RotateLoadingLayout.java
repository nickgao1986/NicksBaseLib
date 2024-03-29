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
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView.ScaleType;

import com.base.reddots.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;

@SuppressLint("ViewConstructor")
public class RotateLoadingLayout extends LoadingLayout {

	static final int ROTATION_ANIMATION_DURATION = 1400;

	private final Animation mRotateAnimation;
	private final Matrix mHeaderImageMatrix;

	private float mRotationPivotX, mRotationPivotY;
	private float mCenterPivotX, mCenterPivotY;

	public RotateLoadingLayout(Context context, Mode mode, Orientation scrollDirection, TypedArray attrs, boolean isSlide) {
		super(context, mode, scrollDirection, attrs, isSlide);

		mHeaderImage.setScaleType(ScaleType.MATRIX);
		mHeaderImageMatrix = new Matrix();
		mHeaderImage.setImageMatrix(mHeaderImageMatrix);
		if (mHeaderImageBody != null) {
			mHeaderImageBody.setImageDrawable(context.getResources().getDrawable(R.drawable.pull_to_refresh_body));
			mHeaderImageBody.setScaleType(ScaleType.MATRIX);
			mHeaderImageBody.setImageMatrix(mHeaderImageMatrix);
		}
		mRotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
		mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
		mRotateAnimation.setRepeatCount(Animation.INFINITE);
		mRotateAnimation.setRepeatMode(Animation.RESTART);
	}

	@Override
	protected void onPullSlideImpl(float scaleOfLayout) {
	}

	@Override
	protected void releaseToSlideImpl() {
		resetImageRotation();
	}

	@Override
	public void onPullDrawableSet(Drawable imageDrawable) {
		if (null != imageDrawable) {
			mCenterPivotX = Math.round(imageDrawable.getIntrinsicWidth() / 2f);
			mCenterPivotY = Math.round(imageDrawable.getIntrinsicHeight() / 2f);
			mRotationPivotX = mCenterPivotX;
			switch (mMode) {
				case PULL_FROM_END:
					mRotationPivotY = 0;
					break;
				case PULL_FROM_START:
				default:
					mRotationPivotY = Math.round(imageDrawable.getIntrinsicHeight());
					break;
			}
		}
	}

	@Override
	protected void onPullImpl(float scaleOfLayout) {
		setImageScale(scaleOfLayout);
		setTextAlpha(scaleOfLayout - 0.9f);
	}

	@Override
	protected void refreshingImpl() {
		mHeaderImage.startAnimation(mRotateAnimation);
		resetImageRotation();
	}

	@Override
	protected void resetImpl() {
		mHeaderImage.clearAnimation();
		setTextAlpha(0f);
		if (null != mHeaderImageMatrix) {
			mHeaderImageMatrix.reset();
		}
	}

	@Override
	protected void pullToRefreshImpl() {
		// NO-OP
	}

	@Override
	protected void releaseToRefreshImpl() {
		// NO-OP
		setImageScale(1f);
	}

	@Override
	protected int getDefaultDrawableResId() {
		return R.drawable.pull_to_refresh_a0;
	}

	private void resetImageRotation() {
		if (null != mHeaderImageMatrix) {
			mHeaderImageMatrix.reset();
			mHeaderImage.setImageMatrix(mHeaderImageMatrix);
			if (mHeaderImageBody != null) {
				mHeaderImageBody.setImageMatrix(mHeaderImageMatrix);
			}
		}
	}

	private void setImageScale(float scaleOfLayout) {

		mHeaderImageMatrix.reset();
		if (scaleOfLayout <= 1f) {
			mHeaderImageMatrix.postScale(scaleOfLayout, scaleOfLayout, mRotationPivotX, mRotationPivotY);
			if (mHeaderImageBody != null) {
				mHeaderImageBody.setImageMatrix(mHeaderImageMatrix);
			}
			mHeaderImageMatrix.postRotate(scaleOfLayout * 90f, mCenterPivotX, Math.abs(mRotationPivotY - mCenterPivotY * scaleOfLayout));
		} else {
			mHeaderImageMatrix.postRotate(scaleOfLayout * 90f, mCenterPivotX, mCenterPivotY);
		}
		mHeaderImage.setImageMatrix(mHeaderImageMatrix);
	}

	private void setTextAlpha(float alpha) {
		if (mSlideText != null) {
			if (alpha < 0f) {
				alpha = 0f;
			} else if (alpha > 0.85f) {
				alpha = 0.85f;
			}
			mSlideText.setAlpha(alpha);
		}
	}

}
