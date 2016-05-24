package com.chen.ucatch.adapter;

import java.util.List;

import android.content.Context;

import com.chen.ucatch.R;
import com.chen.ucatch.model.MySubjectVO;

public class MyMessageAdapter extends CommonAdapter<MySubjectVO> {

	public MyMessageAdapter(Context context, List<MySubjectVO> datas, int layoutId) {
		super(context, datas, layoutId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void convert(ViewHolder holder, MySubjectVO t) {
		holder.setText(R.id.tv_message_text, t.getTitle());
		holder.setText(R.id.tv_message_location, t.getAddressLabel());
		holder.setText(R.id.my_message_time, t.getCreateTime());
		holder.setText(R.id.mymessage_praise, t.getPraiseCount()+"");
		holder.setText(R.id.mymessage_comment, t.getReplyCount()+"");
		holder.setText(R.id.mymessage_share, 0+"");

	}

}
