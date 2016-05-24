package com.chen.ucatch.model;

import java.io.Serializable;

/**
 * 留言主题
 * @author SXZ
 * @version 1.0  2015年8月19日 下午9:31:07
 *
 */
public class SubjectVO implements Serializable {
	protected static final long serialVersionUID = -1061138243803237728L;

	protected String id;
	//留言
	protected String title;
	//图片地址(冗余)
	protected String picUrl;
	//地址标签
	protected String addressLabel;
	//描述
	protected String description;
	//创建时间
	protected String createTime;
	//点赞人数
	protected int praiseNum;
	//评论人数
	protected int commentNum;
	//留言人
	protected UserVO user;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getAddressLabel() {
		return addressLabel;
	}
	public void setAddressLabel(String addressLabel) {
		this.addressLabel = addressLabel;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getPraiseNum() {
		return praiseNum;
	}
	public void setPraiseNum(int praiseNum) {
		this.praiseNum = praiseNum;
	}
	public int getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}
	public UserVO getUser() {
		return user;
	}
	public void setUser(UserVO user) {
		this.user = user;
	}
	
}
