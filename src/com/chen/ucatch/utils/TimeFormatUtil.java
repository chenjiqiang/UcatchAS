package com.chen.ucatch.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * 时间格式化工具. <br>
 * @author huanglihou <br>
 * @version 1.0.0 2015年4月24日 下午3:45:20 <br>
 * @see 
 * @since JDK 1.4.2.6
 */
public class TimeFormatUtil {
	/**
	 * 将String型格式化,比如想要将2011-11-11格式化成2011年11月11日,就StringPattern("2011-11-11",
	 * "yyyy-MM-dd","yyyy年MM月dd日").
	 * 
	 * @param date
	 *            String 想要格式化的日期
	 * @param oldPattern
	 *            String 想要格式化的日期的现有格式
	 * @param newPattern
	 *            String 想要格式化成什么格式
	 * @return String
	 */
	public static final String stringPattern(final String date, final String oldPattern,
			final String newPattern) {
		if (date == null || oldPattern == null || newPattern == null) {
			return "";
		}
		SimpleDateFormat sdf1 = new SimpleDateFormat(oldPattern); // 实例化模板对象
		SimpleDateFormat sdf2 = new SimpleDateFormat(newPattern); // 实例化模板对象
		Date d = null;
		try {
			d = sdf1.parse(date); // 将给定的字符串中的日期提取出来
		} catch (Exception e) { // 如果提供的字符串格式有错误，则进行异常处理
			e.printStackTrace(); // 打印异常信息
		}
		return sdf2.format(d);
	}
	
	public static String getCurrentDataToString(){
		Date lastDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(lastDate);
	}
}
