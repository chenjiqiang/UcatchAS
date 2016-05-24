/*
 * Copyright 2000-2020 YGSoft.Inc All Rights Reserved.
 */
package com.chen.ucatch.view;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.chen.ucatch.R;
/**
 * 右边是取消按钮 .<br>
 * @author liulongzhenhai <br>
 * @version 1.0.0 2013-5-16<br>
 * @see
 * @since JDK 1.4.2.6
 */
public class CancelProgressDialog extends ProgressDialog {

	/**
	 * activity .
	 */
	Activity activity;

	/**
	 * CancelProgressDialog . <br>
	 * @author liulongzhenhai 2013-5-16 下午5:52:34 <br>
	 * @param context Context
	 */
	public CancelProgressDialog(final Context context) {
		super(context);
	}

	/**
	 * CancelProgressDialog . <br>
	 * @author liulongzhenhai 2013-5-16 下午5:52:34 <br>
	 * @param context Context
	 */
	public CancelProgressDialog(final Activity context) {
		super(context);
		activity = context;
	}

	/**
	 * CancelProgressDialog . <br>
	 * @author liulongzhenhai 2013-5-16 下午5:52:34 <br>
	 * @param context Context
	 * @param finish 是否更新
	 */
	public CancelProgressDialog(final Activity context, final boolean finish) {
		super(context);
		activity = context;
		isFinish = finish;
	}

	/**
	 * IProgressCallBack .
	 */
	IProgressCallBack mProgressCallBack;
	/**
	 * 标题 .
	 */
	String title = "";

	/**
	 * 标题 .
	 */
	TextView mTitle;
	/**
	 * 置是否关闭的时候关闭窗口 .
	 */
	boolean isFinish = true;
	/**
	 * 返回如果是activity的话就关闭窗口 .
	 */
	boolean callBackFinishActivity = false;

	/**
	 * {@inheritDoc}
	 * @see android.app.ProgressDialog#onCreate(android.os.Bundle)
	 * @author liulongzhenhai 2013-5-16 下午5:49:42 <br>
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cancel_progress_dialog);
		mTitle = (TextView) findViewById(R.id.title);
		mTitle.setText(title);
		findViewById(R.id.cancel).setVisibility(View.GONE);
//		findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
//
//			/**
//			 * {@inheritDoc}
//			 * @see android.view.View.OnClickListener#onClick(android.view.View)
//			 * @author liulongzhenhai 2013-5-16 下午5:52:25 <br>
//			 */
//			@Override
//			public void onClick(final View v) {
//				dismiss();
//				if (mProgressCallBack != null) {
//					mProgressCallBack.cancel();
//				}
//				finishActivity();
//			}
//		});
		setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(final DialogInterface dialog, final int keyCode, final KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					dismiss();
					if (mProgressCallBack != null) {
						mProgressCallBack.cancel();
					}
					finishActivity();
				}
				return false;
			}

		});
	}

	/**
	 * 关闭窗口, . <br>
	 * @author liulongzhenhai 2013-10-14 下午1:20:30 <br>
	 */
	private void finishActivity() {
		if (activity != null && isFinish) {
			activity.finish();
		}
	}

	/**
	 * 设置是否关闭的时候关闭窗口 . <br>
	 * @author liulongzhenhai 2013-10-27 下午3:48:20 <br>
	 * @param finish true false
	 */
	public void setFinish(final boolean finish) {
		isFinish = finish;
	}

	/**
	 * 设置标题 . <br>
	 * @author liulongzhenhai 2013-5-16 下午5:53:44 <br>
	 * @param title 标题
	 */
	public void setTitle(final String title) {
		// this.title = title;
	}

	/**
	 * {@inheritDoc}
	 * @see android.app.Dialog#setTitle(int)
	 * @author liulongzhenhai 2014年2月17日 下午3:25:07 <br>
	 */
	@Override
	public void setTitle(final int titleResId) {
		// this.title=this.getContext().getResources().getString(titleResId);
	}

	/**
	 * 点击返回 . <br>
	 * @author liulongzhenhai 2013-5-16 下午5:51:19 <br>
	 * @param callback IProgressCallBack
	 */
	public void setCallBack(final IProgressCallBack callback) {
		this.mProgressCallBack = callback;
	}

	/**
	 * 返回直接关闭程序 . <br>
	 * @author liulongzhenhai 2013-10-14 上午11:53:13 <br>
	 */
	public void setCallBackFinish() {
		callBackFinishActivity = true;
	}

	/**
	 * 显示进度 . <br>
	 * @author liulongzhenhai 2013-5-16 下午5:54:43 <br>
	 * @param title 标题
	 */
	public void show(final String title) {
		this.title = title;
		if (mTitle != null) {
			mTitle.setText(title);
		}
		show();
	}

	/**
	 * 取消接口 .<br>
	 * @author liulongzhenhai <br>
	 * @version 1.0.0 2013-5-16<br>
	 * @see
	 * @since JDK 1.4.2.6
	 */
	public interface IProgressCallBack {
		/**
		 * 主动取消了 . <br>
		 * @author liulongzhenhai 2013-5-16 下午5:04:48 <br>
		 */
		void cancel();
	}
}
