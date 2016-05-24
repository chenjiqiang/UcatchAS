package com.chen.ucatch.adapter;

import java.util.List;

import android.content.Context;

import com.chen.ucatch.R;
import com.chen.ucatch.model.UserVO;

public class FriendsAdapter extends CommonAdapter<UserVO> {
	public FriendsAdapter(Context context, List<UserVO> choose_datas,int layoutId) {
		super(context, choose_datas,layoutId);

	}
/**
 * 
 * 给布局设值
 * @see com.chen.ucatch.adapter.CommonAdapter#convert(com.chen.ucatch.adapter.ViewHolder, java.lang.Object) 
 * @author chenjiqiang 2015年8月26日 上午8:57:06 <br>
 */
	@Override
	public void convert(final ViewHolder holder, UserVO b) {
		holder.setText(R.id.tv_friend_item, b.getNickName());
	}
}

