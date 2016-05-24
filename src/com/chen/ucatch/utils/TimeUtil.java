/*
 * Copyright 2000-2020 YGSoft.Inc All Rights Reserved.
 */
package com.chen.ucatch.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类,格式化时间 . <br>
 * @author liulongzhenhai <br>
 * @version 1.0.0 2012-7-5 下午3:59:10 <br>
 * @see
 * @since JDK 1.4.2.6
 */
public final class TimeUtil {
	// 一天=86400000(24*60*60*1000)毫秒(MSEL)millisecond.

	/**
	 * 压缩后的日系数 (秒).
	 */
	private static final int DAYTICK = 86400;

	/**
	 * 压缩后的月系数 (秒).
	 */
	private static final int MONTHTICK = 2592000;

	/**
	 * 压缩后的小时系数 (秒) .
	 */
	private static final int HOURTICK = 3600;

	/**
	 * 压缩后的分钟系数 (秒) .
	 */
	private static final int MINUTETICK = 60;

	/**
	 * 压缩的系数 (秒) .
	 */
	private static final int MODULUS = 1000;

	/**
	 * 时间转换的基础 .
	 */
	private static final SimpleDateFormat SIMPLEDATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 日期描述 .
	 */
	private static final String[] DAYS = new String[] { "日 ", "一 ", "二 ", "三 ", "四 ", "五 ", "六 " };

	/**
	 * 构造 . <br>
	 * @author liulongzhenhai 2012-9-7 下午2:31:29 <br>
	 */
	private TimeUtil() {
	}

	/**
	 * 以月为大单位,通过长整形获取时间间隔 . <br>
	 * @author liulongzhenhai 2012-9-7 下午2:31:51 <br>
	 * @param timestamp timestamp
	 * @return 压缩的时间描述 (秒)
	 */
	public static String monthConverTime(final long timestamp) {
		// final long currentSeconds = System.currentTimeMillis();
		// final long timeGap = (currentSeconds - timestamp) / MODULUS;
		// String timeStr = null;
		// if (timeGap > MONTHTICK) {
		// timeStr = timeGap / MONTHTICK + "个月";
		// }
		// if (timeGap > DAYTICK) {
		// timeStr = timeGap / DAYTICK + "天前";
		// } else if (timeGap > HOURTICK) {
		// timeStr = timeGap / HOURTICK + "小时前 ";
		// } else if (timeGap > MINUTETICK) {
		// timeStr = timeGap / MINUTETICK + "分钟前 ";
		// } else {
		// timeStr = "刚刚";
		// }
		// return timeStr;
		return dayConverTime(timestamp);
	}

	/**
	 * 以日为大单位,如果大于日的则显示日期 . <br>
	 * @author liulongzhenhai 2012-9-7 下午2:33:27 <br>
	 * @param timestamp timestamp
	 * @return 时间描述
	 */
	public static String dayConverTime(final long timestamp) {
		final long currentSeconds = System.currentTimeMillis();
		final long timeGap = (currentSeconds - timestamp) / MODULUS; // 与现在时间相差秒数
		if (timeGap > DAYTICK) { // 24 * 60 * 60)
			return getStandardTime(timestamp, FormatTimeType.CMonth);
		} else {
			if (timeGap > DAYTICK) { // 24 * 60 * 60)
				// 1天以上
				return timeGap / DAYTICK + "天前"; // (24 * 60 * 60) + "天前";
			} else if (timeGap > HOURTICK) { // 60 * 60)
				// 1小时-24小时
				return timeGap / HOURTICK + "小时前 "; // (60 * 60) ;
			} else if (timeGap > MINUTETICK) { // 1分钟-59分钟
				return timeGap / MINUTETICK + "分钟";
			} else { // 1秒钟-59秒钟
				return "刚刚";
			}
		}
	}

	/**
	 * 通过时间戟获取日期 . <br>
	 * @author liulongzhenhai 2012-9-7 下午2:34:31 <br>
	 * @param timestamp 时间戟
	 * @return 日期 yyyy年MM月dd日 HH:mm
	 */
	public static String getStandardTime(final long timestamp) {
		return getStandardTime(timestamp, FormatTimeType.CDateTime);
	}

	/**
	 * 格式化一个时间戟 . <br>
	 * @author liulongzhenhai 2012-9-7 下午2:34:47 <br>
	 * @param timestamp timestamp
	 * @param type FormatTimeType
	 * @return 日期
	 */
	public static String getStandardTime(final long timestamp, final FormatTimeType type) {
		return formateDate(new Date(timestamp), type);
	}

	/**
	 * 格式化一个时间戟 . <br>
	 * @author liulongzhenhai 2012-9-7 下午2:34:47 <br>
	 * @param date 日期
	 * @param type FormatTimeType
	 * @return 日期
	 */
	public static String formateDate(final Date date, final FormatTimeType type) {
		if (date != null) {
			final SimpleDateFormat sdf = new SimpleDateFormat(type.getFormateString());
			return sdf.format(date);
		}
		return "";
	}

	/**
	 * Timestamp 转 Date. <br>
	 * @author liulongzhenhai 2013-8-15 上午8:29:27 <br>
	 * @param ts Timestamp
	 * @return Date
	 */
	public static Date timestampToDate(final Timestamp ts) {
		Date date = new Date();
		date = ts;
		return date;

	}

	/**
	 * Date转 Timestamp. <br>
	 * @author liulongzhenhai 2013-8-15 上午8:30:00 <br>
	 * @return Timestamp
	 */
	public static Timestamp dateToTimeStamp(final Date date) {
		return new Timestamp(date.getTime());
	}

	/**
	 * 显然日期2012-12-12 . <br>
	 * @author liulongzhenhai 2012-9-7 下午2:35:41 <br>
	 * @param time time
	 * @return time
	 */
	public static String formatDate(final long time) {
		return SIMPLEDATEFORMAT.format(time);
	}

	/**
	 * 显然日期2012-12-12 . <br>
	 * @author liulongzhenhai 2012-9-7 下午2:35:54 <br>
	 * @param time time
	 * @return time
	 */
	public static String formatDate(final String time) {
		String result = "";
		if (null != time && time.length() > 0) {
			try {
				result = SIMPLEDATEFORMAT.format(Long.parseLong(time));
			} catch (final Exception err) {
				throw new RuntimeException(err);
			}
		}
		return result;
	}

	/**
	 * 获取时间的日期 . <br>
	 * @author liulongzhenhai 2012-9-7 下午2:36:11 <br>
	 * @param timestamp timestamp
	 * @return 星期描述
	 */
	public static String getStandardTimeZhou(final long timestamp) {
		return getStandardTimeZhou(new Date(timestamp));
	}

	/**
	 * TODO 在此写上方法的相关说明 . <br>
	 * @author liulongzhenhai 2012-9-7 下午2:38:37 <br>
	 * @param date date
	 * @return 星期描述
	 */
	public static String getStandardTimeZhou(final Date date) {
		return "星期" + DAYS[date.getDay()];
	}

	/**
	 * 获取当日日期的星期一和星期日的时间 . <br>
	 * @author liulongzhenhai 2012-9-8 下午2:07:37 <br>
	 * @return 0星期一得日期,1星期日的日期
	 */
	public static String[] getMondaySundayDate() {
		final Date date = new Date();
		return new String[] {
				SIMPLEDATEFORMAT.format(new Date(date.getTime() - (date.getDay() - date.getDay()) * DAYTICK * MODULUS)),
				SIMPLEDATEFORMAT.format(new Date(date.getTime() + (7 - date.getDay()) * DAYTICK * MODULUS)) };
	}

	/**
	 * 获取当前时间 . 20120909034019转换为2012-09-09 03:40:19
	 * @author wangning4 2012-9-4 下午3:50:42 <br>
	 * @return str str
	 */
	public static String getCurrentTime() {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	/**
	 * 转换时间格式 .
	 * @author wangning4 2012-9-4 下午3:50:42 <br>
	 * @return currentTime currentTime
	 */
	public static String changeCurrentTimeFormat(final String currentTime) {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.format(new SimpleDateFormat("yyyyMMddHHmmss").parse(currentTime));
		} catch (final ParseException e) {
			throw new RuntimeException();
		}
	}

	/**
	 * 格式化年月日 ，只要用在选择系统默认时间，转化为2013-03-03格式 .
	 * @author wangning4 2013-6-6 下午2:16:00 <br>
	 * @param year 年 2013
	 * @param month 月 3或03
	 * @param day 日 8或08
	 * @return yyyy-MM-dd格式
	 */
	public static String formatYearMonthDday(final int year, final int month, final int day) {
		String dayString = "";
		final int max = 9;
		if (day > max) {
			dayString = String.valueOf(day);
		} else {
			dayString = "0" + day;
		}

		String monthString = "";
		if (month + 1 > max) {
			monthString = String.valueOf(month + 1);
		} else {
			monthString = "0" + (month + 1);
		}
		final String result = year + "-" + monthString + "-" + dayString;
		return result;
	}

	/**
	 * 时间字符串转为Date, 格式：yyyy-MM-dd . <br>
	 * @author liulongzhenhai 2012-9-10 下午3:04:42 <br>
	 * @param time time
	 * @return Date
	 * @throws ParseException
	 */
	public static Date stringToDate(final String time) {
		return stringToDate(time, FormatTimeType.Date);
	}

	/**
	 * 字符串转为. <br>
	 * @author liulongzhenhai 2013-7-12 下午5:37:01 <br>
	 * @param time 时间
	 * @param ft FormatTimeType
	 * @return Date
	 */
	public static Date stringToDate(final String time, final FormatTimeType ft) {
		final SimpleDateFormat format = new SimpleDateFormat(ft.getFormateString());
		try {
			return format.parse(time);
		} catch (final ParseException ex) {
			throw new RuntimeException();
		}
	}

	/**
	 * 获取当前时间的完整字符串格式 . <br>
	 * @author liulongzhenhai 2012-9-24 下午4:21:11 <br>
	 * @return 时间格式
	 */
	public static String getNowCurrentTime() {
		return formateDate(new Date(), FormatTimeType.CDateTime);
	}

	/**
	 * 获取当前系统时间的时间轴 . <br>
	 * @author liulongzhenhai 2012-10-9 下午7:22:50 <br>
	 * @return long
	 */
	public static long getNowTimeTick() {
		return new Date().getTime();
	}

	/**
	 * 格式化Timestamp 日期和时间. <br>
	 * @author liulongzhenhai 2012-11-22 下午6:58:43 <br>
	 * @param timeStamp Timestamp
	 * @return 日期描述
	 */
	public static String formateTimeStampFull(final Timestamp timeStamp) {
		final Calendar c = Calendar.getInstance();
		c.setTime(timeStamp);
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(c.getTime());
	}

	/**
	 * 格式化Timestamp 日期和时间只有日期. <br>
	 * @author liulongzhenhai 2012-11-22 下午6:58:43 <br>
	 * @param timeStamp Timestamp
	 * @return 日期描述
	 */
	public static String formateTimeStampDay(final Timestamp timeStamp) {
		final Calendar c = Calendar.getInstance();
		c.setTime(timeStamp);
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(c.getTime());
	}

	/**
	 * 格式化TimeStamp为时间 . <br>
	 * @author liulongzhenhai 2012-11-22 下午6:59:42 <br>
	 * @param timeStamp Timestamp
	 * @return 时间
	 */
	public static String formatTimeStampTime(final Timestamp timeStamp) {
		final Calendar c = Calendar.getInstance();
		c.setTime(timeStamp);
		final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(c.getTime());
	}

	/**
	 * 字符串转Timestamp . <br>
	 * @author liulongzhenhai 2013-2-19 下午5:23:04 <br>
	 * @param time 字符串
	 * @return Timestamp
	 */
	public static Timestamp stringToTimestamp(final String time) {
		// final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// format.setLenient(false);
		// // 要转换字符串 str_test
		// try {
		// final Timestamp ts = new Timestamp(format.parse(time).getTime());
		// return ts;
		// } catch (final ParseException e) {
		// throw new RuntimeException("time error");
		// }
		return dateToTimeStamp(new Date(NumberUtil.StringToLonger(time, 0)));
	}

	/**
	 * 计算两个日期之间的时间差 返回多少天的数字(int)
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return startDate - endDate
	 */
	public static int getDaysLeft(final Date startDate, final Date endDate) {
		long betweenTime = endDate.getTime() - startDate.getTime();
		betweenTime = betweenTime / 1000 / 60 / 60 / 24;
		return (int) betweenTime;
	}

	/**
	 * 转换时间类型. <br>
	 * @author wangjun4 2014年2月28日 上午11:26:23 <br>
	 * @param time String类型的秒时间，注意单位是秒
	 * @param format 指定时间格式
	 * @return 按指定时间格式输出String
	 */
	public static String convertTimeTypeFormat(final String time, final FormatTimeType format) {
		String res = "";

		if (null != time && time.length() > 0) {
			try {
				Date date = new Date();
				date.setTime(MODULUS * Long.valueOf(time));
				res = new SimpleDateFormat(format.getFormateString()).format(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return res;
	}

	/**
	 * 格式化时间的类型 .<br>
	 * @author liulongzhenhai <br>
	 * @version 1.0.0 2013-3-19<br>
	 * @see
	 * @since JDK 1.4.2.6
	 */
	public enum FormatTimeType {
		/**
		 * yyyy年MM月dd日 HH:mm .
		 */
		CDateTime("yyyy年MM月dd日 HH:mm"),
		/**
		 * MM月dd日 HH:mm .
		 */
		MDateTime("MM月dd日 HH:mm"),
		/**
		 * MM月dd日 .
		 */
		CMonth("MM月dd日"),
		/**
		 * yyyy年MM月dd日 .
		 */
		CDate("yyyy年MM月dd日"),
		/**
		 * yyyy-MM-dd HH:mm:ss .
		 */
		DateTime("yyyy-MM-dd HH:mm:ss"),
		/**
		 * yyyy-MM-dd HH:mm .
		 */
		SimpleDateTime("yyyy-MM-dd HH:mm"),
		/**
		 * yyyy-MM-dd .
		 */
		Date("yyyy-MM-dd"),
		/**
		 * HH:mm .
		 */
		Time("HH:mm"),
		/**
		 * HH:mm:ss .
		 */
		MTime("HH:mm:ss"),
		/**
		 * yyyy-MM-dd .
		 */
		CDateWeek("yyyy年MM月dd日 E"),
		/**
		 * .
		 */
		DateFull("yyyyMMddHHmmss");
		/**
		 * 格式化的字符串 .
		 */
		String formateString;

		/**
		 * FormatTimeType . <br>
		 * @author liulongzhenhai 2013-7-12 下午5:39:17 <br>
		 * @param str 格式化的样式
		 */
		private FormatTimeType(final String str) {
			formateString = str;
		}

		/**
		 * 获取格式化的时间 . <br>
		 * @author liulongzhenhai 2013-7-12 下午5:40:41 <br>
		 * @return String
		 */
		public String getFormateString() {
			return formateString;
		}
	}
}
