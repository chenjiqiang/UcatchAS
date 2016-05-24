package com.chen.ucatch.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.ucatch.ConstantValue;
import com.chen.ucatch.R;
import com.chen.ucatch.adapter.MyMessageAdapter;
import com.chen.ucatch.model.MySubjectVO;
import com.chen.ucatch.model.ReturnVO;
import com.chen.ucatch.model.SubjectVO;
import com.chen.ucatch.utils.HttpRequestUtils;
import com.chen.ucatch.utils.ServerUrl;
import com.chen.ucatch.utils.TimeFormatUtil;
import com.chen.ucatch.utils.UserInfoUtils;
import com.chen.ucatch.view.XListView;
import com.chen.ucatch.view.XListView.IXListViewListener;

public class MyMessageActivity extends MyBaseActivity implements
		OnClickListener {
	private XListView xListView;
	private Handler mHandler;
	private ImageView mBack;
	private TextView title;
	private List<MySubjectVO> mDatas;
	private MyMessageAdapter mAdapter;
	/**
	 * 是否下拉刷新
	 */
	private boolean isPullRefresh = false;
	/**
	 * 页号
	 */
	private int pageNo = 1;
	/**
	 * 分页大小
	 */
	private int pageSize = 10;
	private static final int GET_LIST = 1001;
	private static final int TURNTODETAIL = 10001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mymessage);
		initView();
		initHandler();
		initSetListView();
		initListener();
	}

	private void initSetListView() {
		mAdapter = new MyMessageAdapter(mContext, mDatas,
				R.layout.mymessage_item);
		xListView.setAdapter(mAdapter);
		xListView.setAdapter(mAdapter);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(xListViewListener);
		// 主动请求刷新
		xListView.loadDefault();
		xListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position <= 0) {
					return;
				}
				Intent intent = new Intent(mContext, CommentAcitivity.class);
				Bundle b = new Bundle();
				b.putString(PickActivity.SUBJECTID, mDatas.get(position - 1)
						.getId());
				intent.putExtras(b);
				startActivityForResult(intent, TURNTODETAIL);
			}
		});
	}

	private void initView() {
		mDatas = new ArrayList<MySubjectVO>();
		findViewById(R.id.rightpraise).setVisibility(View.GONE);
		findViewById(R.id.rightcollect).setVisibility(View.GONE);
		findViewById(R.id.rightshare).setVisibility(View.GONE);
		mBack = (ImageView) findViewById(R.id.leftimageview);
		title = (TextView) findViewById(R.id.titletext);
		title.setText("我的留言");
		xListView = (XListView) findViewById(R.id.my_message_xlistview);
	}

	private void initHandler() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				ReturnVO vo = (ReturnVO) msg.obj;
				if (vo == null) {
					if (msg.what == ConstantValue.NETWORK_NO_RESPONE) {
						Toast.makeText(mContext,
								ConstantValue.NETWORK_NO_RESPONE_TEXT,
								Toast.LENGTH_SHORT).show();
					} else if (msg.what == ConstantValue.SERVER_NO_RESPONE) {
						Toast.makeText(mContext,
								ConstantValue.SERVER_NO_RESPONE_TEXT,
								Toast.LENGTH_SHORT).show();
					}
					xListView.stopRefresh();
					xListView.stopLoadMore();
					return;
				}
				if (vo.getSuccessFlag() == 1) {
					switch (msg.what) {
					case GET_LIST:
						List<MySubjectVO> DataList = (List<MySubjectVO>) com.alibaba.fastjson.JSONArray
								.parseArray(String.valueOf(vo.getData()),
										MySubjectVO.class);
						if (isPullRefresh) {
							mDatas.clear();
							mDatas.addAll(DataList);
						} else {
							mDatas.addAll(DataList);
						}
						if (mDatas.size() < 1) {
							Toast.makeText(mContext, "没有留言过...",
									Toast.LENGTH_SHORT).show();
						} else if (DataList.size() < 1) {
							Toast.makeText(mContext, "没有更多数据",
									Toast.LENGTH_SHORT).show();
						}
						mAdapter.notifyDataSetChanged();
						xListView.stopRefresh();
						xListView.stopLoadMore();
						pageNo++;
						break;
					default:
						break;
					}
				} else if (vo.getSuccessFlag() == 0) {
					xListView.stopRefresh();
					xListView.stopLoadMore();
					Toast.makeText(mContext, vo.getMessage(),
							Toast.LENGTH_SHORT).show();
				} else {
					xListView.stopRefresh();
					xListView.stopLoadMore();
					Toast.makeText(mContext, vo.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	private void initListener() {
		mBack.setOnClickListener(this);
	}

	private void initData() {
		if (isPullRefresh) {
			pageNo = 1;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", UserInfoUtils.getUser(mContext).getId().toString());
		params.put("pageNo", pageNo);
		params.put("pageSize", pageSize);
		HttpRequestUtils.post(mContext, ServerUrl.MY_MESSAGE_LIST, params,
				mHandler, GET_LIST);
	}

	/**
	 * 列表刷新间隔控制用，防止连续快速刷新
	 */
	private long freshTime = 0;
	/**
	 * 下拉刷新与上拉加载
	 */
	IXListViewListener xListViewListener = new IXListViewListener() {

		@Override
		public void onRefresh() {
			long time = System.currentTimeMillis();
			if (time - freshTime < 1000) {
				return;
			}
			freshTime = time;
			isPullRefresh = true;
			xListView.setRefreshTime(TimeFormatUtil.getCurrentDataToString());
			initData();
		}

		@Override
		public void onLoadMore() {
			long time = System.currentTimeMillis();
			if (time - freshTime < 1000) {
				return;
			}
			freshTime = time;
			isPullRefresh = false;
			initData();
		};

	};

	@Override
	public void onClick(View v) {
		if (v.equals(mBack)) {
			finish();
		}
	}

}
