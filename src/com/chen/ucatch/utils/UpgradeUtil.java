/*
 * Copyright 2000-2012 远光软件股份有限公司 All Rights Reserved.
 */
package com.chen.ucatch.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.ucatch.R;

/**
 * APK自动升级接口 实现 .
 * 
 * @author wangning4 <br>
 * @author huanglihou <br>
 * @version 1.0.0 2013-6-9 上午8:39:53 <br>
 * @see
 * @since JDK 1.4.2.6
 */
public class UpgradeUtil {

	/**
	 * . 上下文.
	 */
	private Context context;

	/**
	 * . 获取服务端返回来的下载地址.
	 */
	private String downloadURL;

	/**
	 * . 下载时的对话框
	 */
	private Dialog downloadDialog;

	/**
	 * . 下载包安装路径
	 */
	private static String savePath;
	/**
	 * . 保存的文件名
	 */
	private static String saveFileName;

	/**
	 * . 进度条与通知ui刷新的handler和msg常量
	 */
	private ProgressBar mProgress;

	/**
	 * . 更新中的状态码
	 */
	private static final int DOWN_UPDATE = 1;
	/**
	 * . 更新完毕后的状态码
	 */
	private static final int DOWN_OVER = 2;
	/**
	 * . 更新中进度条的
	 */
	private int progress;
	/**
	 * . 下载的线程
	 */
	private Thread downLoadThread;
	/**
	 * . 用于显示是否选择了取消下载,点击取消就停止下载.默认不停止
	 */
	private boolean interceptFlag = false;

	/**
	 * . mHandler处理返回的数据.
	 */

	private Handler mHandler = new Handler() {
		public void handleMessage(final Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				// 更新安装进度
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:
				// 更新完毕则安装apk
				installApk();
				break;
			default:
				break;
			}
		};
	};

	/**
	 * UpdateManagerImpl .
	 * 
	 * @author wangning4 2013-6-9 上午8:55:29 <br>
	 * @param context2
	 *            上下文环境
	 */
	public UpgradeUtil(final Context context2) {
		this.context = context2;
	}

	/**
	 * 
	 * 显示更新提示框. <br>
	 * 
	 * @author huanglihou 2015年6月10日 上午8:45:23 <br>
	 * @param downloadURL
	 * @param mandatoryUpgrade
	 * @param upgradeTitle
	 * @param upgradeMsg
	 */
	public void showUpgradeTip(final String downloadURL,
			final boolean mandatoryUpgrade, final String upgradeTitle,
			final String upgradeMsg) {
		// 如果下载地址不为空，则采用该下载地址，否则采用默认下载地址
		if (downloadURL != null && !downloadURL.equals("")) {
			this.downloadURL = downloadURL;
		}

		/*
		 * final Dialog dialog = new Dialog(context,
		 * android.R.style.Theme_Translucent_NoTitleBar);
		 * dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		 * dialog.setContentView(R.layout.show_upgrade_tip); ((TextView)
		 * dialog.findViewById(R.id.dialog_title)).setText(upgradeTitle);
		 * ((TextView)
		 * dialog.findViewById(R.id.dialog_message)).setText(upgradeMsg);
		 */
		final Dialog dialog = new Dialog(context,
				R.style.CommonDialogTransparentStyle);
		dialog.setContentView(R.layout.settings_update_dialog);
		dialog.setCanceledOnTouchOutside(false);
		final TextView title = (TextView) dialog
				.findViewById(R.id.update_title);
		// title.setText(mandatoryUpgrade ? "检测有新版本\n更新后才能使用" :
		// "已检测有新版本，是否更新？");
		title.setText("版本更新");
		final TextView content = (TextView) dialog
				.findViewById(R.id.update_context);
		content.setText("我们推出了V2.0新版本，进行了多处更新优化，使用更流畅，快升级体验吧！");
		// 取消
		TextView cancel = (TextView) dialog.findViewById(R.id.dialog_cancel);
		cancel.setText(mandatoryUpgrade ? "取消" : "以后再说");
		// 确定
		TextView ok = (TextView) dialog.findViewById(R.id.dialog_ok);
		ok.setText(mandatoryUpgrade ? "确定" : "体验新版");

		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!mandatoryUpgrade) {
					mCallback.cancelUpgrade();
					dialog.dismiss();
				} else {
					Intent intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
					Uri content_url = Uri
							.parse("http://zmt.zhuhai.gov.cn:8080/index.html");
					intent.setData(content_url);
					context.startActivity(intent);
				}
			}
		});
		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				fileOpera();
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * 升级提示框 .
	 * 
	 * @param downloadURL
	 *            下载地址
	 * @param mandatoryUpgrade
	 *            是否需要强制升级
	 */
	public void showUpgradeTip(final String downloadURL,
			final boolean mandatoryUpgrade) {
		String upgradeTitle = "升级提醒： ";
		String upgradeMsg = "软件已更新到新版本，为了您更好的使用新功能，请及时更新。";
		showUpgradeTip(downloadURL, mandatoryUpgrade, upgradeTitle, upgradeMsg);
	}

	/**
	 * 检查SD卡，创建文件，并弹出下载进度框 .
	 */
	private void fileOpera() {
		// 获取SDcard路径：：
		String sdPath = getSDPath();
		if (sdPath == null || "".equals(sdPath)) {
			Toast.makeText(context, "SD卡不存在 ", Toast.LENGTH_LONG).show();
		} else {
			if (!sdPath.endsWith("/")) {
				savePath = sdPath + "/ygsoft/";
			} else {
				savePath = sdPath + "ygsoft/";
			}
			File file = new File(savePath);
			// 如果文件目录不存在，先创建目录，否则直接写文件
			if (!file.exists()) {
				// 如果创建目录不成功。
				if (!file.mkdir()) {
					if (!sdPath.endsWith("/")) {
						savePath += "/";
					}
				}
			}
			showCustomMessageOK("软件版本更新 ", "新版本下载 ");
		}

	}

	/**
	 * 下载apk .
	 * 
	 * @param pTitle
	 *            标题
	 * @param pMsg
	 *            提醒信息
	 */
	private void showCustomMessageOK(final String pTitle, final String pMsg) {
		downloadDialog = new Dialog(context,
				R.style.CommonDialogTransparentStyle);
		// downloadDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		downloadDialog.setContentView(R.layout.settings_upgrade_down_view);
		mProgress = (ProgressBar) downloadDialog.findViewById(R.id.progress);

		((TextView) downloadDialog.findViewById(R.id.dialog_title))
				.setText(pTitle);
		((TextView) downloadDialog.findViewById(R.id.dialog_message))
				.setText(pMsg);
		((Button) downloadDialog.findViewById(R.id.cancel)).setText("取消下载 ");
		((Button) downloadDialog.findViewById(R.id.cancel))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(final View view) {
						// downloadDialog.dismiss();
						// ((Activity) context).finish();
						interceptFlag = true;
						downloadDialog.dismiss();
					}
				});

		downloadDialog.show();
		downloadApk();
	}

	/**
	 * 获取SD卡根目录 .
	 * 
	 * @return path
	 */
	public String getSDPath() {
		File sdDir = null;
		// 判断sd卡是否存在
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			// 获取根目录
			sdDir = Environment.getExternalStorageDirectory();
			return sdDir.toString();
		} else {
			return "";
		}
	}

	/**
	 * 开启线程下载，并显示进度条 .
	 */
	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {

				// 默认apk名称
				String apkName = "";
				if (downloadURL != null && !"".equals(downloadURL)
						&& downloadURL.endsWith(".apk")) {
					int index = downloadURL.lastIndexOf("/");
					// 服务器返回最新apk名称
					apkName = downloadURL.substring(index + 1,
							downloadURL.length());
				}
				URL url = new URL(downloadURL);

				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				saveFileName = savePath + apkName;

				String apkFileName = saveFileName;
				final File apkFile = new File(apkFileName);
				FileOutputStream fos = new FileOutputStream(apkFile);

				int count = 0;
				final int max = 1024;
				byte[] buf = new byte[max];

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);

					// 点击取消就停止下载.
				} while (!interceptFlag);

				fos.close();
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				downloadDialog.dismiss();
			}
		}
	};

	/**
	 * 下载apk .
	 */
	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * 安装apk .
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		context.startActivity(i);
	}

	private CompleteListenter mCallback;

	public void setCompleteListenter(CompleteListenter listenter) {
		mCallback = listenter;
	}

	public interface CompleteListenter {
		public abstract void cancelUpgrade();
	}
}
