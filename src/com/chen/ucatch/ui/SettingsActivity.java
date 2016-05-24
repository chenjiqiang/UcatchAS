package com.chen.ucatch.ui;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alibaba.fastjson.JSON;
import com.chen.ucatch.ConstantValue;
import com.chen.ucatch.R;
import com.chen.ucatch.model.UpdateInfo;
import com.chen.ucatch.model.UserVO;
import com.chen.ucatch.utils.AppInfo;
import com.chen.ucatch.utils.JPushUtil;
import com.chen.ucatch.utils.UpgradeUtil;
import com.chen.ucatch.utils.UpgradeUtil.CompleteListenter;
import com.chen.ucatch.utils.UserInfoUtils;
import com.chen.ucatch.utils.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SettingsActivity extends FragmentActivity implements
		OnClickListener, CompleteListenter, OnCheckedChangeListener {
	private static final String TAG = SettingsActivity.class.getSimpleName();
	public static final int YES = 1;
	public static final int NO = 0;
	/**
	 * 设置是否需要推送消息
	 */
	private RelativeLayout mSettingsPushMessageLayout;

	/**
	 * 是否推送消息CheckBox
	 */
	private CheckBox mSettingsPushMessageCbx;

	/**
	 * 是否声音提醒RelativeLayout
	 */
	private RelativeLayout mSettingsVoiceReminderLayout;

	/**
	 * 是否声音提醒CheckBox
	 */
	private CheckBox mSettingsVoiceReminderCbx;

	/**
	 * 是否震动提醒RelativeLayout
	 */
	private RelativeLayout mSettingsShakeReminderLayout;

	/**
	 * 是否震动提醒CheckBox
	 */
	private CheckBox mSettingsShakeReminderCbx;

	/**
	 * 设置防打扰RelativeLayout
	 */
	private RelativeLayout mSettingsAntiInterferencePatternLayout;

	/**
	 * 是否防打扰CheckBox
	 */
	private CheckBox mSettingsAntiInterferencePatternCbx;

	/**
	 * 防打扰开始时间RelativeLayout
	 */
	private RelativeLayout mSettingsStartTimeLayout;

	/**
	 * 防打扰开始时间标签
	 */
	private TextView mSettingsStartTimeText;

	/**
	 * 防打扰结束时间
	 */
	private RelativeLayout mSettingsEndTimeLayout;

	/**
	 * 防打扰结束时间标签
	 */
	private TextView mSettingsEndTimeText;

	/**
	 * 关于RelativeLayout
	 */
	private RelativeLayout mSettingsAboutLayout;

	/**
	 * 免责声明
	 */
	private RelativeLayout mSettingsDisclaimerLayout;

	/**
	 * 检查更新
	 */
	private RelativeLayout mSettingsCheckForUpdatesLayout;

	/**
	 * 版本号标签
	 */
	private TextView mSettingsCheckForUpdatesText;

	/**
	 * 标题栏左侧返回
	 */
	private ImageView leftReturn;

	private SharedPreferences mSharedPreferences;

	private SharedPreferences.Editor mEditor;
	/**
	 * 用来保存 最后一次设置的用户
	 */
	private SharedPreferences lastUserSharedPreferences;

	private Context mContext;

	/**
	 * 防打扰开始时间
	 */
	private String mBeginHhmi = "";

	/**
	 * 防打扰结束时间
	 */
	private String mEndHhmi = "";

	/**
	 * 最新版本号
	 */
	private float mNewestVersion;

	private String mUpgradeUrl;
	/**
	 * 强制升级版本
	 */
	private float mForcedUpgrade;

	private AsyncHttpClient mAsyncHttpClient;

	/**
	 * 获取升级信息的handler
	 */
	private JsonHttpResponseHandler mGetUpgradeInfoResponseHandler;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_settings);
		initVariable();
		initView();
		initEvent();
		getUpgradeInfo();
	}

	private void initEvent() {
		leftReturn.setOnClickListener(this);
		mSettingsStartTimeLayout.setOnClickListener(this);
		mSettingsEndTimeLayout.setOnClickListener(this);
		mSettingsAboutLayout.setOnClickListener(this);
		mSettingsCheckForUpdatesLayout.setOnClickListener(this);
		mSettingsDisclaimerLayout.setOnClickListener(this);
		mSettingsPushMessageLayout.setOnClickListener(this);
		mSettingsVoiceReminderLayout.setOnClickListener(this);
		mSettingsAntiInterferencePatternLayout.setOnClickListener(this);
		mSettingsShakeReminderLayout.setOnClickListener(this);

		mSettingsAntiInterferencePatternCbx.setOnCheckedChangeListener(this);
		mSettingsPushMessageCbx.setOnCheckedChangeListener(this);
		mSettingsVoiceReminderCbx.setOnCheckedChangeListener(this);
		mSettingsShakeReminderCbx.setOnCheckedChangeListener(this);
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		leftReturn = (ImageView) findViewById(R.id.leftimageview);
		((TextView) findViewById(R.id.titletext)).setText("设置");
		findViewById(R.id.rightpraise).setVisibility(View.GONE);
		findViewById(R.id.rightcollect).setVisibility(View.GONE);
		findViewById(R.id.rightshare).setVisibility(View.GONE);

		mSettingsPushMessageLayout = (RelativeLayout) findViewById(R.id.settings_push_message);
		mSettingsPushMessageCbx = (CheckBox) findViewById(R.id.settings_push_message_checkbox);
		mSettingsPushMessageCbx.setChecked(mSharedPreferences.getInt(
				ConstantValue.SETTINGS_ALLOW_PUSH, YES) == YES);

		mSettingsVoiceReminderLayout = (RelativeLayout) findViewById(R.id.settings_voice_reminder);
		mSettingsVoiceReminderCbx = (CheckBox) findViewById(R.id.settings_voice_reminder_checkbox);
		mSettingsVoiceReminderCbx.setChecked(mSharedPreferences.getInt(
				ConstantValue.SETTINGS_VOICEREMINDER, YES) == YES);

		mSettingsShakeReminderLayout = (RelativeLayout) findViewById(R.id.settings_shake_reminder);
		mSettingsShakeReminderCbx = (CheckBox) findViewById(R.id.settings_shake_reminder_checkbox);
		mSettingsShakeReminderCbx.setChecked(mSharedPreferences.getInt(
				ConstantValue.SETTINGS_SHAKEREMINDER, YES) == YES);

		mSettingsAntiInterferencePatternLayout = (RelativeLayout) findViewById(R.id.settings_anti_interference_pattern);
		mSettingsAntiInterferencePatternCbx = (CheckBox) findViewById(R.id.settings_anti_interference_pattern_checkbox);
		mSettingsAntiInterferencePatternCbx.setChecked(mSharedPreferences
				.getInt(ConstantValue.SETTINGS_ANTI_DISTURB_MODE, NO) == YES);

		mSettingsStartTimeLayout = (RelativeLayout) findViewById(R.id.settings_start_time);
		mSettingsStartTimeText = (TextView) findViewById(R.id.settings_start_time_show);
		mSettingsStartTimeText.setText(mSharedPreferences.getString(
				ConstantValue.SETTINGS_ANTI_DISTURB_MODE_START_TIME, ""));

		mSettingsEndTimeLayout = (RelativeLayout) findViewById(R.id.settings_end_time);
		mSettingsEndTimeText = (TextView) findViewById(R.id.settings_end_time_show);
		mSettingsEndTimeText.setText(mSharedPreferences.getString(
				ConstantValue.SETTINGS_ANTI_DISTURB_MODE_END_TIME, ""));

		mSettingsAboutLayout = (RelativeLayout) findViewById(R.id.settings_about);
		mSettingsDisclaimerLayout = (RelativeLayout) findViewById(R.id.settings_disclaimer);
		mSettingsCheckForUpdatesLayout = (RelativeLayout) findViewById(R.id.settings_check_for_updates);
		mSettingsCheckForUpdatesText = (TextView) findViewById(R.id.settings_check_for_updates_show);

		mSettingsCheckForUpdatesText.setText("当前版本号"
				+ AppInfo.getVersion(mContext));

	}

	/**
	 * 初始化变量
	 */
	private void initVariable() {
		mContext = getApplicationContext();
		UserVO user = UserInfoUtils.getUser(mContext);
		mSharedPreferences = getApplicationContext().getSharedPreferences(
				user.getId() + ConstantValue.SETTING_SHARED_PREFERENCE_NAME,
				Context.MODE_PRIVATE);

		mEditor = mSharedPreferences.edit();
		mAsyncHttpClient = Utility.getAsyncHttpClient();
		mGetUpgradeInfoResponseHandler = new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				Log.e(TAG, "getUpgrade------success" + response.toString());
				try {
					if (response.getInt("code") == 0) {
						UpdateInfo info = null;
						try {
							info = com.alibaba.fastjson.JSONObject.parseObject(
									response.getString("data"),
									UpdateInfo.class);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (info != null) {
							mNewestVersion = Float.valueOf(info
									.getDataVersion());
							mUpgradeUrl = info.getDownloadUrl();
							mForcedUpgrade = Float.valueOf(info
									.getForcedUpgrade());
						}
					} else {

					}
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				Log.e(TAG, "getUpgrade------failure" + responseString);
			}
		};
	}

	@Override
	public void onClick(View v) {
		if (v.equals(mSettingsEndTimeLayout)) {
			Calendar c = Calendar.getInstance();
			new TimePickerDialog(
					this,
					new TimePickerDialog.OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							mEndHhmi = hourOfDay + (minute <= 9 ? ":0" : ":")
									+ minute;
							mSettingsEndTimeText.setText(mEndHhmi);
							mEditor.putString(
									ConstantValue.SETTINGS_ANTI_DISTURB_MODE_END_TIME,
									mEndHhmi).apply();
						}
					}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
					true).show();
		} else if (v.equals(mSettingsStartTimeLayout)) {
			Calendar c = Calendar.getInstance();
			new TimePickerDialog(
					this,
					new TimePickerDialog.OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							mBeginHhmi = hourOfDay + (minute <= 9 ? ":0" : ":")
									+ minute;
							mSettingsStartTimeText.setText(mBeginHhmi);
							mEditor.putString(
									ConstantValue.SETTINGS_ANTI_DISTURB_MODE_START_TIME,
									mBeginHhmi).apply();
						}
					}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
					true).show();
		} else if (v.equals(mSettingsAboutLayout)) {
//			AboutDisclaimerDialog aboutDialog = new AboutDisclaimerDialog(this,
//					true);
//			aboutDialog.show();

		} else if (v.equals(mSettingsCheckForUpdatesLayout)) {
			float currentVersion = AppInfo.getVersion(mContext);
			UpgradeUtil upgradeUtil = new UpgradeUtil(this);
			upgradeUtil.setCompleteListenter(this);
			if (currentVersion == mNewestVersion) {
				mSettingsCheckForUpdatesText.setText("当前已经是最新版本");
			} else if (mForcedUpgrade <= currentVersion
					&& AppInfo.getVersion(mContext) < mNewestVersion) {
				upgradeUtil.showUpgradeTip(mUpgradeUrl, false);
			} else {
				upgradeUtil.showUpgradeTip(mUpgradeUrl, true);
			}
		} else if (v.equals(leftReturn)) {
			finish();

		} else if (v.equals(mSettingsDisclaimerLayout)) {
//			AboutDisclaimerDialog disclaimerDialog = new AboutDisclaimerDialog(
//					this, false);
//			disclaimerDialog.show();

		} else if (v.equals(mSettingsPushMessageLayout)) {
			mSettingsPushMessageCbx.setChecked(!mSettingsPushMessageCbx
					.isChecked());
			saveSettings(ConstantValue.SETTINGS_ALLOW_PUSH,
					mSettingsPushMessageCbx);
		} else if (v.equals(mSettingsVoiceReminderLayout)) {
			mSettingsVoiceReminderCbx.setChecked(!mSettingsVoiceReminderCbx
					.isChecked());
			saveSettings(ConstantValue.SETTINGS_VOICEREMINDER,
					mSettingsVoiceReminderCbx);
		} else if (v.equals(mSettingsShakeReminderLayout)) {
			mSettingsShakeReminderCbx.setChecked(!mSettingsShakeReminderCbx
					.isChecked());
			saveSettings(ConstantValue.SETTINGS_SHAKEREMINDER,
					mSettingsShakeReminderCbx);
		} else if (v.equals(mSettingsAntiInterferencePatternLayout)) {
			mSettingsAntiInterferencePatternCbx
					.setChecked(!mSettingsAntiInterferencePatternCbx
							.isChecked());
			saveSettings(ConstantValue.SETTINGS_ANTI_DISTURB_MODE,
					mSettingsAntiInterferencePatternCbx);
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView.equals(mSettingsPushMessageCbx)) {
			saveSettings(ConstantValue.SETTINGS_ALLOW_PUSH,
					mSettingsPushMessageCbx);
		} else if (buttonView.equals(mSettingsVoiceReminderCbx)) {
			saveSettings(ConstantValue.SETTINGS_VOICEREMINDER,
					mSettingsVoiceReminderCbx);
		} else if (buttonView.equals(mSettingsShakeReminderCbx)) {
			saveSettings(ConstantValue.SETTINGS_SHAKEREMINDER,
					mSettingsShakeReminderCbx);
		} else if (buttonView.equals(mSettingsAntiInterferencePatternCbx)) {
			saveSettings(ConstantValue.SETTINGS_ANTI_DISTURB_MODE,
					mSettingsAntiInterferencePatternCbx);
		}
	}

	/**
	 * 保存配置到sharedperferance
	 * 
	 * @param key
	 * @param checkBox
	 */
	private void saveSettings(String key, CheckBox checkBox) {
		mEditor.putInt(key, checkBox.isChecked() ? YES : NO).apply();
		try {
			JPushUtil.customSettings(getApplicationContext());
		} catch (ParseException e) {
			e.printStackTrace();
			Log.i(TAG, "设置极光推送 属性异常");
		}
	}

	private void getUpgradeInfo() {
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("type", "android");
		StringEntity entity = null;
		try {
			entity = new StringEntity(JSON.toJSONString(paraMap), "UTF-8");
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json;charset=utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// mAsyncHttpClient.post(mContext, CommonConfig.GET_UPGRADE_INFO,
		// entity,
		// "application/json;charset=utf-8",
		// mGetUpgradeInfoResponseHandler);
	}

	@Override
	public void cancelUpgrade() {

	}

}
