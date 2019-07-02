package com.handmark.pulltorefresh.library;

import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

public interface ILoadingLayout {

	/**
	 * Set the Last Updated Text. This displayed under the main label when
	 * Pulling
	 * 
	 * @param label
	 *            - Label to set
	 */
	void setLastUpdatedLabel(CharSequence label);

	/**
	 * Set the loading drawable used in the loading layout. This is the same as
	 * calling <code>setLoadingDrawable(drawable, Mode.BOTH)</code>
	 * 
	 * @param drawable
	 *            - Drawable to display
	 */
	void setLoadingDrawable(Drawable drawable);

	/**
	 * Set the pull drawable used in the loading layout. This is the same as
	 * calling <code>setPullDrawable(drawable, Mode.BOTH)</code>
	 * 
	 * @param drawable
	 *            - Drawable to display
	 */
	void setPullDrawable(Drawable drawable);

	/**
	 * Set Text to show when the Widget is being Pulled
	 * <code>setPullLabel(releaseLabel, Mode.BOTH)</code>
	 * 
	 * @param pullLabel
	 *            - CharSequence to display
	 */
	void setPullLabel(CharSequence pullLabel);

	/**
	 * Set Text to show when the Widget is refreshing
	 * <code>setRefreshingLabel(releaseLabel, Mode.BOTH)</code>
	 * 
	 * @param refreshingLabel
	 *            - CharSequence to display
	 */
	void setRefreshingLabel(CharSequence refreshingLabel);

	/**
	 * Set Text to show when the Widget is being pulled, and will refresh when
	 * released. This is the same as calling
	 * <code>setReleaseLabel(releaseLabel, Mode.BOTH)</code>
	 * 
	 * @param releaseLabel
	 *            - CharSequence to display
	 */
	void setReleaseLabel(CharSequence releaseLabel);

	/**
	 * Set's the Sets the typeface and style in which the text should be
	 * displayed. Please see
	 * {@link android.widget.TextView#setTypeface(Typeface)
	 * TextView#setTypeface(Typeface)}.
	 */

	void setSubHeaderText(CharSequence label);
	
	void setSubTextAppearance(int value);

	void setSubTextColor(ColorStateList color);

	void setTextTypeface(Typeface tf);

	void setTextAppearance(int value);

	void setTextSize(float size);

	void setTextColor(ColorStateList color);

}
