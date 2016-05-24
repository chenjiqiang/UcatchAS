package com.chen.ucatch.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 分享详情主题
 * 
 * @author SXZ
 * @version 1.0 2015年8月19日 下午9:48:00
 *
 */
public class PhotoShareDetailVO extends PhotoShareVO {
	private static final long serialVersionUID = 3084996815035356328L;
	// 分享id
	protected String id;
	// 留言id
	protected String subjectId;
	// 留言标题
	protected String title;
	// 留言用户
	private UserVO subjectUser;
	// 图片地址(冗余)
	protected String picUrl;
	// 地址标签
	protected String addressLabel;
	// 描述
	protected String description;
	// 创建时间
	protected String createTime;
	// 点赞人数
	protected int praiseNum;
	// 评论人数
	protected int commentNum;
	// 分享人
	protected UserVO user;

	// 点赞用户
	private List<UserVO> praiseUserList=new ArrayList<UserVO>();

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

	public List<UserVO> getPraiseUserList() {
		return praiseUserList;
	}

	public void setPraiseUserList(List<UserVO> praiseUserList) {
		this.praiseUserList = praiseUserList;
	}

	public UserVO getSubjectUser() {
		return subjectUser;
	}

	public void setSubjectUser(UserVO subjectUser) {
		this.subjectUser = subjectUser;
	}

}
