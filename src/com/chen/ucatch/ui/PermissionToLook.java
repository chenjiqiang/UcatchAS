package com.chen.ucatch.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.chen.ucatch.ConstantValue;
import com.chen.ucatch.R;
import com.chen.ucatch.adapter.FriendsAdapter;
import com.chen.ucatch.model.UserVO;

public class PermissionToLook extends Activity implements OnClickListener {
	private Context mContext;
	/**
	 * 确定按钮
	 */
	private Button mbt_ok;
	/**
	 * 公开的布局
	 */
	private RelativeLayout rl_open_look;
	/**
	 * 私密的布局
	 */
	private RelativeLayout rl_person_look;
	/**
	 * 部分人可见的布局
	 */
	private RelativeLayout rl_part_look;
	/**
	 * 公开的打钩
	 */
	private ImageView iv_open_look;
	/**
	 * 私密的打钩
	 */
	private ImageView iv_person_look;
	/**
	 * 部分好友可见的列表
	 */
	private ListView mListView;
	/**
	 * 部分好友的数据
	 */
	private List<UserVO> choose_datas = new ArrayList<UserVO>();
	/**
	 * 适配器
	 */
	private FriendsAdapter adapter;
	/**
	 * 部分好友可见
	 */
	public static final int PARTOFFRIENDS_RESULT_CODE = 1114;
	public static final int PARTOFFRIENDS_REQUEST_CODE = 1111;
	private int mChooseType = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.who_can_look);
		mContext = this;
		mbt_ok = (Button) findViewById(R.id.bt_head_ok);
		iv_open_look = (ImageView) findViewById(R.id.iv_open_look);
		iv_person_look = (ImageView) findViewById(R.id.iv_person_look);
		rl_open_look = (RelativeLayout) findViewById(R.id.rl_open_look);
		rl_person_look = (RelativeLayout) findViewById(R.id.rl_person_look);
		rl_part_look = (RelativeLayout) findViewById(R.id.rl_part_look);
		mListView = (ListView) findViewById(R.id.lv_parts_friends);
		mbt_ok.setOnClickListener(this);
		rl_open_look.setOnClickListener(this);
		rl_person_look.setOnClickListener(this);
		rl_part_look.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case ChooseFriends.CHOOSEFRIENDS_RESULT_CODE:
			Bundle b = data.getExtras();
			UserVO userVO = (UserVO) b
					.getSerializable(ConstantValue.CHOOSE_FRIENDS);
			choose_datas = userVO.getList();
			adapter = new FriendsAdapter(mContext, choose_datas,
					R.layout.friends_item);
			mListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			break;

		default:
			break;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(mbt_ok)) {
			Intent intent = new Intent();
			Bundle b = new Bundle();
			UserVO userVO = new UserVO();
			userVO.setList(choose_datas);
			b.putInt(ConstantValue.CHOOSE_TYPE_RESULT_CODE, mChooseType);
			b.putSerializable(ConstantValue.CHOOSE_FRIENDS, userVO);
			intent.putExtras(b);
			setResult(PARTOFFRIENDS_RESULT_CODE, intent);
			finish();
		} else if (v.equals(rl_open_look)) {
			mChooseType=0;
			iv_open_look.setVisibility(View.VISIBLE);
			iv_person_look.setVisibility(View.GONE);
		} else if (v.equals(rl_part_look)) {
			mChooseType=2;
			Intent intent = new Intent();
			intent.setClass(mContext, ChooseFriends.class);
			iv_person_look.setVisibility(View.GONE);
			iv_open_look.setVisibility(View.GONE);
			startActivityForResult(intent,PARTOFFRIENDS_REQUEST_CODE);
		} else if (v.equals(rl_person_look)) {
			mChooseType=1;
			iv_person_look.setVisibility(View.VISIBLE);
			iv_open_look.setVisibility(View.GONE);
		}
	}
}
