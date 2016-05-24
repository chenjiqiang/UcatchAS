package com.chen.ucatch.ui;

import java.util.HashMap;
import java.util.Map;

import org.afinal.simplecache.ACache;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.chen.ucatch.ConstantValue;
import com.chen.ucatch.R;
import com.chen.ucatch.model.ReturnVO;
import com.chen.ucatch.model.UserVO;
import com.chen.ucatch.utils.HttpRequestUtils;
import com.chen.ucatch.utils.NumberUtil;
import com.chen.ucatch.utils.ServerUrl;
import com.chen.ucatch.utils.StringUtil;
import com.chen.ucatch.view.CancelProgressDialog;

@SuppressLint("ResourceAsColor")
public class RegsiterActivity extends Activity implements OnClickListener {
	/**
	 * 标题栏左侧返回
	 */
	private ImageView leftReturn;
	/**
	 * 标题栏标题
	 */
	private TextView tvTitle;

	/**
	 * 手机号
	 */
	private EditText mobilePhone;

	/**
	 * 发送验证码
	 */
	private Button sendVerficationCode;

	/**
	 * 验证码
	 */
	private EditText verficationCode;

	/**
	 * 密码
	 */
	private EditText password;

	/**
	 * 注册协议选择
	 */
	private TextView agreementFlag;

	/**
	 * true表示同意协议，false表示不同意
	 */
	private boolean agreeFlag = true;

	/**
	 * 同意协议图
	 */
	private Drawable dr1;
	/**
	 * 不同意协议图
	 */
	private Drawable dr2;
	/**
	 * 注册协议
	 */
	private TextView agreementTextView;

	/**
	 * 确定注册
	 */
	private Button confirmRegister;
	/**
	 * 进度框
	 */
	private CancelProgressDialog mCancelProgressDialog;
	/**
	 * 获取验证码
	 */
	private static final int GET_VERFICATION_CODE = 10001;

	/**
	 * 基础时间（秒）
	 */
	private static final int BASETIME = 60;
	/**
	 * 倒数的时间（毫秒）
	 */
	private static final int RECIPROCALTIME = 1000;
	/**
	 * 时间60秒 .
	 */
	private int time = BASETIME;
	private Handler handler;
	/**
	 * 注册
	 */
	private static final int REGISTER_CODE = 10002;
	private Context mContext;
	private UserVO userVO;
	private ACache mCache;
	ColorStateList whiteColor;
	ColorStateList redColor;
	/**
	 * 倒计时的标志
	 */
	public static final int COUNTDOWN = 1003;
	/**
	 * 滑动按钮
	 */
	private Switch mSwitch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_main);
		mContext = this;
		userVO = new UserVO();
		mCache = ACache.get(mContext);
		mCancelProgressDialog = new CancelProgressDialog(mContext);
		initView();
		initListener();
		initHandler();
		whiteColor = getResources().getColorStateList(R.color.white);
		redColor = getResources().getColorStateList(R.color.red);

	}

	private void setRetransCanClick() {
		handler.removeCallbacks(runnable);
		sendVerficationCode
				.setBackgroundResource(R.drawable.usercenter_verification_button_red_bg);
		sendVerficationCode.setText("重新验证");
		sendVerficationCode.setOnClickListener(this);
	}

	private void initHandler() {
		handler = new Handler() {
			public void handleMessage(Message msg) {
				mCancelProgressDialog.cancel();
				if (msg.what == RegsiterActivity.COUNTDOWN) {
					sendVerficationCode.setText("重新验证(" + time + ")");
					handler.post(runnable);
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
					} else {
						Toast.makeText(mContext,
								ConstantValue.SERVER_NO_RESPONE_TEXT, 2000)
								.show();
					}
					return;
				}
				if (vo.getSuccessFlag() == 1) {
					switch (msg.what) {
					case GET_VERFICATION_CODE:
						Toast.makeText(mContext, vo.getMessage(), 2000).show();
						disposeVerficationCode(msg);
						break;
					case REGISTER_CODE:
						finish();
						Toast.makeText(mContext,
								ConstantValue.REGSITER_SUCCESS, 2000).show();
						break;
					default:
						break;
					}
				} else if (vo.getSuccessFlag() == 0) {
					Toast.makeText(mContext, vo.getMessage(), 2000).show();
				}
			}

		};
	}

	/**
	 * 
	 * 处理验证码
	 * 
	 * @author chenjiqiang 2015年9月23日 下午2:04:07 <br>
	 * @param msg
	 */
	protected void disposeVerficationCode(Message msg) {
		mCancelProgressDialog.cancel();
		handler.post(runnable);
		sendVerficationCode
				.setBackgroundResource(R.drawable.usercenter_verification_button_gray_bg);
		sendVerficationCode.setText("重新验证(" + time + ")");
		sendVerficationCode.setOnClickListener(null);
	}

	private void initView() {
		mSwitch = (Switch) findViewById(R.id.swich_button);
		mSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					password.setTransformationMethod(HideReturnsTransformationMethod
							.getInstance());
				} else {
					password.setTransformationMethod(PasswordTransformationMethod
							.getInstance());
				}

			}
		});
		// 设置标题栏数据
		leftReturn = (ImageView) findViewById(R.id.leftimageview);
		tvTitle = (TextView) findViewById(R.id.titletext);
		tvTitle.setText("填写注册信息");
		findViewById(R.id.rightpraise).setVisibility(View.GONE);
		findViewById(R.id.rightcollect).setVisibility(View.GONE);
		findViewById(R.id.rightshare).setVisibility(View.GONE);
		findViewById(R.id.rightTextView).setVisibility(View.GONE);

		mobilePhone = (EditText) findViewById(R.id.register_mobile_phone);
		sendVerficationCode = (Button) findViewById(R.id.register_send_verification_code);
		password = (EditText) findViewById(R.id.register_password);
		verficationCode = (EditText) findViewById(R.id.register_verification_code);
		agreementFlag = (TextView) findViewById(R.id.select_flag);
		agreementTextView = (TextView) findViewById(R.id.select_agreement);
		confirmRegister = (Button) findViewById(R.id.register_ok);
		confirmRegister.setOnClickListener(confirm);
		dr1 = mContext.getResources().getDrawable(R.drawable.checked);
		dr1.setBounds(0, 0, dr1.getIntrinsicWidth(), dr1.getIntrinsicHeight());
		dr2 = mContext.getResources().getDrawable(R.drawable.check_nomal);
		dr2.setBounds(0, 0, dr2.getIntrinsicWidth(), dr2.getIntrinsicHeight());
	}

	private void initListener() {
		leftReturn.setOnClickListener(this);
		sendVerficationCode.setOnClickListener(this);
		agreementFlag.setOnClickListener(this);
		agreementTextView.setOnClickListener(this);
		mobilePhone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (TextUtils.isEmpty(s)) {
					confirmRegister
							.setBackgroundResource(R.drawable.usercenter_register_button_gray_bg);
					confirmRegister.setTextColor(R.color.red);
					// confirmRegister.setOnClickListener(null);
				} else {
					if (isSatsiy()) {
						confirmRegister
								.setBackgroundResource(R.drawable.usercenter_register_button_blue_bg);
						confirmRegister.setOnClickListener(confirm);
						confirmRegister.setTextColor(whiteColor);
					} else {
						confirmRegister
								.setBackgroundResource(R.drawable.usercenter_register_button_gray_bg);
						confirmRegister.setTextColor(R.color.red);
						// confirmRegister.setOnClickListener(null);
					}
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

		password.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (TextUtils.isEmpty(s)) {
					confirmRegister
							.setBackgroundResource(R.drawable.usercenter_register_button_gray_bg);
					// confirmRegister.setOnClickListener(null);
					confirmRegister.setTextColor(redColor);
				} else {
					if (isSatsiy()) {
						confirmRegister
								.setBackgroundResource(R.drawable.usercenter_register_button_blue_bg);
						confirmRegister.setOnClickListener(confirm);
						confirmRegister.setTextColor(whiteColor);
					} else {
						confirmRegister
								.setBackgroundResource(R.drawable.usercenter_register_button_gray_bg);
						// confirmRegister.setOnClickListener(null);
						confirmRegister.setTextColor(redColor);
					}
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
		return NumberUtil.isMobileNum(mobilePhone.getText().toString())
				&& !TextUtils.isEmpty(password.getText().toString())
				&& password.getText().toString().toString().length() >= 8
				&& password.getText().toString().toString().length() <= 16;
	}

	private OnClickListener confirm = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!agreeFlag) {
				Toast.makeText(mContext, "请同意服务许可及隐私协议！", 2000).show();
				// CommonUI.showToast(context, "请同意服务许可及隐私协议！");
				return;
			}
			String phone = mobilePhone.getText().toString();
			phone = StringUtil.replaceBlank(phone);
			if (!NumberUtil.isMobileNum(mobilePhone.getText().toString())) {
				Toast.makeText(mContext, "手机号码不正确", 2000).show();
				return;
			} else if (password.getText().toString().length() < 8
					|| password.getText().toString().length() > 16) {
				Toast.makeText(mContext, "请输入8-16位密码", 2000).show();
				return;
			}
			mCancelProgressDialog.show("注册中...");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("phone", phone);
			// params.put("smsCodeType", 1);
			params.put("checkCode", verficationCode.getText().toString());
			params.put("password", password.getText().toString());
			HttpRequestUtils.post(mContext, ServerUrl.REGISTER, params,
					handler, REGISTER_CODE);
		}
	};

	/**
	 * 倒计时
	 */
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			time--;
			if (time > 0) {
				handler.sendEmptyMessageDelayed(COUNTDOWN, RECIPROCALTIME);
			} else {
				setRetransCanClick();
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(leftReturn)) {
			finish();
		} else if (v.equals(agreementFlag)) {
			agreeFlag = !agreeFlag;
			if (agreeFlag) {
				agreementFlag.setCompoundDrawables(dr1, null, null, null);
			} else {
				agreementFlag.setCompoundDrawables(dr2, null, null, null);
			}
		} else if (v.equals(sendVerficationCode)) {
			if (StringUtil.isEmptyString(mobilePhone.getText().toString())) {
				return;
			} else if (!NumberUtil
					.isMobileNum(mobilePhone.getText().toString())) {
				Toast.makeText(mContext, "手机号码不正确", 2000).show();
				return;
			}
			// 发送获取验证码请求
			mCancelProgressDialog.show("获取验证码...");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("phone", mobilePhone.getText().toString());
			// params.put("smsCodeType", 1);
			HttpRequestUtils.postJson(mContext, ServerUrl.GETSMSCODE, params,
					handler, GET_VERFICATION_CODE);
		} else if (v.equals(agreementTextView)) {
			// RegsiterAgreementDialog dialog = new RegsiterAgreementDialog(
			// mContext);
			// dialog.show();
			// showService();
		}
	}
}
