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

import android.os.Parcel;
import android.os.Parcelable;

import com.easemob.chat.EMContact;
import com.j256.ormlite.field.DatabaseField;

public class UserBean extends EMContact implements Parcelable {
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
		super.nick = nickname;
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
	public String getEid() {
		return super.eid;
		
	}
	

	public void setEasemobId(String easemobId) {
		this.easemobId = easemobId;
		super.eid = easemobId;
		
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

	public UserBean() {
		super();
	}

	public UserBean(String userId, String nickname, String freeTime, String password,
			long createTime, String easemobId, String easemobPassword, String token, String avatar,
			int unreadMsgCount, String header) {
		super(easemobId);
		this.userId = userId;
		this.nickname = nickname;
		this.freeTime = freeTime;
		this.password = password;
		this.createTime = createTime;
		this.easemobId = easemobId;
		this.easemobPassword = easemobPassword;
		this.token = token;
		this.avatar = avatar;
		this.unreadMsgCount = unreadMsgCount;
		this.header = header;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(super.eid);
		out.writeString(super.nick);
		out.writeString(super.username);
		out.writeString(userId);
		out.writeString(nickname);
		out.writeString(freeTime);
		out.writeString(password);
		out.writeLong(createTime);
		out.writeString(easemobId);
		out.writeString(easemobPassword);
		out.writeString(token);
		out.writeString(avatar);
		out.writeInt(unreadMsgCount);
		out.writeString(header);
	}

	public static final Parcelable.Creator<UserBean> CREATOR = new Creator<UserBean>() {
		@Override
		public UserBean[] newArray(int size) {
			return new UserBean[size];
		}

		@Override
		public UserBean createFromParcel(Parcel in) {
			return new UserBean(in);
		}
	};

	public UserBean(Parcel in) {
		super.eid = in.readString();
		super.nick = in.readString();
		super.username = in.readString();
		userId = in.readString();
		nickname = in.readString();
		freeTime = in.readString();
		password = in.readString();
		createTime = in.readLong();
		easemobId = in.readString();
		easemobPassword = in.readString();
		token = in.readString();
		avatar = in.readString();
		unreadMsgCount = in.readInt();
		header = in.readString();
	}

}
