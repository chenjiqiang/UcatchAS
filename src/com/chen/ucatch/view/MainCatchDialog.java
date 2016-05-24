package com.chen.ucatch.view;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.chen.ucatch.ConstantValue;
import com.chen.ucatch.R;
import com.chen.ucatch.ui.LeaveMsg;
import com.chen.ucatch.ui.PickActivity;

public class MainCatchDialog extends Dialog implements
		android.view.View.OnClickListener {
	/**
	 * 拾取
	 */
	private Button bt_catch;
	/**
	 * 留言
	 */
	private Button bt_leave_msg;
	private Context mContext;

	public MainCatchDialog(Context context) {
		super(context, R.style.dialog_style);
		mContext = context;
		setContentView(R.layout.ucatch_dialog);
		initView();
	}

	private void initView() {
		bt_catch = (Button) findViewById(R.id.bt_catch);
		bt_leave_msg = (Button) findViewById(R.id.bt_leave_msg);
		bt_catch.setOnClickListener(this);
		bt_leave_msg.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.equals(bt_catch)) {
			Intent intent = new Intent();
			intent.setClass(mContext, PickActivity.class);
			mContext.startActivity(intent);
			this.cancel();
			Toast.makeText(mContext, ConstantValue.CATCH_SOMETHING, 2000)
					.show();
		} else if (v.equals(bt_leave_msg)) {
			Intent intent = new Intent();
			intent.setClass(mContext, LeaveMsg.class);
			mContext.startActivity(intent);
			this.cancel();
		}
	}
}
