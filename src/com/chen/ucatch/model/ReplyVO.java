package com.chen.ucatch.model;

/**
 * 评论vo
 * @author SXZ
 * @version 1.0  2015年9月29日 下午10:59:20
 *
 */
public class ReplyVO {
	private String id;
	//业务类型,0:留言主题,1:分享图片
	private int bizType;
	//父亲评论用户
	private UserVO parentReplyUser;
	//用户id
	private UserVO user;
	//评论内容
	private String content;
	//评论时间
	private String replyTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getBizType() {
		return bizType;
	}
	public void setBizType(int bizType) {
		this.bizType = bizType;
	}
	public UserVO getParentReplyUser() {
		return parentReplyUser;
	}
	public void setParentReplyUser(UserVO parentReplyUser) {
		this.parentReplyUser = parentReplyUser;
	}
	public UserVO getUser() {
		return user;
	}
	public void setUser(UserVO user) {
		this.user = user;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}
	
}
