/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @FileCache.java  2014-4-21 上午9:14:57 - Carson
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

package com.givon.baseproject.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.os.Environment;

public class FileCache {
	public static boolean saveObject(Context context,String key, Object obj) {
		boolean ret = false;
		if (null == obj) {
			return false;
		}
		FileOutputStream outStream = null;
		try {
			outStream = context.openFileOutput(key,
					Context.MODE_PRIVATE);
			ObjectOutputStream objectStream = new ObjectOutputStream(outStream);
			objectStream.writeObject(obj);
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

	public static Object getObject(Context context,String key) {
		Object obj = null;
		FileInputStream fin = null;
		try {
			fin = context.openFileInput(key);
			ObjectInputStream inStream = new ObjectInputStream(fin);
			obj = inStream.readObject();
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
		return obj;
	}
	
	public static String getSDPath(){
		  File sdDir = null;
		  boolean sdCardExist = Environment.getExternalStorageState()
		  .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
		  if (sdCardExist)
		  {
		  sdDir = Environment.getExternalStorageDirectory();//获取跟目录
		  }
		  return sdDir.toString();
		   
		 }
}
