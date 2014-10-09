/* 
 * Copyright 2013 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @PullListView.java  2013-12-11 9:14:41 - Carson
 * @author YanXu
 * @email:981385016@qq.com
 * @version 1.0
 */

package com.givon.baseproject.view;

import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.givon.anhao.R;


public class PullListView extends ListView implements OnScrollListener {

	private final static int SCROLLBACK_HEADER = 0;
	private final static int SCROLLBACK_FOOTER = 1;

	private final static int SCROLL_DURATION = 500;
	private final static int PULL_LOAD_MORE_DELTA = 50;
	private final static float OFFSET_RADIO = 1.6f;

	private int mTotalItemCount;
	private int mScrollBack;
	private float mLastX = -1;
	private float mLastY = -1;
	private Scroller mScroller;
	private OnScrollListener mScrollListener;
	private PullListViewListener mListViewListener;

	private PullListViewHeader mHeaderView;
	private RelativeLayout mHeaderViewContent;
	private TextView mHeaderTimeView;
	private int mHeaderViewHeight;
	private boolean mEnablePullRefresh = true;
	private boolean mPullRefreshing;

	private PullListViewFooter mFooterView;
	private boolean mEnablePullLoad;
	private boolean mPullLoading;
	private boolean mIsFooterReady;
	private SlideMenu mSlideMenu;

	public PullListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context);
	}
	
	

	public SlideMenu getmSlideMenu() {
		return mSlideMenu;
	}



	public void setmSlideMenu(SlideMenu mSlideMenu) {
		this.mSlideMenu = mSlideMenu;
	}



	public PullListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}

	public PullListView(Context context) {
		super(context);
		initWithContext(context);
	}

	private void initWithContext(Context context) {
		this.setCacheColorHint(android.R.color.transparent);
		mScroller = new Scroller(context, new DecelerateInterpolator());
		super.setOnScrollListener(this);

		mHeaderView = new PullListViewHeader(context);
		mHeaderViewContent = (RelativeLayout) mHeaderView.findViewById(R.id.pull_listview_content);
		mHeaderTimeView = (TextView) mHeaderView.findViewById(R.id.listview_header_time);
		mHeaderTimeView.setText(new Date().toLocaleString());
		this.addHeaderView(mHeaderView);
		this.setCacheColorHint(this.getResources().getColor(R.color.transparent));
		mFooterView = new PullListViewFooter(context);
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				mHeaderViewHeight = mHeaderViewContent.getHeight();
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
		resetHeaderHeight();
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		if (mIsFooterReady == false) {
			mIsFooterReady = true;
			addFooterView(mFooterView);
		}
		if (!mEnablePullLoad) {
			mFooterView.hide();
			mFooterView.setOnClickListener(null);
		}
		super.setAdapter(adapter);
	}

	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		if (mEnablePullRefresh) {
			mHeaderViewContent.setVisibility(View.VISIBLE);
		} else {
			mHeaderViewContent.setVisibility(View.INVISIBLE);
		}
	}

	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;
		if (mEnablePullLoad) {
			mPullLoading = false;
			mFooterView.show();
			mFooterView.setState(PullListViewFooter.STATE_NORMAL);
			mFooterView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startLoadMore();
				}
			});
		} else {
			mFooterView.hide();
			mFooterView.setOnClickListener(null);
		}
	}

	public void onRefreshFinish() {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
		mHeaderTimeView.setText(new Date().toLocaleString());
	}

	public void onLoadFinish() {
		if (mPullLoading == true) {
			mPullLoading = false;
			mFooterView.setState(PullListViewFooter.STATE_NORMAL);
		}
	}

	public void setRefreshTime(String time) {
		mHeaderTimeView.setText(time);
	}

	private void invokeOnScrolling() {
		if (mScrollListener instanceof OnXScrollListener) {
			OnXScrollListener l = (OnXScrollListener) mScrollListener;
			l.onXScrolling(this);
		}
	}

	private void updateHeaderHeight(float delta) {
		mHeaderView.setVisiableHeight((int) delta + mHeaderView.getVisiableHeight());
		if (mEnablePullRefresh && !mPullRefreshing) {
			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
				mHeaderView.setState(PullListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(PullListViewHeader.STATE_NORMAL);
			}
		}
		setSelection(0);
	}

	private void resetHeaderHeight() {
		int height = mHeaderView.getVisiableHeight();
		if (height == 0) {
			height = mHeaderViewHeight;
		}
		// refreshing and header isn't shown fully. do nothing.
		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}
		int finalHeight = 0; // default: scroll back to dismiss header.
		// is refreshing, just scroll back to show all the header.
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
		invalidate();
	}

	private void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) {
				mFooterView.setState(PullListViewFooter.STATE_READY);
			} else {
				mFooterView.setState(PullListViewFooter.STATE_NORMAL);
			}
		}
		mFooterView.setBottomMargin(height);
	}

	private void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);
			invalidate();
		}
	}

	private void startLoadMore() {
		if (mPullLoading) {
			return;
		}
		mPullLoading = true;
		mFooterView.setState(PullListViewFooter.STATE_LOADING);
		if (mListViewListener != null) {
			mListViewListener.onPullLoadMore();
		}
	}

	public void startOnRefresh() {
		mPullRefreshing = true;
		mHeaderView.setState(PullListViewHeader.STATE_REFRESHING);
		if (mListViewListener != null) {
			mListViewListener.onPullRefresh();
		}
	}

	private float mLastMotionX;
	private float mLastMotionY;
	private boolean flip = true;;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(null!=mSlideMenu){
			if(mSlideMenu.isOpened()){
				mSlideMenu.closeMenu();
				return false;
			}else {
				return super.dispatchTouchEvent(ev);
			}
		}
		return super.dispatchTouchEvent(ev);
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}
		final float x = ev.getX();
		final float y = ev.getY();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastX = ev.getRawX();
			mLastY = ev.getRawY();
			mLastMotionX = x;
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			final int xDiff = (int) Math.abs(x - mLastMotionX);
			final int yDiff = (int) Math.abs(y - mLastMotionY);
			if(xDiff>yDiff){
				flip = false;
			}else{
				flip = true;
			}

			final float deltaY = ev.getRawY() - mLastY;
			mLastY = ev.getRawY();
			if (getFirstVisiblePosition() == 0
					&& (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
				// the first item is showing, header has shown or pull down.
				updateHeaderHeight(deltaY / OFFSET_RADIO);

				invokeOnScrolling();

 			} else if (getLastVisiblePosition() == mTotalItemCount - 1
					&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
				// last item, already pulled up or want to pull up.
				updateFooterHeight(-deltaY / OFFSET_RADIO);
			}
			break;
		default:
			mLastY = -1; // reset
			if (getFirstVisiblePosition() == 0) {
				// invoke refresh
				if (mEnablePullRefresh && mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
					startOnRefresh();
				}
				if (mHeaderView.getVisiableHeight() > 0) {
					resetHeaderHeight();
				}
			}
			if (getLastVisiblePosition() == mTotalItemCount - 1) {
				if (mEnablePullLoad && mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
					startLoadMore();
				}
				resetFooterHeight();
			}
			break;

		}
		return super.onTouchEvent(ev)&&flip;
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
			} else {
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
			int totalItemCount) {
		// send to user's listener
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}

	public void setPullListener(PullListViewListener l) {
		mListViewListener = l;
	}

	/**
	 * you can listen ListView.OnScrollListener or this one. it will invoke onXScrolling when header/footer scroll back.
	 */
	public interface OnXScrollListener extends OnScrollListener {
		public void onXScrolling(View view);
	}

	/**
	 * implements this interface to get refresh/load more event.
	 */
	public interface PullListViewListener {
		public void onPullRefresh();

		public void onPullLoadMore();
	}
}
