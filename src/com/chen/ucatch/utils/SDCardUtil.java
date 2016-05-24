/*
 * Copyright 2000-2020 YGSoft.Inc All Rights Reserved.
 */
package com.chen.ucatch.utils;

import java.io.File;

/**
 * 
 * SD卡 工具类.
 * @author chenjiqiang <br>
 * @version 1.0.0 2015年8月19日 下午10:04:46 <br>
 * @see 
 * @since JDK 1.4.2.6
 */
public final class SDCardUtil {

	/**
	 * 使用单例模式 .
	 */
	private static SDCardUtil sdCardUtil;

	/**
	 * ..
	 * @author wangning4 2012-9-4 上午11:47:18 <br>
	 */
	private SDCardUtil() {

	}

	/**
	 * 
	 * TODO 在此写上方法的相关说明. <br>
	 * @author chenjiqiang 2015年8月19日 下午10:05:02 <br> 
	 * @return
	 */
	public static SDCardUtil getInstance() {
		if (sdCardUtil == null) {
			sdCardUtil = new SDCardUtil();
		}
		return sdCardUtil;
	}

	/**
	 * 一级目录 .
	 */
	public static final String FIRST_DIR = "eods";
	
	/**
	 * 一级目录 .
	 */
	public static final String FIRST_DIR_DOWNLOAD_FILE = "应急系统";

	/**
	 * 缓存
	 */
	private final static String SECOND_DIR_CACHE = "cache";
	
	/**
	 * 二级目录  图片.
	 */
	private final static String SECOND_DIR_IMAGE = "图片";

	/**
	 * 二级目录  录音.
	 */
	private final static String SECOND_DIR_RECORD = "录音";

	/**
	 * 二级目录  视频.
	 */
	private final static String SECOND_DIR_VIDEO = "视频";
	
	/**
	 * 一级存放doc文件   为什么不放在FIRST_DIR目录下呢？ 因为这些doc文件不需给用看， 而FIRST_DIR目录下的图片、视频、录音文件要供用户选择的  .
	 */
	private final static String FIRST_DOC_DIR = "eods_doc";

	
	/**
	 * 初始化SD卡文件目录 .
	 * @author wangning4 2012-9-4 下午3:42:33 <br>
	 * @return savePath 保存路径
	 */
	public String getFirstDir() {
		String savePath;
		// 获取跟目录
		final String sdPath = getSDCardPath();

		if (!sdPath.endsWith("/")) {
			savePath = sdPath + "/" + FIRST_DIR + "/";
		} else {
			savePath = sdPath + FIRST_DIR + "/";
		}
		// 创建一级目录
		final File file = new File(savePath);
		if (!file.exists()) {
			file.mkdir();
		}
		return savePath;
	}
	
	/**
	 * 初始化SD卡文件目录 （存放各种文件，如pdf、chm 等等 ， 方便用户查看） .
	 * @author wangning4 2012-9-4 下午3:42:33 <br>
	 * @return savePath 保存路径
	 */
	public String getFirstDirDownloadFile() {
		String savePath;
		// 获取跟目录
		final String sdPath = getSDCardPath();

		if (!sdPath.endsWith("/")) {
			savePath = sdPath + "/" + FIRST_DIR_DOWNLOAD_FILE + "/";
		} else {
			savePath = sdPath + FIRST_DIR_DOWNLOAD_FILE + "/";
		}
		// 创建一级目录
		final File file = new File(savePath);
		if (!file.exists()) {
			file.mkdir();
		}
		return savePath;
	}	
	
	
	/**
	 * 初始化SD卡doc文件目录 .
	 * @author wangning4 2012-9-4 下午3:42:33 <br>
	 * @return savePath 保存路径
	 */
	public String getFirstDocDir() {
		String savePath;
		// 获取跟目录
		final String sdPath = getSDCardPath();

		if (!sdPath.endsWith("/")) {
			savePath = sdPath + "/" + FIRST_DOC_DIR + "/";
		} else {
			savePath = sdPath + FIRST_DOC_DIR + "/";
		}
		// 创建一级目录
		final File file = new File(savePath);
		if (!file.exists()) {
			file.mkdir();
		}
		return savePath;
	}
	
	/**
	 * 获取二级缓存目录 .
	 * @author chenyu 2013-7-18 下午3:42:33 <br>
	 * @return savePath 保存路径
	 */
	public String getSecordDirCache() {
		String savePath = getFirstDir();
		// 创建二级目录
		savePath += SECOND_DIR_CACHE + "/";
		final File file = new File(savePath);
		if (!file.exists()) {
			file.mkdir();
		}
		return savePath;
	}
	
	/**
	 * 获取二级图片目录 .
	 * @author wangning4 2012-9-4 下午3:42:33 <br>
	 * @return savePath 保存路径
	 */
	public String getSecordDirImage() {
		String savePath = getFirstDir();
		// 创建二级目录
		savePath += SECOND_DIR_IMAGE + "/";
		final File file = new File(savePath);
		if (!file.exists()) {
			file.mkdir();
		}
		return savePath;
	}
	
	/**
	 * 获取二级录音目录 .
	 * @author wangning4 2012-9-4 下午3:42:33 <br>
	 * @return savePath 保存路径
	 */
	public String getSecordDirRecord() {
		String savePath = getFirstDir();
		// 创建二级目录
		savePath += SECOND_DIR_RECORD + "/";
		final File file = new File(savePath);
		if (!file.exists()) {
			file.mkdir();
		}
		return savePath;
	}
	
	/**
	 * 获取二级视频目录 .
	 * @author wangning4 2012-9-4 下午3:42:33 <br>
	 * @return savePath 保存路径
	 */
	public String getSecordDirVideo() {
		String savePath = getFirstDir();
		// 创建二级目录
		savePath += SECOND_DIR_VIDEO + "/";
		final File file = new File(savePath);
		if (!file.exists()) {
			file.mkdir();
		}
		return savePath;
	}
	
	/**
	 * 	获取sd卡目录 .
	 * @author wangning4 2012-9-8 下午9:34:38 <br> 
	 * @return sdPath;
	 */
	public String getSDCardPath() {
		return android.os.Environment.getExternalStorageDirectory().toString();
	}

	/**
	 * 判断sd卡是否存在或可用 .
	 * @author wangning4 2012-9-4 下午4:14:42 <br>
	 * @return sdCardExist sdCardExist
	 */
	public boolean sdCardExist() {
		boolean sdCardExist = false;
		sdCardExist = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		return sdCardExist;
	}

}
