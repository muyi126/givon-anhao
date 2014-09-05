/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @UserBean.java  2014年8月27日 下午9:39:33 - Guzhu
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

package com.givon.baseproject.entity;

import java.io.Serializable;

import com.easemob.chat.EMContact;
import com.j256.ormlite.field.DatabaseField;

public class UserBean extends EMContact implements Serializable {
	private static final long serialVersionUID = 1L;
	@DatabaseField(useGetSet = true, columnName = "userId")
	private String userId;
	@DatabaseField(useGetSet = true, columnName = "nickname")
	private String nickname;
	@DatabaseField(useGetSet = true, columnName = "freeTime")
	private String freeTime;
	@DatabaseField(useGetSet = true, columnName = "password")
	private String password;
	@DatabaseField(useGetSet = true, columnName = "createTime")
	private long createTime;
	@DatabaseField(useGetSet = true, columnName = "easemobId")
	private String easemobId;
	@DatabaseField(useGetSet = true, columnName = "easemobPassword")
	private String easemobPassword;
	@DatabaseField(useGetSet = true, columnName = "token")
	private String token;
	@DatabaseField(useGetSet = true, columnName = "avatar")
	private String avatar;
	@DatabaseField(useGetSet = true, columnName = "unreadMsgCount")
	private int unreadMsgCount;
	@DatabaseField(useGetSet = true, columnName = "header")
	private String header;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getFreeTime() {
		return freeTime;
	}

	public void setFreeTime(String freeTime) {
		this.freeTime = freeTime;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getEasemobId() {
		return easemobId;
	}

	public void setEasemobId(String easemobId) {
		this.easemobId = easemobId;
	}

	public String getEasemobPassword() {
		return easemobPassword;
	}

	public void setEasemobPassword(String easemobPassword) {
		this.easemobPassword = easemobPassword;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getUnreadMsgCount() {
		return unreadMsgCount;
	}

	public void setUnreadMsgCount(int unreadMsgCount) {
		this.unreadMsgCount = unreadMsgCount;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}
	
	
	

}
