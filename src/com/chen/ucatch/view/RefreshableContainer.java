/*
 * Copyright 2000-2020 YGSoft.Inc All Rights Reserved.
 */
package com.chen.ucatch.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chen.ucatch.R;
import com.chen.ucatch.utils.TimeUtil;
import com.chen.ucatch.utils.ViewUtil;


/**
 * 一个下拉刷新的容器类,可通过属性设置是否上拉和下拉<br>
 * pull="true|false"是否使用上拉刷新操作 ,默认为true<br>
 * footer="true|false"是否使用下拉加载更多操作 ,默认为true <br>
 * footerRefreshing()为主动加载更多 完成使用onFooterRefreshComplete(),监听setOnFooterRefreshListener<br>
 * headerRefreshing() 主动下拉加载 .完成使用onHeaderRefreshComplete(),监听setOnHeaderRefreshListener .<br>
 * @author liulongzhenhai <br>
 * @version 1.0.0 2013-4-11<br>
 * @see
 * @since JDK 1.4.2.6
 */
public class RefreshableContainer extends LinearLayout {
	/**
	 * TAG .
	 */
	private static final String TAG = "PullToRefreshView";

	/**
	 * 是否可以下拉加载更多属性名 .
	 */
	private static final String CANPULLATT = "pull";

	/**
	 * 是否可以刷新的属性名 .
	 */
	private static final String CANFOOTERATT = "footer";

	/**
	 * 正在操作刷新 .
	 */
	private static final int PULL_TO_REFRESH = 2;

	/**
	 * 刷新 释放刷新 .
	 */
	private static final int RELEASE_TO_REFRESH = 3;

	/**
	 * 正在刷新 .
	 */
	private static final int REFRESHING = 4;

	/**
	 * 上拉状态 .
	 */
	private static final int PULL_UP_STATE = 0;
	/**
	 * 加载更多状态 .
	 */
	private static final int PULL_DOWN_STATE = 1;

	/**
	 * 下拉刷新 .
	 */
	private String pullToreFreshPull = "下拉刷新";

	/**
	 * 松开后刷新.
	 */
	private static final String PULLTOREFRESHRELEASE = "松开后刷新 ";

	/**
	 * 刷新 .
	 */
	private static final String PULLTOREFRESHREFRESHING = "刷新";

	/**
	 * 松开后加载 .
	 */
	private static final String PULLTOREFRESHFOOTERRELEASE = "松开后加载 ";

	/**
	 * 松开后加载 .
	 */
	private static final String PULLTOREFRESHFOOTPULL = "上拉加载更多";

	/**
	 * 加载中 .
	 */
	private static final String PULLTOREFRESHFOOTREFRESHING = "加载中 ";

	/**
	 * last y .
	 */
	private int mLastMotionY;

	/**
	 * lock .
	 */
	private boolean mLock;

	/**
	 * header view .
	 */
	private View mHeaderView;

	/**
	 * footer view .
	 */
	private View mFooterView;

	/**
	 * list or grid .
	 */
	private AdapterView<?> mAdapterView;

	/**
	 * scrollview .
	 */
	private ScrollView mScrollView;

	/**
	 * header view height .
	 */
	private int mHeaderViewHeight = 0;

	/**
	 * footer view height .
	 */
	private int mFooterViewHeight = 0;

	/**
	 * header view image .
	 */
	private ImageView mHeaderImageView;

	/**
	 * footer view image .
	 */
	private ImageView mFooterImageView;

	/**
	 * header tip text .
	 */
	private TextView mHeaderTextView;

	/**
	 * footer tip text .
	 */
	private TextView mFooterTextView;

	/**
	 * header refresh time .
	 */
	private TextView mHeaderUpdateTextView;

	/**
	 * 底部的高度 .<br>
	 */
	int bottomHeight;
	/**
	 * 容器距离底部的范围 .
	 */
	int containerPaddingBottom = 0;
	/**
	 * footer refresh time
	 */
	// private TextView mFooterUpdateTextView;

	/**
	 * header progress bar .
	 */
	private ProgressBar mHeaderProgressBar;

	/**
	 * footer progress bar .
	 */
	private ProgressBar mFooterProgressBar;

	/**
	 * layout inflater .
	 */
	private LayoutInflater mInflater;

	/**
	 * header view current state .
	 */
	private int mHeaderState;

	/**
	 * footer view current state .
	 */
	private int mFooterState;

	/**
	 * pull state,pull up or pull down;PULL_UP_STATE or PULL_DOWN_STATE .
	 */
	private int mPullState;

	/**
	 * 变为向下的箭头,改变箭头方向 .
	 */
	private RotateAnimation mFlipAnimation;

	/**
	 * 变为逆向的箭头,旋转 .
	 */
	private RotateAnimation mReverseFlipAnimation;

	/**
	 * footer refresh listener .
	 */
	private OnFooterRefreshListener mOnFooterRefreshListener;
	/**
	 * footer refresh listener .
	 */
	private OnHeaderRefreshListener mOnHeaderRefreshListener;

	/**
	 * last update time .
	 */
	private String mLastUpdateTime;

	/**
	 * 是否可以刷新 .
	 */
	public boolean isPull = true;
	/**
	 * 在进行切换的时候的变量保留 .
	 */
	 boolean tmpIsPull = true;
	/**
	 * 是否可以加载更多 .
	 */
	 public boolean isFooter = true;

	/**
	 * 在进行切换的时候的变量保留 . .
	 */
	boolean tmpIsFooter = true;


	public boolean isPull() {
		return isPull;
	}

	public void setPull(boolean isPull) {
		this.isPull = isPull;
	}

	public boolean isFooter() {
		return isFooter;
	}

	public void setFooter(boolean isFooter) {
		this.isFooter = isFooter;
	}

	/**
	 * PullToRefreshView . <br>
	 * @author liulongzhenhai 2013-4-11 上午10:21:36 <br>
	 * @param context Context
	 * @param attrs AttributeSet
	 */
	public RefreshableContainer(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		setAttribute(attrs);
		init(isPull, isFooter);
	}

	/**
	 * PullToRefreshView . <br>
	 * @author liulongzhenhai 2013-4-11 下午2:57:14 <br>
	 * @param context context
	 */
	private RefreshableContainer(final Context context) {
		super(context);
		init(isPull, isFooter);
	}

	/**
	 * PullToRefreshView . <br>
	 * @author liulongzhenhai 2013-4-11 上午10:21:32 <br>
	 * @param context Context
	 * @param canRef 是否可以刷新
	 * @param canFooter 是否加载更多
	 */
	public RefreshableContainer(final Context context, final boolean canRef, final boolean canFooter) {
		super(context);
		setCanRefresh(canRef);
		setCanRefresh(canFooter);
		init(isPull, isFooter);
	}

	/**
	 * 设置属性 . <br>
	 * @author liulongzhenhai 2013-4-11 上午10:49:54 <br>
	 * @param attrs AttributeSet
	 */
	private void setAttribute(final AttributeSet attrs) {
		if ("false".equalsIgnoreCase(attrs.getAttributeValue(null, CANPULLATT))) {
			setCanRefresh(false);

		}

		if ("false".equalsIgnoreCase(attrs.getAttributeValue(null, CANFOOTERATT))) {
			setCanFooter(false);

		}
	}

	/**
	 * 是否能刷新 . <br>
	 * @author liulongzhenhai 2013-4-11 上午10:50:28 <br>
	 * @param canpull true false
	 */
	private void setCanRefresh(final boolean canpull) {
		isPull = canpull;
		tmpIsPull = canpull;
	}

	/**
	 * 是否能加载更多 . <br>
	 * @author liulongzhenhai 2013-4-11 上午10:50:56 <br>
	 * @param canFooter true false
	 */
	public void setCanFooter(final boolean canFooter) {
		isFooter = canFooter;
		tmpIsFooter = canFooter;
	}

	/**
	 * 初始化 . <br>
	 * @author liulongzhenhai 2013-4-11 上午10:21:49 <br>
	 */
	/**
	 * 初始化 . <br>
	 * @author liulongzhenhai 2013-4-11 上午10:51:56 <br>
	 * @param canRef 是否可以刷新
	 * @param canFooter 是否加载更多
	 */
	private void init(final boolean canRef, final boolean canFooter) {
		if (canRef || canFooter) {
			// Load all of the animations we need in code rather than through XML
			mFlipAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
					RotateAnimation.RELATIVE_TO_SELF, 0.5f);
			mFlipAnimation.setInterpolator(new LinearInterpolator());
			mFlipAnimation.setDuration(250);
			mFlipAnimation.setFillAfter(true);
			mReverseFlipAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
					RotateAnimation.RELATIVE_TO_SELF, 0.5f);
			mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
			mReverseFlipAnimation.setDuration(250);
			mReverseFlipAnimation.setFillAfter(true);

			mInflater = LayoutInflater.from(getContext());
		}
		// header view 在此添加,保证是第一个添加到linearlayout的最上端
		this.setOrientation(LinearLayout.VERTICAL);
		addHeaderView();

	}

	/**
	 * 添加头下拉刷新 . <br>
	 * @author liulongzhenhai 2013-4-11 上午10:55:02 <br>
	 */
	private void addHeaderView() {
		if (isPull) {
			// header view
			mHeaderView = mInflater.inflate(R.layout.pulltorefreshview_header, this, false);
			mHeaderImageView = (ImageView) mHeaderView.findViewById(R.id.pull_to_refresh_image);
			mHeaderTextView = (TextView) mHeaderView.findViewById(R.id.pull_to_refresh_text);
			mHeaderUpdateTextView = (TextView) mHeaderView.findViewById(R.id.pull_to_refresh_updated_at);
			mHeaderProgressBar = (ProgressBar) mHeaderView.findViewById(R.id.pull_to_refresh_progress);
			// header layout
		} else { // 给个空的
			mHeaderView = new View(getContext());
		}
		ViewUtil.measureView(mHeaderView);
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		final LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mHeaderViewHeight);
		// 设置topMargin的值为负的header View高度,即将其隐藏在最上方
		params.topMargin = -(mHeaderViewHeight);
		// mHeaderView.setLayoutParams(params1);
		addView(mHeaderView, params);

	}

	/**
	 * 添加加载更多 . <br>
	 * @author liulongzhenhai 2013-4-11 上午10:57:07 <br>
	 */
	private void addFooterView() {
		// footer view
		mFooterView = mInflater.inflate(R.layout.pulltorefreshview_footer, this, false);
		mFooterImageView = (ImageView) mFooterView.findViewById(R.id.pull_to_load_image);
		mFooterTextView = (TextView) mFooterView.findViewById(R.id.pull_to_load_text);
		mFooterProgressBar = (ProgressBar) mFooterView.findViewById(R.id.pull_to_load_progress);
		// footer layout
		ViewUtil.measureView(mFooterView);
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		final LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mFooterViewHeight);
		// int top = getHeight();
		// params.topMargin
		// =getHeight();//在这里getHeight()==0,但在onInterceptTouchEvent()方法里getHeight()已经有值了,不再是0;
		// getHeight()什么时候会赋值,稍候再研究一下
		// 由于是线性布局可以直接添加,只要AdapterView的高度是MATCH_PARENT,那么footer view就会被添加到最后,并隐藏
		addView(mFooterView, params);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		// footer view 在此添加保证添加到linearlayout中的最后
		if (isFooter) {
			addFooterView();
		}
		initContentAdapterView();
	}

	/**
	 * 初始化布局信息. <br>
	 * @author liulongzhenhai 2013-4-11 上午10:57:39 <br>
	 */
	private void initContentAdapterView() {
		final int count = getChildCount();
		if (isFooter && isPull && count < 3) {
			throw new IllegalArgumentException(
					"this layout must contain 3 child views,and AdapterView or ScrollView must in the second position!");
		}
		View view = null;
		for (int i = 0; i < count; ++i) {
			view = getChildAt(i);
			if (view instanceof AdapterView<?>) {
				mAdapterView = (AdapterView<?>) view;
				containerPaddingBottom = mAdapterView.getPaddingBottom();
			}
			if (view instanceof ScrollView) {
				// finish later
				mScrollView = (ScrollView) view;
				containerPaddingBottom = mScrollView.getPaddingBottom();
			}
		}
		if (mAdapterView == null && mScrollView == null) {
			throw new IllegalArgumentException("must contain a AdapterView or ScrollView in this layout!");
		}
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.ViewGroup#onInterceptTouchEvent(android.view.MotionEvent)
	 * @author liulongzhenhai 2013-4-11 上午11:00:06 <br>
	 */
	@Override
	public boolean onInterceptTouchEvent(final MotionEvent e) {
		final int y = (int) e.getRawY();
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 首先拦截down事件,记录y坐标
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			// deltaY > 0 是向下运动,< 0是向上运动
			final int deltaY = y - mLastMotionY;
			if (isRefreshViewScroll(deltaY)) {
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			break;
		default:
			break;
		}
		return false;
	}

	/*
	 * 如果在onInterceptTouchEvent()方法中没有拦截(即onInterceptTouchEvent()方法中 return false)则由PullToRefreshView
	 * 的子View来处理;否则由下面的方法来处理(即由PullToRefreshView自己来处理)
	 */
	/**
	 * {@inheritDoc}
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 * @author liulongzhenhai 2013-4-11 上午11:00:24 <br>
	 */
	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		if (mLock) {
			return true;
		}
		final int y = (int) event.getRawY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// onInterceptTouchEvent已经记录
			// mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			final int deltaY = y - mLastMotionY;

			if (isPull && mPullState == PULL_DOWN_STATE) {
				// PullToRefreshView执行下拉
				headerPrepareToRefresh(deltaY);
				tmpBottomHeight = bottomHeight;
				// setHeaderPadding(-mHeaderViewHeight);
			} else if (isFooter && mPullState == PULL_UP_STATE) {
				// PullToRefreshView执行上拉
				if (containerPaddingBottom > 0) {
					setContainerBottom(0);
				}
				footerPrepareToRefresh(deltaY);

			}
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			tmpBottomHeight = bottomHeight;
			final int topMargin = getHeaderTopMargin();
			if (isPull && mPullState == PULL_DOWN_STATE) {
				if (topMargin >= 0) {
					// 开始刷新
					headerRefreshing();
				} else {
					// 还没有执行刷新，重新隐藏
					setHeaderTopMargin(-mHeaderViewHeight);
				}
			} else if (isFooter && mPullState == PULL_UP_STATE) {
				if (Math.abs(topMargin) >= mHeaderViewHeight + mFooterViewHeight) {
					// 开始执行footer 刷新
					footerRefreshing();
				} else {
					// 还没有执行刷新，重新隐藏
					setHeaderTopMargin(-mHeaderViewHeight);
					if (containerPaddingBottom > 0) {
						setContainerBottom(containerPaddingBottom);
					}
				}
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 是否应该到了父View,即PullToRefreshView滑动 .
	 * @param deltaY , deltaY > 0 是向下运动,< 0是向上运动
	 * @return 是否可以滑
	 */
	private boolean isRefreshViewScroll(final int deltaY) {
		if (mHeaderState == REFRESHING || mFooterState == REFRESHING) {
			return false;
		}
		// 对于ListView和GridView
		if (mAdapterView != null) {
			// 子view(ListView or GridView)滑动到最顶端
			if (deltaY > 0 && isPull) { // 如果是下拉加载更多并且设置了可以下拉的

				final View child = mAdapterView.getChildAt(0);
				if (child != null) {
					// 如果mAdapterView中没有数据,直接可以刷新
					// return true;

					if (mAdapterView.getFirstVisiblePosition() == 0 && child.getTop() == 0) {
						mPullState = PULL_DOWN_STATE;
						return true;
					}
					final int top = child.getTop();
					final int padding = mAdapterView.getPaddingTop();
					if (mAdapterView.getFirstVisiblePosition() == 0 && Math.abs(top - padding) <= 8) {
						// 这里之前用3可以判断,但现在不行,还没找到原因
						mPullState = PULL_DOWN_STATE;
						return true;
					}
				} else {
					mPullState = PULL_DOWN_STATE;
					return true;
				}
			} else if (deltaY < 0 && isFooter) { // 同上的原理判断

				final View lastChild = mAdapterView.getChildAt(mAdapterView.getChildCount() - 1);
				if (lastChild == null) {
					// 如果mAdapterView中没有数据,不拦截
					return false;
				}
				// 最后一个子view的Bottom小于父View的高度说明mAdapterView的数据没有填满父view,
				// 等于父View的高度说明mAdapterView已经滑动到最后
				if (lastChild.getBottom() <= (getHeight()-bottomHeight)
						&& mAdapterView.getLastVisiblePosition() == mAdapterView.getCount() - 1) {
					mPullState = PULL_UP_STATE;
					return true;
				}
			}

		}
		// 对于ScrollView
		if (mScrollView != null) {
			// 子scroll view滑动到最顶端
			final View child = mScrollView.getChildAt(0);
			if (deltaY > 0 && isPull && mScrollView.getScrollY() == 0) {
				mPullState = PULL_DOWN_STATE;
				return true;
			} else if (deltaY < 0 && isFooter
					&& child.getMeasuredHeight() <= getHeight() + mScrollView.getScrollY()) {
				mPullState = PULL_UP_STATE;
				return true;
			}
		}
		return false;
	}

	/**
	 * 设置底部的高度 .<br>
	 * @param height 底部要加的距离
	 */
	public void setBottomHeight(final int height) {
		bottomHeight = height;
		containerPaddingBottom = height;
		tmpBottomHeight = bottomHeight;
		mFooterViewHeight += bottomHeight;
	}

	/**
	 * header 准备刷新,手指移动过程,还没有释放 .
	 * @param deltaY ,手指滑动的距离
	 */
	private void headerPrepareToRefresh(final int deltaY) {
		final int newTopMargin = changingHeaderViewTopMargin(deltaY, 0);
		// 当header view的topMargin>=0时，说明已经完全显示出来了,修改header view 的提示状态
		if (newTopMargin >= 0 && mHeaderState != RELEASE_TO_REFRESH) {
			mHeaderTextView.setText(PULLTOREFRESHRELEASE);
			mHeaderUpdateTextView.setVisibility(View.VISIBLE);
			mHeaderImageView.clearAnimation();
			mHeaderImageView.startAnimation(mFlipAnimation);
			mHeaderState = RELEASE_TO_REFRESH;
		} else if (newTopMargin < 0 && newTopMargin > -mHeaderViewHeight) {
			// 拖动时没有释放
			mHeaderImageView.clearAnimation();
			mHeaderImageView.startAnimation(mFlipAnimation);
			// mHeaderImageView.
			mHeaderTextView.setText(pullToreFreshPull);
			mHeaderState = PULL_TO_REFRESH;
		}
	}

	/**
	 * .
	 */
	int tmpBottomHeight;

	/**
	 * footer 准备刷新,手指移动过程,还没有释放 移动footer view高度同样和移动header view 高度是一样，都是通过修改header view的topmargin的值来达到 .
	 * @param deltaY ,手指滑动的距离
	 */
	private void footerPrepareToRefresh(final int deltaY) {
		final int newTopMargin = changingHeaderViewTopMargin(deltaY, tmpBottomHeight * 2);
		tmpBottomHeight = 0;
		// 如果header view topMargin 的绝对值大于或等于header + footer 的高度
		// 说明footer view 完全显示出来了，修改footer view 的提示状态
		if (Math.abs(newTopMargin) >= (mHeaderViewHeight + mFooterViewHeight)
				&& mFooterState != RELEASE_TO_REFRESH) {
			mFooterTextView.setText(PULLTOREFRESHFOOTERRELEASE);
			mFooterImageView.clearAnimation();
			mFooterImageView.startAnimation(mFlipAnimation);
			mFooterState = RELEASE_TO_REFRESH;
		} else if (Math.abs(newTopMargin) < (mHeaderViewHeight + mFooterViewHeight)) {
			mFooterImageView.clearAnimation();
			mFooterImageView.startAnimation(mFlipAnimation);
			mFooterTextView.setText(PULLTOREFRESHFOOTPULL);
			mFooterState = PULL_TO_REFRESH;
		}
	}

	/**
	 * 修改Header view top margin的值 . . <br>
	 * @author liulongzhenhai 2013-4-11 上午11:05:55 <br>
	 * @param deltaY 坐标
	 * @param bottomHeight 底部高度
	 * @return 值
	 */
	private int changingHeaderViewTopMargin(final int deltaY, final int bottomHeight) {
		final LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		// log.warn("base:" + params.topMargin + ":" + bottomHeight);

		final float newTopMargin = (params.topMargin) + deltaY * 0.3f - bottomHeight;
		// log.warn("newTopMargin:" + newTopMargin);
		// 这里对上拉做一下限制,因为当前上拉后然后不释放手指直接下拉,会把下拉刷新给触发了,感谢网友yufengzungzhe的指出
		// 表示如果是在上拉后一段距离,然后直接下拉
		// if (deltaY > 0 && mPullState == PULL_UP_STATE && Math.abs(params.topMargin) <= mHeaderViewHeight) {
		// log.warn("uuuuuuuuuuuu");
		// return params.topMargin;
		// }
		// // 同样地,对下拉做一下限制,避免出现跟上拉操作时一样的bug
		// if (deltaY < 0 && mPullState == PULL_DOWN_STATE && Math.abs(params.topMargin) >= mHeaderViewHeight) {
		// log.warn("dddddddddd");
		// return params.topMargin;
		// }
		params.topMargin = (int) newTopMargin;
		// log.warn("2:" + params.topMargin);
		mHeaderView.setLayoutParams(params);
		invalidate();
		return params.topMargin;
	}

	/**
	 * 主动请求下拉刷新 .
	 */
	public void headerRefreshing() {
		if (isPull) {
			mHeaderState = REFRESHING;
			setHeaderTopMargin(0);
			mHeaderImageView.setVisibility(View.GONE);
			mHeaderImageView.clearAnimation();
			mHeaderImageView.setImageDrawable(null);
			mHeaderProgressBar.setVisibility(View.VISIBLE);
			mHeaderTextView.setText(PULLTOREFRESHFOOTREFRESHING);
			if (mOnHeaderRefreshListener != null) {
				mOnHeaderRefreshListener.onHeaderRefresh(this);
			}
		}
	}

	/**
	 * 主动加载更多 .
	 */
	public void footerRefreshing() {
		if (isFooter) {
			mFooterState = REFRESHING;
			final int top = mHeaderViewHeight + mFooterViewHeight;
			setHeaderTopMargin(-top);
			if (containerPaddingBottom > 0) {
				setContainerBottom(0);
			}
			mFooterImageView.setVisibility(View.GONE);
			mFooterImageView.clearAnimation();
			mFooterImageView.setImageDrawable(null);
			mFooterProgressBar.setVisibility(View.VISIBLE);
			mFooterTextView.setText(PULLTOREFRESHFOOTREFRESHING);
			if (mOnFooterRefreshListener != null) {
				mOnFooterRefreshListener.onFooterRefresh(this);
			}
		}
	}

	/**
	 * 设置header view 的topMargin的值 .
	 * @description
	 * @param topMargin ，为0时，说明header view 刚好完全显示出来； 为-mHeaderViewHeight时，说明完全隐藏了 hylin 2012-7-31上午11:24:06
	 */
	private void setHeaderTopMargin(final int topMargin) {
		final LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		params.topMargin = topMargin;
		mHeaderView.setLayoutParams(params);
		invalidate();
	}

	/**
	 * 获取布局上第一个View. <br>
	 * @author liulongzhenhai 2013-4-12 下午3:43:07 <br>
	 * @return View
	 */
	// private View getFirstView() {
	// if (mHeaderView != null) {
	// return mHeaderView;
	// }
	// return getChildAt(0);
	// }

	/**
	 * header view 完成更新后恢复初始状态 .
	 */
	public void onHeaderRefreshComplete() {
		setHeaderTopMargin(-mHeaderViewHeight);
		mHeaderImageView.setVisibility(View.VISIBLE);
		mHeaderImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow);
		mHeaderTextView.setText(pullToreFreshPull);
		mHeaderProgressBar.setVisibility(View.GONE);
		// mHeaderUpdateTextView.setText("");
		setLastUpdated("更新于:" + TimeUtil.getNowCurrentTime());
		mHeaderState = PULL_TO_REFRESH;
	}

	/**
	 * Resets the list to a normal state after a refresh.
	 * @param lastUpdated Last updated at.
	 */
	public void onHeaderRefreshComplete(final CharSequence lastUpdated) {
		setLastUpdated(lastUpdated);
		onHeaderRefreshComplete();
	}

	/**
	 * footer view 完成更新后恢复初始状态 .
	 */
	public void onFooterRefreshComplete() {
		setHeaderTopMargin(-mHeaderViewHeight);
		mFooterImageView.setVisibility(View.VISIBLE);
		mFooterImageView.setImageResource(R.drawable.ic_pulltorefresh_up);
		mFooterTextView.setText(PULLTOREFRESHFOOTPULL);
		mFooterProgressBar.setVisibility(View.GONE);
		if (containerPaddingBottom > 0) {
			setContainerBottom(containerPaddingBottom);
		}
		// mHeaderUpdateTextView.setText("");
		mFooterState = PULL_TO_REFRESH;
	}

	/**
	 * 设置容器的底部高度 . <br>
	 * @author liulongzhenhai 2013-6-28 上午10:25:08 <br>
	 * @param height 高度
	 */
	private void setContainerBottom(final int height) {

		
		if (mAdapterView != null) {
		
			if (mAdapterView.getPaddingBottom() != height) {
			
				mAdapterView.setPadding(mAdapterView.getPaddingLeft(), mAdapterView.getPaddingTop(),
						mAdapterView.getPaddingRight(), height);
				mAdapterView.invalidate();
			}
		} else if (mScrollView != null) {
			if (mScrollView.getPaddingBottom() != height) {
				mScrollView.setPadding(mScrollView.getPaddingLeft(), mScrollView.getPaddingTop(),
						mScrollView.getPaddingRight(), height);
				mAdapterView.invalidate();
			}
		}
	}

	/**
	 * 主动判断当前的刷新 . <br>
	 * @author liulongzhenhai 2013-6-13 下午3:00:22 <br>
	 */
	public void refreshComplete() {
		if (mFooterState == REFRESHING) {
			onFooterRefreshComplete();
		}

		if (mHeaderState == REFRESHING) {
			onHeaderRefreshComplete();
		}
	}

	/**
	 * Set a text to represent when the list was last updated.
	 * @param lastUpdated Last updated at.
	 */
	public void setLastUpdated(final CharSequence lastUpdated) {
		if (lastUpdated != null) {
			mHeaderUpdateTextView.setVisibility(View.VISIBLE);
			mHeaderUpdateTextView.setText(lastUpdated);
		} else {
			mHeaderUpdateTextView.setVisibility(View.GONE);
		}
	}

	/**
	 * 获取当前header view 的topMargin . <br>
	 * @author liulongzhenhai 2013-4-11 上午11:04:21 <br>
	 * @return 高度
	 */
	private int getHeaderTopMargin() {
		if (mHeaderView != null) {
			final LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
			return params.topMargin;
		}
		return -1;
	}

	
	/**
	 * 获取pullToreFreshPull.
	 * @return the pullToreFreshPull
	 */
	public String getPullToreFreshPull() {
		return pullToreFreshPull;
	}

	/**
	 * 设置pullToreFreshPull.
	 * @param pullToreFreshPull the pullToreFreshPull to set
	 */
	public void setPullToreFreshPull(final String pullToreFreshPull) {
		this.pullToreFreshPull = pullToreFreshPull;
	}

	/**
	 * lock .
	 */
	private void lock() {
		mLock = true;
	}

	/**
	 * unlock .
	 * @description hylin 2012-7-27下午6:53:18
	 */
	private void unlock() {
		mLock = false;
	}

	/**
	 * 设置头部加载更多回调 . <br>
	 * @author liulongzhenhai 2013-4-11 上午11:03:51 <br>
	 * @param headerRefreshListener OnHeaderRefreshListener
	 */
	public void setOnHeaderRefreshListener(final OnHeaderRefreshListener headerRefreshListener) {
		mOnHeaderRefreshListener = headerRefreshListener;
	}

	/**
	 * 加载更多. <br>
	 * @author liulongzhenhai 2013-4-11 上午11:04:12 <br>
	 * @param footerRefreshListener OnFooterRefreshListener
	 */
	public void setOnFooterRefreshListener(final OnFooterRefreshListener footerRefreshListener) {
		mOnFooterRefreshListener = footerRefreshListener;
	}

	/**
	 * 设置是否能进行滚动 如果为true.就恢复原来的状态. <br>
	 * @author liulongzhenhai 2013-6-4 上午11:32:14 <br>
	 * @param canScroll 是否能进行
	 */
	public void setCanScroll(final boolean canScroll) {
		if (canScroll) {
			isPull = tmpIsPull;
			isFooter = tmpIsFooter;
		} else {
			isPull = false;
			isFooter = false;
		}
		onFooterRefreshComplete();
		onHeaderRefreshComplete();
	}

	/**
	 * 加载更多接口 .<br>
	 * @author liulongzhenhai <br>
	 * @version 1.0.0 2013-4-11<br>
	 * @see
	 * @since JDK 1.4.2.6
	 */
	public interface OnFooterRefreshListener {
		/**
		 * UI线程,加载更多 . <br>
		 * @author liulongzhenhai 2013-4-11 上午11:04:51 <br>
		 * @param view PullToRefreshView
		 */
		void onFooterRefresh(final RefreshableContainer view);
	}

	/**
	 * 下拉刷新回调 .<br>
	 * @author liulongzhenhai <br>
	 * @version 1.0.0 2013-4-11<br>
	 * @see
	 * @since JDK 1.4.2.6
	 */
	public interface OnHeaderRefreshListener {
		/**
		 * 下拉刷新,UI线程. <br>
		 * @author liulongzhenhai 2013-4-11 上午11:04:54 <br>
		 * @param view PullToRefreshView
		 */
		void onHeaderRefresh(final RefreshableContainer view);
	}
	
}
