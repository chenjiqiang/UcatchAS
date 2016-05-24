package com.chen.ucatch.fragment;

import java.util.HashMap;
import java.util.Map;

import org.afinal.simplecache.ACache;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chen.ucatch.ConstantValue;
import com.chen.ucatch.R;
import com.chen.ucatch.model.ReturnVO;
import com.chen.ucatch.model.User;
import com.chen.ucatch.model.UserDetailVO;
import com.chen.ucatch.model.UserVO;
import com.chen.ucatch.ui.FriendListActivity;
import com.chen.ucatch.ui.GuideActivity;
import com.chen.ucatch.ui.LoginActivity;
import com.chen.ucatch.ui.MyAttentionActivity;
import com.chen.ucatch.ui.MyMessageActivity;
import com.chen.ucatch.ui.MyPickActivity;
import com.chen.ucatch.utils.HttpRequestUtils;
import com.chen.ucatch.utils.ServerUrl;
import com.chen.ucatch.utils.StringUtil;
import com.chen.ucatch.utils.TopTitleUtils;
import com.chen.ucatch.utils.UserInfoUtils;

public class MineFragment extends Fragment implements OnClickListener {
	/**
	 * fragment的布局
	 */
	private View mMainview = null;
	/**
	 * 上下文
	 */
	private Context mContext;
	private Handler mHandler;
	/**
	 * 好友数目
	 */
	private TextView mFriends_Num;
	/**
	 * 关注数目
	 */
	private TextView mAttention_Num;
	/**
	 * 粉丝数目
	 */
	private TextView mFans_Num;
	/**
	 * 好友的数目布局
	 */
	private LinearLayout ll_Friends_Num;
	/**
	 * 关注数目的布局
	 */
	private LinearLayout ll_Attention_Num;
	/**
	 * 粉丝数目的布局
	 */
	private LinearLayout ll_Fans_Num;
	private LinearLayout ll_myMsg;
	/**
	 * 我的拾取
	 */
	private LinearLayout ll_mypick;
	/**
	 * 标题
	 */
	private TextView mtv_title;
	/**
	 * 用户的昵称
	 */
	private TextView mUserName;
	/**
	 * 用户名
	 */
	private String muserName;
	/**
	 * 用户账户
	 */
	private TextView mUserAccount;
	/**
	 * 退出登录
	 */
	private Button mBt_logout;
	/**
	 * 个人信息
	 */
	private UserDetailVO mUserDetailVO;
	/**
	 * 缓存的工具
	 */
	private ACache mCache;
	private UserVO userVO;
	private JSONObject user;
	/**
	 * 跳转到登录界面的请求码
	 */
	private final static int TURNTOLOGIN = 1111;
	private final static int TURNTOMYMSG=10001;
	private final static int TURNTOMYPIC=10002;
	/**
	 * 获取个人信息
	 */
	private final static int GETUSERINFORMATION = 1001;
	private final static int UPDATENICKNAME = 1002;
	private final static int LOGINOUT = 1003;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mMainview = inflater.inflate(R.layout.mine, container, false);
		mUserDetailVO = new UserDetailVO();
		ViewGroup parent = (ViewGroup) mMainview.getParent();
		this.mContext = getActivity();
		if (parent != null) {
			parent.removeView(mMainview);
		}
		mCache = ACache.get(mContext);
		user = new JSONObject();
		userVO = new UserVO();
		initView();
		initHandler();
		initListener();
		initData();
		return mMainview;
	}

	private void initData() {
		TopTitleUtils.getUserVODetail(mContext, UserInfoUtils.getUser(mContext)
				.getId(), mHandler, GETUSERINFORMATION);

	}

	private void initHandler() {
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				ReturnVO vo = (ReturnVO) msg.obj;
				if (vo == null) {
					if (msg.what == ConstantValue.NETWORK_NO_RESPONE) {
						Toast.makeText(mContext,
								ConstantValue.NETWORK_NO_RESPONE_TEXT, Toast.LENGTH_SHORT)
								.show();
					} else if (msg.what == ConstantValue.SERVER_NO_RESPONE) {
						Toast.makeText(mContext,
								ConstantValue.SERVER_NO_RESPONE_TEXT, Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(mContext,
								ConstantValue.SERVER_NO_RESPONE_TEXT, Toast.LENGTH_SHORT)
								.show();
					}
					return;
				}
				if (vo.getSuccessFlag() == 1) {
					switch (msg.what) {
					case GETUSERINFORMATION:
						mUserDetailVO = JSON.parseObject(
								String.valueOf(vo.getData()),
								UserDetailVO.class);
						setUserData(mUserDetailVO);
						break;
					case UPDATENICKNAME:
						Toast.makeText(mContext, "更新昵称成功~", Toast.LENGTH_SHORT)
								.show();
						break;
					case LOGINOUT:
						Toast.makeText(mContext, "退出成功", Toast.LENGTH_SHORT)
								.show();
						userVO.setId("");
						try {
							user = new JSONObject(userVO.toString());
							mCache.put("user", user);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Intent intent=new Intent();
						intent.setClass(mContext, GuideActivity.class);
						startActivity(intent);
						getActivity().finish();
						break;
					default:
						break;
					}
				} else if (vo.getSuccessFlag() == 0) {
					Toast.makeText(mContext, vo.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}

		};// TODO Auto-generated method stub

	}

	private void initListener() {
		ll_Friends_Num.setOnClickListener(this);
		ll_Fans_Num.setOnClickListener(this);
		ll_Attention_Num.setOnClickListener(this);
		mBt_logout.setOnClickListener(this);
		mUserName.setOnClickListener(this);
		ll_myMsg.setOnClickListener(this);
		ll_mypick.setOnClickListener(this);
		if (UserInfoUtils.getUser(mContext) == null) {
			Intent intent = new Intent();
			intent.setClass(mContext, LoginActivity.class);
			startActivityForResult(intent, TURNTOLOGIN);
			return;
		}
	}

	private void initView() {
		mMainview.findViewById(R.id.rightpraise).setVisibility(View.GONE);
		mMainview.findViewById(R.id.rightcollect).setVisibility(View.GONE);
		mMainview.findViewById(R.id.rightshare).setVisibility(View.GONE);
		mMainview.findViewById(R.id.leftimageview).setVisibility(View.GONE);
		mtv_title = (TextView) mMainview.findViewById(R.id.titletext);
		mtv_title.setText("个人中心");
		mUserAccount = (TextView) mMainview.findViewById(R.id.mine_userAccount);
		mUserName = (TextView) mMainview.findViewById(R.id.mine_userName);
		ll_Friends_Num = (LinearLayout) mMainview
				.findViewById(R.id.ll_Friends_Num);
		ll_Fans_Num = (LinearLayout) mMainview.findViewById(R.id.ll_Fans_Num);
		ll_Attention_Num = (LinearLayout) mMainview
				.findViewById(R.id.ll_Attention_Num);
		mFriends_Num = (TextView) mMainview.findViewById(R.id.tv_Friends_Num);
		mFans_Num = (TextView) mMainview.findViewById(R.id.tv_Fans_Num);
		mAttention_Num = (TextView) mMainview
				.findViewById(R.id.tv_Attention_Num);
		mBt_logout = (Button) mMainview.findViewById(R.id.bt_log_out);
		ll_myMsg=(LinearLayout) mMainview.findViewById(R.id.ll_myMsg);
		ll_mypick=(LinearLayout) mMainview.findViewById(R.id.ll_mypick);
	}

	/**
	 * 
	 * 个人界面信息赋值
	 * 
	 * @author chenjiqiang 2015年11月18日 下午8:26:25 <br>
	 * @param userDetailVO
	 */
	private void setUserData(UserDetailVO userDetailVO) {
		mFriends_Num.setText(userDetailVO.getFriendNum() + "");
		mFans_Num.setText(userDetailVO.getFanNum() + "");
		mAttention_Num.setText(userDetailVO.getFocusNum() + "");
		mUserAccount.setText(userDetailVO.getPhone() + "");
		mUserName.setText(userDetailVO.getNickName());
	}

	@Override
	public void onClick(View v) {
		if (v.equals(mBt_logout)) {
			TopTitleUtils.Loginout(mContext, "", mHandler, LOGINOUT);
		} else if (v.equals(mUserName)) {
			getNameDialog().show();
		} else if (v.equals(ll_Friends_Num)) {
			Intent intent = new Intent();
			intent.setClass(mContext, FriendListActivity.class);
			startActivity(intent);
		}else if (v.equals(ll_Attention_Num)) {
			Intent intent = new Intent();
			intent.setClass(mContext, MyAttentionActivity.class);
			startActivity(intent);
		}else if (v.equals(ll_myMsg)) {
			Intent intent = new Intent();
			intent.setClass(mContext, MyMessageActivity.class);
			startActivityForResult(intent, TURNTOMYMSG);;
		}else if (v.equals(ll_mypick)) {
			Intent intent = new Intent();
			intent.setClass(mContext, MyPickActivity.class);
			startActivityForResult(intent, TURNTOMYPIC);;
		}
	}

	/**
	 * 
	 * 修改姓名对话框. <br>
	 * 
	 * @author ningguilin 2015年3月27日 上午11:57:14 <br>
	 * @return
	 */
	private Dialog getNameDialog() {
		final Dialog dialog = new Dialog(mContext,
				R.style.CommonDialogTransparentStyle);
		dialog.setContentView(R.layout.usercenter_name_dialog);
		dialog.setCanceledOnTouchOutside(false);
		final EditText nameEditText = (EditText) dialog
				.findViewById(R.id.dialog_name);
		// 确定
		TextView ok = (TextView) dialog.findViewById(R.id.dialog_ok);
		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = nameEditText.getText().toString().trim();
				muserName = name;
				mUserDetailVO.setNickName(name);
				dialog.dismiss();
				if (StringUtil.isEmptyString(muserName)) {
					Toast.makeText(mContext, "用户昵称不能为空", Toast.LENGTH_SHORT).show();
					return;
				} else if (muserName.length() > 12) {
					Toast.makeText(mContext,
							ConstantValue.USERNAME_LENGTH_WRONG, Toast.LENGTH_SHORT).show();
					return;
				}
				mUserName.setText(name);
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("nickName", muserName);
				params.put("userId", mUserDetailVO.getId());
				HttpRequestUtils.post(mContext, ServerUrl.UPDATE_USERNICKNAME,
						params, mHandler, UPDATENICKNAME);
				// updateUserInformation();
			}
		});

		// 取消
		TextView cancel = (TextView) dialog.findViewById(R.id.dialog_cancel);
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		return dialog;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
}
