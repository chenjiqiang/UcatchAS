package com.chen.ucatch.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.ucatch.ConstantValue;
import com.chen.ucatch.R;
import com.chen.ucatch.model.PhotoShareVO;
import com.chen.ucatch.model.ReturnVO;
import com.chen.ucatch.utils.TopTitleUtils;
import com.chen.ucatch.utils.UserInfoUtils;

public class MyAdapter extends CommonAdapter<PhotoShareVO> {
	private List<Integer> mpos = new ArrayList<Integer>();
	private int position;
	/**
	 * 目标用户的id
	 */
	private String mDestUserId;
	private Handler mHandler;
	/**
	 * 关注取消关注的请求码
	 */
	private static final int ATTENTION = 1001;
	/**
	 * 点赞取消点赞的请求码
	 */
	private static final int PRAISE = 1002;
	private PhotoShareVO mPhotoShareVO;
	private List<PhotoShareVO> mDatas;

	public MyAdapter(Context context, List<PhotoShareVO> datas, int layoutId) {
		super(context, datas, layoutId);
		this.mDatas = datas;
		setmDatas(mDatas);
		notifyDataSetChanged();
		initHandler();
	}

	private void initHandler() {
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
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
					return;
				}
				if (vo.getSuccessFlag() == 1) {
					switch (msg.what) {
					case ATTENTION:
						Toast.makeText(mContext,
								"关注成功" + position + mDestUserId, 5000).show();
						break;
					case PRAISE:
						if (!mPhotoShareVO.getIsPraised()) {
							mPhotoShareVO.setPraised(true);
							int praiseNum = mPhotoShareVO.getPraiseNum() + 1;
							mPhotoShareVO.setPraiseNum(praiseNum);
							mDatas.set(position, mPhotoShareVO);
							setmDatas(mDatas);
							notifyDataSetChanged();
							Toast.makeText(mContext, "点赞成功", Toast.LENGTH_SHORT)
									.show();
						} else {
							mPhotoShareVO.setPraised(false);
							int praiseNum = mPhotoShareVO.getPraiseNum() + 1;
							mPhotoShareVO.setPraiseNum(praiseNum);
							mDatas.set(position, mPhotoShareVO);
							setmDatas(mDatas);
							notifyDataSetChanged();
							Toast.makeText(mContext, "取消点赞" + position,
									Toast.LENGTH_SHORT).show();
						}
						break;
					}
				} else {
					Toast.makeText(mContext, vo.getMessage(), 1000).show();
				}

			}
		};
	}

	@Override
	public void convert(final ViewHolder holder, final PhotoShareVO photoShareVO) {
		position = holder.getPosition();
		mPhotoShareVO = photoShareVO;
		holder.setText(
				R.id.find_listview_occur_time,
				photoShareVO.getAddressLabel() + "\u3000"
						+ photoShareVO.getCreateTime());
		holder.setText(R.id.find_listview_comment,
				photoShareVO.getDescription());
		holder.setText(R.id.find_listview_user_name, photoShareVO.getUser()
				.getNickName());
		holder.setText(R.id.find_listview_leave, photoShareVO.getTitle());
		holder.setImg(R.id.find_listview_item_img, photoShareVO.getPicUrl());
		holder.setText(R.id.find_list_comment_num, photoShareVO.getReplyCount()
				+ "");
		holder.setText(R.id.find_list_praise_num, photoShareVO.getPraiseNum()
				+ "");
		final Button bt = holder.getView(R.id.bt_attention);
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
		final TextView tv_praise = holder.getView(R.id.find_list_comment_num);
		tv_praise.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!mPhotoShareVO.getIsPraised()) {
					TopTitleUtils.submitPraiseInfo(mContext, true,
							UserInfoUtils.getUser(mContext).getId(),
							mPhotoShareVO.getId(), mHandler, PRAISE);
				} else if (mPhotoShareVO.getIsPraised()) {
					TopTitleUtils.submitPraiseInfo(mContext, false,
							UserInfoUtils.getUser(mContext).getId(),
							mPhotoShareVO.getId(), mHandler, PRAISE);
				}
			}
		});
	}
}
