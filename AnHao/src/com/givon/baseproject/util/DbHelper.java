package com.givon.baseproject.util;
/* 
 * Copyright 2014 Guzhu.Ltd  All rights reserved.
 * SiChuan Guzhu.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @DbHelper.java  2014-2-27 ����10:18:34 - Carson
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.givon.baseproject.entity.ChatBaseBean;
import com.givon.baseproject.entity.RoomBean;
import com.givon.baseproject.entity.UserBean;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DbHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "ormlite.db";
	private static final int DATABASE_VERSION = 1;

	private Dao<RoomBean, Integer> roomDao = null;
	private Dao<UserBean, Integer> userDao = null;

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, RoomBean.class);
			TableUtils.createTable(connectionSource, ChatBaseBean.class);
		} catch (SQLException e) {
			Log.e(DbHelper.class.getName(), "Unable to create datbases", e);
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource,
			int oldVer, int newVer) {
		try {
			TableUtils.dropTable(connectionSource, RoomBean.class, true);
			TableUtils.dropTable(connectionSource, ChatBaseBean.class, true);
			onCreate(sqliteDatabase, connectionSource);
		} catch (SQLException e) {
			Log.e(DbHelper.class.getName(), "Unable to upgrade database from version " + oldVer
					+ " to new " + newVer, e);
		}
	}

	public Dao<RoomBean, Integer> getRoomDao() throws SQLException {
		if (roomDao == null) {
			roomDao = getDao(RoomBean.class);
		}
		return roomDao;
	}
	public Dao<UserBean, Integer> getChatUserDao() throws SQLException {
		if (userDao == null) {
			userDao = getDao(UserBean.class);
		}
		return userDao;
	}
}