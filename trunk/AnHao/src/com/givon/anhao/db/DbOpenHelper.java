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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.givon.anhao.AnhaoApplication;

public class DbOpenHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static DbOpenHelper instance;

	private static final String USERNAME_TABLE_CREATE = "CREATE TABLE " + UserDaoOld.TABLE_NAME
			+ " (" + UserDaoOld.COLUMN_NAME_NICK + " TEXT, " + UserDaoOld.COLUMN_USER_TYPE
			+ " TEXT, " + UserDaoOld.COLUMN_NAME_ID + " TEXT PRIMARY KEY);";
	private static final String HELLOW_USERNAME_TABLE_CREATE = "CREATE TABLE "
			+ HelloUserDao.TABLE_NAME + " (" + HelloUserDao.COLUMN_NAME_NICK + " TEXT, "
			+ UserDaoOld.COLUMN_USER_TYPE + " TEXT, " + HelloUserDao.COLUMN_NAME_ID
			+ " TEXT PRIMARY KEY);";
	// private static final String USERNAME_TABLE_CREATE = "CREATE TABLE "
	// + UserDao.TABLE_NAME + " ("
	// + UserDao.COLUMN_NAME_NICK +" TEXT, "
	// + UserDao.COLUMN_NAME_USERID +" TEXT, "
	// + UserDao.COLUMN_NAME_FREETIME +" TEXT, "
	// + UserDao.COLUMN_NAME_PASSWORD +" TEXT, "
	// + UserDao.COLUMN_NAME_CREATETIME +" TEXT, "
	// + UserDao.COLUMN_NAME_ID +" TEXT, "
	// + UserDao.COLUMN_NAME_EASEMOBPASSWORD +" TEXT, "
	// + UserDao.COLUMN_NAME_TOKEN +" TEXT, "
	// + UserDao.COLUMN_NAME_AVATAR +" TEXT, "
	// + UserDao.COLUMN_NAME_UNREADMSGCOUNT +" TEXT, "
	// + UserDao.COLUMN_NAME_HEADER +" TEXT, "
	// + UserDao.COLUMN_NAME_EASEMOBID + " TEXT PRIMARY KEY);";
	private static final String YE_USERNAME_TABLE_CREATE = "CREATE TABLE " + YeUserDao.TABLE_NAME
			+ " (" + YeUserDao.COLUMN_NAME_NICK + " TEXT, " + YeUserDao.COLUMN_NAME_USERID
			+ " TEXT, " + YeUserDao.COLUMN_NAME_FREETIME + " TEXT, "
			+ YeUserDao.COLUMN_NAME_PASSWORD + " TEXT, " + YeUserDao.COLUMN_NAME_CREATETIME
			+ " TEXT, " + YeUserDao.COLUMN_NAME_ID + " TEXT, "
			+ YeUserDao.COLUMN_NAME_EASEMOBPASSWORD + " TEXT, " + YeUserDao.COLUMN_NAME_TOKEN
			+ " TEXT, " + YeUserDao.COLUMN_NAME_AVATAR + " TEXT, "
			+ YeUserDao.COLUMN_NAME_UNREADMSGCOUNT + " TEXT, " + YeUserDao.COLUMN_NAME_HEADER
			+ " TEXT, " + YeUserDao.COLUMN_NAME_EASEMOBID + " TEXT PRIMARY KEY);";

	private static final String INIVTE_MESSAGE_TABLE_CREATE = "CREATE TABLE "
			+ InviteMessgeDao.TABLE_NAME + " (" + InviteMessgeDao.COLUMN_NAME_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + InviteMessgeDao.COLUMN_NAME_FROM + " TEXT, "
			+ InviteMessgeDao.COLUMN_NAME_GROUP_ID + " TEXT, "
			+ InviteMessgeDao.COLUMN_NAME_GROUP_Name + " TEXT, "
			+ InviteMessgeDao.COLUMN_NAME_REASON + " TEXT, " + InviteMessgeDao.COLUMN_NAME_STATUS
			+ " INTEGER, " + InviteMessgeDao.COLUMN_NAME_ISINVITEFROMME + " INTEGER, "
			+ InviteMessgeDao.COLUMN_NAME_TIME + " TEXT); ";

	private DbOpenHelper(Context context) {
		super(context, getUserDatabaseName(), null, DATABASE_VERSION);
	}

	public static DbOpenHelper getInstance(Context context) {
		if (instance == null) {
			instance = new DbOpenHelper(context.getApplicationContext());
		}
		return instance;
	}

	private static String getUserDatabaseName() {
		return AnhaoApplication.getInstance().getUserName() + "_demo.db";
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(USERNAME_TABLE_CREATE);
		db.execSQL(YE_USERNAME_TABLE_CREATE);
		db.execSQL(INIVTE_MESSAGE_TABLE_CREATE);
		db.execSQL(HELLOW_USERNAME_TABLE_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void closeDB() {
		if (instance != null) {
			try {
				SQLiteDatabase db = instance.getWritableDatabase();
				db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			instance = null;
		}
	}

}
