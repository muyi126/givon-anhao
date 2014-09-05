/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @RoomEntity.java  2014年8月20日 下午2:51:51 - Guzhu
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

package com.givon.baseproject.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.j256.ormlite.field.DatabaseField;

public class RoomEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private ArrayList<RoomBean> result;

	public ArrayList<RoomBean> getResult() {
		return result;
	}

	public void setResult(ArrayList<RoomBean> result) {
		this.result = result;
	}

}
