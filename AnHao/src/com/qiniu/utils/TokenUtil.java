/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @TokenUtil.java  2014年9月25日 上午11:00:57 - Guzhu
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

package com.qiniu.utils;

import java.io.UnsupportedEncodingException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;
import com.qiniu.auth.Authorizer;
import com.qiniu.rs.Base64;

public class TokenUtil {
	// 加解密统一使用的编码方式
	private final static String encoding = "utf-8";
	private final static String SecretKey = "MREftAk5KRYhNNQDltAp-RDtOsqjRFvMPW1ncFvW";
	private final static String AccessKey = "zRmhvNuRmGVVFnxk5Oc7dsKcxQyJTeOoc_Fsj5in";
	private static String uptoken;
	public static String getToken(String bucketName, long deadline) {
		String unSignString = "{\"deadline\":" + deadline + "," + "\"scope\":\"" + bucketName
				+ "\"}";
		Config.ACCESS_KEY = AccessKey;
		Config.SECRET_KEY = SecretKey;
		byte[] encryptData;
		try {
			encryptData = unSignString.getBytes(encoding);
			String encode = Base64.encode(encryptData);
			String sign = hmac_sha1(encode);
			sign += ":" + encode;
			uptoken = sign;
			return sign;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";

	}

	private static String hmac_sha1(String datas) {
		String reString = "";
		try {
			// 生成一个指定 Mac 算法 的 Mac 对象
			Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
			// 用给定密钥初始化 Mac 对象
			byte[] text = datas.getBytes("UTF-8");
			// 完成 Mac 操作
			String text1 = mac.sign(text);
			return text1;
		} catch (Exception e) {
		}
		return reString;
	}
	
	public static Authorizer auth = new Authorizer();
	{
		auth.setUploadToken(uptoken); 
	}

}
