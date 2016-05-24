package com.chen.ucatch.ui;

import com.chen.ucatch.utils.UserInfoUtils;

import android.content.Intent;
import android.os.Bundle;

public class GuideActivity extends MyBaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (UserInfoUtils.getUser(mContext) != null) {
			Intent intent = new Intent();
			intent.setClass(mContext, UcatchMainActivity.class);
			startActivity(intent);
		}else{
			Intent intent = new Intent();
			intent.setClass(mContext, LoginActivity.class);
			startActivity(intent);
		}
		finish();
	}
}
