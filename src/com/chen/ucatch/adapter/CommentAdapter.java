package com.chen.ucatch.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chen.ucatch.ConstantValue;
import com.chen.ucatch.R;
import com.chen.ucatch.model.ReplyVO;
import com.chen.ucatch.model.ReturnVO;
import com.chen.ucatch.ui.LoginActivity;
import com.chen.ucatch.utils.ServerUrl;
import com.chen.ucatch.utils.StringUtil;
import com.chen.ucatch.utils.TopTitleUtils;
import com.chen.ucatch.utils.UserInfoUtils;
import com.chen.ucatch.utils.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.meg7.widget.CircleImageView;

@SuppressLint("ResourceAsColor")
public class CommentAdapter extends BaseAdapter {
	private final String TAG = "CommentAdapter";
	/**
	 * 上下文
	 */
	private Context context;
	/**
	 * 评论信息
	 */
	private List<ReplyVO> list;
	/**
	 * 加载布局
	 */
	private LayoutInflater inflater;
	private Handler handler;
	private AsyncHttpClient asyncHttpClient = Utility.getAsyncHttpClient();
	private TextView praised;
	private Map<String, Integer> reviewId;

	public CommentAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		reviewId = new HashMap<String, Integer>();
		initHandler();
	}

	private void initHandler() {
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				ReturnVO vo = (ReturnVO) msg.obj;
				praised.setClickable(true);
				if (vo == null) {
					if (msg.what == ConstantValue.NETWORK_NO_RESPONE) {
						Toast.makeText(context,
								ConstantValue.NETWORK_NO_RESPONE_TEXT, 2000)
								.show();
					} else if (msg.what == ConstantValue.SERVER_NO_RESPONE) {
						Toast.makeText(context,
								ConstantValue.SERVER_NO_RESPONE_TEXT, 2000)
								.show();
					} else if (msg.what == 900 || msg.what == 901
							|| msg.what == 902) {
						Intent intent = new Intent();
						intent.setClass(context, LoginActivity.class);
						context.startActivity(intent);
					} else {
						Toast.makeText(context, "数据加载失败" + msg.what, 1000)
								.show();
					}
					return;
				}

				if (vo.getSuccessFlag() == 1) {
					switch (msg.what) {
					case TopTitleUtils.ADD_PRAISE:

						// Toast.makeText(context, "点赞成功", 1000).show();
						break;
					}
				} else if (vo.getSuccessFlag() == 1) {
					Toast.makeText(context, vo.getMessage(), 2000).show();
				} else {
					Toast.makeText(context, vo.getMessage(), 2000).show();
				}
			}
		};
	}

	@Override
	public int getCount() {
		Log.i(TAG, "size==" + list.size());
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holderView = null;
		if (convertView == null) {
			holderView = new HolderView();
			convertView = inflater.inflate(R.layout.comment_item, null);
			initView(convertView, holderView);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}
		setView(holderView, list, position);
		return convertView;
	}

	public List<ReplyVO> getList() {
		return list;
	}

	public void setList(List<ReplyVO> list) {
		this.list = list;
	}

	/**
	 * 初始化控件
	 * 
	 * @author liuhuan2 2015年4月8日 上午10:21:36 <br>
	 * @param view
	 * @param holderView
	 */
	private void initView(View view, HolderView holderView) {
		holderView.userHeadImage = (CircleImageView) view
				.findViewById(R.id.comment_item_headPic);
		holderView.userName = (TextView) view
				.findViewById(R.id.comment_item_userName);
		holderView.commentContent = (TextView) view
				.findViewById(R.id.comment_item_content);
		holderView.publishDate = (TextView) view
				.findViewById(R.id.comment_item_time);
	}

	/**
	 * 为控件赋值
	 * 
	 * @author liuhuan2 2015年4月8日 上午10:21:51 <br>
	 * @param holderView
	 * @param list
	 * @param position
	 */
	private void setView(HolderView holderView, List<ReplyVO> list, int position) {

		if (StringUtil.isEmptyString(list.get(position).getUser().getPicUrl())) {
			// 如果图片地址 为空 则设置默认图片
			holderView.userHeadImage
					.setImageResource(R.drawable.main_rightmenu_default_head);
		} else {
			Glide.with(context)
					.load(ServerUrl.DOLOADPIC_URL + "fileId="
							+ list.get(position).getUser().getPicUrl()
							+ "&picType=2")
					.placeholder(R.drawable.main_rightmenu_default_head)
					.error(R.drawable.main_rightmenu_default_head)
					.into(holderView.userHeadImage);
		}
		holderView.commentContent.setText(list.get(position).getContent());
		holderView.publishDate.setText(list.get(position).getReplyTime());
		holderView.userName.setText(list.get(position).getUser().getNickName());
	}

	class HolderView {
		/**
		 * 用户头像控件
		 */
		CircleImageView userHeadImage;
		/**
		 * 用户名字
		 */
		TextView userName;
		/**
		 * 评论的内容
		 */
		TextView commentContent;
		/**
		 * 评论的时间
		 */
		TextView publishDate;
	}

}
