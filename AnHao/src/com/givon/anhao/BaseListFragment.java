/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @BaseListFragment.java  2014年3月25日 上午10:03:26 - Guzhu
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

package com.givon.anhao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.givon.baseproject.entity.Constant;
import com.givon.baseproject.util.BitmapHelp;
import com.givon.baseproject.util.StringUtil;
import com.givon.baseproject.view.AppTitleBar;
import com.givon.baseproject.view.PullListView;
import com.lidroid.xutils.BitmapUtils;

public abstract class BaseListFragment<T> extends BaseFragment {
	protected List<T> mListData = new ArrayList<T>();
	protected PullListView mListView;
	protected ListAdapter mAdapter;
	protected int mPageIndex = 1;
	protected BitmapUtils bitmapUtils;
	protected TextView mErrorInfo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View v = inflater.inflate(R.layout.layout_listview, container, false);
		bitmapUtils = BitmapHelp.getBitmapUtils(getActivity());
		bitmapUtils.configDefaultLoadingImage(R.drawable.bg_me_top);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.bg_me_top);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		mAdapter = new ListAdapter(getActivity());
		mTitleBar = (AppTitleBar) v.findViewById(R.id.id_titlebar);
		mListView = (PullListView) v.findViewById(R.id.id_pull_listview);
		mErrorInfo = (TextView) v.findViewById(R.id.tv_info);
		mListView.setHeaderDividersEnabled(false);
		mListView.setFooterDividersEnabled(false);
		mListView.setSelector(android.R.color.transparent);
		if (null != addHeaderViewCallBack) {
			addHeaderViewCallBack.addHeaderView();
		}
		mListView.setAdapter(mAdapter);
		return v;
	}

	protected abstract View getItemView(int position, View convertView, ViewGroup parent);
	protected abstract int getViewType(int position);
	protected abstract int getTypeCount();

	public class ListAdapter extends SimpleAdapter {

		public ListAdapter(Context context) {
			super(context, null, 0, null, null);
		}

		public void clearAdapter() {
			if (mListData != null) {
				mListData.clear();
				this.notifyDataSetChanged();
			}

		}

		public void putData(List<T> data) {
			if (mPageIndex == 1) {
				mListData.clear();
			}
			if (null != data) {
				mListData.addAll(data);
				if (data.size() < Constant.PAGE_SIZE) {
					mListView.setPullLoadEnable(false);
				} else {
					mListView.setPullLoadEnable(true);
				}
			}
			mAdapter.notifyDataSetChanged();
		}

		public void putDoMainData(List<T> data) {
			if (mPageIndex == 0) {
				mListData.clear();
			}
			if (null != data) {
				mListData.addAll(data);
				if (data.size() < Constant.PAGE_SIZE) {
					mListView.setPullLoadEnable(false);
				} else {
					mListView.setPullLoadEnable(true);
				}
			}
			mAdapter.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mListData.size();
		}

		@Override
		public T getItem(int position) {
			if (position > -1 && position < mListData.size()) {
				return mListData.get(position);
			}
			return null;
		}

		@Override
		public boolean isEnabled(int position) {
			return true;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getItemView(position, convertView, parent);
		}
		
		@Override
		public int getViewTypeCount() {
			return getTypeCount();
		}
		
		@Override
		public int getItemViewType(int position) {
			return getViewType(position);
		}
	}

	public TextView getmErrorInfo() {
		return mErrorInfo;
	}

	public void setErrorInfo(String mErrorInfo) {
		if (null != this.mErrorInfo && !StringUtil.isEmpty(mErrorInfo)) {
			mListView.setVisibility(View.GONE);
			this.mErrorInfo.setVisibility(View.VISIBLE);
			this.mErrorInfo.setText(mErrorInfo);
		} else {
			this.mErrorInfo.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
		}
	}

	public interface AddHeaderViewCallBack {
		public void addHeaderView();
	}

	public AddHeaderViewCallBack addHeaderViewCallBack;

	public void setHeaderView(AddHeaderViewCallBack addHeaderViewCallBack) {
		this.addHeaderViewCallBack = addHeaderViewCallBack;
	}
}
