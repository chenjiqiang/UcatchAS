package com.chen.ucatch.utils;



import java.io.Serializable;

/**
 * 图片BO. <br>
 * @author guohuoping <br>
 * @version 1.0.0 2013-4-1 下午3:22:24 <br>
 * @see 
 * @since JDK 1.6.0
 */
public class Picture implements Serializable {
	/**
	 * 序列化版本号.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 低分率图片<br>.
	 */
	public static final int PIC_LOW = 1;
	
	/**
	 * 中分辨率图片<br>.
	 */
	public static final int PIC_MIDDLE = 2;
	
	/**
	 * 高分辨图片<br>.
	 */
	public static final int PIC_HIGH = 3;
	
	/**
	 * 获取附件的图片<br>.
	 */
	public static final int FILE_PIC = 4;
	
	/**
	 * 获取的是URL . 
	 */
	public static final int PIC_URL = 5;
	
	/**
	 * 图片ID<br>.
	 */

	private Integer picId;
	
	/**
	 * 图片名称.
	 */

	private String picName;
	
	/**
	 * 图片格式.
	 */

	private String picFormate;
	
	/**
	 * 高分辨率图<br>.
	 */

	private byte[] picHigh;
	
	/**
	 * 中分辨率图<br>.
	 */

	private byte[] picMiddle;
	
	/**
	 * 低分辨率图<br>.
	 */

	private byte[] picLow;
	
	/**
	 * 上传的用户ID<br>.
	 */

	private String userId;
	
	/**
	 * 是否默认图, 0：否     1：是<br>.
	 */
	private Integer isDefault;
	
	/**
	 * 最后更新时间<br>.
	 */
	private java.util.Date picLastDate;
	
	/**
	 *  因为以前上传头像方式很慢 ， 现在改用上传文件到服务端方式实现 。 对应的服务器临时文件ID .
	 */
	private String tempFileId;
	

	/**
	 * 获取picId. <br>
	 * @author guohuoping 2013-4-1 上午11:14:07 <br> 
	 * @return 返回picId
	 */
	public Integer getPicId() {
		return picId;
	}

	/**
	 * 设置picId. <br>
	 * @author guohuoping 2013-4-1 上午11:14:34 <br> 
	 * @param newpicId 图片ID
	 */
	public void setPicId(final Integer newpicId) {
		this.picId = newpicId;
	}

	/**
	 * 获取picName. <br>
	 * @author guohuoping 2013-4-1 上午11:15:16 <br> 
	 * @return picName
	 */
	public String getPicName() {
		return picName;
	}

	/**
	 * 设置picName. <br>
	 * @author guohuoping 2013-4-1 上午11:15:33 <br> 
	 * @param newpicName 图片名称
	 */
	public void setPicName(final String newpicName) {
		this.picName = newpicName;
	}

	/**
	 * 获取picFormate. <br>
	 * @author guohuoping 2013-4-1 上午11:15:16 <br> 
	 * @return picFormate
	 */
	public String getPicFormate() {
		return picFormate;
	}

	/**
	 * 设置picFormate. <br>
	 * @author guohuoping 2013-4-1 上午11:15:33 <br> 
	 * @param newpicFormate 图片格式
	 */
	public void setPicFormate(final String newpicFormate) {
		this.picFormate = newpicFormate;
	}

	/**
	 * 获取picHigh. <br>
	 * @author guohuoping 2013-4-1 上午11:15:16 <br> 
	 * @return picHigh
	 */
	public byte[] getPicHigh() {
		return picHigh;
	}

	/**
	 * 设置picHigh. <br>
	 * @author guohuoping 2013-4-1 上午11:15:33 <br> 
	 * @param newpicHigh 高分辨率图
	 */
	public void setPicHigh(final byte[] newpicHigh) {
		this.picHigh = newpicHigh;
	}

	/**
	 * 获取picMiddle. <br>
	 * @author guohuoping 2013-4-1 上午11:15:16 <br> 
	 * @return picMiddle
	 */
	public byte[] getPicMiddle() {
		return picMiddle;
	}

	/**
	 * 设置picMiddle. <br>
	 * @author guohuoping 2013-4-1 上午11:15:33 <br> 
	 * @param newpicMiddle 中分辨率图
	 */
	public void setPicMiddle(final byte[] newpicMiddle) {
		this.picMiddle = newpicMiddle;
	}

	/**
	 * 获取picLow. <br>
	 * @author guohuoping 2013-4-1 上午11:15:16 <br> 
	 * @return picLow
	 */
	public byte[] getPicLow() {
		return picLow;
	}

	/**
	 * 设置picLow. <br>
	 * @author guohuoping 2013-4-1 上午11:15:33 <br> 
	 * @param newpicLow 低分辨率图
	 */
	public void setPicLow(final byte[] newpicLow) {
		this.picLow = newpicLow;
	}

	/**
	 * 获取userId. <br>
	 * @author guohuoping 2013-4-1 上午11:15:16 <br> 
	 * @return userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置userId. <br>
	 * @author guohuoping 2013-4-1 上午11:15:33 <br> 
	 * @param newuserId 上传的用户ID
	 */
	public void setUserId(final String newuserId) {
		this.userId = newuserId;
	}

	/**
	 * 获取isDefault. <br>
	 * @author guohuoping 2013-4-1 上午11:15:16 <br> 
	 * @return isDefault
	 */
	public Integer getIsDefault() {
		return isDefault;
	}

	/**
	 * 设置isDefault. <br>
	 * @author guohuoping 2013-4-1 上午11:15:33 <br> 
	 * @param newisDefault 是否默认图
	 */
	public void setIsDefault(final Integer newisDefault) {
		this.isDefault = newisDefault;
	}

	/**
	 * 获取picLastDate. <br>
	 * @author guohuoping 2013-4-1 上午11:15:16 <br> 
	 * @return picLastDate
	 */
	public java.util.Date getPicLastDate() {
		return picLastDate;
	}

	/**
	 * 设置picLastDate. <br>
	 * @author guohuoping 2013-4-1 上午11:15:33 <br> 
	 * @param newpicLastDate 最后更新时间
	 */
	public void setPicLastDate(final java.util.Date newpicLastDate) {
		this.picLastDate = newpicLastDate;
	}
	/**
	 * getTempFileId
	 * @return the tempFileId
	 */
	public String getTempFileId() {
		return tempFileId;
	}
	/**
	 * setTempFileId
	 * @param tempFileId the tempFileId to set
	 */
	public void setTempFileId(final String tempFileId) {
		this.tempFileId = tempFileId;
	}

	
}
