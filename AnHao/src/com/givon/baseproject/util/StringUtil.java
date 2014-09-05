/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @StringUtil.java  2014年3月25日 上午9:44:56 - Guzhu
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

package com.givon.baseproject.util;

import java.text.DecimalFormat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

public class StringUtil {

	public static boolean isEmpty(CharSequence text) {
		if (text == null || text.length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static String getDefultString(CharSequence text) {
		if (isEmpty(text)) {
			return "";
		} else {
			return text.toString().trim();
		}
	}
	/**
	 * 获取本地版本号
	 * @return
	 */
	public static int getLoaclVersion(Context context) {
		int localVersion =0;
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			localVersion = packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return localVersion;
	}
	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return verName;
	}
	/**
	 * 退出程序
	 * @param ctx
	 */
	//验证邮政编码  
	    public static boolean checkPost(String post){  
	        if(post.matches("[1-9]\\d{5}(?!\\d)")){  
	            return true;  
	       }else{  
	            return false;  
	        }  
	    }  
		public static boolean isNetworkAvailable(Context context) {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm == null) {
			} else {
				// 如果仅仅是用来判断网络连接 ,则可以使用 cm.getActiveNetworkInfo().isAvailable();
				NetworkInfo[] info = cm.getAllNetworkInfo();
				if (info != null) {
					for (int i = 0; i < info.length; i++) {
						if (info[i].getState() == NetworkInfo.State.CONNECTED) {
							return true;
						}
					}
				}
			}
			return false;
		}  
		public static String getFileName(String key) {
			if (key == null) {
				key = "";
			} else {
				if (key.contains("/")) {
					key = key.substring(key.lastIndexOf("/") + 1);
				}
			}
			return key;
		}
		
		public static String double2String(double d){
//			 DecimalFormat df0 = new DecimalFormat("###");
//			   DecimalFormat df1 = new DecimalFormat("###.0");
			
			   DecimalFormat df2 = new DecimalFormat("#.00");
			   String fot = df2.format(d);
			   if(d<1&&d>=0){
					fot = "0"+fot;
				}
			   
			   return fot;
			
		}
}
