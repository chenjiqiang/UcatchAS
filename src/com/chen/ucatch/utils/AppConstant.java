/*
 * Copyright 2000-2020 YGSoft.Inc All Rights Reserved.
 */
package com.chen.ucatch.utils;

/**
 * 系统的一些常量信息 .<br>
 * @author liulongzhenhai <br>
 * @version 1.0.0 2013-4-28<br>
 * @see
 * @since JDK 1.4.2.6
 */
public final class AppConstant {
	/**
	 * AppConstant . <br>
	 * @author liulongzhenhai 2013-4-28 上午10:14:58 <br>
	 */
	private AppConstant() {

	}
	
	/**
	 * 设置界面sharedperference的名字
	 */
	public static final String SETTING_SHARED_PREFERENCE_NAME = "settings";
	/**
	 *  保存 用户最后一次设置推送  的sharedperference 的名字
	 */
	public static final String LAST_SETTING_NAME="last_settings_name";
	/**
	 * 最后一次设置推送 的用户标志
	 */
	public static final String LAST_SETTING_USER_ID="last_settings_user_id";
	//设置界面 0表示不开启，1表示开启
	/**
	 * 是否声音提醒 SharedPreferences存储标识
	 */
	public static final String SETTINGS_VOICEREMINDER = "VoiceReminder";
	/**
	 * 是否震动提醒 SharedPreferences存储标识
	 */
	public static final String SETTINGS_SHAKEREMINDER = "ShakeReminder"; 
	/**
	 * 是否允许消息推送SharedPreferences存储标识
	 */
	public static final String SETTINGS_ALLOW_PUSH = "allow_push";
	/**
	 * 是否开启防打扰模式
	 */
	public static final String SETTINGS_ANTI_DISTURB_MODE = "anti_disturb_mode";
	
	public static final String SETTINGS_ANTI_DISTURB_MODE_START_TIME = "anti_disturb_mode_start_time";
	
	public static final String SETTINGS_ANTI_DISTURB_MODE_END_TIME = "anti_disturb_mode_end_time";
	
	/**
	 * 报料人id
	 */
	public static final String PROVIDER_ID = "provide_id";
	/**
	 * 报料人姓名
	 */
	public static final String PROVIDER_NAME = "provide_name";
	/**
	 * 报料人手机号码
	 */
	
	public static final String PROVIDER_MOBILE = "provider_mobile";
	/**
	 * 匿名报料人手机号码
	 */
	public static final String PROVIDER_ANONYMITY_MOBILE = "provider_anonymity_mobile";
	public static final String PROVIDER_ANONYMITY_NAME = "provider_anonymity_name";
	public static final String PROVIDER_ANONYMITY_ID = "provider_anonymity_id";
	
	public static final String GET_PROVIDE_LIST_MOBILE = "get_provide_list_mobile";
	
	

	/**
	 * 主ACTIVITY是否上层显示 .
	 */
	public static boolean mainActivityIsShow = false;
	/**
	 * 缓存路径 .
	 */
	public static final String SDCASHEPATH = "sdcachepath";

	/**
	 * 图片类型 .
	 */
	public static final String IMAGE = "image/";

	/**
	 * 用于APPBROADCASTRECEIVER的 "action".
	 */
	public static final String ACTION = "action";

	/**
	 * 用于 APPBROADCASTRECEIVER,如果要传对象,就传入到这个的字符串.
	 */
	public static final String OBJECT = "object";

	/**
	 * 用于记录是哪个界面打开的 .
	 */
	public static final String TAGID = "tagid";
	/**
	 * 用于 APPBROADCASTRECEIVER 头像路径 .
	 */
	public static final String PATH = "path";

	/**
	 * 要打开指定的窗口<br>
	 * class.getName .
	 */
	public static final String OPENCLASS = "openclass";

	/**
	 * 和OPENCLASS配合 <br>
	 * 传入的是字符串.<br>
	 */
	public static final String OPENTAG = "opentag";

	/**
	 * 获取地理位置成功的页面. <br>
	 */
	public static final String GET_GPS_ADDRESS_SUCCESS_TAG = "intent_action_get_gps_address_success";

	/**
	 * 我的声音列表， 如果选择的卡项下标值 .
	 */
	public static String feedbackSelectedName = "";	
	
	/**
	 * ACTION .<br>
	 * @author liulongzhenhai <br>
	 * @version 1.0.0 2013-5-7<br>
	 * @see
	 * @since JDK 1.4.2.6
	 */
	public static enum Action {
		/**
		 * 注册<Br>
		 * 注意:头像:因为如果注册,修改了头像,都需要从网上重新拿,所以为了好的用户体验,先吧缓存的给拿来.
		 */
		regeist(1),
		/**
		 * 登录 .
		 */
		login(2),
		/**
		 * 切换账号 .
		 */
		switchActount(3),
		/**
		 * 个人资料 .
		 */
		userInfo(4),
		/**
		 * 关闭所有窗体,不包含自己.
		 */
		closeAllActivit(5),
		/**
		 * 根据类型和ID打开窗体.
		 */
		// openActivity(6),
		/**
		 * 刷新信息提醒,以及底部数字.
		 */
		refreshUserOtherInfo(7),

		/**
		 * 用于在登录成功后打开其他窗口的.
		 */
		notificationOtherActivity(8),
		/**
		 * 推送的刷新 .
		 */
		pushRefresh(9),
		/**
		 * 清除底部的数字 .
		 */
		clearCount(10),
		/**
		 * 注销 .
		 */
		logout(11),
		/**
		 * 清除标题 .
		 */
		backTitle(12),
		/**
		 * 刷新信息公开列表 .
		 */
		isInformationRefresh(13);
		/**
		 * code .
		 */
		int code;

		/**
		 * 获取标识码 . <br>
		 * @author liulongzhenhai 2013-5-7 下午5:34:03 <br>
		 * @return code
		 */
		public int getCode() {
			return code;
		}

		/**
		 * Action . <br>
		 * @author liulongzhenhai 2013-5-7 下午5:34:13 <br>
		 * @param code code
		 */
		Action(final int code) {
			this.code = code;
		}

	}
}
