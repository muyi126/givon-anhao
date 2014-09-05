package com.givon.baseproject.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/* 

 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @TimeToUtil.java  Feb 18, 2014 7:42:49 PM - Guzhu
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */
public class TimeToUtil {

	/**
	 * 时间字段常量，表示“秒”
	 */
	public final static int SECOND = 0;

	/**
	 * 时间字段常量，表示“分”
	 */
	public final static int MINUTE = 1;

	/**
	 * 时间字段常量，表示“时”
	 */
	public final static int HOUR = 2;

	/**
	 * 时间字段常量，表示“天”
	 */
	public final static int DAY = 3;

	/**
	 * 各常量允许的最大值
	 */
	private final int[] maxFields = { 59, 59, 23, Integer.MAX_VALUE - 1 };

	/**
	 * 各常量允许的最小值
	 */
	private final int[] minFields = { 0, 0, 0, Integer.MIN_VALUE };

	/**
	 * 默认的字符串格式时间分隔符
	 */
	private String TimeToUtileparator = ":";

	/**
	 * 时间数据容器
	 */
	private int[] fields = new int[4];

	public int[] getFields() {
		return fields;
	}

	/**
	 * 无参构造，将各字段置为 0
	 */
	public TimeToUtil() {
		this(0, 0, 0, 0);
	}

	/**
	 * 使用时、分构造一个时间
	 * @param hour
	 *        小时
	 * @param minute
	 *        分钟
	 */
	public TimeToUtil(int hour, int minute) {
		this(0, hour, minute, 0);
	}

	/**
	 * 使用时、分、秒构造一个时间
	 * @param hour
	 *        小时
	 * @param minute
	 *        分钟
	 * @param second
	 *        秒
	 */
	public TimeToUtil(int hour, int minute, int second) {
		this(0, hour, minute, second);
	}

	/**
	 * 使用一个字符串构造时间<br/>
	 * Time time = new Time("14:22:23");
	 * @param time
	 *        字符串格式的时间，默认采用“:”作为分隔符
	 */
	public TimeToUtil(String time) {
		this(time, null);
	}

	/**
	 * 使用天、时、分、秒构造时间，进行全字符的构造
	 * @param day
	 *        天
	 * @param hour
	 *        时
	 * @param minute
	 *        分
	 * @param second
	 *        秒
	 */
	public TimeToUtil(int day, int hour, int minute, int second) {
		initialize(day, hour, minute, second);
	}

	/**
	 * 使用一个字符串构造时间，指定分隔符<br/>
	 * Time time = new Time("14-22-23", "-");
	 * @param time
	 *        字符串格式的时间
	 */
	public TimeToUtil(String time, String TimeToUtileparator) {
		if (TimeToUtileparator != null) {
			setTimeToUtileparator(TimeToUtileparator);
		}
		parseTime(time);
	}

	/**
	 * 设置时间字段的值
	 * @param field
	 *        时间字段常量
	 * @param value
	 *        时间字段的值
	 */
	public void set(int field, int value) {
		if (value < minFields[field]) {
			throw new IllegalArgumentException(value + ", time value must be positive.");
		}
		fields[field] = value % (maxFields[field] + 1);
		// 进行进位计算
		int carry = value / (maxFields[field] + 1);
		if (carry > 0) {
			int upFieldValue = get(field + 1);
			set(field + 1, upFieldValue + carry);
		}
	}

	/**
	 * 获得时间字段的值
	 * @param field
	 *        时间字段常量
	 * @return 该时间字段的值
	 */
	public int get(int field) {
		if (field < 0 || field > fields.length - 1) {
			throw new IllegalArgumentException(field + ", field value is error.");
		}
		return fields[field];
	}

	/**
	 * 将时间进行“加”运算，即加上一个时间
	 * @param time
	 *        需要加的时间
	 * @return 运算后的时间
	 */
	public TimeToUtil addTime(TimeToUtil time) {
		TimeToUtil result = new TimeToUtil();
		int up = 0; // 进位标志
		for (int i = 0; i < fields.length; i++) {
			int sum = fields[i] + time.fields[i] + up;
			up = sum / (maxFields[i] + 1);
			result.fields[i] = sum % (maxFields[i] + 1);
		}
		return result;
	}

	/**
	 * 将时间进行“减”运算，即减去一个时间
	 * @param time
	 *        需要减的时间
	 * @return 运算后的时间
	 */
	public TimeToUtil subtractTime(TimeToUtil time) {
		TimeToUtil result = new TimeToUtil();
		int down = 0; // 退位标志
		for (int i = 0, k = fields.length - 1; i < k; i++) {
			int difference = fields[i] + down;
			if (difference >= time.fields[i]) {
				difference -= time.fields[i];
				down = 0;
			} else {
				difference += maxFields[i] + 1 - time.fields[i];
				down = -1;
			}
			result.fields[i] = difference;
		}
		result.fields[DAY] = fields[DAY] - time.fields[DAY] + down;
		return result;
	}

	/**
	 * 获得时间字段的分隔符
	 * @return
	 */
	public String getTimeToUtileparator() {
		return TimeToUtileparator;
	}

	/**
	 * 设置时间字段的分隔符（用于字符串格式的时间）
	 * @param TimeToUtileparator
	 *        分隔符字符串
	 */
	public void setTimeToUtileparator(String TimeToUtileparator) {
		this.TimeToUtileparator = TimeToUtileparator;
	}

	private void initialize(int day, int hour, int minute, int second) {
		set(DAY, day);
		set(HOUR, hour);
		set(MINUTE, minute);
		set(SECOND, second);
	}

	private void parseTime(String time) {
		if (time == null) {
			initialize(0, 0, 0, 0);
			return;
		}
		String t = time;
		int field = DAY;
		set(field--, 0);
		int p = -1;
		while ((p = t.indexOf(TimeToUtileparator)) > -1) {
			parseTimeField(time, t.substring(0, p), field--);
			t = t.substring(p + TimeToUtileparator.length());
		}
		parseTimeField(time, t, field--);
	}

	private void parseTimeField(String time, String t, int field) {
		if (field < SECOND || t.length() < 1) {
			parseTimeException(time);
		}
		char[] chs = t.toCharArray();
		int n = 0;
		for (int i = 0; i < chs.length; i++) {
			if (chs[i] <= ' ') {
				continue;
			}
			if (chs[i] >= '0' && chs[i] <= '9') {
				n = n * 10 + chs[i] - '0';
				continue;
			}
			parseTimeException(time);
		}
		set(field, n);
	}

	private void parseTimeException(String time) {
		throw new IllegalArgumentException(time + ", time format error, HH"
				+ this.TimeToUtileparator + "mm" + this.TimeToUtileparator + "ss");
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(16);
		// sb.append(fields[DAY]).append(',').append(' ');
		buildString(sb, HOUR).append(TimeToUtileparator);
		buildString(sb, MINUTE).append(TimeToUtileparator);
		buildString(sb, SECOND);
		return sb.toString();
	}

	private StringBuilder buildString(StringBuilder sb, int field) {
		if (fields[field] < 10) {
			sb.append('0');
		}
		return sb.append(fields[field]);
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + Arrays.hashCode(fields);
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TimeToUtil other = (TimeToUtil) obj;
		if (!Arrays.equals(fields, other.fields)) {
			return false;
		}
		return true;
	}

	public static String getCountDown(long time) {
		long count = time / 1000;
		long hour = (count / 3600);
		count = count % 3600;
		long mils = (count / 60);
		count = count % 60;
		long second = count;
		StringBuilder build = new StringBuilder();
		if (hour < 10) {
			build.append(0).append(hour);
		} else {
			build.append(hour);
		}
		build.append(":");
		if (mils < 10) {
			build.append(0).append(mils);
		} else {
			build.append(mils);
		}
		build.append(":");
		if (second < 10) {
			build.append(0).append(second);
		} else {
			build.append(second);
		}
		return build.toString();
	}

	public static int[] getTime(int time) {
		int[] times = new int[4];
		times[0] = time /86400;
		time = time % 86400;
		times[1] = time / 3600;
		time = time % 3600;
		times[2] = time / 60;
		time = time % 60;
		times[3] = time;
		return times;
	}

	public final static String ACCURATE_TO_THE_TIME = "HH:mm:ss";
	public final static String ACCURATE_TO_THE_TIME_HOUS = "HH:mm";

	/** 输出格式: 2006-4-16 */
	public final static String ACCURATE_TO_THE_DAY = "yyyy-MM-dd";
	/** 输出格式: 2006年4月16日 星期六 */
	public final static String ACCURATE_TO_THE_WEEK = "yyyy年MM月dd日 EEEE";

	/** 输出格式: 2006-01-01 00:00:00 */
	public final static String ACCURATE_TO_THE_DAYANDTIME = "yyyy-MM-dd HH:mm:ss";
	/** 输出格式: 20060101000000 ***/
	public final static String ACCURATE_TO_THE_STRINGS = "yyyyMMddHHmmss";
	public final static String ACCURATE_TO_THE_WEEK_DAY = "dd/MM  EEEE";
	public final static String ACCURATE_TO_THE_WEEK_DAY_A = "dd/MM  HH:mm:ss";

	public static long getSystemTime() {
		return System.currentTimeMillis();
	}

	public static String getSystemTimeFormat(long time) {
		SimpleDateFormat format = new SimpleDateFormat(ACCURATE_TO_THE_STRINGS, Locale.CHINA);
		Date date = new Date(time);
		return format.format(date);
	}

	public static String getSystemTimeAFormat(long time) {
		SimpleDateFormat format = new SimpleDateFormat(ACCURATE_TO_THE_TIME, Locale.CHINA);
		Date date = new Date(time);
		return format.format(date);
	}

	public static String getSystemDayFormat() {
		SimpleDateFormat format = new SimpleDateFormat(ACCURATE_TO_THE_DAY, Locale.CHINA);
		Date date = new Date(getSystemTime());
		return format.format(date);

	}

	public static String getSystemTimeFormatWeek() {
		SimpleDateFormat format = new SimpleDateFormat(ACCURATE_TO_THE_WEEK_DAY_A, Locale.CHINA);
		Date date = new Date(getSystemTime());
		return format.format(date);

	}

	public static String getSystemTimeFormatWeek(long dates) {
		SimpleDateFormat format = new SimpleDateFormat(ACCURATE_TO_THE_WEEK_DAY, Locale.CHINA);
		Date date = new Date(dates);
		return format.format(date);

	}
	public static String getSystemTimeFormatWeekA(long dates) {
		SimpleDateFormat format = new SimpleDateFormat(ACCURATE_TO_THE_WEEK_DAY_A, Locale.CHINA);
		Date date = new Date(dates);
		return format.format(date);
		
	}

	public static String getSystemTimeSetFormat(long time, String formatString) {
		SimpleDateFormat format = new SimpleDateFormat(formatString, Locale.CHINA);
		Date date = new Date(time);
		return format.format(date);
	}
  //	当前时间是上午还是下午 (用于活动预告)
	public static String getFormat(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.CHINA);
		Date date = new Date(time);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
	    int am_pm = calendar.get(Calendar.AM_PM);
	    String amorPm="";
		String times=format.format(date);
		switch (am_pm) {
		case 0:
			amorPm="上午  ";
			break;
		case 1:
			amorPm="下午  ";
			break;
		default:
			break;
		}
		return amorPm+times;
	}

	
	public static String getSystemTimeSetFormat(String formatString) {
		SimpleDateFormat format = new SimpleDateFormat(formatString, Locale.CHINA);
		Date date = new Date(getSystemTime());
		return format.format(date);

	}

	/**
	 * 根据日期获取时戳
	 * @param date
	 *        "yyyy-MM-dd"
	 * @return
	 */
	public static long convert2long(String date) {
		if (!StringUtil.isEmpty(date)) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				return sf.parse(date).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return 0l;
	}

}
