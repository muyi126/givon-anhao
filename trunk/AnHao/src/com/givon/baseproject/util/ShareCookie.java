/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @ShareCookie.java  2014年4月1日 下午7:46:02 - Guzhu
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

package com.givon.baseproject.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.givon.anhao.AnhaoApplication;
import com.givon.baseproject.entity.LoginBean;

public class ShareCookie {

	private static final String SATEL_LITE_MENU_IS_OPEN = "SATEL_LITE_MENU_IS_OPEN";
	private final static String USER_INFO = "user_infos";
	private final static String TOKEN_INFO = "token_infos";
	private final static String BACKGROUND_COLOR = "back_color";
	private static String mProductContent;

	public static String getmProductContent() {
		return mProductContent;
	}

	public static void setmProductContent(String mProductContent) {
		ShareCookie.mProductContent = mProductContent;
	}

	public static boolean isLoginAuth() {
		SharedPreferences cookie = AnhaoApplication.getInstance().getSharedPreferences(
				Constants.COOKIE_FILE, Context.MODE_PRIVATE);
		return cookie.getBoolean(Constants.IS_LOGIN_AUTH, false) && (getUserInfo() != null);
	}

	public static void setLoginAuth(boolean login) {
		SharedPreferences cookie = AnhaoApplication.getInstance().getSharedPreferences(
				Constants.COOKIE_FILE, Context.MODE_PRIVATE);
		Editor editor = cookie.edit();
		editor.putBoolean(Constants.IS_LOGIN_AUTH, login);
		editor.commit();
	}

	public static void clearUserInfo() {
		AnhaoApplication.getInstance().deleteFile(USER_INFO);
		setLoginAuth(false);
	}

	public static boolean saveUserInfo(LoginBean entity) {
		boolean ret = false;
		if (null == entity) {
			return false;
		}
		FileOutputStream outStream = null;
		try {
			outStream = AnhaoApplication.getInstance().openFileOutput(USER_INFO,
					Context.MODE_PRIVATE);
			ObjectOutputStream objectStream = new ObjectOutputStream(outStream);
			objectStream.writeObject(entity);
			ret = true;
		} catch (IOException e) {
			ret = false;
		}
		if (null != outStream) {
			try {
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	public static LoginBean getUserInfo() {
		LoginBean entity = null;
		FileInputStream fin = null;
		try {
			fin = AnhaoApplication.getInstance().openFileInput(USER_INFO);
			ObjectInputStream inStream = new ObjectInputStream(fin);
			entity = (LoginBean) inStream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (null != fin) {
			try {
				fin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (entity == null) {
			ShareCookie.setLoginAuth(false);
		}
		return entity;

	}

	/**
	 * 暗号ID
	 * @return
	 */
	public static String getUserAnId() {
		LoginBean bean = getUserInfo();
		if (bean != null) {
			return String.valueOf(bean.getUserId());
		}
		return "0";
	}

	/**
	 * 环信ID
	 * @return
	 */
	public static String getUserId() {
		LoginBean bean = getUserInfo();
		if (bean != null) {
			return String.valueOf(bean.getEasemobId());
		}
		return "0";
	}

	public static boolean isAutoLogin() {
		SharedPreferences cookie = AnhaoApplication.getInstance().getSharedPreferences(
				Constants.COOKIE_FILE, Context.MODE_PRIVATE);
		return cookie.getBoolean(Constants.AUTO_LOGIN, false);
	}

	public static boolean setAutoLogin(boolean bool) {
		SharedPreferences cookie = AnhaoApplication.getInstance().getSharedPreferences(
				Constants.COOKIE_FILE, Context.MODE_PRIVATE);
		Editor editor = cookie.edit();
		editor.putBoolean(Constants.AUTO_LOGIN, bool);
		return editor.commit();
	}

	public static String getUserName() {
		return getCookie(Constants.USER_NAME);
	}

	public static boolean setUserName(String value) {
		return setCookie(Constants.USER_NAME, value);
	}

	private static boolean setCookie(String key, String value) {
		if (key != null) {
			SharedPreferences cookie = AnhaoApplication.getInstance().getSharedPreferences(
					Constants.COOKIE_FILE, Context.MODE_PRIVATE);
			Editor editor = cookie.edit();
			editor.putString(key, value);
			return editor.commit();
		}
		return false;
	}

	private static String getCookie(String key) {
		if (key != null) {
			SharedPreferences cookie = AnhaoApplication.getInstance().getSharedPreferences(
					Constants.COOKIE_FILE, Context.MODE_PRIVATE);
			return cookie.getString(key, "");
		}
		return "";
	}

}
