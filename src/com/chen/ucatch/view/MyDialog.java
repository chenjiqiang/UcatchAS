package com.chen.ucatch.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.chen.ucatch.R;
import com.chen.ucatch.ui.LeaveMsg;

public class MyDialog extends Dialog implements
		android.view.View.OnClickListener {
	private Context mContext;
	/**
	 * 关闭按钮
	 */
	private Button mCancel;
	/**
	 * 确认按钮
	 */
	private Button mOK;

	public MyDialog(Context context) {
		super(context, R.style.activity_dialog);
		setContentView(R.layout.my_dialog);
		this.mContext = context;
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mCancel = (Button) findViewById(R.id.bt_dialog_cancel);
		mOK = (Button) findViewById(R.id.bt_dialog_ok);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(mCancel)) {
			cancel();
		} else if (v.equals(mOK)) {
			Intent intent = new Intent();
			intent.setClass(mContext, LeaveMsg.class);
			mContext.startActivity(intent);
		}
	}

}
