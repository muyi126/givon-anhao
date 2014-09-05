/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @BaseFragment.java  2014年3月25日 上午9:32:18 - Guzhu
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

package com.givon.anhao;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.givon.baseproject.util.StringUtil;
import com.givon.baseproject.view.AppDialog;
import com.givon.baseproject.view.AppDialog.Builder;
import com.givon.baseproject.view.AppTitleBar;
import com.givon.baseproject.view.WaitingDialog;

public class BaseFragment extends Fragment {
	public AppTitleBar mTitleBar;
	private WaitingDialog mWaitingDialog;

	@Override
	public void onStart() {
		super.onStart();
		if (mTitleBar != null) {
			mTitleBar.setBackOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					getActivity().onBackPressed();
				}
			});
		}
	}

	/**
	 * 根据id获取string中的数据
	 * @param id
	 * @return
	 */
	protected String getStringValue(int id) {
		String value = AnhaoApplication.getInstance().getResources().getString(id);
		return StringUtil.isEmpty(value) ? "" : value;
	}

	protected void showActivity(Class<?> classz) {
		try {
			startActivity(new Intent(getActivity(), classz));
		} catch (Exception e) {
			Log.e("ss", e.getMessage());
		}

	}

	public void showWaitingDialog() {
		if (mWaitingDialog == null) {
			mWaitingDialog = new WaitingDialog(getActivity());
			// mWaitingDialog = new ProgressDialog(this);
			mWaitingDialog.setCanceledOnTouchOutside(false);
			// mWaitingDialog.setMessage(getString(R.string.action_waiting));
			mWaitingDialog.setCancelable(true);
		}
		if (!mWaitingDialog.isShowing()) {
			mWaitingDialog.show();
		}
	}

	public void showWaitingDialog(String msg) {
		if (mWaitingDialog == null) {
			mWaitingDialog = new WaitingDialog(getActivity());
			// mWaitingDialog = new ProgressDialog(this);
			mWaitingDialog.setCanceledOnTouchOutside(false);
			// mWaitingDialog.setMessage(getString(R.string.action_waiting));
			mWaitingDialog.setCancelable(true);
		}
		mWaitingDialog.setMessage(msg);
		if (!mWaitingDialog.isShowing()) {
			mWaitingDialog.show();
		}
	}

	public void dismissWaitingDialog() {
		if (mWaitingDialog != null && mWaitingDialog.isShowing()) {
			mWaitingDialog.dismiss();
		}
	}

	public void showDialogMessage(String text, DialogInterface.OnClickListener l) {
		Builder ibuilder = new AppDialog.Builder(getActivity());
		ibuilder.setMessage(text);
		ibuilder.setPositiveButton(R.string.confirm, l);
		ibuilder.create().show();
	}

	public void showDialogMessage(String text, DialogInterface.OnClickListener l,
			DialogInterface.OnClickListener listener) {
		Builder ibuilder = new AppDialog.Builder(getActivity());
		ibuilder.setMessage(text);
		ibuilder.setPositiveButton(R.string.confirm, l);
		ibuilder.setNegativeButton(R.string.cancel, listener);
		ibuilder.create().show();
	}

}
