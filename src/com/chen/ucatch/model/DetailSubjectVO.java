package com.chen.ucatch.model;

import java.util.List;

/**
 * 留言详情主题
 * @author SXZ
 * @version 1.0  2015年8月19日 下午9:48:00
 *
 */
public class DetailSubjectVO extends SubjectVO {
	private static final long serialVersionUID = 3084996815035356328L;
	//点赞用户
	private List<UserVO> praiseUserList;
	public List<UserVO> getPraiseUserList() {
		return praiseUserList;
	}
	public void setPraiseUserList(List<UserVO> praiseUserList) {
		this.praiseUserList = praiseUserList;
	}
	
	
}
