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
import android.graphics.drawable.Drawable;
import android.view.View;

import com.base.reddots.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;

@SuppressLint("ViewConstructor")
public class FlipLoadingLayout extends LoadingLayout {

	public FlipLoadingLayout(Context context, final Mode mode, final Orientation scrollDirection, TypedArray attrs, boolean isSlide) {
		super(context, mode, scrollDirection, attrs, isSlide);
		if (mHeaderImageBody != null) {
			mHeaderImageBody.setImageDrawable(context.getResources().getDrawable(R.drawable.pull_to_refresh_body));
		}
	}

	@Override
	protected void onPullDrawableSet(Drawable imageDrawable) {
	}

	@Override
	protected void onPullImpl(float scaleOfLayout) {
		// NO-OP
	}

	@Override
	protected void pullToRefreshImpl() {
	}

	@Override
	protected void refreshingImpl() {
		mHeaderImage.clearAnimation();
		mHeaderImage.setVisibility(View.INVISIBLE);
		mHeaderProgress.setVisibility(View.VISIBLE);
	}

	@Override
	protected void releaseToRefreshImpl() {
	}

	@Override
	protected void resetImpl() {
		mHeaderImage.clearAnimation();
		mHeaderProgress.setVisibility(View.GONE);
		mHeaderImage.setVisibility(View.VISIBLE);
	}

	@Override
	protected int getDefaultDrawableResId() {
		return R.drawable.pull_to_refresh_a0;
	}

	@Override
	protected void onPullSlideImpl(float scaleOfLayout) {
	}

	@Override
	protected void releaseToSlideImpl() {
	}

}
