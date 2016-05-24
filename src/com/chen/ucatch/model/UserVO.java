package com.chen.ucatch.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class UserVO extends User implements Serializable {
	private static final long serialVersionUID = -3007482008511098325L;
	/**
	 * 用户id
	 */
	private String id;
	// 昵称
	private String nickName;
	// 性别(男,女)
	private String sex;
	// logo图片地址
	private String picUrl;
	public List<UserVO> getList() {
		return list;
	}

	public void setList(List<UserVO> list) {
		this.list = list;
	}

	private List<UserVO> list;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return JSONObject.toJSONString(this);
	}
}
