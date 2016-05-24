package com.chen.ucatch.model;

public  class BizTypeEnum {
	/**
	 * 拾取留言,bizId表示subjectId
	 */
	public static final String pickup_subject = "pickup_subject";
	/**
	 * 评论留言,bizId表示subjectId
	 */
	public static final String reply_subject = "reply_subject";
	/**
	 * 评论分享,bizId表示shareId
	 */
	public static final String reply_share = "reply_share";
	/**
	 * 关注用户,bizId表示被关注用户id
	 */
	public static final String focused_user = "focused_user";

}
