package com.chen.ucatch.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
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
import com.chen.ucatch.model.PhotoShareDetailVO;
import com.chen.ucatch.model.ReplyVO;
import com.chen.ucatch.model.ReturnVO;
import com.chen.ucatch.utils.HttpRequestUtils;
import com.chen.ucatch.utils.ServerUrl;
import com.chen.ucatch.utils.TopTitleUtils;
import com.chen.ucatch.utils.UserInfoUtils;
import com.chen.ucatch.view.CancelProgressDialog;
import com.chen.ucatch.view.MyListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.meg7.widget.CircleImageView;

public class ShareDetailAcitivity extends Activity implements OnClickListener {
	private final String TAG = "ShareDetail";
	private Context mContext;
	private ImageView leftimageview;
	/**
	 * 进度框
	 */
	private CancelProgressDialog mCancelProgressDialog;
	/**
	 * 详情点赞的头像
	 */
	private GridView gridHead;
	private ArrayList<String> mHeadPics;
	/**
	 * 数据加载时的显示的图片
	 */
	private ImageView loadingImage;
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
	/**
	 * 评论的总数
	 */
	private int totalCount = 0;
	private Handler mHandler;
	/**
	 * 分享id
	 */
	private String shareId;
	/**
	 * 详情的vo
	 */
	private PhotoShareDetailVO shareDetailVO;
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
	/**
	 * 留言标题
	 */
	private TextView mDescripe;
	/**
	 * 用户头像
	 */
	private CircleImageView mUserHeadPic;
	/**
	 * 用户昵称
	 */
	private TextView mNickName;
	/**
	 * 发布时间
	 */
	private TextView mTime;
	/**
	 * 地理位置
	 */
	private TextView mLocation;
	/**
	 * 评论的视图
	 */
	private TextView mContent;
	/**
	 * 点赞
	 */
	private ImageView iv_praise;
	private int pageNo = 1;
	private int pageSize = 10;
	/**
	 * 是否刷新
	 */
	private boolean isPullRefresh = false;
	/**
	 * 上拉刷新、下拉加载控件
	 */
	// private RefreshableContainer mPullToRefreshView;
	private PullToRefreshScrollView mPullRefreshScrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_detail);
		mContext = this;
		shareId = getIntent().getStringExtra("id");
		commentList = new ArrayList<ReplyVO>();
		mCancelProgressDialog = new CancelProgressDialog(mContext);
		mCancelProgressDialog.show("加载中...");
		initView();
		initHandler();
		initListener();
		initData();
	}

	private void initData() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", UserInfoUtils.getUser(mContext).getId().toString());
		params.put("id", shareId);
		HttpRequestUtils.post(mContext, ServerUrl.PUBLICSHED_SHARE_DETAIL,
				params, mHandler, GET_DETAIL_CODE);

	}

	/**
	 * 
	 * 获取评论列表数据
	 * 
	 * @author chenjiqiang 2015年11月3日 下午8:01:53 <br>
	 */
	private void getComment() {
		TopTitleUtils.getComment(mContext, UserInfoUtils.getUser(mContext)
				.getId(), shareDetailVO.getId(), "all", pageNo, pageSize,
				mHandler, GET_COMMENT);
	}

	private void initListener() {
		iv_praise.setOnClickListener(this);
		commentBtn.setOnClickListener(this);
		leftimageview.setOnClickListener(this);
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
						shareDetailVO = com.alibaba.fastjson.JSONObject
								.parseObject(String.valueOf(vo.getData()),
										PhotoShareDetailVO.class);
						totalCount = shareDetailVO.getCommentNum();
						getComment();
						showData();
						break;
					case SAVE_PRAISE:
						praise(true);
						break;
					case SAVE_COMMENT:
						pageNo = 1;
						commentList.clear();
						getComment();
						totalCount++;
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
					}
				} else {
					Toast.makeText(mContext, vo.getMessage(), 1000).show();
				}

			}
		};
	}
	private void initView() {
		leftimageview=(ImageView) findViewById(R.id.leftimageview);
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView.setMode(Mode.PULL_FROM_END);
		mPullRefreshScrollView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
		mPullRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						getComment();
						// new GetDataTask().execute();
					}
				});

		// mScrollView = mPullRefreshScrollView.getRefreshableView();
		lv_comment = (MyListView) findViewById(R.id.lv_comment);
		et_comment = (EditText) findViewById(R.id.share_detail_comment_edit);
		commentBtn = (Button) findViewById(R.id.share_detail_submit);
		iv_praise = (ImageView) findViewById(R.id.rightpraise);
		mShareDetailImg = (ImageView) findViewById(R.id.listview_item_img);
		mDescripe = (TextView) findViewById(R.id.listview_item_descripe);
		mUserHeadPic = (CircleImageView) findViewById(R.id.userhead_pic);
		mNickName = (TextView) findViewById(R.id.comment_user_name);
		mTime = (TextView) findViewById(R.id.comment_occur_time);
		mLocation = (TextView) findViewById(R.id.home_item_location);
		mContent = (TextView) findViewById(R.id.home_item_comment);
		gridHead = (GridView) findViewById(R.id.share_detail_heads);
		adapter = new CommentAdapter(mContext);
		adapter.setList(commentList);
		lv_comment.setAdapter(adapter);
		mHeadPics = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			mHeadPics.add("");
		}
		gridHead.setAdapter(new GridAdapter(mContext, mHeadPics));
	}

	/**
	 * 
	 * 展示数据
	 * 
	 * @author chenjiqiang 2015年10月30日 下午4:54:36 <br>
	 */
	private void showData() {
		Glide.with(mContext).load(shareDetailVO.getPicUrl())
				.placeholder(R.drawable.default_pic)
				.error(R.drawable.default_pic).into(mShareDetailImg);
		try {
			mNickName.setText(shareDetailVO.getSubjectUser().getNickName());
			Glide.with(mContext)
					.load(shareDetailVO.getSubjectUser().getPicUrl())
					.placeholder(R.drawable.default_pic)
					.error(R.drawable.default_pic).into(mUserHeadPic);
		} catch (Exception e) {
			Log.i("", e.toString());
		}

		mDescripe.setText(shareDetailVO.getTitle());
		mContent.setText(shareDetailVO.getDescription());
		mTime.setText(shareDetailVO.getCreateTime());
		mLocation.setText(shareDetailVO.getAddressLabel());

		// 缺少是否点赞过的字段，缺少评论的字段
		// if (shareDetailVO.get) {
		//
		// }
	}

	/**
	 * 
	 * 点赞取消点赞
	 * 
	 * @author chenjiqiang 2015年11月3日 下午7:01:58 <br>
	 */
	private void praise(boolean save_praise) {
		if (save_praise) {
			iv_praise.setImageResource(R.drawable.title_bar_praise_press);
		} else {
			iv_praise.setImageResource(R.drawable.title_bar_praise_normal);
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

	@Override
	public void onClick(View v) {
		if (v.equals(iv_praise)) {
			TopTitleUtils.submitPraiseInfo(mContext, true, UserInfoUtils
					.getUser(mContext).getId(), shareId, mHandler, SAVE_PRAISE);
		} else if (v.equals(commentBtn)) {
			TopTitleUtils.submitCommentInfo(mContext,
					UserInfoUtils.getUser(mContext).getId(),
					shareDetailVO.getId(), et_comment.getText().toString(),
					mHandler, SAVE_COMMENT);
			mCancelProgressDialog.show("请稍等...");
		}else if (v.equals(leftimageview)) {
			finish();
		}
	}
}
