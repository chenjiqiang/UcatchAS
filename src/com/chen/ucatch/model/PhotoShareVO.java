package com.chen.ucatch.model;

import java.io.Serializable;

/**
 * 分享图片
 * @author SXZ
 * @version 1.0  2015年8月19日 下午9:31:07
 *
 */
public class PhotoShareVO implements Serializable {
	protected static final long serialVersionUID = -1061138243803237728L;
	//分享id
	protected String id;
	//留言id
	protected String subjectId;
	//留言标题
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
	protected int replyCount;
	//分享人
	protected UserVO user;
	//当前用户是否已经点赞过
	protected boolean isPraised;
	//当前用户是否已经关注过
	protected boolean isFocused;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
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
	
	public int getReplyCount() {
		return replyCount;
	}
	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}
	public UserVO getUser() {
		return user;
	}
	public void setUser(UserVO user) {
		this.user = user;
	}
	public boolean getIsPraised() {
		return isPraised;
	}
	public void setPraised(boolean isPraised) {
		this.isPraised = isPraised;
	}
	public boolean getIsFocused() {
		return isFocused;
	}
	public void setFocused(boolean isFocused) {
		this.isFocused = isFocused;
	}
	
}
