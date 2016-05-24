/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */
package com.chen.ucatch.view;

import com.chen.ucatch.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;



public class XListViewFooter extends LinearLayout {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;

	private Context mContext;

	private View mContentView;
	private View mProgressBar;
	private TextView mHintView;
	
	/**
	 * XListViewFooter .
	 * @author wangning4
	 * @date 2014年3月31日 下午3:57:49 
	 * @param context Context
	 */
	public XListViewFooter(Context context) {
		super(context);
		initView(context);
	}
	
	/**
	 * XListViewFooter .
	 * @author wangning4
	 * @date 2014年3月31日 下午3:57:36 
	 * @param context Context
	 * @param attrs AttributeSet
	 */
	public XListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	/**
	 * 设置列表下拉时各个状态的界面显示. <br>
	 * 状态包含：列表到达底部，没有下拉的情况（STATE_NORMAL）；列表下拉没有松手的情况（STATE_READY）；列表下拉并且松手的情况（STATE_LOADING）；
	 * 
	 * @author wangjun4 2014年4月8日 上午11:05:29 <br>
	 * @param state 设置列表下拉状态.
	 */
	public void setState(final int state) {
		// 设置下拉加载进度条和文本的隐藏，不占布局
		mProgressBar.setVisibility(View.GONE);
		mHintView.setVisibility(View.GONE);

		if (state == STATE_READY) {
			// 当列表下拉没有松手的情况时，只显示文本（比如：松开加载更多）
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.xlistview_footer_hint_ready);
		} else if (state == STATE_LOADING) {
			// 当列表下拉松手的情况时，显示进度条
			mProgressBar.setVisibility(View.VISIBLE);
		} else {
			mHintView.setText(R.string.xlistview_footer_hint_normal);
		}
	}
	
	/**
	 * setBottomMargin .
	 * @author wangning4
	 * @date 2014年3月31日 下午3:56:33 
	 * @param height 高度
	 */
	public void setBottomMargin(final int height) {
		if (height < 0) {
			return ;
		}
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}
	
	/**
	 * getBottomMargin .
	 * @author wangning4
	 * @date 2014年3月31日 下午3:56:22 
	 * @return int 
	 */
	public int getBottomMargin() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
		return lp.bottomMargin;
	}
	
	/**
	 * normal status .
	 * @author wangning4
	 * @date 2014年3月31日 下午3:56:11
	 */
	public void normal() {
		mHintView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
	}
	
	/**
	 * loading status .
	 * @author wangning4
	 * @date 2014年3月31日 下午3:56:01
	 */
	public void loading() {
		mHintView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
	}
	
	/**
	 * hide footer when disable pull load more .
	 * @author wangning4
	 * @date 2014年3月31日 下午3:55:52
	 */
	public void hide() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}
	
	/**
	 * show footer .
	 * @author wangning4
	 * @date 2014年3月31日 下午3:55:00
	 */
	public void show() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
	}

	/**
	 * 初始化视图  .
	 * @author wangning4
	 * @date 2014年3月31日 下午3:55:13 
	 * @param context Context
	 */
	private void initView(final Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.xlistview_footer, null);
		addView(moreView);
		moreView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
		mContentView = moreView.findViewById(R.id.xlistview_footer_content);
		mProgressBar = moreView.findViewById(R.id.xlistview_footer_progressbar);
		mHintView = (TextView) moreView.findViewById(R.id.xlistview_footer_hint_textview);
	}	
	
}
