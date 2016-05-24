package com.chen.ucatch;

/**
 * 
 * 常量的定义
 * 
 * @author chenjiqiang <br>
 * @version 1.0.0 2015年8月19日 下午4:52:26 <br>
 * @see
 * @since JDK 1.4.2.6
 */
public class ConstantValue {

	/**
	 * 设置界面sharedperference的名字
	 */
	public static final String SETTING_SHARED_PREFERENCE_NAME = "settings";
	// 设置界面 0表示不开启，1表示开启
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
	 * 保存 用户最后一次设置推送 的sharedperference 的名字
	 */
	public static final String LAST_SETTING_NAME = "last_settings_name";
	/**
	 * 最后一次设置推送 的用户标志
	 */
	public static final String LAST_SETTING_USER_ID = "last_settings_user_id";
	/**
	 * 是否开启防打扰模式
	 */
	public static final String SETTINGS_ANTI_DISTURB_MODE = "anti_disturb_mode";

	public static final String SETTINGS_ANTI_DISTURB_MODE_START_TIME = "anti_disturb_mode_start_time";

	public static final String SETTINGS_ANTI_DISTURB_MODE_END_TIME = "anti_disturb_mode_end_time";
	/**
	 * 网络无响应
	 */
	public static final int NETWORK_NO_RESPONE = 1015;
	/**
	 * 网络无响应
	 */
	public static final String NETWORK_NO_RESPONE_TEXT = "网络不给力";
	/**
	 * 服务器无响应
	 */
	public static final int SERVER_NO_RESPONE = 1016;
	/**
	 * 服务器无响应
	 */
	public static final String SERVER_NO_RESPONE_TEXT = "网络不给力";
	/**
	 * 注册成功的提示语
	 */
	public static final String REGSITER_SUCCESS = "注册成功...";
	/**
	 * 拾取
	 */
	public static final String CATCH_SOMETHING = "拾取";
	/**
	 * 留言
	 */
	public static final String LEAVE_MSG = "留言";
	/**
	 * 图片（头像）缓存路径
	 */
	public static final String SDCASHEPATH = "/ucatch/picture.png";
	/**
	 * （谁可以看）选择好友可见的列表
	 */
	public static final String CHOOSE_FRIENDS = "choose_friends";
	/**
	 * （谁可以看）选择的类型
	 */
	public static final String CHOOSE_TYPE_RESULT_CODE = "choose_friends_type";
	/**
	 * 手机号验证
	 */
	public static final String CHECK_PHONE = "请输入正确的手机号码";
	/**
	 * 登录密码验证
	 */
	public static final String CHECK_LOGIN_PWD = "请输入8-16位密码";
	/**
	 * 校验用户昵称长度
	 */
	public static final String USERNAME_LENGTH_WRONG = "昵称长度不能超过12个字符";
	/**
	 * 留言成功的提示语
	 */
	public static final String LEAVE_MSG_SUCCESS = "留言成功";
	/**
	 * 图片没上传成功
	 */
	public static final String PIC_NO_SUCCESS = "图片还没上传成功！";
}
