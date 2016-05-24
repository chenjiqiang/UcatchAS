package com.chen.ucatch.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.afinal.simplecache.ACache;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.chen.ucatch.ConstantValue;
import com.chen.ucatch.R;
import com.chen.ucatch.model.GPSPointVO;
import com.chen.ucatch.model.MessagePermissions;
import com.chen.ucatch.model.ReturnVO;
import com.chen.ucatch.model.UserVO;
import com.chen.ucatch.utils.HttpRequestUtils;
import com.chen.ucatch.utils.ServerUrl;
import com.chen.ucatch.utils.StringUtil;
import com.chen.ucatch.utils.UserInfoUtils;
import com.chen.ucatch.view.CancelProgressDialog;

public class LeaveMsg extends BaseActivity implements OnClickListener {
	private Context mContext;
	/**
	 * 谁可以看
	 */
	private RelativeLayout rl_permissionsToLook;
	/**
	 * 发布按钮
	 */
	private Button mButton;
	/**
	 * 地理位置
	 */
	private TextView mtv_location;
	/**
	 * 地理位置
	 */
	private String address;
	/**
	 * 纬度
	 */
	private Double Latitude;
	/**
	 * 经度
	 */
	private Double Longitude;
	/**
	 * 选择的数据
	 */
	private List<UserVO> choose_datas;
	/**
	 * 可看的权限
	 */
	private TextView tv_permissionsToLook;
	/**
	 * 可看的权限
	 */
	private String permissionsToLook = MessagePermissions.all;
	/**
	 * 此时此刻想说的话
	 */
	private EditText mEdittext;
	/**
	 * 进度框
	 */
	private CancelProgressDialog mCancelProgressDialog;
	/**
	 * 留言的请求码
	 */
	private static final int LEAVE_MSG = 1001;
	/**
	 * 地理位置vo
	 */
	private GPSPointVO mGprsPointVO;
	private List<String> openUserIds = new ArrayList<String>();
	private Handler handler;
	private UserVO userVO;
	private ACache mCache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.main_leave_msg);
		userVO = new UserVO();
		mGprsPointVO = new GPSPointVO();
		mCache = ACache.get(mContext);
		mCancelProgressDialog = new CancelProgressDialog(mContext);
		initView();
		initHandler();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		choose_datas = new ArrayList<UserVO>();
	}

	private void initView() {
		mEdittext = (EditText) findViewById(R.id.et_leave_msg);
		tv_permissionsToLook = (TextView) findViewById(R.id.tv_permissionsToLook);
		mtv_location = (TextView) findViewById(R.id.tv_leave_msg_location);
		mButton = (Button) findViewById(R.id.head_right);
		mButton.setText("发布");
		mButton.setVisibility(View.VISIBLE);
		mButton.setOnClickListener(this);
		rl_permissionsToLook = (RelativeLayout) findViewById(R.id.rl_permissionsToLook);
		mtv_location.setOnClickListener(this);
		rl_permissionsToLook.setOnClickListener(this);
	}

	private void initHandler() {
		handler = new Handler() {
			public void handleMessage(Message msg) {
				mCancelProgressDialog.cancel();
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
					case LEAVE_MSG:
						Toast.makeText(mContext,
								ConstantValue.LEAVE_MSG_SUCCESS, 2000).show();
						finish();
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

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		// TODO Auto-generated method stub
		mGprsPointVO.setLat(amapLocation.getLatitude());
		mGprsPointVO.setLon(amapLocation.getLongitude());
		address = amapLocation.getAddress();
		Latitude = amapLocation.getLatitude();
		Longitude = amapLocation.getLongitude();
		mtv_location.setText(amapLocation.getAddress() + "【修改】");
		// 移除定位请求
		mLocationManagerProxy.removeUpdates(this);
		// // 销毁定位
		mLocationManagerProxy.destory();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case TestForData.LOCATION_CHANGE_RESULT_CODE:
			Bundle b = data.getExtras();
			address = b.getString("location");
			Latitude = b.getDouble("Latitude");
			Longitude = b.getDouble("Longitude");
			mtv_location.setText(address + "【修改】");
			break;
		case PermissionToLook.PARTOFFRIENDS_RESULT_CODE:
			Bundle bundle = data.getExtras();
			int type = bundle.getInt(ConstantValue.CHOOSE_TYPE_RESULT_CODE);
			switch (type) {
			case 0:
				tv_permissionsToLook.setText("公开");
				permissionsToLook = MessagePermissions.all;
				break;
			case 1:
				tv_permissionsToLook.setText("仅自己");
				permissionsToLook = MessagePermissions.privates;
				break;
			case 2:
				tv_permissionsToLook.setText("部分好友可见");
				permissionsToLook = MessagePermissions.friend;
				break;
			default:
				break;
			}
			UserVO model = (UserVO) bundle
					.getSerializable(ConstantValue.CHOOSE_FRIENDS);
			choose_datas = model.getList();
			for (int i = 0; i < choose_datas.size(); i++) {
				openUserIds.add(choose_datas.get(i).getId());
				// openUserIds.add("56374e1ce4b0e0b9a3bbae2d");
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(rl_permissionsToLook)) {
			Intent intent = new Intent();
			intent.setClass(mContext, PermissionToLook.class);
			startActivityForResult(intent, 1);
		} else if (v.equals(mtv_location)) {
			Intent intent = new Intent();
			intent.setClass(mContext, TestForData.class);
			startActivityForResult(intent, 1);
		} else if (v.equals(mButton)) {
			if (UserInfoUtils.getUser(mContext) == null) {
				Intent intent = new Intent();
				intent.setClass(mContext, LoginActivity.class);
				startActivity(intent);
				return;
			}
			if (mEdittext.getText().toString().length() < 0) {
				Toast.makeText(mContext, "请输入你想说的话", 2000).show();
				return;
			} else if (StringUtil.isEmptyString(address)) {
				Toast.makeText(mContext, "还没定位成功", 2000).show();
				return;
			}
			String id = openUserIds.toString().substring(1,
					openUserIds.toString().length() - 1);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("userId", UserInfoUtils.getUser(mContext).getId());
			params.put("title", mEdittext.getText().toString());
			params.put("addressGpsJson", mGprsPointVO.toString());
			params.put("addressLabel", address);
			params.put("openType", permissionsToLook);
			params.put("openUserIds", id);
			HttpRequestUtils.post(mContext, ServerUrl.SAVE_MESSAGE, params,
					handler, LEAVE_MSG);
		}
	}
}
