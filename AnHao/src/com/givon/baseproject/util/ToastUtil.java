/* 
 * Copyright 2013 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @ToastUtil.java  2013-12-11 ����5:11:52 - Carson
 * @author YanXu
 * @email:981385016@qq.com
 * @version 1.0
 */

package com.givon.baseproject.util;

import android.widget.Toast;

import com.givon.anhao.AnhaoApplication;
import com.givon.anhao.R;

public class ToastUtil {

	public static void showMessage(int msg) {
		Toast toast = Toast.makeText(AnhaoApplication.getInstance(), msg, Toast.LENGTH_SHORT);
		toast.getView().setBackgroundResource(R.drawable.base_tip_bg);
		toast.getView().setPadding(45, 25, 45, 25);
		toast.show();
	}

	public static void showMessage(CharSequence text) {
		Toast toast = Toast.makeText(AnhaoApplication.getInstance(), text, Toast.LENGTH_SHORT);
		toast.getView().setBackgroundResource(R.drawable.base_tip_bg);
		toast.getView().setPadding(45, 25, 45, 25);
		toast.show();
	}

}