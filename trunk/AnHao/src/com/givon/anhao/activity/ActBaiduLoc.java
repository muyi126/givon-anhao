/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @ActBaiduLoc.java  2014年9月1日 上午10:03:25 - Guzhu
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

package com.givon.anhao.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.givon.anhao.AnhaoApplication;
import com.givon.anhao.BaseActivity;
import com.givon.anhao.R;

public class ActBaiduLoc extends BaseActivity {

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;

	MapView mMapView;
	BaiduMap mBaiduMap;
	Button sendButton = null;
	static BDLocation lastLocation = null;

	// UI相关
	OnCheckedChangeListener radioButtonListener;
	// Button requestLocButton;
	boolean isFirstLoc = true;// 是否首次定位

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baidumap);
		// requestLocButton = (Button) findViewById(R.id.button1);
		// mCurrentMode = LocationMode.NORMAL;
		sendButton = (Button) findViewById(R.id.btn_location_send);
		sendButton.setEnabled(false);
		mCurrentMode = LocationMode.FOLLOWING;
		
		// requestLocButton.setText("普通");
		// OnClickListener btnClickListener = new OnClickListener() {
		// public void onClick(View v) {
		// switch (mCurrentMode) {
		// case NORMAL:
		// requestLocButton.setText("跟随");
		// mCurrentMode = LocationMode.FOLLOWING;
		// mBaiduMap
		// .setMyLocationConfigeration(new MyLocationConfiguration(
		// mCurrentMode, true, mCurrentMarker));
		// break;
		// case COMPASS:
		// requestLocButton.setText("普通");
		// mCurrentMode = LocationMode.NORMAL;
		// mBaiduMap
		// .setMyLocationConfigeration(new MyLocationConfiguration(
		// mCurrentMode, true, mCurrentMarker));
		// break;
		// case FOLLOWING:
		// requestLocButton.setText("罗盘");
		// mCurrentMode = LocationMode.COMPASS;
		// mBaiduMap
		// .setMyLocationConfigeration(new MyLocationConfiguration(
		// mCurrentMode, true, mCurrentMarker));
		// break;
		// }
		// }
		// };
		// requestLocButton.setOnClickListener(btnClickListener);

		// RadioGroup group = (RadioGroup) this.findViewById(R.id.radioGroup);
		// radioButtonListener = new OnCheckedChangeListener() {
		// @Override
		// public void onCheckedChanged(RadioGroup group, int checkedId) {
		// if (checkedId == R.id.defaulticon) {
		// // 传入null则，恢复默认图标
		// mCurrentMarker = null;
		// mBaiduMap
		// .setMyLocationConfigeration(new MyLocationConfiguration(
		// mCurrentMode, true, null));
		// }
		// if (checkedId == R.id.customicon) {
		// // 修改为自定义marker
		// mCurrentMarker = BitmapDescriptorFactory
		// .fromResource(R.drawable.icon001);
		// mBaiduMap
		// .setMyLocationConfigeration(new MyLocationConfiguration(
		// mCurrentMode, true, mCurrentMarker));
		// }
		// }
		// };
		// group.setOnCheckedChangeListener(radioButtonListener);

		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true,
				mCurrentMarker));
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		
		Intent intent = getIntent();
		if (intent.hasExtra("latitude")) {
			// showMap

			sendButton.setVisibility(View.GONE);
			// MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(17);
			// mBaiduMap.animateMapStatus(u);
			double latitude = intent.getDoubleExtra("latitude", 0);
			double longtitude = intent.getDoubleExtra("longitude", 0);
			String address = intent.getStringExtra("address");
			// showMap(latitude, longtitude, address);
			initMap(latitude, longtitude);
		} else {
				sendButton.setVisibility(View.VISIBLE);
				initMap(AnhaoApplication.getLatitude(), AnhaoApplication.getLongtitude());
//				LocationClientOption option = new LocationClientOption();
//				option.setIsNeedAddress(true);
//				option.setOpenGps(true);// 打开gps
//				option.setCoorType("bd09ll"); // 设置坐标类型
//				option.setScanSpan(1000);
//				mLocClient.setLocOption(option);
//				mLocClient.start();
			
		}
	}
	
	
	private void initMap(double latitude,double longtitude){
		MyLocationData locData = new MyLocationData.Builder().accuracy(0)
				// 此处设置开发者获取到的方向信息，顺时针0-360
						.direction(100).latitude(latitude).longitude(longtitude).build();
				mBaiduMap.setMyLocationData(locData);
				if (isFirstLoc) {
					isFirstLoc = false;
					LatLng ll = new LatLng(latitude, longtitude);
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
					MapStatusUpdate u1 = MapStatusUpdateFactory.zoomTo(17);
					mBaiduMap.animateMapStatus(u);
					mBaiduMap.animateMapStatus(u1);
					sendButton.setEnabled(true);
				}
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			lastLocation = location;
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				MapStatusUpdate u1 = MapStatusUpdateFactory.zoomTo(17);
				mBaiduMap.animateMapStatus(u);
				mBaiduMap.animateMapStatus(u1);
				sendButton.setEnabled(true);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	public void sendLocation(View view) {
		Intent intent = this.getIntent();
		intent.putExtra("latitude", AnhaoApplication.getLatitude());
		intent.putExtra("longitude", AnhaoApplication.getLongtitude());
		intent.putExtra("address", AnhaoApplication.getAddrStr());
		this.setResult(RESULT_OK, intent);
		finish();
		overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
	}

}
