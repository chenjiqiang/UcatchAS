package com.chen.ucatch.ui;

import com.amap.api.location.AMapLocation;
import com.chen.ucatch.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class TestForLocation extends BaseActivity implements OnClickListener{
	private TextView mTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine);
		init();
//		mTv = (TextView) findViewById(R.id.tv_show_mine);
//		mTv.setOnClickListener(this);
	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		// TODO Auto-generated method stub
		super.onLocationChanged(amapLocation);
//		mTv.setText("获取到定位信息"+amapLocation.getAddress());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		init();
	}

}
