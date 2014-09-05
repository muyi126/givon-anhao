/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @ChatBaseBean.java  2014年9月3日 上午9:51:52 - Guzhu
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

package com.givon.baseproject.entity;

import java.io.Serializable;

import com.givon.anhao.activity.ChatActivity;
import com.j256.ormlite.field.DatabaseField;

public class ChatBaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	@DatabaseField(generatedId = true)//自增长的主键
	private int id;
	@DatabaseField(useGetSet=true,columnName="Type")  //ChatActivity.CHATTYPE_GROUP  
	private int Type;
	@DatabaseField(useGetSet=true,columnName="EmId")    
	private String EmId;
	@DatabaseField(useGetSet=true,columnName="AnId")    
	private String AnId;
	@DatabaseField(useGetSet=true,columnName="isFriend")    
	private boolean isFriend;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return Type;
	}
	public void setType(int type) {
		Type = type;
	}
	public String getEmId() {
		return EmId;
	}
	public void setEmId(String emId) {
		EmId = emId;
	}
	public String getAnId() {
		return AnId;
	}
	public void setAnId(String anId) {
		AnId = anId;
	}
	public boolean getIsFriend() {
		return isFriend;
	}
	public void setIsFriend(boolean isFriend) {
		this.isFriend = isFriend;
	}

}
