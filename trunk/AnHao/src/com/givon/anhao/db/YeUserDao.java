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
package com.givon.anhao.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.easemob.util.HanziToPinyin;
import com.givon.anhao.Constant;
import com.givon.baseproject.entity.UserBean;
import com.givon.baseproject.util.StringUtil;

public class YeUserDao {
	public static final String TABLE_NAME = "yeuers";
	public static final String COLUMN_NAME_ID = "username";
	public static final String COLUMN_NAME_USERID = "userId";
	public static final String COLUMN_NAME_NICK = "nick";
	public static final String COLUMN_NAME_FREETIME = "freeTime";
	public static final String COLUMN_NAME_PASSWORD = "password";
	public static final String COLUMN_NAME_CREATETIME = "createTime";
	public static final String COLUMN_NAME_EASEMOBID = "easemobId";
	public static final String COLUMN_NAME_EASEMOBPASSWORD = "easemobPassword";
	public static final String COLUMN_NAME_TOKEN = "token";
	public static final String COLUMN_NAME_AVATAR = "avatar";
	public static final String COLUMN_NAME_UNREADMSGCOUNT = "unreadMsgCount";
	public static final String COLUMN_NAME_HEADER = "header";

	private DbOpenHelper dbHelper;

	public YeUserDao(Context context) {
		dbHelper = DbOpenHelper.getInstance(context);
	}

	/**
	 * 保存好友list
	 * 
	 * @param contactList
	 */
	public void saveContactList(List<UserBean> contactList) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.delete(TABLE_NAME, null, null);
			for (UserBean user : contactList) {
				ContentValues values = new ContentValues();
				if(StringUtil.isEmpty(user.getEasemobId())){
					return ;
				}
				values.put(COLUMN_NAME_EASEMOBID, user.getEasemobId());
				if (!StringUtil.isEmpty(user.getNick())){
					values.put(COLUMN_NAME_NICK, user.getNick());
				}
				if(!StringUtil.isEmpty(user.getAvatar())){
					values.put(COLUMN_NAME_AVATAR, user.getAvatar());
				}
				if(!StringUtil.isEmpty(user.getEasemobPassword())){
					values.put(COLUMN_NAME_EASEMOBPASSWORD, user.getEasemobPassword());
				}
				if(!StringUtil.isEmpty(user.getFreeTime())){
					values.put(COLUMN_NAME_FREETIME, user.getFreeTime());
				}
				if(!StringUtil.isEmpty(user.getHeader())){
					values.put(COLUMN_NAME_HEADER, user.getHeader());
				}
				if(!StringUtil.isEmpty(user.getNickname())){
					values.put(COLUMN_NAME_NICK, user.getNickname());
				}
				if(!StringUtil.isEmpty(user.getPassword())){
					values.put(COLUMN_NAME_PASSWORD, user.getPassword());
				}
				if(!StringUtil.isEmpty(user.getToken())){
					values.put(COLUMN_NAME_TOKEN, user.getToken());
				}
				if(!StringUtil.isEmpty(user.getUserId())){
					values.put(COLUMN_NAME_USERID, user.getUserId());
				}
				if(!StringUtil.isEmpty(user.getUsername())){
					values.put(COLUMN_NAME_ID, user.getUsername());
				}
				db.insert(TABLE_NAME, null, values);
			}
		}
	}

	/**
	 * 获取好友list
	 * 
	 * @return
	 */
	public Map<String, UserBean> getContactList() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Map<String, UserBean> users = new HashMap<String, UserBean>();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME /* + " desc" */, null);
			while (cursor.moveToNext()) {
				String emId = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_EASEMOBID));
				String nick = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NICK));
				UserBean user = new UserBean();
				user.setUsername(emId);
				user.setNick(nick);
				String headerName = null;
				if (!TextUtils.isEmpty(user.getNick())) {
					headerName = user.getNick();
				} else {
					headerName = user.getUsername();
				}

				if (emId.equals(Constant.NEW_FRIENDS_USERNAME)
						|| emId.equals(Constant.GROUP_USERNAME)) {
					user.setHeader("");
				} else if (Character.isDigit(headerName.charAt(0))) {
					user.setHeader("#");
				} else {
					user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1))
							.get(0).target.substring(0, 1).toUpperCase());
					char header = user.getHeader().toLowerCase().charAt(0);
					if (header < 'a' || header > 'z') {
						user.setHeader("#");
					}
				}
				users.put(emId, user);
			}
			cursor.close();
		}
		return users;
	}

	/**
	 * 删除一个联系人
	 * @param username
	 */
	public void deleteContact(String emId) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.delete(TABLE_NAME, COLUMN_NAME_EASEMOBID + " = ?", new String[] { emId });
		}
	}

	/**
	 * 保存一个联系人
	 * @param user
	 */
	public void saveContact(UserBean user) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		if(StringUtil.isEmpty(user.getEasemobId())){
			return;
		}
		values.put(COLUMN_NAME_EASEMOBID, user.getEasemobId());
		if (!StringUtil.isEmpty(user.getNick())){
			values.put(COLUMN_NAME_NICK, user.getNick());
		}
		if(!StringUtil.isEmpty(user.getAvatar())){
			values.put(COLUMN_NAME_AVATAR, user.getAvatar());
		}
		if(!StringUtil.isEmpty(user.getEasemobPassword())){
			values.put(COLUMN_NAME_EASEMOBPASSWORD, user.getEasemobPassword());
		}
		if(!StringUtil.isEmpty(user.getFreeTime())){
			values.put(COLUMN_NAME_FREETIME, user.getFreeTime());
		}
		if(!StringUtil.isEmpty(user.getHeader())){
			values.put(COLUMN_NAME_HEADER, user.getHeader());
		}
		if(!StringUtil.isEmpty(user.getNickname())){
			values.put(COLUMN_NAME_NICK, user.getNickname());
		}
		if(!StringUtil.isEmpty(user.getPassword())){
			values.put(COLUMN_NAME_PASSWORD, user.getPassword());
		}
		if(!StringUtil.isEmpty(user.getToken())){
			values.put(COLUMN_NAME_TOKEN, user.getToken());
		}
		if(!StringUtil.isEmpty(user.getUserId())){
			values.put(COLUMN_NAME_USERID, user.getUserId());
		}
		if(!StringUtil.isEmpty(user.getUsername())){
			values.put(COLUMN_NAME_ID, user.getUsername());
		}
		if (db.isOpen()) {
			db.insert(TABLE_NAME, null, values);
		}
	}
/**
 * 判断数据库是否有数据
 * @return
 */
	public boolean hasData() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME /* + " desc" */, null);
			if (cursor.moveToNext()) {
				cursor.close();
				return true;
			} else {
				cursor.close();
				return false;
			}
		}
		return false;
	}
	
	public boolean hasEmId(String emId){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			String table = TABLE_NAME;  
			String[] columns = new String[] {COLUMN_NAME_EASEMOBID, COLUMN_NAME_NICK};  
			String selection = COLUMN_NAME_EASEMOBID;  
			String[] selectionArgs = new String[]{emId};  
			String groupBy = COLUMN_NAME_EASEMOBID;  
			String having = null;  
			String orderBy = null;  
			Cursor a = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
			if(a.moveToNext()){
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
		
	}

}
