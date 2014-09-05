/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.givon.anhao;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.support.httpclient.HttpClientAsync;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.easemob.chat.ConnectionListener;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.OnNotificationClickListener;
import com.givon.anhao.activity.ChatActivity;
import com.givon.anhao.activity.MainActivity;
import com.givon.anhao.db.DbOpenHelper;
import com.givon.anhao.db.UserDao;
import com.givon.anhao.utils.PreferenceUtils;
import com.givon.baseproject.entity.UserBean;
import com.givon.baseproject.util.ShareCookie;
import com.givon.baseproject.util.StringUtil;
import com.umeng.analytics.MobclickAgent;

public class AnhaoApplication extends Application {

	public static Context applicationContext;
	private static AnhaoApplication instance;
	// login user name
	public final String PREF_USERNAME = "username";
	private String userName = null;
	// login password
	private static final String PREF_PWD = "pwd";
	private String password = null;
	private Map<String, UserBean> contactList;
	private static Typeface typeface;
	private LocationManager locationManager;
	private LocationListener locationListener;
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;
	private static BDLocation lastLocation = null;
	private static double latitude = 30;
	private static double longtitude = 104;

	public static Typeface getTypeface() {
		return typeface;
	}

	public static void setTypeface(Typeface typeface) {
		AnhaoApplication.typeface = typeface;
	}

	public static String getCityAddress() {
		if (null != lastLocation) {
			if (!StringUtil.isEmpty(lastLocation.getCity())) {
				return lastLocation.getCity();
			} else {
				return "";
			}
		}
		return "";
	}
	public static String getAddrStr() {
		if (null != lastLocation) {
			if (!StringUtil.isEmpty(lastLocation.getAddrStr())) {
				return lastLocation.getAddrStr();
			} else {
				return "";
			}
		}
		return "";
	}
	
	

	public static double getLatitude() {
		return latitude;
	}

	public static void setLatitude(double latitude) {
		AnhaoApplication.latitude = latitude;
	}

	public static double getLongtitude() {
		return longtitude;
	}

	public static void setLongtitude(double longtitude) {
		AnhaoApplication.longtitude = longtitude;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		typeface = Typeface.createFromAsset(getAssets(), "font/thisfont.otf");
		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
		initLoacation();
		HttpClientAsync.getInstance().setPrintLog(true);
		// 如果使用到百度地图或者类似启动remote service的第三方库，这个if判断不能少
		if (processAppName == null || processAppName.equals("")) {
			// workaround for baidu location sdk
			// 百度定位sdk，定位服务运行在一个单独的进程，每次定位服务启动的时候，都会调用application::onCreate
			// 创建新的进程。
			// 但环信的sdk只需要在主进程中初始化一次。 这个特殊处理是，如果从pid 找不到对应的processInfo
			// processName，
			// 则此application::onCreate 是被service 调用的，直接返回
			return;
		}
		applicationContext = this;
		instance = this;
		// 初始化环信SDK,一定要先调用init()
		Log.d("EMChat Demo", "initialize EMChat SDK");
		EMChat.getInstance().init(applicationContext);
		// debugmode设为true后，就能看到sdk打印的log了
		EMChat.getInstance().setDebugMode(true);

		// 获取到EMChatOptions对象
		EMChatOptions options = EMChatManager.getInstance().getChatOptions();
		// 默认添加好友时，是不需要验证的，改成需要验证
		options.setAcceptInvitationAlways(false);
		// 设置收到消息是否有新消息通知，默认为true
		options.setNotificationEnable(PreferenceUtils.getInstance(applicationContext)
				.getSettingMsgNotification());
		// 设置收到消息是否有声音提示，默认为true
		options.setNoticeBySound(PreferenceUtils.getInstance(applicationContext)
				.getSettingMsgSound());
		// 设置收到消息是否震动 默认为true
		options.setNoticedByVibrate(PreferenceUtils.getInstance(applicationContext)
				.getSettingMsgVibrate());
		// 设置语音消息播放是否设置为扬声器播放 默认为true
		options.setUseSpeaker(PreferenceUtils.getInstance(applicationContext)
				.getSettingMsgSpeaker());

		// 设置notification消息点击时，跳转的intent为自定义的intent
		options.setOnNotificationClickListener(new OnNotificationClickListener() {

			@Override
			public Intent onNotificationClick(EMMessage message) {
				Intent intent = new Intent(applicationContext, ChatActivity.class);
				ChatType chatType = message.getChatType();
				if (chatType == ChatType.Chat) { // 单聊信息
					intent.putExtra("userId", message.getFrom());
					intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
				} else { // 群聊信息
							// message.getTo()为群聊id
					intent.putExtra("groupId", message.getTo());
					intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
				}
				return intent;
			}
		});
		// 设置一个connectionlistener监听账户重复登陆
		EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
		// 取消注释，app在后台，有新消息来时，状态栏的消息提示换成自己写的
		// options.setNotifyText(new OnMessageNotifyListener() {
		//
		// @Override
		// public String onNewMessageNotify(EMMessage message) {
		// //可以根据message的类型提示不同文字，demo简单的覆盖了原来的提示
		// return "你的好基友" + message.getFrom() + "发来了一条消息哦";
		// }
		//
		// @Override
		// public String onLatestMessageNotify(EMMessage message, int fromUsersNum, int messageNum) {
		// return fromUsersNum + "个基友，发来了" + messageNum + "条消息";
		// }
		// });

		MobclickAgent.onError(applicationContext);
	}

	public static AnhaoApplication getInstance() {
		return instance;
	}

	// List<String> list = new ArrayList<String>();
	// list.add("1406713081205");
	// options.setReceiveNotNoifyGroup(list);
	/**
	 * 获取内存中好友user list
	 * 
	 * @return
	 */
	public Map<String, UserBean> getContactList() {
		if (getUserName() != null && contactList == null) {
			UserDao dao = new UserDao(applicationContext);
			// 获取本地好友user list到内存,方便以后获取好友list
			contactList = dao.getContactList();
		}
		return contactList;
	}

	/**
	 * 设置好友user list到内存中
	 * 
	 * @param contactList
	 */
	public void setContactList(Map<String, UserBean> contactList) {
		this.contactList = contactList;
	}

	/**
	 * 获取当前登陆用户名
	 * 
	 * @return
	 */
	public String getUserName() {
		if (userName == null) {
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(applicationContext);
			userName = preferences.getString(PREF_USERNAME, null);
		}
		return userName;
	}

	/**
	 * 获取密码
	 * 
	 * @return
	 */
	public String getPassword() {
		if (password == null) {
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(applicationContext);
			password = preferences.getString(PREF_PWD, null);
		}
		return password;
	}

	/**
	 * 设置用户名
	 * 
	 * @param user
	 */
	public void setUserName(String username) {
		if (username != null) {
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(applicationContext);
			SharedPreferences.Editor editor = preferences.edit();
			if (editor.putString(PREF_USERNAME, username).commit()) {
				userName = username;
			}
		}
	}

	/**
	 * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk 内部的自动登录需要的密码，已经加密存储了
	 * @param pwd
	 */
	public void setPassword(String pwd) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(applicationContext);
		SharedPreferences.Editor editor = preferences.edit();
		if (editor.putString(PREF_PWD, pwd).commit()) {
			password = pwd;
		}
	}

	/**
	 * 退出登录,清空数据
	 */
	public void logout() {
		// 先调用sdk logout，在清理app中自己的数据
		EMChatManager.getInstance().logout();
		DbOpenHelper.getInstance(applicationContext).closeDB();
		// reset password to null
		setPassword(null);
		setContactList(null);
		ShareCookie.setLoginAuth(false);

	}

	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
					.next());
			try {
				if (info.pid == pID) {
					CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName,
							PackageManager.GET_META_DATA));
					// Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
					// info.processName +"  Label: "+c.toString());
					// processName = c.toString();
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
				// Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}

	class MyConnectionListener implements ConnectionListener {
		@Override
		public void onReConnecting() {
		}

		@Override
		public void onReConnected() {
		}

		@Override
		public void onDisConnected(String errorString) {
			if (errorString != null && errorString.contains("conflict")) {
				Intent intent = new Intent(applicationContext, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("conflict", true);
				startActivity(intent);
			}

		}

		@Override
		public void onConnecting(String progress) {

		}

		@Override
		public void onConnected() {
		}
	}

	/**
	 * 定位
	 */
	private void initLoacation() {
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setIsNeedAddress(true);
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		// locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		// String provider = LocationManager.GPS_PROVIDER;// 获得provider
		// locationListener = new LocationListener() {
		// public void onLocationChanged(Location location) {
		// if (null != location) {
		// latitude = location.getLatitude();
		// longitude = location.getLongitude();
		// }
		// Log.e("onLocationChanged", location.getLatitude() + ",," + location.getLongitude());
		//
		// }
		//
		// public void onStatusChanged(String provider, int status, Bundle extras) {
		// }
		//
		// public void onProviderEnabled(String provider) {
		// }
		//
		// public void onProviderDisabled(String provider) {
		// }
		// };
		//
		// if (locationManager.isProviderEnabled(provider)) {
		// Log.i("gps", "gps enabled");
		// Location location = locationManager.getLastKnownLocation(provider);
		// if (null != location) {
		// latitude = location.getLatitude();
		// longitude = location.getLongitude();
		// }
		//
		// locationManager.requestLocationUpdates(provider, 2000, 0, locationListener);
		// } else {
		// /**
		// * 基站定位
		// * */
		// Log.i("network", "network enabled");
		// String networkProvider = LocationManager.NETWORK_PROVIDER;
		// Log.i("network", networkProvider);
		// Location networkLocation = locationManager.getLastKnownLocation(networkProvider);
		// if (null != networkLocation) {
		// latitude = networkLocation.getLatitude();
		// longitude = networkLocation.getLongitude();
		// }
		// locationManager.requestLocationUpdates(networkProvider, 2000, 0, locationListener);
		// }
	}

	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			System.out.println("lastLocation");
			if (location == null) {
				return;
			} else {
				LocationClientOption opt = mLocClient.getLocOption();
				opt.setScanSpan(30000);
			}
			lastLocation = location;
			latitude = location.getLatitude();
			longtitude = location.getLongitude();
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
