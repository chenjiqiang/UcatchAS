package com.chen.ucatch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.alibaba.fastjson.JSONObject;
import com.chen.ucatch.model.JPushVO;
import com.chen.ucatch.model.UserVO;
import com.chen.ucatch.ui.SettingsActivity;
import com.chen.ucatch.utils.JPushUtil;
import com.chen.ucatch.utils.StringUtil;
import com.chen.ucatch.utils.UserInfoUtils;

/**
 * 接收 极光推送 发送过来的广播
 * 
 * @author liuhuan2
 * 
 */
public class JPushBroadcastReceiver extends BroadcastReceiver {

	private final String TAG = "JPushBroadcastReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Bundle bundle = intent.getExtras();
		Log.i(TAG, "[MyReceiver] onReceive - " + intent.getAction()
				+ ", extras: " + printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.i(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Log.i(TAG,
					"[MyReceiver] 接收到推送下来的自定义消息: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));

			String msgId = bundle.getString(JPushInterface.EXTRA_MSG_ID).trim();
			String title = bundle.getString(JPushInterface.EXTRA_TITLE);
			String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String keyValueData = bundle.getString(JPushInterface.EXTRA_EXTRA);
			long notificationID = Long.parseLong(msgId);
			Log.i(TAG, "notificationID===" + notificationID);
			JPushVO jPushVO = null;
			try {
				jPushVO = JSONObject.parseObject(keyValueData, JPushVO.class);
			} catch (Exception e) {
				Log.i(TAG, "解析推送数据异常");
			}
			if (jPushVO != null) {
				UserVO userVO = UserInfoUtils.getUser(context);
				// 游客 有铃声 也有震动
				if (userVO == null) {

					JPushUtil.setSoundAndVibration(context, title, content,
							(int) notificationID, jPushVO, true, true);
					// 获取最后设置推送的用户
					SharedPreferences lastSharedPreferences = context
							.getSharedPreferences(
									ConstantValue.LAST_SETTING_NAME,
									Context.MODE_PRIVATE);
					String userId = lastSharedPreferences.getString(
							ConstantValue.LAST_SETTING_USER_ID, null);
					if (StringUtil.isEmptyString(userId)) {
						// 这里是 游客从未注册登录过
						JPushUtil.setSoundAndVibration(context, title, content,
								(int) notificationID, jPushVO, true, true);
					} else {
						// 这里是 用户登录后 又 注销登录
						lastSettings(context, userId, title, content,
								(int) notificationID, jPushVO);
					}

				} else {
					// 这里是 用户登录
					lastSettings(context, userVO.getId(), title, content,
							(int) notificationID, jPushVO);
				}
			}

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {

			Log.i(TAG, "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.i(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.i(TAG, "[MyReceiver] 用户点击打开了通知");
			// 打开自定义的Activity
			

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Log.i(TAG,
					"[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等...

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
			boolean connected = intent.getBooleanExtra(
					JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			// Log.i(TAG, "[MyReceiver]" + intent.getAction()
			// +" connected state change to "+connected);
		} else {
			// Log.i(TAG, "[MyReceiver] Unhandled intent - " +
			// intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	/**
	 * 最后一次的用户设置的极光推送
	 * 
	 * @param context
	 *            上下文
	 * @param userId
	 *            最后一次登录用户的id
	 * @param title
	 *            推送的内容（通知栏弹出时显示的文字）；
	 * @param notificationID
	 *            通知id
	 * @param jPushVO
	 *            推送的json数据
	 */
	private void lastSettings(Context context, String userId, String title,
			String content, int notificationID, JPushVO jPushVO) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				userId + ConstantValue.SETTING_SHARED_PREFERENCE_NAME,
				Context.MODE_PRIVATE);
		int vibration = sharedPreferences.getInt(
				ConstantValue.SETTINGS_SHAKEREMINDER, SettingsActivity.YES);
		int sound = sharedPreferences.getInt(
				ConstantValue.SETTINGS_VOICEREMINDER, SettingsActivity.YES);
		int silence = sharedPreferences.getInt(
				ConstantValue.SETTINGS_ANTI_DISTURB_MODE, SettingsActivity.NO);
		Log.i(TAG, "vibration==" + vibration);
		Log.i(TAG, "sound==" + sound);
		if (silence == SettingsActivity.YES) {
			// 免打扰
			JPushUtil.setSoundAndVibration(context, title, content,
					notificationID, jPushVO, false, false);
		} else {
			JPushUtil.setSoundAndVibration(context, title, content,
					notificationID, jPushVO, sound == SettingsActivity.YES,
					vibration == SettingsActivity.YES);
		}
	}
}
