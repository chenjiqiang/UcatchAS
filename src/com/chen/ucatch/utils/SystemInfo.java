/*
 * Copyright 2000-2020 YGSoft.Inc All Rights Reserved.
 */
package com.chen.ucatch.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.util.DisplayMetrics;

/**
 * 系统的一些信息获取 .<br>
 * @author liulongzhenhai <br>
 * @version 1.0.0 2013-5-4<br>
 * @see
 * @since JDK 1.4.2.6
 */
public final class SystemInfo {

	/**
	 * 系统任务栏的高度.
	 */
	static int systemTitleHeight = 0;
	/**
	 * 屏幕的高度 .
	 */
	static int displayHeight = 0;
	/**
	 * 屏幕显示的宽度 .
	 */
	static int disaplayWidth = 0;

	/**
	 * SystemInfo . <br>
	 * @author liulongzhenhai 2013-5-4 下午4:08:18 <br>
	 */
	private SystemInfo() {

	}

	/**
	 * 获取屏幕的高度宽度信息,以及系统标题栏的高度 . <br>
	 * @author liulongzhenhai 2013-5-4 下午4:06:14 <br>
	 * @param activity Activity
	 * @return [0]屏幕宽度 [1]屏幕高度 [2]标题栏高度
	 */
	public static int[] getDisplaySize(final Activity activity) {
//		if (displayHeight <= 0 || disaplayWidth <= 0) {
			final DisplayMetrics dm = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
			final Rect rect = new Rect();
			activity.getWindow().getDecorView()
					.getWindowVisibleDisplayFrame(rect);
			systemTitleHeight = rect.top;
			displayHeight = dm.heightPixels;
			disaplayWidth = dm.widthPixels;
//		}
		return new int[] { disaplayWidth, displayHeight, systemTitleHeight };
	}
}
