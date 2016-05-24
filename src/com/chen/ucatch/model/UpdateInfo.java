package com.chen.ucatch.model;
/**
 * 
 * 版本更新信息. <br>
 * @author huanglihou <br>
 * @version 1.0.0 2015年6月9日 下午1:42:50 <br>
 * @see 
 * @since JDK 1.4.2.6
 */
public class UpdateInfo {
	/**
	 * 强制更新版本
	 */
	private String forcedUpgrade;
	/**
	 * 日常版本
	 */
	private String dataVersion;
	/**
	 * 下载链接
	 */
	private String downloadUrl;
	/**
	 * 发布时间
	 */
	private String publishDate;
	/**
	 * 安装包大小
	 */
	private String size;
	/**
	 * 升级描述
	 */
	private String descript;
	public String getForcedUpgrade() {
		return forcedUpgrade;
	}
	public void setForcedUpgrade(String forcedUpgrade) {
		this.forcedUpgrade = forcedUpgrade;
	}
	public String getDataVersion() {
		return dataVersion;
	}
	public void setDataVersion(String dataVersion) {
		this.dataVersion = dataVersion;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
	
}
