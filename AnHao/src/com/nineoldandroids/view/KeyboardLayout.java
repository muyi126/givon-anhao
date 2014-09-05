/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @KeyboardLayout.java  2014年8月9日 下午9:55:09 - Guzhu
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

package com.nineoldandroids.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RelativeLayout;

public class KeyboardLayout extends RelativeLayout {
	private static final String TAG = KeyboardLayout.class.getSimpleName();
	public static final byte KEYBOARD_STATE_SHOW = -3;
	public static final byte KEYBOARD_STATE_HIDE = -2;
	public static final byte KEYBOARD_STATE_INIT = -1;
	private boolean mHasInit;
	private boolean mHasKeybord;
	private int mHeight;
	private onKybdsChangeListener mListener;

	public KeyboardLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public KeyboardLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public KeyboardLayout(Context context) {
		super(context);
	}

	/**
	 * set keyboard state listener
	 */
	public void setOnkbdStateListener(onKybdsChangeListener listener) {
		mListener = listener;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (!mHasInit) {
			mHasInit = true;
			mHeight = b;
			if (mListener != null) {
				mListener.onKeyBoardStateChange(KEYBOARD_STATE_INIT,b);
			}
		} else {
			mHeight = mHeight < b ? b : mHeight;
		}
		if (mHasInit && mHeight > b) {
			mHasKeybord = true;
			if (mListener != null) {
				mListener.onKeyBoardStateChange(KEYBOARD_STATE_SHOW,b);
			}
			Log.w(TAG, "show keyboard.......");
		}
		if (mHasInit && mHasKeybord && mHeight == b) {
			mHasKeybord = false;
			if (mListener != null) {
				mListener.onKeyBoardStateChange(KEYBOARD_STATE_HIDE,b);
			}
			Log.w(TAG, "hide keyboard.......");
		}
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		System.out.println("dispatchKeyEvent:"+event.getAction()+" "+KeyEvent.KEYCODE_BACK+"  "+KeyEvent.KEYCODE_CLEAR);
		return super.dispatchKeyEvent(event);
	}

	public interface onKybdsChangeListener {
		public void onKeyBoardStateChange(int state,int height);
	}
}