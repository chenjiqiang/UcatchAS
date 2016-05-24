package com.chen.ucatch.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chen.ucatch.ConstantValue;
import com.chen.ucatch.R;
import com.chen.ucatch.adapter.CommentAdapter;
import com.chen.ucatch.adapter.GridAdapter;
import com.chen.ucatch.model.ReplyVO;
import com.chen.ucatch.model.ReturnVO;
import com.chen.ucatch.model.SubjectDetailVO;
import com.chen.ucatch.model.UserVO;
import com.chen.ucatch.utils.HttpRequestUtils;
import com.chen.ucatch.utils.ServerUrl;
import com.chen.ucatch.utils.StringUtil;
import com.chen.ucatch.utils.TopTitleUtils;
import com.chen.ucatch.utils.UserInfoUtils;
import com.chen.ucatch.view.CancelProgressDialog;
import com.chen.ucatch.view.MyListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.meg7.widget.CircleImageView;

public class CommentAcitivity extends MyBaseActivity implements OnClickListener {
	private ImageView iv_back;
	private TextView tv_praiseNum;
	private TextView tv_commentNum;
	private CircleImageView userPic;
	/**
	 * 拾取到的留言
	 */
	private TextView tv_leave_msg;
	/**
	 * 用户名
	 */
	private TextView userName;
	/**
	 * 留言时间
	 */
	private TextView leave_msg_time;
	private Button bt_attention;
	private TextView tv_location;
	/**
	 * 详情点赞的头像
	 */
	private GridView gridHead;
	private ArrayList<String> mHeadPics;
	/**
	 * 评论列表数据集合
	 */
	private List<ReplyVO> commentList;
	/**
	 * 评论适配器
	 */
	private CommentAdapter adapter;
	/**
	 * 评论的listview
	 */
	private MyListView lv_comment;
	/**
	 * 评论textWatcher 用来过滤表情图
	 */
	// private MyExpressionTextWatcher commentTextWatcherListener;
	/**
	 * 评论按钮
	 */
	private Button commentBtn;
	/**
	 * 评论的edtitext
	 */
	private EditText et_comment;
	private Handler mHandler;
	/**
	 * 留言id
	 */
	private String subjectId;
	/**
	 * 详情的vo
	 */
	private SubjectDetailVO subjectDetailVO;
	/**
	 * 获取详情请求码
	 */
	private static final int GET_DETAIL_CODE = 1001;
	/**
	 * 点赞
	 */
	private static final int SAVE_PRAISE = 1002;
	/**
	 * 评论
	 */
	private static final int SAVE_COMMENT = 1003;
	/**
	 * 获取评论列表
	 */
	private static final int GET_COMMENT = 1004;
	/**
	 * 详情的图
	 */
	private ImageView mShareDetailImg;
	private int pageNo = 1;
	private int pageSize = 10;
	/**
	 * 评论的总数
	 */
	private int totalCount = 0;
	/**
	 * 点赞总数
	 */
	private int praiseCount = 0;
	/**
	 * 上拉刷新、下拉加载控件
	 */
	// private RefreshableContainer mPullToRefreshView;
	private PullToRefreshScrollView mPullRefreshScrollView;
	/**
	 * 进度框
	 */
	private CancelProgressDialog mCancelProgressDialog;
	/**
	 * 关注
	 */
	private static final int ATTENTION = 10001;
	/**
	 * 取消关注
	 */
	private static final int CANCEL_ATTENTION = 10002;
	/**
	 * 用户id
	 */
	private String curUserId;
	/**
	 * 目标用户id
	 */
	private String destUserId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pick_comment);
		initView();
		mCancelProgressDialog = new CancelProgressDialog(mContext);
		mCancelProgressDialog.show("加载中...");
		initHandler();
		initListener();
		initData();

	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		userName = (TextView) findViewById(R.id.comment_listview_user_name);
		gridHead = (GridView) findViewById(R.id.share_detail_heads);
		tv_location = (TextView) findViewById(R.id.tv_location);
		tv_praiseNum = (TextView) findViewById(R.id.tv_praise_num);
		tv_commentNum = (TextView) findViewById(R.id.tv_comment_num);
		tv_leave_msg = (TextView) findViewById(R.id.tv_leave_msg);
		leave_msg_time = (TextView) findViewById(R.id.comment_listview_occur_time);
		bt_attention = (Button) findViewById(R.id.bt_attention);
		userPic = (CircleImageView) findViewById(R.id.comment_listview_user_icon);
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		lv_comment = (MyListView) findViewById(R.id.lv_comment);
		et_comment = (EditText) findViewById(R.id.share_detail_comment_edit);
		commentBtn = (Button) findViewById(R.id.share_detail_submit);
		gridHead = (GridView) findViewById(R.id.share_detail_heads);
		adapter = new CommentAdapter(mContext);
		commentList = new ArrayList<ReplyVO>();
		adapter.setList(commentList);
		lv_comment.setAdapter(adapter);
		Bundle b = getIntent().getExtras();
		subjectId = b.getString(PickActivity.SUBJECTID);
	}

	private void initData() {
		Map<String, Object> params = new HashMap<String, Object>();
		curUserId = UserInfoUtils.getUser(mContext).getId().toString();
		params.put("userId", curUserId);
		params.put("subjectId", subjectId);
		HttpRequestUtils.post(mContext, ServerUrl.COMMENT_DETAIL, params,
				mHandler, GET_DETAIL_CODE);

	}

	/**
	 * 
	 * 获取评论列表数据
	 * 
	 * @author chenjiqiang 2015年11月3日 下午8:01:53 <br>
	 */
	private void getComment() {
		TopTitleUtils.getComment(mContext, UserInfoUtils.getUser(mContext)
				.getId(), subjectDetailVO.getId(), "all", pageNo, pageSize,
				mHandler, GET_COMMENT);
	}

	private void initHandler() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (mCancelProgressDialog.isShowing()) {
					mCancelProgressDialog.cancel();
				}
				ReturnVO vo = (ReturnVO) msg.obj;
				mPullRefreshScrollView.onRefreshComplete();
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
					case GET_DETAIL_CODE:
						subjectDetailVO = com.alibaba.fastjson.JSONObject
								.parseObject(String.valueOf(vo.getData()),
										SubjectDetailVO.class);
						totalCount = subjectDetailVO.getReplyCount();
						showData();
						getComment();
						break;
					case SAVE_PRAISE:
						praise(true);
						break;
					case SAVE_COMMENT:
						pageNo = 1;
						commentList.clear();
						getComment();
						totalCount++;
						tv_commentNum.setText(totalCount + "");
						et_comment.setText("");
						closeKeyboard();
						break;
					case GET_COMMENT:
						List<ReplyVO> list = (List<ReplyVO>) com.alibaba.fastjson.JSONArray
								.parseArray(String.valueOf(vo.getData()),
										ReplyVO.class);
						commentList.addAll(list);
						adapter.setList(commentList);
						adapter.notifyDataSetChanged();
						pageNo++;
						if (commentList.size() < 1) {
							// noDataText.setVisibility(View.VISIBLE);
						} else if (list.size() < 1) {
							Toast.makeText(mContext, "没有更多数据", 2000).show();
						}
						break;
					case ATTENTION:
						bt_attention.setText("已关注");
						break;
					case CANCEL_ATTENTION:
						bt_attention.setText("关注");
						break;
					}
				} else {
					Toast.makeText(mContext, vo.getMessage(), 1000).show();
				}

			}
		};
	}

	/**
	 * 
	 * 展示数据
	 * 
	 * @author chenjiqiang 2015年10月30日 下午4:54:36 <br>
	 */
	private void showData() {
		try {
			destUserId=subjectDetailVO.getUser().getId();
			praiseCount = subjectDetailVO.getPraiseCount();
			totalCount = subjectDetailVO.getReplyCount();
			tv_commentNum.setText(totalCount + "");
			tv_praiseNum.setText(praiseCount + "");
			if (!StringUtil.isEmptyString(subjectDetailVO.getUser()
					.getNickName())) {
				userName.setText(subjectDetailVO.getUser().getNickName());
			} else {
				userName.setText("无昵称");
			}
			if (subjectDetailVO.isFocused()) {
				bt_attention.setText("已关注");
			} else {
				bt_attention.setText("关注");
			}
			tv_leave_msg.setText(subjectDetailVO.getTitle() + "");
			leave_msg_time.setText(subjectDetailVO.getCreateTime() + "");
			tv_location.setText(subjectDetailVO.getAddressLabel() + "");
			Glide.with(mContext).load(subjectDetailVO.getUser().getPicUrl())
					.placeholder(R.drawable.default_pic)
					.error(R.drawable.default_pic).into(userPic);
			mHeadPics = new ArrayList<String>();
			List<UserVO> replyUserList = subjectDetailVO.getReplyUserList();
			if (replyUserList != null && replyUserList.size() > 0) {
				findViewById(R.id.ll_share_detail_heads).setVisibility(
						View.VISIBLE);
				for (int i = 0; i < replyUserList.size(); i++) {
					mHeadPics.add(replyUserList.get(i).getPicUrl());
				}
				if (replyUserList.size() > 5) {
					findViewById(R.id.tv_more).setVisibility(View.VISIBLE);
				}
				gridHead.setAdapter(new GridAdapter(mContext, mHeadPics));
			}
		} catch (Exception e) {
			Log.i("", e.toString());
		}

	}

	/**
	 * 
	 * 点赞取消点赞
	 * 
	 * @author chenjiqiang 2015年11月3日 下午7:01:58 <br>
	 */
	private void praise(boolean save_praise) {
		if (save_praise) {
			// praiseCount++;
			// tv_praiseNum.setText(praiseCount+"");
		} else {
			// praiseCount--;
			// tv_praiseNum.setText(praiseCount + "");
		}
	}

	/**
	 * 
	 * 关闭软键盘
	 * 
	 * @author chenjiqiang 2015年11月3日 下午7:38:19 <br>
	 */
	public void closeKeyboard() {
		InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		im.hideSoftInputFromWindow(getCurrentFocus()
				.getApplicationWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	private void initListener() {
		commentBtn.setOnClickListener(this);
		bt_attention.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		mPullRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						getComment();
					}
				});
		et_comment.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					commentBtn.setVisibility(View.VISIBLE);
					if (UserInfoUtils.getUser(mContext) == null) {
						Intent intent = new Intent();
						intent.setClass(mContext, LoginActivity.class);
						startActivity(intent);
						et_comment.clearFocus();
						return;
					}
				} else {
					commentBtn.setVisibility(View.GONE);
				}
			}
		});
		et_comment.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (et_comment.getText().toString().trim().length() <= 0) {
					commentBtn
							.setBackgroundResource(R.drawable.common_comment_button_normal_gb);
					commentBtn.setTextColor(getResources().getColor(
							R.color.gray));
					commentBtn.setEnabled(false);
				} else {
					commentBtn
							.setBackgroundResource(R.drawable.common_comment_button_submit_bg);
					commentBtn.setTextColor(getResources().getColor(
							R.color.white));
					commentBtn.setEnabled(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(commentBtn)) {
			TopTitleUtils.submitMsgCommentInfo(mContext,
					UserInfoUtils.getUser(mContext).getId(), subjectId,
					et_comment.getText().toString(), mHandler, SAVE_COMMENT);
			mCancelProgressDialog.show("请稍等...");
		} else if (v.equals(iv_back)) {
			finish();
		} else if (v.equals(bt_attention)) {
			mCancelProgressDialog.show("请稍等...");
			if (bt_attention.getText().toString().equals("关注")) {
				TopTitleUtils.attention(mContext, curUserId, destUserId, 0,
						mHandler, ATTENTION);
			} else {
				TopTitleUtils.attention(mContext, curUserId, destUserId, 1,
						mHandler, CANCEL_ATTENTION);
			}
		}
	}

}
