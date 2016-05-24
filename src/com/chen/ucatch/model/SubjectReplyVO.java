package com.chen.ucatch.model;

import java.io.Serializable;

/**
 * 留言评论
 * @author SXZ
 * @version 1.0  2015年8月19日 下午9:43:10
 *
 */
public class SubjectReplyVO implements Serializable {
	private static final long serialVersionUID = 6431949870502402915L;
	//评论id
	private String replyId;
	//留言id
	private String subjectId;
	//评论用户
	private UserVO replyUser;
	//评论内容
	private String comment;
	//评论时间
	private String commentTimeShow;
	public String getReplyId() {
		return replyId;
	}
	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public UserVO getReplyUser() {
		return replyUser;
	}
	public void setReplyUser(UserVO replyUser) {
		this.replyUser = replyUser;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCommentTimeShow() {
		return commentTimeShow;
	}
	public void setCommentTimeShow(String commentTimeShow) {
		this.commentTimeShow = commentTimeShow;
	}
	
}
