/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @BaseActivity.java  2014年3月25日 上午9:31:21 - Guzhu
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

package com.givon.anhao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.easemob.chat.EMChatManager;
import com.givon.baseproject.entity.Constant;
import com.givon.baseproject.view.AppDialog;
import com.givon.baseproject.view.AppDialog.Builder;
import com.givon.baseproject.view.AppTitleBar;
import com.givon.baseproject.view.WaitingDialog;

public abstract class BaseActivity extends FragmentActivity {

	private BroadcastReceiver mBroadcastReceiver;
	public AppTitleBar mTitleBar;
	private WaitingDialog mWaitingDialog;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (mBroadcastReceiver == null) {
			mBroadcastReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					finish();
				}
			};
			IntentFilter filter = new IntentFilter();
			filter.addAction(getApplicationContext().getPackageName() + Constant.ACTION_EXIT_SYSTEM);
			this.registerReceiver(mBroadcastReceiver, filter);
		}
		initLayoutView();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (mTitleBar != null && mTitleBar.getBackOnClickListener() == null) {
			mTitleBar.setBackOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onBackPressed();
				}
			});
		}
	}

	protected void initLayoutView() {
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mBroadcastReceiver != null) {
			this.unregisterReceiver(mBroadcastReceiver);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// onresume时，取消notification显示
		EMChatManager.getInstance().activityResumed();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}

	public void showActivity(Class<?> classz, boolean finish) {
		startActivity(new Intent(this, classz));
		if (finish) {
			BaseActivity.this.finish();
		}
	}

	public void showWaitingDialog() {
		if (mWaitingDialog == null) {
			mWaitingDialog = new WaitingDialog(this);
			// mWaitingDialog = new ProgressDialog(this);
			mWaitingDialog.setCanceledOnTouchOutside(false);
			// mWaitingDialog.setMessage(getString(R.string.action_waiting));
			mWaitingDialog.setCancelable(true);
		}
		if (!this.isFinishing() && !mWaitingDialog.isShowing()) {
			mWaitingDialog.show();
		}
	}

	public void showWaitingDialog(String msg) {
		if (mWaitingDialog == null) {
			mWaitingDialog = new WaitingDialog(this);
			// mWaitingDialog = new ProgressDialog(this);
			mWaitingDialog.setCanceledOnTouchOutside(false);
			// mWaitingDialog.setMessage(getString(R.string.action_waiting));
			mWaitingDialog.setCancelable(true);
		}
		mWaitingDialog.setMessage(msg);
		if (!this.isFinishing() && !mWaitingDialog.isShowing()) {
			mWaitingDialog.show();
		}
	}

	public void dismissWaitingDialog() {
		if (mWaitingDialog != null && mWaitingDialog.isShowing() && !this.isFinishing()) {
			mWaitingDialog.dismiss();
		}
	}
	
	public boolean isShowingDialog(){
		if (mWaitingDialog != null && mWaitingDialog.isShowing() && !this.isFinishing()) {
			return true;
		}else {
			return false;
		}
		
	}

	/**
	 * 根据id获取string中的数据
	 * 
	 * @param id
	 * @return
	 */
	protected String getStringValue(int id) {
		return getResources().getString(id);
	}

	protected Drawable getDrawableId(int id) {
		return getResources().getDrawable(id);
	}

	public void showDialogMessage(String text) {

		Builder ibuilder = new AppDialog.Builder(this);
		ibuilder.setMessage(text);
		ibuilder.setPositiveButton(R.string.know, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		ibuilder.create().show();
	}

	public void showDialogMessage(int textRes) {
		Builder ibuilder = new AppDialog.Builder(this);
		ibuilder.setMessage(textRes);
		ibuilder.setPositiveButton(R.string.know, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		ibuilder.create().show();
	}

	public void showDialogMessage(String text, DialogInterface.OnClickListener l) {
		Builder ibuilder = new AppDialog.Builder(this);
		ibuilder.setMessage(text);
		ibuilder.setPositiveButton(R.string.confirm, l);
		ibuilder.create().show();
	}

	public void showDialogMessage(String text, DialogInterface.OnClickListener l,
			DialogInterface.OnClickListener listener) {

		Builder ibuilder = new AppDialog.Builder(this);
		ibuilder.setMessage(text);
		ibuilder.setPositiveButton(R.string.confirm, l);
		ibuilder.setNegativeButton(R.string.cancel, listener);
		ibuilder.create().show();
	}
}