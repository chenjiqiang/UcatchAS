package com.chen.ucatch.ui;

import java.text.ParseException;
import java.util.List;
import java.util.Set;

import org.afinal.simplecache.ACache;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.chen.ucatch.R;
import com.chen.ucatch.fragment.HomeFragment;
import com.chen.ucatch.fragment.MineFragment;
import com.chen.ucatch.model.BizTypeEnum;
import com.chen.ucatch.model.JPushVO;
import com.chen.ucatch.utils.JPushCheckUtils;
import com.chen.ucatch.utils.JPushUtil;
import com.chen.ucatch.utils.UserInfoUtils;
import com.chen.ucatch.utils.Utility;
import com.chen.ucatch.view.MainCatchDialog;
import com.loopj.android.http.AsyncHttpClient;
public class UcatchMainActivity extends FragmentActivity {
	public static final String TAG = "UcatchMainActivity";
	/**
	 * 上下文
	 */
	private Context mContext;
	/**
	 * 发现按钮
	 */
	private Button mbt_find;
	/**
	 * 中间突出按钮
	 */
	private Button mbt_catch;
	/**
	 * 右边我的按钮
	 */
	private Button mbt_mine;
	/**
	 * 缓存的工具
	 */
	public static ACache mCache;
	/**
	 * 网络连接对象
	 */
	private AsyncHttpClient asyncHttpClient;
	/**
	 * 存放2个fragment的list
	 */
	private List<Fragment> fragmentList = null;
	/**
	 * 屏幕的宽度
	 */
	public static int width;
	/**
	 * 屏幕的高
	 */
	public static int height;
	/**
	 * 发现页面
	 */
	private Fragment findFragment;
	/**
	 * 个人中心
	 */
	private Fragment personFragment;
	private FragmentManager fragmentManager;
	private FragmentTransaction transaction;
	private Handler mHandler;
	/**
	 * 极光推送设置 别名的求情码
	 */
	public final int MSG_SET_ALIAS = 1003;
	public static String JPUSH_INFO = "JpushVo";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 获取点击通知栏后 传过来的推送数据打开详情界面
		openDetail(this, getIntent());
		setContentView(R.layout.home);
		mContext = this;
		mCache = ACache.get(mContext);
		asyncHttpClient = Utility.getAsyncHttpClient();
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		width = metric.widthPixels;
		height = metric.heightPixels;
		fragmentManager = getSupportFragmentManager();
		initView();
		initHandle();
		imageViewBindListner();
		initData();
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		return super.onCreateView(name, context, attrs);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onResumeFragments() {
		// TODO Auto-generated method stub
		super.onResumeFragments();
	}

	private void initData() {
		if (UserInfoUtils.getUser(mContext) != null) {
			try {
				JPushUtil.customSettings(mContext);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JPushInterface.setAlias(mContext, UserInfoUtils.getUser(mContext)
					.getId(), mAliasCallback);
		} else {
			finish();
		}
	}

	private void initHandle() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg.what == MSG_SET_ALIAS) {
					JPushInterface.setAlias(mContext, (String) msg.obj,
							mAliasCallback);
					return;
				}
			}
		};
	}

	private void initView() {
		transaction = fragmentManager.beginTransaction();
		Fragment fragment = new HomeFragment();
		transaction.replace(R.id.content, fragment);
		transaction.commit();
		// mViewPager = (Home_Viewpager) findViewById(R.id.viewpager);
		mbt_find = (Button) findViewById(R.id.find);
		mbt_catch = (Button) findViewById(R.id.uCatch);
		mbt_mine = (Button) findViewById(R.id.main_mine);
		// fg_find = new FindFragment();
		// fg_mine = new MineFragment();
		// fragmentList = new ArrayList<Fragment>();
		// fragmentList.add(fg_find);
		// fragmentList.add(fg_mine);
		// mViewPager.setAdapter(new MyViewPagerAdapter(
		// getSupportFragmentManager(), fragmentList));
		// mViewPager.setOnPageChangeListener(new MyPageChangeListener());
		// mViewPager.setOffscreenPageLimit(2);
		// mViewPager.setCurrentItem(0, false);
	}

	/**
	 * 底部导航绑定监听
	 */
	public void imageViewBindListner() {
		mbt_find.setOnClickListener(new MyOnClickListener());
		mbt_catch.setOnClickListener(new MyOnClickListener());
		mbt_mine.setOnClickListener(new MyOnClickListener());
	}

	/**
	 * 底部三个导航的监听
	 */
	class MyOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.find:
				transaction = fragmentManager.beginTransaction();
				Fragment homeFragment = new HomeFragment();
				transaction.replace(R.id.content, homeFragment);
				transaction.commit();
				break;
			case R.id.uCatch:
				MainCatchDialog dialog = new MainCatchDialog(mContext);
				dialog.show();
				break;
			case R.id.main_mine:
				transaction = fragmentManager.beginTransaction();
				Fragment mineFragment = new MineFragment();
				transaction.replace(R.id.content, mineFragment);
				transaction.commit();
				break;
			}
		}
	}


	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		openDetail(this, intent);
	}

	/**
	 * 根据推送信息打开详情界面
	 */
	private void openDetail(Context context, Intent newIntent) {
		JPushVO jPushVO = (JPushVO) newIntent
				.getSerializableExtra(this.JPUSH_INFO);

		if (jPushVO == null) {
			return;
		}
		// Intent intent = null;
		if (jPushVO.getBizType().equals(BizTypeEnum.pickup_subject)) {
			Toast.makeText(context, "pickup_subject", 2000).show();
		} else if (jPushVO.getBizType().equals(BizTypeEnum.focused_user)) {
			Toast.makeText(context, "focused_user", 2000).show();
		} else if (jPushVO.getBizType().equals(BizTypeEnum.reply_share)) {
			Toast.makeText(context, "reply_share", 2000).show();
		} else if (jPushVO.getBizType().equals(BizTypeEnum.reply_subject)) {
			Toast.makeText(context, "reply_subject", 2000).show();
		}

		// this.startActivity(intent);
	}

	/**
	 * 极光推送设置别名的回调
	 */
	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "极光推送 设置别名成功";
				break;

			case 6002:
				logs = "极光推送 设置别名失败，60秒后重新设置";
				if (JPushCheckUtils.isConnected(getApplicationContext())) {
					mHandler.sendMessageDelayed(
							mHandler.obtainMessage(MSG_SET_ALIAS, alias),
							1000 * 60);
				} else {
					Log.i(TAG, "没有网络");
				}
				break;

			default:
				logs = "Failed with errorCode = " + code;
				Log.e(TAG, logs);
			}
		}

	};
	/**
	 * 第一次按下返回键
	 */
	private long firstPressTime = 0;
	/**
	 * 设置按两次退出程序
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long secondTime = System.currentTimeMillis();
			if (secondTime - firstPressTime > 2000) { // 如果两次按键时间间隔大于1000毫秒，则不退出
				Toast.makeText(mContext, "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				firstPressTime = secondTime;// 更新firstTime
				return true;
			} else {
				int pid = android.os.Process.myPid();
				android.os.Process.killProcess(pid);
				System.exit(0);// 否则退出程序
			}
		}
		return super.onKeyUp(keyCode, event);
	}
}
