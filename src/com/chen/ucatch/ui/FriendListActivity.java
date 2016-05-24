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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.chen.ucatch.ConstantValue;
import com.chen.ucatch.R;
import com.chen.ucatch.adapter.FindItemAdapter;
import com.chen.ucatch.adapter.MyFriendListAdapter;
import com.chen.ucatch.model.PhotoShareVO;
import com.chen.ucatch.model.ReturnVO;
import com.chen.ucatch.model.UserVO;
import com.chen.ucatch.utils.HttpRequestUtils;
import com.chen.ucatch.utils.ServerUrl;
import com.chen.ucatch.utils.TimeFormatUtil;
import com.chen.ucatch.utils.UserInfoUtils;
import com.chen.ucatch.view.XListView;
import com.chen.ucatch.view.XListView.IXListViewListener;

/**
 * 
 * TODO 在此写上类的相关说明. <br>
 * 
 * @author chenjiqiang <br>
 * @version 1.0.0 2016年3月14日 上午9:54:08 <br>
 * @see
 * @since JDK 1.4.2.6
 */
public class FriendListActivity extends MyBaseActivity implements
		OnClickListener {
	private Handler mHandler;
	/**
	 * 返回按钮
	 */
	private ImageView iv_back;
	/**
	 * 标题
	 */
	private TextView tv_title_top;
	private XListView xListView;
	private MyFriendListAdapter adapter;
	private List<UserVO> datas;
	/**
	 * 是否下拉刷新
	 */
	private boolean isPullRefresh = false;
	private final int GET_LIST = 1001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myfriends_layout);
		initView();
		initHandler();
		initListener();
		initData();
	}

	private void initView() {
		findViewById(R.id.rightpraise).setVisibility(View.GONE);
		findViewById(R.id.rightcollect).setVisibility(View.GONE);
		findViewById(R.id.rightshare).setVisibility(View.GONE);
		iv_back = (ImageView) findViewById(R.id.leftimageview);
		tv_title_top = (TextView) findViewById(R.id.titletext);
		tv_title_top.setText("我的好友");
		xListView = (XListView) findViewById(R.id.xListView_myfriends);
		datas = new ArrayList<UserVO>();
		adapter = new MyFriendListAdapter(mContext, datas,
				R.layout.myfriends_list_item);
		xListView.setAdapter(adapter);
		xListView.setPullLoadEnable(false);
		xListView.setPullRefreshEnable(false);
		// xListView.setXListViewListener(xListViewListener);
		// // 主动请求刷新
		// xListView.loadDefault();
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
								ConstantValue.NETWORK_NO_RESPONE_TEXT, 2000)
								.show();
					} else if (msg.what == ConstantValue.SERVER_NO_RESPONE) {
						Toast.makeText(mContext,
								ConstantValue.SERVER_NO_RESPONE_TEXT, 2000)
								.show();
					}
					xListView.stopRefresh();
					xListView.stopLoadMore();
					return;
				}
				if (vo.getSuccessFlag() == 1) {
					switch (msg.what) {
					case GET_LIST:
						List<UserVO> DataList = (List<UserVO>) com.alibaba.fastjson.JSONArray
								.parseArray(String.valueOf(vo.getData()),
										UserVO.class);
						if (DataList.size() <= 0) {
							Toast.makeText(mContext, "你目前还没有好友，快去添加好友吧~",
									Toast.LENGTH_SHORT).show();
							return;
						}
						datas.addAll(DataList);
						adapter.notifyDataSetChanged();
						xListView.stopRefresh();
						xListView.stopLoadMore();
						break;
					default:
						break;
					}
				} else if (vo.getSuccessFlag() == 0) {
					xListView.stopRefresh();
					xListView.stopLoadMore();
					Toast.makeText(mContext, vo.getMessage(), 2000).show();
				} else {
					xListView.stopRefresh();
					xListView.stopLoadMore();
					Toast.makeText(mContext, vo.getMessage(), 2000).show();
				}
			}
		};
	}

	private void initListener() {
		iv_back.setOnClickListener(this);
	}

	private void initData() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", UserInfoUtils.getUser(mContext).getId().toString());
		HttpRequestUtils.post(mContext, ServerUrl.GETFRIENDS, params, mHandler,
				GET_LIST);
	}

	@Override
	public void onClick(View v) {
		if (v.equals(iv_back)) {
			finish();
		}
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
}
