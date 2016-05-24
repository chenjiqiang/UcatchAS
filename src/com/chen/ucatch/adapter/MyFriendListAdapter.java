package com.chen.ucatch.adapter;

import java.util.List;

import android.content.Context;

import com.chen.ucatch.R;
import com.chen.ucatch.model.UserVO;

public class MyFriendListAdapter extends CommonAdapter<UserVO> {

	public MyFriendListAdapter(Context context, List<UserVO> datas, int layoutId) {
		super(context, datas, layoutId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void convert(ViewHolder holder, UserVO t) {
		holder.setText(R.id.tv_user_name, t.getNickName());
		holder.setUserPic(R.id.iv_userhead, t.getUserPicId());
	}
}
