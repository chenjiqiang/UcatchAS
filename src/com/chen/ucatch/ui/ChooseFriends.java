package com.chen.ucatch.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.afinal.simplecache.ACache;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.ucatch.ConstantValue;
import com.chen.ucatch.R;
import com.chen.ucatch.model.ReturnVO;
import com.chen.ucatch.model.UserVO;
import com.chen.ucatch.utils.TopTitleUtils;
import com.chen.ucatch.utils.UserInfoUtils;
import com.chen.ucatch.utils.Utility;
import com.chen.ucatch.view.CharacterParser;
import com.chen.ucatch.view.ClearEditText;
import com.chen.ucatch.view.PinyinComparator;
import com.chen.ucatch.view.SideBar;
import com.chen.ucatch.view.SideBar.OnTouchingLetterChangedListener;
import com.chen.ucatch.view.SortAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.meg7.widget.CircleImageView;

public class ChooseFriends extends Activity implements OnClickListener {
	private Context mContext;
	/**
	 * listview
	 */
	private ListView sortListView;
	/**
	 * 适配器
	 */
	private SortAdapter adapter;
	/**
	 * 搜索框
	 */
	private ClearEditText mClearEditText;

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	/**
	 * list数据
	 */
	private List<UserVO> listDatas = new ArrayList<UserVO>();
	/**
	 * 数据
	 */
	private List<UserVO> SourceDateList = new ArrayList<UserVO>();
	/**
	 * 选择的数据
	 */
	private List<UserVO> choose_datas = new ArrayList<UserVO>();
	/**
	 * 缓存的数据
	 */
	private List<UserVO> listData = new ArrayList<UserVO>();

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	/**
	 * 请求
	 */
	AsyncHttpClient asyncHttpClient = Utility.getAsyncHttpClient();
	/**
	 * 取消按钮
	 */
	private TextView bt_cancel;
	/**
	 * edtitext的父控件
	 */
	private LinearLayout editText_parent;
	/**
	 * 通讯录右边a-z 标识
	 */
	private SideBar sideBar;
	/**
	 * 对话框
	 */
	private TextView dialog;
	/**
	 * 缓存的工具
	 */
	private ACache mCache;
	/**
	 * 头像的列表布局视图
	 */
	private LinearLayout mheads;
	/**
	 * 第一个好友
	 */
	private CircleImageView mUserHead1;
	/**
	 * 第二个好友
	 */
	private CircleImageView mUserHead2;
	/**
	 * 第三个好友
	 */
	private CircleImageView mUserHead3;
	/**
	 * 省略号
	 */
	private TextView mTv_more;
	/**
	 * 头像的个数
	 */
	private Integer userhead_num = 0;
	private List positions = new ArrayList<Integer>();
	/**
	 * 确定按钮
	 */
	private Button mBt_ok;
	/**
	 * 返回结果码
	 */
	public static final int CHOOSEFRIENDS_RESULT_CODE = 1112;
	public static final int GETFRIENDS_CODE = 1001;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_friends);
		mContext = this;
		initView();
		initHandler();
		initData();
	}

	private void initHandler() {
		// TODO Auto-generated method stub
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
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
					} else {
						Toast.makeText(mContext,
								ConstantValue.SERVER_NO_RESPONE_TEXT, 2000)
								.show();
					}
					return;
				}
				if (vo.getSuccessFlag() == 1) {
					switch (msg.what) {
					case GETFRIENDS_CODE:
						listDatas = com.alibaba.fastjson.JSONObject.parseArray(
								String.valueOf(vo.getData()), UserVO.class);
						if (listDatas != null && listDatas.size() > 0) {
							SourceDateList.clear();
							SourceDateList = filledData(listDatas);
							listDatas.clear();
							// 根据a-z进行排序源数据
							Collections.sort(SourceDateList, pinyinComparator);
							adapter.updateListView(SourceDateList);
						}
						break;
					default:
						break;
					}
				} else if (vo.getSuccessFlag() == 0) {
					Toast.makeText(mContext, vo.getMessage(), 2000).show();
				}
			}
		};
	}

	private void initData() {
		// TODO Auto-generated method stub
		TopTitleUtils.getFriends(mContext, UserInfoUtils.getUser(mContext)
				.getId(), mHandler, GETFRIENDS_CODE);
		// if (listDatas != null && listDatas.size() > 0) {
		// SourceDateList.clear();
		// SourceDateList = filledData(listDatas);
		// listDatas.clear();
		// // 根据a-z进行排序源数据
		// Collections.sort(SourceDateList, pinyinComparator);
		// adapter.updateListView(SourceDateList);
		// }
	}

	private void initView() {
		mBt_ok = (Button) findViewById(R.id.bt_ok);
		mBt_ok.setOnClickListener(this);
		mUserHead1 = (CircleImageView) findViewById(R.id.choose_friends_userhead1);
		mUserHead2 = (CircleImageView) findViewById(R.id.choose_friends_userhead2);
		mUserHead3 = (CircleImageView) findViewById(R.id.choose_friends_userhead3);
		mTv_more = (TextView) findViewById(R.id.choose_friends_userhead4);
		editText_parent = (LinearLayout) findViewById(R.id.editText_parent);
		bt_cancel = (TextView) findViewById(R.id.tv_cancel);
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});

		sortListView = (ListView) findViewById(R.id.lv_choose_friends);
		adapter = new SortAdapter(mContext, SourceDateList);
		sortListView.setAdapter(adapter);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				if (positions.contains(position)) {
					choose_datas.remove((UserVO) adapter.getItem(position));
					userhead_num--;
					positions.remove(Integer.valueOf(position));
					if (mUserHead1.getTag().equals(position)) {
						mUserHead1.setTag(-1);
						mUserHead1.setVisibility(View.GONE);
					} else if (mUserHead2.getTag().equals(position)) {
						mUserHead2.setTag(-1);
						mUserHead2.setVisibility(View.GONE);

					} else if (mUserHead3.getTag().equals(position)) {
						mUserHead3.setTag(-1);
						mUserHead3.setVisibility(View.GONE);
					}
					mTv_more.setVisibility(View.GONE);
					return;
				}
				choose_datas.add((UserVO) adapter.getItem(position));
				positions.add(position);
				userhead_num++;
				if (mUserHead1.getVisibility() == 8) {
					mUserHead1.setTag(position);
					mUserHead1.setVisibility(View.VISIBLE);
				} else if (mUserHead2.getVisibility() == 8) {
					mUserHead2.setTag(position);
					mUserHead2.setVisibility(View.VISIBLE);
				} else if (mUserHead3.getVisibility() == 8) {
					mUserHead3.setTag(position);
					mUserHead3.setVisibility(View.VISIBLE);
				}
				if (userhead_num > 3) {
					mTv_more.setVisibility(View.VISIBLE);
				}
			}
		});
		// SourceDateList = filledData(listDatas);

		/*
		 * // 根据a-z进行排序源数据 Collections.sort(SourceDateList, pinyinComparator);
		 * adapter = new SortAdapter(mContext, SourceDateList);
		 * sortListView.setAdapter(adapter);
		 */

		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		mClearEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					mClearEditText.setCursorVisible(true);
					bt_cancel.setVisibility(View.VISIBLE);
				} else {
					bt_cancel.setVisibility(View.GONE);
					mClearEditText.setClearIconVisible(false);
					mClearEditText.setText("");
				}
			}
		});

		bt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				bt_cancel.setVisibility(View.GONE);
				mClearEditText.clearFocus();
				editText_parent.setFocusable(true);
				editText_parent.setFocusableInTouchMode(true);
				InputMethodManager imm = (InputMethodManager) mClearEditText
						.getContext()
						.getSystemService(
								android.inputmethodservice.InputMethodService.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mClearEditText.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);

			}
		});
		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
				/*
				 * Collections.sort(SourceDateList, pinyinComparator); adapter =
				 * new SortAdapter(mContext, SourceDateList);
				 * sortListView.setAdapter(adapter);
				 */
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param listDatas2
	 * @return
	 */
	private List<UserVO> filledData(List<UserVO> listDatas2) {
		List<UserVO> mSortList = new ArrayList<UserVO>();

		for (int i = 0; i < listDatas2.size(); i++) {
			UserVO userVO = new UserVO();
			/**
			 * 名字的保存
			 */
			userVO.setNickName(listDatas2.get(i).getNickName());
			/**
			 * 用户的id
			 */
			userVO.setId(listDatas2.get(i).getId());
			/**
			 * 用户的性别
			 */
			userVO.setSex(listDatas2.get(i).getSex());
			/**
			 * 汉字转换成拼音
			 */
			String pinyin = characterParser.getSelling(listDatas2.get(i)
					.getNickName());
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				userVO.setSortLetters(sortString.toUpperCase());
			} else {
				userVO.setSortLetters("#");
			}

			mSortList.add(userVO);
		}
		return mSortList;

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	List<UserVO> filterDateList = new ArrayList<UserVO>();

	private void filterData(String filterStr) {
		if (TextUtils.isEmpty(filterStr)) {
			filterDateList.clear();
			filterDateList.addAll(SourceDateList);
		} else {
			filterDateList.clear();
			for (UserVO sortModel : SourceDateList) {
				String name = sortModel.getNickName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(mBt_ok)) {
			Intent intent = new Intent();
			Bundle b = new Bundle();
			UserVO sortModel = new UserVO();
			sortModel.setList(choose_datas);
			b.putSerializable(ConstantValue.CHOOSE_FRIENDS, sortModel);
			intent.putExtras(b);
			setResult(CHOOSEFRIENDS_RESULT_CODE, intent);
			finish();
		}
	}
}
