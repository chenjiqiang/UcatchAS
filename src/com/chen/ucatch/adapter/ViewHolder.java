package com.chen.ucatch.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chen.ucatch.R;
import com.meg7.widget.CircleImageView;

public class ViewHolder {

	private SparseArray<View> mViews;
	private int mPosition;
	private Context mContext;

	public int getPosition() {
		return mPosition;
	}

	private View mConvertView;

	public ViewHolder(Context context, ViewGroup parent, int layoutId,
			int position) {
		this.mContext = context;
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		this.mConvertView = LayoutInflater.from(context).inflate(layoutId,
				parent, false);
		mConvertView.setTag(this);

	}

	public static ViewHolder get(Context context, View convertView,
			ViewGroup parent, int layoutId, int position) {
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		} else {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.mPosition = position;
			return holder;
		}
	}

	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;

	}

	public View getConvertView() {
		return mConvertView;
	}

	/**
	 * 
	 * 给文本赋值
	 * 
	 * @author chenjiqiang 2015年10月28日 下午7:36:48 <br>
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text) {
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}

	// public ViewHolder setTvAndOnclick(int viewId, String text,
	// final boolean isSavePraise, final String userId,
	// final String shareId) {
	// this.mIsSavePraise = isSavePraise;
	// TextView tv = getView(viewId);
	// tv.setText(text);
	// tv.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// TopTitleUtils.submitPraiseInfo(mContext, mIsSavePraise, userId,
	// shareId, mHandler, PRAISE);
	// }
	// });
	// return this;
	// }

	/**
	 * 
	 * 设置图片
	 * 
	 * @author chenjiqiang 2015年10月28日 下午7:37:04 <br>
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setImg(int viewId, String url) {
		ImageView img = getView(viewId);
		Glide.with(mContext).load(url).placeholder(R.drawable.default_pic)
				.error(R.drawable.default_pic).into(img);
		return this;
	}

	/**
	 * 
	 * 设置头像
	 * 
	 * @author chenjiqiang 2016年3月14日 下午7:02:28 <br>
	 * @param viewId
	 * @param url
	 * @return
	 */
	public ViewHolder setUserPic(int viewId, String url) {
		CircleImageView img = getView(viewId);
		Glide.with(mContext).load(url).placeholder(R.drawable.main_rightmenu_default_head)
				.error(R.drawable.main_rightmenu_default_head).into(img);
		return this;
	}
	/**
	 * 
	 * 设置关注按钮
	 * 
	 * @author chenjiqiang 2015年11月13日 下午5:10:36 <br>
	 * @param viewId
	 * @param url
	 * @return
	 */
	// public ViewHolder setButton(int viewId, String str, String destUserId) {
	// Button bt = getView(viewId);
	// bt.setText(str);
	// mDestUserId = destUserId;
	// bt.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// if (UserInfoUtils.getUser(mContext) == null) {
	// Intent intent = new Intent();
	// intent.setClass(mContext, LoginActivity.class);
	// mContext.startActivity(intent);
	// return;
	// } else if (!StringUtil.isEmptyString(mDestUserId)) {
	// TopTitleUtils.attention(mContext,
	// UserInfoUtils.getUser(mContext).getId(),
	// mDestUserId, 0, mHandler, ATTENTION);
	// }
	// }
	// });
	// return this;
	// }
}
