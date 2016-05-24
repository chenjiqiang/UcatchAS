package com.chen.ucatch.utils;

import java.text.ParseException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.chen.ucatch.R;
import com.chen.ucatch.model.BizTypeEnum;
import com.chen.ucatch.model.JPushVO;
import com.chen.ucatch.ui.SettingsActivity;
import com.chen.ucatch.ui.UcatchMainActivity;

/**
 * 推送设置 工具类
 * 
 * @author liuhuan2
 * 
 */
public class JPushUtil {

	/**
	 * 用户的 设置
	 * 
	 * @throws ParseException
	 */
	public static void customSettings(Context context) throws ParseException {
		Log.i("JPushUtil", "设置用户推送");
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				AppConstant.SETTING_SHARED_PREFERENCE_NAME,
				Context.MODE_PRIVATE);
		// =====================是否允许推送消息 1 允许 0 不允许========================
		int yesOrNo = sharedPreferences.getInt(AppConstant.SETTINGS_ALLOW_PUSH,
				SettingsActivity.YES);
		if (yesOrNo == SettingsActivity.YES) {
			// 恢复推送
			JPushInterface.resumePush(context);
		} else {
			// 停止推送
			JPushInterface.stopPush(context);
		}

		// ======================是否设置防打扰模式(即：静音模式)======================
		int isSilence = sharedPreferences.getInt(
				AppConstant.SETTINGS_ANTI_DISTURB_MODE, SettingsActivity.NO);

		if (isSilence == SettingsActivity.YES) {
			// 一整天都是免打扰模式
			JPushInterface.setSilenceTime(context, 0, 0, 23, 59);
		} else {
			JPushInterface.setSilenceTime(context, 0, 0, 0, 0);
		}

		/*
		 * //========================设置推送的时间段========先注释==========================
		 * ====== //开始时间字符串格式 String startTime =
		 * sharedPreferences.getString(AppConstant
		 * .SETTINGS_ANTI_DISTURB_MODE_START_TIME, ""); //结束时间字符串格式 String
		 * endTime = sharedPreferences.getString(AppConstant.
		 * SETTINGS_ANTI_DISTURB_MODE_END_TIME, "");
		 * 
		 * 
		 * if(StringUtil.isEmptyString(startTime) ||
		 * StringUtil.isEmptyString(endTime)){ //Sdk1.2.9 –
		 * 新功能:set的值为null,则任何时间都可以收到消息和通知，set的size为0，则表示任何时间都收不到消息和通知. //此次SDK
		 * 版本为1.7。5 JPushInterface.setPushTime(context, null, 0, 23);
		 * 
		 * }else if(!StringUtil.isEmptyString(startTime) &&
		 * !StringUtil.isEmptyString(endTime)){ //当开始时间和 结束时间都有的时候
		 * SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		 * 
		 * Date starDate = format.parse(startTime); Date endDate =
		 * format.parse(endTime);
		 * 
		 * //开始推送的时间 int startHour = starDate.getHours(); //结束时间 int endtHour =
		 * endDate.getHours(); //整个星期都接受推送 Set<Integer> days = new
		 * HashSet<Integer>(); days.add(1); days.add(2); days.add(3);
		 * days.add(4); days.add(5); days.add(6); //星期日 days.add(0);
		 * //设置接收推送的时间段 JPushInterface.setPushTime(context, days, startHour,
		 * endtHour); }
		 */
	}

	/**
	 * 默认的推送设置设置 即游客 或者 用户退出登录的时候
	 */
	public static void defaultSettings(Context context) {
		Log.i("JPushUtil", "设置默认推送");
		// 恢复推送
		JPushInterface.resumePush(context);
		// Sdk1.2.9 –
		// 新功能:set的值为null,则任何时间都可以收到消息和通知，set的size为0，则表示任何时间都收不到消息和通知.
		// 此次SDK 版本为1.7。5
		JPushInterface.setPushTime(context, null, 0, 23);
		// 取消免打扰
		JPushInterface.setSilenceTime(context, 0, 0, 0, 0);
	}

	/**
	 * 有铃声 有震动
	 * 
	 * @param context
	 * @param jPushVO
	 *            推送下来的额外数据 json格式
	 */
	public static void setSoundAndVibration(Context context, String title,
			String content, int msgId, JPushVO jPushVO, boolean hasSound,
			boolean hasVibration) {

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// 把推送的值传给 主界面
		Intent intent = new Intent(context, UcatchMainActivity.class);
		intent.putExtra(UcatchMainActivity.JPUSH_INFO, jPushVO);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, msgId,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder notification = new NotificationCompat.Builder(
				context);
		// Notification.Builder notification = new
		// Notification.Builder(context);
		notification.setAutoCancel(true);
		notification.setTicker(content);
		notification.setSmallIcon(R.drawable.ic_launcher);
		if (jPushVO.getBizType().equals(BizTypeEnum.pickup_subject)) {
			notification.setContentTitle(title);
		} else if (jPushVO.getBizType().equals(BizTypeEnum.focused_user)) {
			notification.setContentTitle(title);
		} else if (jPushVO.getBizType().equals(BizTypeEnum.reply_share)) {
			notification.setContentTitle(title);
		} else if (jPushVO.getBizType().equals(BizTypeEnum.reply_subject)) {
			notification.setContentTitle(title);
		} else {
			notification.setContentTitle("个人中心");
		}

		notification.setContentText(content);

		notification.setDefaults(Notification.DEFAULT_LIGHTS);
		// 有震动
		if (hasVibration) {
			notification.setDefaults(Notification.DEFAULT_VIBRATE
					| Notification.DEFAULT_LIGHTS);
		}
		// 有铃声
		if (hasSound) {
			notification.setDefaults(Notification.DEFAULT_SOUND
					| Notification.DEFAULT_LIGHTS);
		}
		if (hasVibration && hasSound) {
			// 有铃声 有震动，有闪烁灯
			notification.setDefaults(Notification.DEFAULT_SOUND
					| Notification.DEFAULT_VIBRATE
					| Notification.DEFAULT_LIGHTS);
		}
		notification.setContentIntent(pendingIntent);

		notificationManager.notify(msgId, notification.build());

	}
}
