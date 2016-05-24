package com.chen.ucatch.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class Home_Viewpager extends ViewPager {
	private int childId;
	ViewPager pager=null;
	public Home_Viewpager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public Home_Viewpager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if (childId > 0) {
			 this.pager = (ViewPager) findViewById(childId);
		}
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (pager != null) {
				pager.requestDisallowInterceptTouchEvent(false);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (pager != null) {
				pager.requestDisallowInterceptTouchEvent(false);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (pager != null) {
				pager.requestDisallowInterceptTouchEvent(true);
			}
			break;

		default:
			break;
		}

		return super.onInterceptTouchEvent(event);
	}

	public void setChildId(int id) {
		this.childId = id;
	}
}
