package com.chen.ucatch.fragment;

import java.util.ArrayList;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.autonavi.amap.mapcore.Convert;
import com.chen.ucatch.R;
import com.chen.ucatch.adapter.MyFragmentPagerAdapter;
import com.chen.ucatch.utils.ConvertPxDp;

public class HomeFragment extends Fragment {

	Resources resources;
	private ViewPager mPager;
	private ArrayList<Fragment> fragmentsList;
	private ImageView ivBottomLine;
	private RadioButton tvTabfind, tvTabattention;

	private int currIndex = 0;
	/**
	 * 细线的长度
	 */
	private int bottomLineWidth;
	/**
	 * 边距
	 */
	private int offset = 0;
	/**
	 * 位置
	 */
	private int position_one;
	public final static int num = 2;
	Fragment home1;
	Fragment home2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.homefragment, null);
		resources = getResources();
		InitTextView(view);
		InitWidth(view);
		InitViewPager(view);
		TranslateAnimation animation = new TranslateAnimation(position_one,
				offset, 0, 0);
		tvTabfind.setTextColor(resources.getColor(R.color.red));
		animation.setFillAfter(true);
		animation.setDuration(300);
		ivBottomLine.startAnimation(animation);
		return view;
	}

	private void InitTextView(View parentView) {
		tvTabfind = (RadioButton) parentView.findViewById(R.id.head_left_first);
		tvTabattention = (RadioButton) parentView.findViewById(R.id.head_left_second);
		tvTabfind.setOnClickListener(new MyOnClickListener(0));
		tvTabattention.setOnClickListener(new MyOnClickListener(1));
	}

	private void InitViewPager(View parentView) {
		mPager = (ViewPager) parentView.findViewById(R.id.viewpager);
		fragmentsList = new ArrayList<Fragment>();
		home1 = new FindFragment();
		home2 = new AttentionFragment();
		fragmentsList.add(home1);
		fragmentsList.add(home2);

		mPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(),
				fragmentsList));
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mPager.setCurrentItem(0);

	}

	private void InitWidth(View parentView) {
		ivBottomLine = (ImageView) parentView
				.findViewById(R.id.tab_menu_red_line);
		bottomLineWidth = ivBottomLine.getLayoutParams().width;
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (int) ((screenW / num - bottomLineWidth)-20);
		int avg = (int) (screenW / num);
		position_one = 56 + offset;
	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(position_one, offset, 0,
							0);
					tvTabattention.setTextColor(resources.getColor(R.color.black));
				}
				tvTabfind.setTextColor(resources.getColor(R.color.red));
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, offset+ConvertPxDp.Dp2Px(getActivity(), 56), 0,
							0);
					
					tvTabfind.setTextColor(resources.getColor(R.color.black));
					
				}
				tvTabattention.setTextColor(resources.getColor(R.color.red));
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(300);
			ivBottomLine.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

}
