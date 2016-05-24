package com.chen.ucatch.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class MyBaseActivity extends Activity {
	protected Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext=this;
	}
}
