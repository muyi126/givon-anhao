/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @SlideMenuItem.java  2014-2-20 下午2:50:48 - Carson
 * @author YanXu
 * @email:981385016@qq.com
 * @version 1.0
 */

package com.givon.baseproject.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.givon.anhao.R;

public class SlideMenuItem extends LinearLayout {

	private ImageView mIcon;
	private TextView mTitle;

	public SlideMenuItem(Context context) {
		super(context);
		initViews(context);
	}

	public SlideMenuItem(Context context, int icon, int title) {
		super(context);
		initViews(context);
		mIcon.setImageResource(icon);
		mTitle.setText(title);
	}

	public SlideMenuItem(Context context, int icon, String title) {
		super(context);
		initViews(context);
		mIcon.setImageResource(icon);
		mTitle.setText(title);
	}

	private void initViews(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.slidemenu_item, this);
		mIcon = (ImageView) findViewById(R.id.iv_icon);
		mTitle = (TextView) findViewById(R.id.tv_title);
	}

	public void setIcon(int icon) {
		mIcon.setImageResource(icon);
	}

	public void setTitle(int title) {
		mTitle.setText(title);
	}

	public void setTitle(String title) {
		mTitle.setText(title);
	}
}
