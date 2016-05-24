package com.chen.ucatch.utils;

public class ServerUrl {
	/**
	 * 后台服务器的地址
	 */
	public static String WS_URL = "http://115.159.102.155:8080/photo/restful";
	/**
	 * 本机的服务器地址
	 */
	// public static String WS_URL = "http://10.52.10.40:8080/photo/restful";
	public static String DOLOADPIC_URL = "http://115.159.102.155:8080/photo/restful";
	/**
	 * 上传拍照拾取的图片，返回id
	 */
	public static String UPLOAD_PICK_PIC = WS_URL + "/pic/uploadSubjectPic";
	/**
	 * 保存留言
	 */
	public static String SAVE_MESSAGE = WS_URL + "/subject/save";
	/**
	 * 获取留言
	 */
	public static String GET_MSG = WS_URL + "/subject/pickupSubject";
	/**
	 * 留言评论详情
	 */
	public static String COMMENT_DETAIL = WS_URL + "/subject/getSubjectDetail";
	/**
	 * 我的留言列表
	 */
	public static String MY_MESSAGE_LIST = WS_URL + "/subject/listMySubject";
	/**
	 * 留言点赞和取消留言点赞
	 */
	public static String SAVE_MSG_PRAISE = WS_URL + "/subject/praiseSubject";
	/**
	 * 注册
	 */
	public static String REGISTER = WS_URL + "/user/register";
	/**
	 * 登录
	 */
	public static String LOGIN = WS_URL + "/user/login";
	/**
	 * 退出登录
	 */
	public static String LOGINOUT = WS_URL + "/user/loginout";

	/**
	 * 获取好友列表
	 */
	public static String GETFRIENDS = WS_URL + "/user/getMyFriends";
	/**
	 * 关注和取消关注
	 */
	public static String ISATTENTION = WS_URL + "/user/focusDestUser";
	/**
	 * 获取个人信息详情
	 */
	public static String GET_USERVODETAIL = WS_URL + "/user/getUserDetail";
	/**
	 * 更新昵称
	 */
	public static String UPDATE_USERNICKNAME = WS_URL + "/user/updateNickName";
	/**
	 * 获取验证码
	 */
	public static String GETSMSCODE = WS_URL + "/user/sendRegChkCode";
	/**
	 * 添加收藏
	 */
	public static String ADD_COLLECT = WS_URL + "/user/sendRegChkCode";
	/**
	 * 取消收藏
	 */
	public static String CANCEL_COLLECT = WS_URL + "/user/sendRegChkCode";

	/**
	 * 保存分享的评论
	 */
	public static String SAVE_COMMENT = WS_URL + "/reply/reply2Share";
	/**
	 * 获取分享的评论列表
	 */
	public static String GET_COMMENT = WS_URL + "/reply/listReply";
	/**
	 * 保存留言评论
	 */
	public static String SAVE_MESSAGE_COMMENT = WS_URL + "/reply/reply2Subject";
	/**
	 * 点赞和取消点赞
	 */
	public static String SAVE_PRAISE = WS_URL + "/photoShare/praiseShare";
	/**
	 * 拍照拾取
	 */
	public static String PHOTOSHAPE = WS_URL + "/photoShare/save";
	/**
	 * 发布分享图片列表
	 */
	public static String GET_PUBLICSHED_SHARE_LIST = WS_URL
			+ "/photoShare/listPublishedShare";
	/**
	 * 发布分享图片列表
	 */
	public static String GET_ATTENTION_LIST = WS_URL
			+ "/photoShare/listMyFocusShare";

	/**
	 * 列表详情
	 */
	public static String PUBLICSHED_SHARE_DETAIL = WS_URL
			+ "/photoShare/getShareDetail";
	/**
	 * 获取我发布的列表
	 */
	public static String MYSHARELIST = WS_URL + "/photoShare/listMyShare";
}
