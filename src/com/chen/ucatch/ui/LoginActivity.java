package com.chen.ucatch.ui;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.afinal.simplecache.ACache;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.chen.ucatch.ConstantValue;
import com.chen.ucatch.R;
import com.chen.ucatch.model.ReturnVO;
import com.chen.ucatch.model.UserVO;
import com.chen.ucatch.utils.HttpRequestUtils;
import com.chen.ucatch.utils.JPushCheckUtils;
import com.chen.ucatch.utils.JPushUtil;
import com.chen.ucatch.utils.NumberUtil;
import com.chen.ucatch.utils.ServerUrl;
import com.chen.ucatch.utils.StringUtil;
import com.chen.ucatch.utils.UserInfoUtils;
import com.chen.ucatch.view.CancelProgressDialog;
import com.loopj.android.http.AsyncHttpClient;

@SuppressLint("NewApi")
public class LoginActivity extends Activity implements OnClickListener {
	private final String TAG = "LoginActivity";
	private Context mContext;
	/**
	 * 用户名
	 */
	private EditText username;

	/**
	 * 密码信息
	 */
	private EditText userPassowrd;

	/**
	 * 登录按钮
	 */
	private Button login;

	/**
	 * 点我注册
	 */
	private TextView register;

	/**
	 * 忘记密码
	 */
	private TextView forgetPassword;
	/**
	 * 进度框
	 */
	private CancelProgressDialog mCancelProgressDialog;
	/**
	 * 缓存的工具
	 */
	private ACache mCache;
	private Handler handler;
	/**
	 * 用户普通登录
	 */
	private final int USER_LOGIN_CODE = 1;
	/**
	 * qq,微博,微信登录
	 */
	private final int USER_LOGINBYQQ_CODE = 1002;
	/**
	 * 设置极光推送别名的 标志
	 */
	private final int MSG_SET_ALIAS_CODE = 1003;
	/**
	 * 登录结束后的返回码
	 */
	private final int RESULT_CODE = 11;
	/**
	 * 网络连接对象
	 */
	private AsyncHttpClient asyncHttpClient;
	private UserVO userVO;
	private JSONObject user;
	/**
	 * 标题布局
	 */
	private RelativeLayout title_relative;
	/**
	 * 记住密码
	 */
	private CheckBox remember_pwd;
	/**
	 * 是否记住密码
	 */
	private boolean mRemember_Pwd = true;
	/**
	 * 界面整体布局
	 */
	private LinearLayout loginLayout;
	private ColorStateList whiteColor;
	private ColorStateList redColor;
	public static final int LOGIN_SUCCESS_CODE = 8888;
	/**
	 * 第一次按下返回键
	 */
	private long firstPressTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_dialog);
		this.mContext = this;
		whiteColor = mContext.getResources().getColorStateList(R.color.white);
		redColor = mContext.getResources().getColorStateList(R.color.red);
		mCancelProgressDialog = new CancelProgressDialog(mContext);
		mCache = ACache.get(mContext);
		user = new JSONObject();
		userVO = new UserVO();
		String phoneNumber = null;
		if (mCache.getAsJSONObject("user") != null) {
			userVO = com.alibaba.fastjson.JSONObject.parseObject(mCache
					.getAsJSONObject("user").toString(), UserVO.class);
			phoneNumber = userVO.getPhone();
			Log.i("phoneNum=", phoneNumber);
		}
		if (userVO != null) {
			phoneNumber = userVO.getPhone();
		}
		initView(phoneNumber);
		initHandler();
		initListener();
		initEvent();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// updateService.shutdown();
		// 极光推送
		JPushInterface.onPause(mContext);

	}

	@Override
	public void onResume() {
		super.onResume();
		// 极光推送
		JPushInterface.onResume(mContext);

	}


	private void initView(String phoneNumber) {
		loginLayout = (LinearLayout) findViewById(R.id.login_layout);
		title_relative = (RelativeLayout) findViewById(R.id.title_relative);
		title_relative.setOnClickListener(this);
		username = (EditText) findViewById(R.id.login_username_edittext);
		if (!StringUtil.isEmptyString(phoneNumber)) {
			username.setText(phoneNumber);
		}
		remember_pwd = (CheckBox) findViewById(R.id.remember_password_checkBox);
		userPassowrd = (EditText) findViewById(R.id.login_password_edittext);
		login = (Button) findViewById(R.id.login_submit);
		login.setOnClickListener(confirm);
		register = (TextView) findViewById(R.id.login_register);
		register.setOnClickListener(this);
		forgetPassword = (TextView) findViewById(R.id.login_forgetpassword);
		forgetPassword.setOnClickListener(this);
		if (userVO.isRememberPwd()) {
			userPassowrd.setText(userVO.getLoginPwd());
		}
		if (isSatsiy()) {
			login.setBackgroundResource(R.drawable.usercenter_register_button_blue_bg);
			login.setTextColor(whiteColor);
		}
	}

	/**
	 * 
	 * 显示密码.
	 * 
	 * @author chenjiqiang 2015年10月29日 下午4:09:12 <br>
	 * @param edit
	 */
	private void showPassword(EditText edit) {
		edit.setTransformationMethod(HideReturnsTransformationMethod
				.getInstance());
	}

	private void initEvent() {
		remember_pwd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					mRemember_Pwd = true;
				} else {
					mRemember_Pwd = false;
				}
			}
		});
	}

	private void initHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				mCancelProgressDialog.cancel();
				if (msg.what == MSG_SET_ALIAS_CODE) {
					JPushInterface.setAlias(mContext, (String) msg.obj,
							mAliasCallback);
					return;
				}
				ReturnVO vo = (ReturnVO) msg.obj;
				if (vo == null) {
					if (msg.what == ConstantValue.NETWORK_NO_RESPONE) {
						Toast.makeText(mContext,
								ConstantValue.NETWORK_NO_RESPONE_TEXT, 2000)
								.show();
					} else if (msg.what == ConstantValue.SERVER_NO_RESPONE) {
						Toast.makeText(mContext,
								ConstantValue.SERVER_NO_RESPONE_TEXT, 2000)
								.show();
					}
					// else {
					// Toast.makeText(mContext, "" + msg.what, 1000).show();
					// }
					return;
				}
				if (vo.getSuccessFlag() == 1) {
					switch (msg.what) {
					case USER_LOGIN_CODE:
						userVO = com.alibaba.fastjson.JSONObject.parseObject(
								String.valueOf(vo.getData()), UserVO.class);
						userVO.setRememberPwd(mRemember_Pwd);
						userVO.setLoginPwd(userPassowrd.getText().toString());
						try {
							user = new JSONObject(userVO.toString());
							mCache.put("user", user);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							// 登录成功后设置用户或游客自己上一次设置的 一些 推送属性
							JPushUtil.customSettings(mContext);
							// 注册用户登录后 设置报料推送的别名
							JPushInterface.setAlias(mContext, UserInfoUtils
									.getUser(mContext).getId(), mAliasCallback);
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						Intent intent = new Intent();
						intent.setClass(mContext, UcatchMainActivity.class);
						startActivity(intent);
						finish();
						Toast.makeText(mContext, vo.getMessage(), 1000).show();
						break;
					}
				} else {
					Toast.makeText(mContext, vo.getMessage(), 1000).show();
				}

			}
		};
	}

	/**
	 * 极光推送设置别名的回调
	 */
	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "极光推送 设置别名成功";
				Log.i(TAG, logs);
				break;

			case 6002:
				logs = "极光推送 设置别名失败，60秒后重新设置";
				Log.i(TAG, logs);
				if (JPushCheckUtils.isConnected(mContext)) {
					handler.sendMessageDelayed(
							handler.obtainMessage(MSG_SET_ALIAS_CODE, alias),
							1000 * 60);
				} else {
					Log.i(TAG, "没有网络");
				}
				break;

			default:
				logs = "Failed with errorCode = " + code;
				Log.e(TAG, logs);
			}
		}

	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		mCache.put("userPhone", username.getText().toString());
		if (v.equals(register)) {
			Intent it = new Intent();
			it.setClass(mContext, RegsiterActivity.class);
			mContext.startActivity(it);
		} else if (v.equals(forgetPassword)) {
			// mCache.put("userPhone", username.getText().toString());
			// Intent it = new Intent();
			// it.setClass(mContext, ForgetPasswordActivity.class);
			// mContext.startActivity(it);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long secondTime = System.currentTimeMillis();
			if (secondTime - firstPressTime > 2000) { // 如果两次按键时间间隔大于1000毫秒，则不退出
				Toast.makeText(mContext, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				firstPressTime = secondTime;// 更新firstTime
				return true;
			} else {
				int pid = android.os.Process.myPid();
				android.os.Process.killProcess(pid);
				System.exit(0);// 否则退出程序
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	private void initListener() {
		// sendVerficationCode.setOnClickListener(this);
		username.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (TextUtils.isEmpty(s)) {
					login.setBackgroundResource(R.drawable.usercenter_register_button_gray_bg);
					login.setTextColor(redColor);
					// login.setOnClickListener(null);
				} else if (isSatsiy()) {
					login.setBackgroundResource(R.drawable.usercenter_register_button_blue_bg);
					login.setTextColor(whiteColor);
					login.setOnClickListener(confirm);
				} else {
					login.setBackgroundResource(R.drawable.usercenter_register_button_gray_bg);
					login.setTextColor(redColor);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		userPassowrd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (TextUtils.isEmpty(s)) {
					login.setBackgroundResource(R.drawable.usercenter_register_button_gray_bg);
					login.setTextColor(redColor);
					// login.setOnClickListener(null);
				} else if (isSatsiy()) {
					login.setBackgroundResource(R.drawable.usercenter_register_button_blue_bg);
					login.setTextColor(whiteColor);
					login.setOnClickListener(confirm);
				} else {
					login.setBackgroundResource(R.drawable.usercenter_register_button_gray_bg);
					login.setTextColor(redColor);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	private boolean isSatsiy() {
		return !TextUtils.isEmpty(userPassowrd.getText().toString())
				&& userPassowrd.getText().toString().length() >= 8
				&& userPassowrd.getText().toString().length() <= 16
				&& NumberUtil.isMobileNum(username.getText().toString());
		// && !TextUtils.isEmpty(username.getText().toString())
	}

	private android.view.View.OnClickListener confirm = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.equals(login)) {
				String userName = username.getText().toString();
				if (TextUtils.isEmpty(userName)) {
					Toast.makeText(mContext, ConstantValue.CHECK_PHONE, 2000)
							.show();
					return;
				} else if (TextUtils.isEmpty(userPassowrd.getText().toString())) {
					Toast.makeText(mContext, ConstantValue.CHECK_LOGIN_PWD,
							2000).show();
				} else {
					userName = StringUtil.replaceBlank(userName);
					mCancelProgressDialog.show("正在登录...");
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("phone", userName);
					params.put("password", userPassowrd.getText().toString());
					HttpRequestUtils.post(mContext, ServerUrl.LOGIN, params,
							handler, USER_LOGIN_CODE);
				}
			}
		}
	};
}
