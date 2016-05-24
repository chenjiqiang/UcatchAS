package com.chen.ucatch.model;

import java.io.Serializable;

/**
 * 
 * TODO 在此写上类的相关说明. <br>
 * 
 * @author chenjiqiang <br>
 * @version 1.0.0 2015年12月3日 下午3:26:42 <br>
 * @see
 * @since JDK 1.4.2.6
 */
public class JPushVO implements Serializable{
	public String getBizId() {
		return bizId;
	}
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	/**
	 * 唯一标识id
	 */
	private String bizId;
	/**
	 * 推送的类型
	 */
	private String bizType;
}
