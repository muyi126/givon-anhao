/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @AppTitleBar.java  2014年3月25日 上午9:35:03 - Guzhu
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

package com.givon.baseproject.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.givon.anhao.AnhaoApplication;
import com.givon.anhao.R;
import com.givon.baseproject.util.StringUtil;

public class AppTitleBar extends RelativeLayout {

	private RelativeLayout mTitleBar;
	private RelativeLayout mRightBar;
	private TextView mTitleBack;
	private TextView mTitleName;
	private ImageView mTitleIcon;
	private TextView mTitleMore;
//	private ImageView mLeftBackground;
	private ImageView mRightBackground;
	private View.OnClickListener mBackListener;

	public AppTitleBar(Context context) {
		super(context);
		initViewLayout(context);
	}

	public AppTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViewLayout(context);
	}

	public AppTitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViewLayout(context);
	}

	private void initViewLayout(Context context) {
		LayoutInflater.from(context).inflate(R.layout.layout_apptitle_bar, this, true);
		mTitleBar = (RelativeLayout) findViewById(R.id.id_titlebar);
		mRightBar = (RelativeLayout) findViewById(R.id.id_right_bar);
		mTitleBack = (TextView) findViewById(R.id.id_button_back);
		mTitleName = (TextView) findViewById(R.id.id_title_content);
		mTitleIcon = (ImageView) findViewById(R.id.id_title_image);
		mTitleMore = (TextView) findViewById(R.id.id_button_more);
//		mLeftBackground = (ImageView) findViewById(R.id.id_left_background);
		mRightBackground = (ImageView) findViewById(R.id.id_right_background);
	}

	public void setTitleOnClickListener(OnClickListener l) {
		mTitleBar.setOnClickListener(l);
		mTitleBar.setOnClickListener(l);
	}

	/**
	 * the left button's onclick listener
	 */
	public void setBackOnClickListener(OnClickListener l) {
		mBackListener = l;
		if(null!=mTitleBack){
			mTitleBack.setOnClickListener(l);
		}
//		mLeftBackground.setOnClickListener(l);
	}

	public OnClickListener getBackOnClickListener() {
		return mBackListener;
	}

	/**
	 * the right button's onclick listener
	 */
	public void setMoreOnClickListener(OnClickListener l) {
		// mTitleMore.setOnClickListener(l);
		mRightBar.setOnClickListener(l);
	}

	public void setBackImage(int resid) {
		if (mTitleBack != null) {
			mTitleBack.setText("");
			mTitleBack.setBackgroundResource(resid);
		}
	}

	public void setTitle(int strId) {
		String title = AnhaoApplication.getInstance().getResources().getString(strId);
		if (mTitleName != null) {
			mTitleName.setText(title);
			mTitleIcon.setVisibility(View.GONE);
		}
	}

	public void setTitle(CharSequence title) {
		if (mTitleName != null) {
			mTitleName.setText(title);
			mTitleName.setVisibility(View.VISIBLE);
			mTitleIcon.setVisibility(View.GONE);
		}
	}

	public void setTitleImage(int resid) {
		if (mTitleIcon != null) {
			mTitleIcon.setImageResource(resid);
			mTitleIcon.setVisibility(View.VISIBLE);
			mTitleName.setVisibility(View.GONE);
		}
	}

	public void setTitle(CharSequence title, int titleSize) {
		if (mTitleName != null) {
			mTitleName.setText(title);
			mTitleName.setTextSize(titleSize);
			mTitleName.setVisibility(View.VISIBLE);
			mTitleIcon.setVisibility(View.GONE);
		}
	}

	public void setTitleColor(int color) {
		if (mTitleName != null) {
			mTitleName.setTextColor(color);
		}
	}

	public void setMoreIcon(int resid) {
		if (mTitleMore != null) {
			mTitleMore.setVisibility(View.VISIBLE);
			mTitleMore.setBackgroundResource(resid);
			mTitleMore.setText("");
		}
	}

	public void setMoreText(CharSequence text) {
		if (mTitleMore != null) {
			mTitleMore.setVisibility(View.VISIBLE);
			if (!StringUtil.isEmpty(text)) {
				mTitleMore.setText(text);
			}
		}
	}

	public void setBarBackground(int color) {
		if (mTitleBar != null) {
			mTitleBar.setBackgroundColor(color);
		}
	}

	public void setBarBackgroundResources(int id) {
		if (mTitleBar != null) {
			mTitleBar.setBackgroundResource(id);
		}
	}

	public TextView getMoreTextView() {
		return mTitleMore;
	}

//	public void setLeftBackground(int resId) {
//		mLeftBackground.setImageResource(resId);
//	}

	public void setDismissmTitleBack() {
		mTitleBack.setVisibility(View.INVISIBLE);
//		if (null!=mLeftBackground) {
//			mLeftBackground.setOnClickListener(null);
//		}
//	    if (null!=mTitleBack) {
//	    	mTitleBack.setOnClickListener(null);
//		}
		setBackOnClickListener(null);
	}

	public void setRightBackground(int resId) {
		mRightBackground.setImageResource(resId);
	}
}
