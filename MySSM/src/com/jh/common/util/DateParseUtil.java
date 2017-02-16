package com.jh.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 把字符串格式的时间转化成Date类型的时间格式
 */
public class DateParseUtil {

	public static final String DEFAULT_PATTERN = DateFormatUtil.DEFAULT_PATTERN;

	public static Date parseDate(String dateStr) {
		return parseDate(dateStr, DEFAULT_PATTERN);
	}

	public static Date parseDate(String dateStr, String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);
		try {
			return df.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}
