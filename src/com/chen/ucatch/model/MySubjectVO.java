package com.chen.ucatch.model;



import java.io.Serializable;

/**
 * 我的留言vo
 * @author sxz  
 * @version 1.0 2015-08-17
 */
public class MySubjectVO implements Serializable {
	private static final long serialVersionUID = 54011784252840072L;
	
	private String id;
	//用户
	private UserVO user;
	//留言标题
	private String title;
	//地址
	private String addressLabel;
	//评论人数
	private int replyCount;
	
	//发表时间
	private String createTime;	
	
	//点赞人数
	private int praiseCount;
	
	
	public String getAddressLabel() {
		return addressLabel;
	}
	public void setAddressLabel(String addressLabel) {
		this.addressLabel = addressLabel;
	}
	public int getReplyCount() {
		return replyCount;
	}
	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public UserVO getUser() {
		return user;
	}
	public void setUser(UserVO user) {
		this.user = user;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getPraiseCount() {
		return praiseCount;
	}
	public void setPraiseCount(int praiseCount) {
		this.praiseCount = praiseCount;
	}
	
}
