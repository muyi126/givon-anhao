/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @ErrorCode.java  2014年8月25日 下午2:27:46 - Guzhu
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

package com.givon.baseproject.entity;

import com.givon.anhao.R;

public enum ErrorCode {
	// TODO 错误代码
	E_1(0, "正常返回"), E_2(1000, "服务器异常【出错】"), E_3(1001, "创建房间失败"), E_4(1002, "房间已经存在"), E_5(2001,
			"注册失败"), E_6(2002, "用户存在"), E_7(2003, "用户名或者密码不正确"), E_8(2004, "token无效，未认证"), E_9(
			2005, "社交帐号登录失败"), E_10(3001, "添加收藏失败"), E_11(3002, "删除收藏失败"), E_12(3003, "获取收藏列表失败"), E_13(
			4001, "获取消息列表失败");
	private int codeNum;
	private String codeValue;

	ErrorCode(int code, String value) {
		this.codeNum = code;
		this.codeValue = value;
	}

	public static String getCodeValue(int num) {
		for (ErrorCode c : ErrorCode.values()) {
			if (c.getCodeNum() == num) {
				return c.codeValue;
			}
		}
		return "";
	}

	public int getCodeNum() {
		return codeNum;
	}

	public void setCodeNum(int codeNum) {
		this.codeNum = codeNum;
	}

	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}

}
