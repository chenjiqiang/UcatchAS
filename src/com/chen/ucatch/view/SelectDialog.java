package com.chen.ucatch.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.chen.ucatch.R;

public class SelectDialog extends AlertDialog implements
		android.view.View.OnClickListener {
	/**
	 * 选择性别的radioGroup
	 */
	private RadioGroup mRadioGroup_Sex;
	/**
	 * 选择全部性别
	 */
	private RadioButton mRadioButton_All_Sex;
	/**
	 * 选择男
	 */
	private RadioButton mRadioButton_Man_Sex;
	/**
	 * 选择女
	 */
	private RadioButton mRadioButton_Woman_Sex;
	/**
	 * 选择时间段的
	 */
	private RadioGroup mRadioGroup_Time;
	/**
	 * 第一个按钮
	 */
	private RadioButton mRadioButton_one;
	/**
	 * 第二个按钮
	 */
	private RadioButton mRadioButton_two;
	/**
	 * 第三个按钮
	 */
	private RadioButton mRadioButton_three;
	/**
	 * 第四个按钮
	 */
	private RadioButton mRadioButton_four;
	/**
	 * 取消按钮
	 */
	private Button bt_cancle;
	/**
	 * 确定按钮
	 */
	private Button bt_Ok;
	/**
	 * 选择的性别
	 */
	private String mSex;
	/**
	 * 选择的时间
	 */
	private String mTime;
	private Context mContext;

	public SelectDialog(Context context, int theme) {
		super(context, theme);
		this.mContext = context;
		
	}

	public SelectDialog(Context context) {
		super(context);
		this.mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSex="全部";
		mTime="十五分钟前";
		setContentView(R.layout.find_seletor);
		bt_cancle = (Button) findViewById(R.id.bt_cancel);
		bt_Ok = (Button) findViewById(R.id.bt_ok);
		bt_cancle.setOnClickListener(this);
		bt_Ok.setOnClickListener(this);
		mRadioGroup_Sex = (RadioGroup) findViewById(R.id.find_seletor_sex);
		mRadioGroup_Time = (RadioGroup) findViewById(R.id.find_seletor_time);
		mRadioButton_All_Sex = (RadioButton) findViewById(R.id.find_seletor_all_sex);
		mRadioButton_Man_Sex = (RadioButton) findViewById(R.id.find_seletor_man_sex);
		mRadioButton_Woman_Sex = (RadioButton) findViewById(R.id.find_seletor_woman_sex);
		mRadioButton_one = (RadioButton) findViewById(R.id.find_seletor_one);
		mRadioButton_two = (RadioButton) findViewById(R.id.find_seletor_two);
		mRadioButton_three = (RadioButton) findViewById(R.id.find_seletor_three);
		mRadioButton_four = (RadioButton) findViewById(R.id.find_seletor_four);
		mRadioGroup_Time
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						int radioButtonId = group.getCheckedRadioButtonId();
						// 根据ID获取RadioButton的实例
						RadioButton rb = (RadioButton) SelectDialog.this
								.findViewById(radioButtonId);
						mTime = rb.getText().toString();

						// 更新文本内容，以符合选中项
						Toast.makeText(mContext,
								"你选择了" + rb.getText().toString(), 2000).show();
					}
				});
		mRadioGroup_Sex
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						// 获取变更后的选中项的ID
						int radioButtonId = group.getCheckedRadioButtonId();
						// 根据ID获取RadioButton的实例
						RadioButton rb = (RadioButton) SelectDialog.this
								.findViewById(radioButtonId);
						mSex = rb.getText().toString();
						// 更新文本内容，以符合选中项
						Toast.makeText(mContext,
								"你选择了" + rb.getText().toString(), 2000).show();
					}
				});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(bt_Ok)) {
			Toast.makeText(mContext, "你选择了" + mSex + "和" + mTime, 2000).show();
		} else if (v.equals(bt_cancle)) {
			this.cancel();
		}
	}
}