package com.chen.ucatch.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.chen.ucatch.utils.ShapeLoadingDialog;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.View.OnClickListener;

public class BaseActivity extends Activity implements AMapLocationListener {
	protected LocationManagerProxy mLocationManagerProxy;
	/**
	 * 地理位置描述
	 */
	private String mLocation;
	/**
	 * 纬度
	 */
	private Double mLatitude;
	/**
	 * 经度
	 */
	private Double mLongitude;
	/**
	 * 拍摄时间
	 */
	private String mDate;
	/**
	 * 定位成功
	 */
	private boolean mPositioning_success;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
	}

	/**
	 * 初始化定位
	 */
	protected void init() {
		// 初始化定位，只采用网络定位
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		mLocationManagerProxy.setGpsEnable(false);
		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
		// 在定位结束后，在合适的生命周期调用destroy()方法
		// 其中如果间隔时间为-1，则定位只定一次,
		// 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, 2 * 1000, 15, this);
	}

	public LocationManagerProxy getmLocationManagerProxy() {
		return mLocationManagerProxy;
	}

	public String getmLocation() {
		return mLocation;
	}

	public Double getmLatitude() {
		return mLatitude;
	}

	public Double getmLongitude() {
		return mLongitude;
	}

	public String getmDate() {
		return mDate;
	}

	public boolean ismPositioning_success() {
		return mPositioning_success;
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 移除定位请求
		mLocationManagerProxy.removeUpdates(this);
		// 销毁定位
		mLocationManagerProxy.destroy();
	}

	protected void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (amapLocation != null
				&& amapLocation.getAMapException().getErrorCode() == 0) {
			// 定位成功回调信息，设置相关消息
			mLocation = amapLocation.getAddress();
			mLatitude = amapLocation.getLatitude();
			mLongitude = amapLocation.getLongitude();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date(amapLocation.getTime());
			mDate = df.format(date);
			mPositioning_success = true;
			// 移除定位请求
			mLocationManagerProxy.removeUpdates(this);
			// 销毁定位
			mLocationManagerProxy.destroy();
		}

	}
}
