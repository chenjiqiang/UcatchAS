package com.chen.ucatch.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 
 * @author huanglihou
 *
 */
public class AppInfo {
	/**
	 * 获取app的版本号
	 * @param context
	 * @return
	 */
	public static float getVersion(Context context) {
		try {
			PackageManager manager = context
					.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context
					.getPackageName(), 0);
			String version = info.versionName;
			return Float.parseFloat(version);
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}
}
