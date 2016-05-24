package com.chen.ucatch.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.afinal.simplecache.ACache;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.chen.ucatch.ConstantValue;
import com.chen.ucatch.R;
import com.chen.ucatch.adapter.FindItemAdapter;
import com.chen.ucatch.adapter.MyAdapter;
import com.chen.ucatch.model.PhotoShareVO;
import com.chen.ucatch.model.ReturnVO;
import com.chen.ucatch.ui.LoginActivity;
import com.chen.ucatch.ui.ShareDetailAcitivity;
import com.chen.ucatch.utils.HttpRequestUtils;
import com.chen.ucatch.utils.ServerUrl;
import com.chen.ucatch.utils.TimeFormatUtil;
import com.chen.ucatch.utils.UserInfoUtils;
import com.chen.ucatch.view.SelectDialog;
import com.chen.ucatch.view.XListView;
import com.chen.ucatch.view.XListView.IXListViewListener;

public class AttentionFragment extends Fragment implements OnClickListener {
	public static final String TAG="AttentionFragment";
	/**
	 * fragment的布局
	 */
	private View mMainview = null;
	/**
	 * 上下文
	 */
	private Context mContext;

	/**
	 * listView
	 */
	private XListView mListView;
	private Handler mHandler;
	/**
	 * 获取列表数据的标志
	 */
	private final int GET_LIST = 1001;

	/**
	 * 是否下拉刷新
	 */
	private boolean isPullRefresh = false;
	/**
	 * 列表数据
	 */
	private List<PhotoShareVO> listDatas = new ArrayList<PhotoShareVO>();
	/**
	 * 自定义适配器
	 */
	private FindItemAdapter adapter;
	/**
	 * 页号
	 */
	private int pageNo = 1;
	/**
	 * 分页大小
	 */
	private int pageSize = 10;
	private static final int LOGINACTIVITY = 10001;
	private ACache mAcache;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mMainview = inflater.inflate(R.layout.find, container, false);
		ViewGroup parent = (ViewGroup) mMainview.getParent();
		this.mContext = getActivity();
		mAcache = ACache.get(mContext);
		if (parent != null) {
			parent.removeView(mMainview);
		}
		initView();
		initHandler();
		setView();
		return mMainview;
	}

	private void initData() {
		if (isPullRefresh) {
			pageNo = 1;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", UserInfoUtils.getUser(mContext).getId().toString());
		params.put("pageNo", pageNo);
		params.put("pageSize", pageSize);
		HttpRequestUtils.post(mContext, ServerUrl.GET_ATTENTION_LIST, params,
				mHandler, GET_LIST);
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
					mListView.stopRefresh();
					mListView.stopLoadMore();
					return;
				}
				if (vo.getSuccessFlag() == 1) {
					switch (msg.what) {
					case GET_LIST:
						List<PhotoShareVO> DataList = (List<PhotoShareVO>) com.alibaba.fastjson.JSONArray
								.parseArray(String.valueOf(vo.getData()),
										PhotoShareVO.class);
						pageNo++;
						// 判断是加载更多还是下拉刷新
						if (isPullRefresh) {
							listDatas.clear();
							listDatas.addAll(DataList);
						} else {
							listDatas.addAll(DataList);
						}
						if (listDatas.size() < 1) {
						} else if (DataList.size() < 1) {
							Toast.makeText(mContext, "没有更多数据", 2000).show();
						}
						adapter.notifyDataSetChanged();
						mListView.stopRefresh();
						mListView.stopLoadMore();
						break;
					default:
						break;
					}
				} else if (vo.getSuccessFlag() == 0) {
					mListView.stopRefresh();
					mListView.stopLoadMore();
					Toast.makeText(mContext, vo.getMessage(), 2000).show();
				} else {
					mListView.stopRefresh();
					mListView.stopLoadMore();
					Toast.makeText(mContext, vo.getMessage(), 2000).show();
				}
			}
		};
	}

	private void initView() {
		mListView = (XListView) mMainview.findViewById(R.id.find_listView);
		// adapter = new MyAdapter(mContext, listDatas,
		// R.layout.find_listview_item);
		adapter = new FindItemAdapter(mContext,TAG);
		adapter.setList(listDatas);
		mListView.setAdapter(adapter);
	}

	private void setView() {
		// TODO Auto-generated method stub
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(xListViewListener);
		// 主动请求刷新
		mListView.loadDefault();
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle b = new Bundle();
				b.putString("id", listDatas.get(position - 1).getId());
				intent.putExtras(b);
				intent.setClass(mContext, ShareDetailAcitivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View v) {

	}

	/**
	 * 列表刷新间隔控制用，防止连续快速刷新
	 */
	private long freshTime = 0;
	/**
	 * 列表刷新间隔控制用，防止连续快速刷新
	 */
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
			mAcache.remove(FindItemAdapter.ATTENTION_SHAREID);
			freshTime = time;
			isPullRefresh = true;
			mListView.setRefreshTime(TimeFormatUtil.getCurrentDataToString());
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
