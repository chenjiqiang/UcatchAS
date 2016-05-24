package com.chen.ucatch.model;

import java.io.Serializable;

/**
 * 返回值vo
 * @author Administrator
 *
 */
public class ReturnVO implements Serializable {
	private static final long serialVersionUID = -6096330790446164145L;
	
	public static final int SUCCESS=1;
	public static final int ERROR=0;
	
	private int successFlag;
	private String message;
	private Object data;
	
	public static ReturnVO ok(Object data){
		ReturnVO v=new ReturnVO();
		v.setSuccessFlag(SUCCESS);
		v.setData(data);
		return v;
	}
	
	public static ReturnVO error(String msg){
		ReturnVO v=new ReturnVO();
		v.setSuccessFlag(ERROR);
		v.setMessage(msg);
		return v;
	}
	
	public int getSuccessFlag() {
		return successFlag;
	}
	public void setSuccessFlag(int successFlag) {
		this.successFlag = successFlag;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
}
