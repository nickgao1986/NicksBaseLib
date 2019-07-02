package com.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class ClipView extends View {
	private int mClipWidth = 0;
	private int mClipHeight = 0;
	private Bitmap mRectBitmap;
	private Paint mEmptyPaint = new Paint();

	public ClipView(Context context) {
		super(context);
	}

	public ClipView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ClipView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		refreshRectBitmap();
	}

	private void refreshRectBitmap() {
		Bitmap recBitmap = mRectBitmap;
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Paint transparentPaint;
		mRectBitmap = Bitmap.createBitmap(getWidth(), getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas temp = new Canvas(mRectBitmap);
		RectF clipRect = getClipRect();
		paint.setARGB(125, 50, 50, 50);
		temp.drawRect(0, 0, temp.getWidth(), temp.getHeight(), paint);
		transparentPaint = new Paint();
		transparentPaint.setAntiAlias(true);
		transparentPaint.setColor(getResources().getColor(
				android.R.color.transparent));
		transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		temp.drawCircle((clipRect.left + clipRect.right) / 2,
				(clipRect.top + clipRect.bottom) / 2,
				(clipRect.right - clipRect.left) / 2, transparentPaint);
		if (recBitmap != null) {
			recBitmap.recycle();
			recBitmap = null;
		}
	}

	public RectF getClipRect() {
		RectF result = new RectF();
		int width = this.getWidth();
		int height = this.getHeight();
		if (mClipWidth != 0 && mClipHeight != 0) {
			int x = ((width - mClipWidth) / 2);
			int y = ((height - mClipHeight) / 2);
			if (x > 0 && y > 0) {
				result.set(x, y, x + mClipWidth, y + mClipHeight);
			}
		}
		return result;

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mRectBitmap != null) {
			canvas.drawBitmap(mRectBitmap, 0, 0, mEmptyPaint);
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mRectBitmap != null) {
			mRectBitmap.recycle();
		}
	}

	public void setSize(int clipViewWidth, int clipViewHeight) {
		mClipWidth = clipViewWidth;
		mClipHeight = clipViewHeight;
	}
}
