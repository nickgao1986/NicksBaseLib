package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.base.reddots.R;


/**
 * Created by tangh on 14-4-1.
 */
public class LoadMoreLayout extends LinearLayout {

	private static final String TAG = LoadMoreLayout.class.getSimpleName();

	private String loadMore;

	private String loadingMore;

	private String loadNoData;

	private View mDivider;

	private TextView mTextView;

	private ProgressBar mProgressBar;

	private State state;

	public LoadMoreLayout(Context context) {
		super(context);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.widget_comm_load_more, this);
		mDivider = findViewById(R.id.divider);
		mTextView = (TextView) findViewById(R.id.text);
		mProgressBar = (ProgressBar) findViewById(R.id.progress);
		initStrings();
		setState(State.STATE_DEFAULT);
	}

	private void initStrings() {
		loadMore = getContext().getString(R.string.load_more);
		loadingMore = getContext().getString(R.string.loading_more);
		loadNoData = getContext().getString(R.string.load_more_no_data);
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
		switch (state) {
			case STATE_DEFAULT:
				mTextView.setVisibility(GONE);
				mProgressBar.setVisibility(GONE);
				break;
			case STATE_LOADMORE:
				mTextView.setText(loadMore);
				mTextView.setVisibility(VISIBLE);
				mProgressBar.setVisibility(GONE);
				break;
			case STATE_LOADING:
				mTextView.setText(loadingMore);
				mTextView.setVisibility(VISIBLE);
				mProgressBar.setVisibility(VISIBLE);
				break;
			case STATE_NO_DATA:
				mTextView.setText(loadNoData);
				mTextView.setVisibility(VISIBLE);
				mProgressBar.setVisibility(GONE);
				break;

		}
	}

	public void showDivider(boolean show) {
		mDivider.setVisibility(show ? VISIBLE : GONE);
	}

	public void setDivider(int resid) {
		mDivider.setBackgroundResource(resid);
	}

	public void setLoadNoData(String loadNoData) {
		this.loadNoData = loadNoData;
	}

	public enum State {
		STATE_LOADMORE,
		STATE_LOADING,
		STATE_NO_DATA,
		STATE_DEFAULT
	}

}
