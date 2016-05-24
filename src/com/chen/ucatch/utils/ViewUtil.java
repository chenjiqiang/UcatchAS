/*
 * Copyright 2000-2020 YGSoft.Inc All Rights Reserved.
 */
package com.chen.ucatch.utils;

import android.app.Activity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * View的一些方法,计算一个View的实际高度 .<br>
 * @author liulongzhenhai <br>
 * @version 1.0.0 2013-5-4<br>
 * @see
 * @since JDK 1.4.2.6
 */
public final class ViewUtil {
	/**
	 * ViewUtil. <br>
	 * @author liulongzhenhai 2013-5-4 下午4:48:53 <br>
	 */
	private ViewUtil() {

	}

	/**
	 * 计算一个布局的高度 . <br>
	 * @author liulongzhenhai 2013-4-11 上午10:59:35 <br>
	 * @param child 布局VIEW
	 */
	public static void measureView(final View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		final int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		final int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	/**
	 * 根据当前的ImageVIew的大小计算出16:10的高度 . <br>
	 * @author liulongzhenhai 2013-7-2 上午11:39:20 <br>
	 * @param iv ImageView
	 */
	public static void proportion16to10Image(final ImageView iv) {
		int width = iv.getWidth();
		if (width <= 0) {
			measureView(iv);
			width = iv.getWidth();
		}
		setImageViewWidth(iv, width);
	}

	/**
	 * 以16:10的来进行设置 . <br>
	 * @author liulongzhenhai 2013-7-2 下午3:56:05 <br>
	 * @param iv ImageView
	 * @param width 宽度
	 */
	private static void setImageViewWidth(final ImageView iv, final int width) {
		if (width > 0) {
			final int height = width / 16 * 10;
			if (iv.getLayoutParams() != null) {
				if (iv.getLayoutParams() instanceof LinearLayout.LayoutParams) {
					final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
					iv.setLayoutParams(params);
				} else if (iv.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
					final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
					iv.setLayoutParams(params);
				} else if (iv.getLayoutParams() instanceof Gallery.LayoutParams) {
					final Gallery.LayoutParams params = new Gallery.LayoutParams(width, height);
					iv.setLayoutParams(params);
				}
			}
		}
	}

	/**
	 * 以屏幕宽度为基础.进行16:10的设置 . <br>
	 * @author liulongzhenhai 2013-7-2 下午3:54:46 <br>
	 * @param iv ImageView
	 * @param activity Activity
	 */
	public static void proportion16to10ImageFullWidth(final ImageView iv, final Activity activity) {
		final int[] pd = SystemInfo.getDisplaySize(activity);
		setImageViewWidth(iv, pd[0]);
	}
}
