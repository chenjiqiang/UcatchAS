/**
 * Copyright 2015 YGSOFT.com, Inc. All Rights Reserved.
 */
package com.chen.ucatch.utils;
import com.chen.ucatch.Application;
import com.loopj.android.http.AsyncHttpClient;

/**
 * 工具集.
 * 
 * @author suntao
 * 
 */
public class Utility {
	public Utility() {
	}

	/**
	 * 获取AsyncHttpClient实例.
	 * 
	 * @return AsyncHttpClient
	 */
	public static AsyncHttpClient getAsyncHttpClient() {
		return Application.masyncHttpClient;
	}

}
