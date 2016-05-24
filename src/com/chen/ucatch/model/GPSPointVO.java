package com.chen.ucatch.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

public class GPSPointVO implements Serializable {
	private static final long serialVersionUID = 0x43678661L;
	/**
	 * 经度,本初子午线以东为东经为正，以西为西经为负值
	 */
	public double lon;
	/**
	 * 纬度，北纬为正，南纬为负
	 */
	public double lat;

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	@Override
	public String toString() {
		JSONObject obj = (JSONObject) JSONObject.toJSON(this);
		String str = new String(obj.toString());
		return str;
	}

}
