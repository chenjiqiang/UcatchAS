package com.chen.ucatch.model;

import com.amap.api.services.core.LatLonPoint;

public class LocationBean {
	/**
	 * poi的唯一标识
	 */
	private String poiId;
	/**
	 * 电子邮件地址
	 */
	private String Email;
	/**
	 * 名称
	 */
	private String title;
	/**
	 * 设置POI 的类型描述。
	 */
	private String typedes;
	/**
	 * 地址
	 */
	private String snippet;
	/**
	 * 经纬度
	 */
	private LatLonPoint latLonPoint;
	/**
	 * 与中心点的距离
	 */
	private int Distance;
	public String getPoiId() {
		return poiId;
	}
	public void setPoiId(String poiId) {
		this.poiId = poiId;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTypedes() {
		return typedes;
	}
	public void setTypedes(String typedes) {
		this.typedes = typedes;
	}
	public String getSnippet() {
		return snippet;
	}
	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}
	public LatLonPoint getLatLonPoint() {
		return latLonPoint;
	}
	public void setLatLonPoint(LatLonPoint latLonPoint) {
		this.latLonPoint = latLonPoint;
	}
	public int getDistance() {
		return Distance;
	}
	public void setDistance(int distance) {
		Distance = distance;
	}

}
