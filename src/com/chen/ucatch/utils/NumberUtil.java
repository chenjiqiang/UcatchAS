/*
 * Copyright 2000-2020 YGSoft.Inc All Rights Reserved.
 */
package com.chen.ucatch.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数值型转换工具类.<br>
 * @author keshaojian <br>
 * @version 1.0.0 2011-11-22<br>
 * @see
 * @since JDK 1.4.2.6
 */
public final class NumberUtil {

	/**
	 * 构造方法.
	 */
	private NumberUtil() {
	}

	/**
	 * 将字符串转换为双浮点型.
	 * @param theValueString 字符串
	 * @return 双浮点型数
	 */
	public static double parse(final String theValueString) {
		double value;
		if (StringUtil.isEmptyString(theValueString)) {
			value = 0;
		} else {
			try {
				value = Double.parseDouble(theValueString);
			} catch (NumberFormatException e) {
				// 无法转换为数值的字符串不帮任何处理
				value = 0;
			}
		}

		return value;
	}

	/**
	 * 字符串转数字
	 * @param s
	 * @param req
	 * @return
	 */
	public static int StringToInteger(final String s, final int req) {
		if (isNumber(s)) {
			return Integer.parseInt(s);
		}
		return req;
	}

	/**
	 * 判断是否数字
	 * @param numberString
	 * @return
	 */
	public static Boolean isNumber(final Object numberString) {
		if (numberString == null || numberString.toString().equals("")) {
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(numberString.toString());
		if (!isNum.matches()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 字符串转长整行
	 * @param s
	 * @param req
	 * @return
	 */
	public static long StringToLonger(final String s, final long req) {
		if (isNumber(s)) {
			return Long.parseLong(s);
		}
		return req;
	}

	/**
	 * 把一个整数转为相对应的位数 . <br>
	 * @author liulongzhenhai 2013-11-4 下午1:07:57 <br>
	 * @param i 要转换的数字
	 * @param d 取多少位
	 * @return double
	 */
	public static double intToLenDouble(final int i, final int d) {
		String s = String.valueOf(Math.abs(i));
		if (s.length() >= d) {
			double r = Math.pow(10, s.length() - d);
			return i / r;
		}
		return i;
	}
	/**
	 * 
	 * 判断是否是手机号
	 * @author chenjiqiang 2015年8月5日 下午4:48:01 <br> 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();

    }
}
