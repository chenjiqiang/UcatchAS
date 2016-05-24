package com.chen.ucatch.model;

import java.io.Serializable;

/**
 * 个人资料详情
 * @author SXZ
 * @version 1.0  2015年9月4日 下午9:46:41
 *
 */
public class UserDetailVO implements Serializable {
	private static final long serialVersionUID = 765748200851098325L;
	private String id;
	private String phone;
	//昵称
	private String nickName;
	//性别(男,女)
	private String sex;
	
	private String picId;
	//logo图片地址
	private String picUrl;
	//好友数
	private int friendNum;
	//关注数
	private int focusNum;
	//粉丝数
	private int fanNum;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public String getPicId() {
		return picId;
	}
	public void setPicId(String picId) {
		this.picId = picId;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public int getFriendNum() {
		return friendNum;
	}
	public void setFriendNum(int friendNum) {
		this.friendNum = friendNum;
	}
	public int getFocusNum() {
		return focusNum;
	}
	public void setFocusNum(int focusNum) {
		this.focusNum = focusNum;
	}
	public int getFanNum() {
		return fanNum;
	}
	public void setFanNum(int fanNum) {
		this.fanNum = fanNum;
	}
	
}
