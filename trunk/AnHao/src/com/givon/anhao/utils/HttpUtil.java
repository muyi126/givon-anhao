/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @HttpUtil.java  2014年8月27日 上午9:13:18 - Guzhu
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

package com.givon.anhao.utils;

public class HttpUtil {
	public static String MainUrl = "http://lbschat.topcoin.cn";
	
	
	public static String getUrl(String url){
		return MainUrl+url;
	}

	
	public static String REGIST = "/api/users/reg";
	public static String LOGIN = "/api/users/login";
	public static String RECOMMEND = "/api/room/recommend";
}
