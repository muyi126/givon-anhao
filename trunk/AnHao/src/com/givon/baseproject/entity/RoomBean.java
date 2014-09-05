/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @RoomBean.java  2014年8月30日 上午8:49:38 - Guzhu
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

package com.givon.baseproject.entity;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class RoomBean extends ChatBaseBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@DatabaseField(useGetSet = true, columnName = "longitude")
	private long longitude;
	@DatabaseField(useGetSet = true, columnName = "latitude")
	private long latitude;
	@DatabaseField(useGetSet = true, columnName = "roomId")
	private String roomId;
	@DatabaseField(useGetSet = true, columnName = "name")
	private String name;
	@DatabaseField(useGetSet = true, columnName = "createUserId")
	private String createUserId;
	@DatabaseField(useGetSet = true, columnName = "roomName")
	private String roomName;
	@DatabaseField(useGetSet = true, columnName = "cover")
	private String cover;
	@DatabaseField(useGetSet = true, columnName = "onlineCount")
	private String onlineCount;
	@DatabaseField(useGetSet = true, columnName = "wsHost")
	private String wsHost;
	@DatabaseField(useGetSet = true, columnName = "recommend")
	private String recommend;
	@DatabaseField(useGetSet = true, columnName = "description")
	private String description;
//	@DatabaseField(uniqueIndex = true, id = true)
	@DatabaseField(useGetSet = true, columnName = "groupId")
	private String groupId;

	public long getLongitude() {
		return longitude;
	}

	public void setLongitude(long longitude) {
		this.longitude = longitude;
	}

	public long getLatitude() {
		return latitude;
	}

	public void setLatitude(long latitude) {
		this.latitude = latitude;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getOnlineCount() {
		return onlineCount;
	}

	public void setOnlineCount(String onlineCount) {
		this.onlineCount = onlineCount;
	}

	public String getWsHost() {
		return wsHost;
	}

	public void setWsHost(String wsHost) {
		this.wsHost = wsHost;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

}
