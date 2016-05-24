package com.chen.ucatch.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.afinal.simplecache.ACache;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chen.ucatch.ConstantValue;
import com.chen.ucatch.R;
import com.chen.ucatch.fragment.AttentionFragment;
import com.chen.ucatch.fragment.FindFragment;
import com.chen.ucatch.model.PhotoShareVO;
import com.chen.ucatch.model.ReturnVO;
import com.chen.ucatch.utils.StringUtil;
import com.chen.ucatch.utils.TopTitleUtils;
import com.chen.ucatch.utils.UserInfoUtils;
import com.meg7.widget.CircleImageView;

public class FindItemAdapter extends BaseAdapter {
	private final String TAG = "FindItemAdapter";
	/**
	 * 上下文
	 */
	private Context mContext;
	private Handler mHandler;
	/**
	 * 加载布局
	 */
	private LayoutInflater inflater;
	/**
	 * 点赞 事件
	 */
	private PraisedOnClickListener onClickListener;
	/**
	 * 点击关注 事件
	 */
	private AttentionOnClickListener mAttentionOnClickListener;
	/**
	 * 关注取消关注的请求码
	 */
	private static final int ATTENTION = 1001;
	/**
	 * 点赞取消点赞的请求码
	 */
	private static final int PRAISE = 1002;
	/**
	 * 评论信息
	 */
	private List<PhotoShareVO> list;

	public List<PhotoShareVO> getList() {
		return list;
	}

	public void setList(List<PhotoShareVO> list) {
		this.list = list;
	}

	/**
	 * 点赞图标
	 */
	private Drawable drawable_do;
	/**
	 * 已经点赞的图
	 */
	private Drawable drawable_done;
	private Map<String, Integer> shareId;
	private ACache mAcache;
	/**
	 * FindFragmentShareId的标识（缓存中的）
	 */
	public static final String SHAREID = "FINDSHAREID";
	/**
	 * AttentionFragmentShareId的标识（缓存中的）
	 */
	public static final String ATTENTION_SHAREID = "ATTENTIONSHAREID";
	/**
	 * 点赞的view
	 */
	private TextView tv_praised;
	/**
	 * 关注的视图
	 */
	private Button bt_attention;
	private String tag;

	public FindItemAdapter(Context context, final String tag) {
		this.mContext = context;
		this.tag = tag;
		mAcache = ACache.get(context);
		inflater = LayoutInflater.from(context);
		shareId = new HashMap<String, Integer>();
		drawable_do = mContext.getResources().getDrawable(
				R.drawable.common_praise_do);
		// / 这一步必须要做,否则不会显示.
		drawable_do.setBounds(0, 0, drawable_do.getMinimumWidth(),
				drawable_do.getMinimumHeight());
		drawable_done = mContext.getResources().getDrawable(
				R.drawable.common_praise_done);
		// / 这一步必须要做,否则不会显示.
		drawable_done.setBounds(0, 0, drawable_do.getMinimumWidth(),
				drawable_done.getMinimumHeight());
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
						if (tag.equals(FindFragment.TAG)) {
							if (mAcache.getAsString(SHAREID) != null) {
								int position = shareId.get(mAcache
										.getAsString(SHAREID));
								if (list.get(position).getIsFocused()) {
									list.get(position).setFocused(false);
									bt_attention.setText("关注");
									Toast.makeText(mContext, "取消关注", 5000)
											.show();
								} else {
									bt_attention.setText("已关注");
									list.get(position).setFocused(true);
									Toast.makeText(mContext, "关注成功", 5000)
											.show();
								}
							}
						} else if (tag.equals(AttentionFragment.TAG)) {
							if (mAcache.getAsString(ATTENTION_SHAREID) != null) {
								int position = shareId.get(mAcache
										.getAsString(ATTENTION_SHAREID));
								if (list.get(position).getIsFocused()) {
									list.get(position).setFocused(false);
									bt_attention.setText("关注");
									Toast.makeText(mContext, "取消关注", 5000)
											.show();
								} else {
									bt_attention.setText("已关注");
									list.get(position).setFocused(true);
									Toast.makeText(mContext, "关注成功", 5000)
											.show();
								}
							}
						}

						break;
					case PRAISE:
						if (tag.equals(FindFragment.TAG)) {
							if (mAcache.getAsString(SHAREID) != null) {
								int position = shareId.get(mAcache
										.getAsString(SHAREID));
								if (list.get(position).getIsPraised()) {
									changeDrawable(tv_praised, 1, position);
								} else {
									changeDrawable(tv_praised, 0, position);
								}
							}
						} else if (tag.equals(AttentionFragment.TAG)) {
							if (mAcache.getAsString(ATTENTION_SHAREID) != null) {
								int position = shareId.get(mAcache
										.getAsString(ATTENTION_SHAREID));
								if (list.get(position).getIsPraised()) {
									changeDrawable(tv_praised, 1, position);
								} else {
									changeDrawable(tv_praised, 0, position);
								}
							}
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
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holderView = null;
		if (convertView == null) {
			holderView = new HolderView();
			convertView = inflater.inflate(R.layout.find_listview_item, null);
			initView(convertView, holderView);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}
		setView(holderView, list, position);
		return convertView;
	}

	/**
	 * 
	 * 陈纪强
	 * 
	 * @author chenjiqiang 2015年11月16日 下午2:46:36 <br>
	 * @param textview
	 * @param isPraised
	 * @param position
	 */
	private void changeDrawable(TextView textview, int isPraised,
			final int position) {
		Drawable drawable = null;
		if (isPraised == 1) {
			// 1表示已经点赞过了，现在修改为没有点赞
			drawable = mContext.getResources().getDrawable(
					R.drawable.common_praise_do);
			list.get(position).setPraiseNum(
					list.get(position).getPraiseNum() - 1);
			list.get(position).setPraised(false);
			textview.setText((list.get(position).getPraiseNum()) + "");
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			textview.setCompoundDrawables(drawable, null, null, null);
		} else {
			// 点赞
			// operationPraised.put(list.get(position).getAffairguideCommentId(),
			// 1);
			drawable = mContext.getResources().getDrawable(
					R.drawable.common_praise_done);
			list.get(position).setPraiseNum(
					list.get(position).getPraiseNum() + 1);
			list.get(position).setPraised(true);
			textview.setText((list.get(position).getPraiseNum()) + "");
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			textview.setCompoundDrawables(drawable, null, null, null);
		}

	}

	/**
	 * 
	 * 陈纪强
	 * 
	 * @author chenjiqiang 2015年11月16日 下午2:30:33 <br>
	 * @param view
	 * @param holderView
	 */
	private void initView(View view, HolderView holderView) {
		holderView.userHeadImage = (CircleImageView) view
				.findViewById(R.id.find_listview_user_icon);
		holderView.userName = (TextView) view
				.findViewById(R.id.find_listview_user_name);
		holderView.LocationAndTime = (TextView) view
				.findViewById(R.id.find_listview_occur_time);
		holderView.shareImg = (ImageView) view
				.findViewById(R.id.find_listview_item_img);
		holderView.leaveMsg = (TextView) view
				.findViewById(R.id.find_listview_leave);
		holderView.comment = (TextView) view
				.findViewById(R.id.find_listview_comment);
		holderView.attention = (Button) view.findViewById(R.id.bt_attention);
		holderView.tv_commentNum = (TextView) view
				.findViewById(R.id.find_list_comment_num);
		holderView.tv_praiseNum = (TextView) view
				.findViewById(R.id.find_list_praise_num);
	}

	/**
	 * 为控件赋值
	 * 
	 * @author liuhuan2 2015年4月8日 上午10:21:51 <br>
	 * @param holderView
	 * @param list
	 * @param position
	 */
	private void setView(HolderView holderView, List<PhotoShareVO> list,
			int position) {

		if (StringUtil.isEmptyString(list.get(position).getUser().getPicUrl())) {
			// 如果图片地址 为空 则设置默认图片
			holderView.userHeadImage
					.setImageResource(R.drawable.main_rightmenu_default_head);
		} else {
			Glide.with(mContext).load(list.get(position).getUser().getPicUrl())
					.placeholder(R.drawable.main_rightmenu_default_head)
					.error(R.drawable.main_rightmenu_default_head)
					.into(holderView.userHeadImage);
		}
		if (!StringUtil.isEmptyString(list.get(position).getPicUrl())) {
			Glide.with(mContext).load(list.get(position).getPicUrl())
					.placeholder(R.drawable.default_pic)
					.error(R.drawable.default_pic).into(holderView.shareImg);
		}
		// 修改点赞图标
		if (list.get(position).getIsPraised()) {
			holderView.tv_praiseNum.setCompoundDrawables(drawable_done, null,
					null, null);
		} else {
			holderView.tv_praiseNum.setCompoundDrawables(drawable_do, null,
					null, null);
		}
		// 没有点赞的时候可以有单击事件，已近点赞的时候则不设置单击事件的监听，当需要点赞 和 去消点赞的功能时 把这段代码注释掉
		onClickListener = new PraisedOnClickListener();
		onClickListener.setPosition(position);
		holderView.tv_praiseNum.setOnClickListener(onClickListener);
		mAttentionOnClickListener = new AttentionOnClickListener();
		mAttentionOnClickListener.setPosition(position);
		holderView.attention.setOnClickListener(mAttentionOnClickListener);
		holderView.LocationAndTime.setText(list.get(position).getCreateTime());
		holderView.leaveMsg.setText(list.get(position).getTitle());
		holderView.tv_commentNum.setText(list.get(position).getReplyCount()
				+ "");
		holderView.tv_praiseNum.setText(list.get(position).getPraiseNum() + "");
		holderView.leaveMsg.setText(list.get(position).getTitle());
		holderView.comment.setText(list.get(position).getDescription());
		holderView.userName.setText(list.get(position).getUser().getNickName());
		holderView.LocationAndTime.setText(list.get(position).getAddressLabel()
				+ "\u3000" + list.get(position).getCreateTime());
		if (list.get(position).getIsFocused()) {
			holderView.attention.setText("已关注");
		} else {
			holderView.attention.setText("关注");
		}
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
		 * 定位的地点以及时间
		 */
		TextView LocationAndTime;
		/**
		 * 图片
		 */
		ImageView shareImg;
		/**
		 * 留言
		 */
		TextView leaveMsg;
		/**
		 * 评论内容
		 */
		TextView comment;
		/**
		 * 关注
		 */
		Button attention;
		/**
		 * 点赞的数目
		 */
		TextView tv_praiseNum;
		/**
		 * 评论数
		 */
		TextView tv_commentNum;;
	}

	class AttentionOnClickListener implements OnClickListener {

		private int position;

		@Override
		public void onClick(View v) {
			bt_attention = (Button) v;
			if (tag.equals(FindFragment.TAG)) {
				mAcache.put(SHAREID, list.get(position).getId());
			} else if (tag.equals(AttentionFragment.TAG)) {
				mAcache.put(ATTENTION_SHAREID, list.get(position).getId());
			}
			shareId.put(list.get(position).getId(), position);
			if (!list.get(position).getIsFocused()) {
				TopTitleUtils.attention(mContext,
						UserInfoUtils.getUser(mContext).getId(),
						list.get(position).getUser().getId(), 0, mHandler,
						ATTENTION);
			} else if (list.get(position).getIsFocused()) {
				TopTitleUtils.attention(mContext,
						UserInfoUtils.getUser(mContext).getId(),
						list.get(position).getUser().getId(), 1, mHandler,
						ATTENTION);
			}
		}

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

	}

	class PraisedOnClickListener implements OnClickListener {

		private int position;

		@Override
		public void onClick(View v) {
			tv_praised = (TextView) v;
			if (tag.equals(FindFragment.TAG)) {
				mAcache.put(SHAREID, list.get(position).getId());
			} else if (tag.equals(AttentionFragment.TAG)) {
				mAcache.put(ATTENTION_SHAREID, list.get(position).getId());
			}
			shareId.put(list.get(position).getId(), position);
			if (!list.get(position).getIsPraised()) {
				TopTitleUtils.submitPraiseInfo(mContext, true, UserInfoUtils
						.getUser(mContext).getId(), list.get(position).getId(),
						mHandler, PRAISE);
			} else if (list.get(position).getIsPraised()) {
				TopTitleUtils.submitPraiseInfo(mContext, false, UserInfoUtils
						.getUser(mContext).getId(), list.get(position).getId(),
						mHandler, PRAISE);
			}
		}

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

	}
}
