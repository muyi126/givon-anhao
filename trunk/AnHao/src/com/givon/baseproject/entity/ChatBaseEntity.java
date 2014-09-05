/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @ChatBaseEntity.java  2014年9月3日 下午4:07:14 - Guzhu
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

package com.givon.baseproject.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class ChatBaseEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	ArrayList<ChatBaseBean> result;

	public ArrayList<ChatBaseBean> getResult() {
		return result;
	}

	public void setResult(ArrayList<ChatBaseBean> result) {
		this.result = result;
	}
	

}
