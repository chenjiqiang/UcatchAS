package com.chen.ucatch.adapter;

import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.chen.ucatch.R;
import com.meg7.widget.CircleImageView;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public class GridAdapter extends BaseAdapter {
	// private ArrayList<String> mNameList = new ArrayList<String>();
	private ArrayList<String> mDrawableList = new ArrayList<String>();
	private LayoutInflater mInflater;
	private Context mContext;
	LinearLayout.LayoutParams params;

	public GridAdapter(Context context, ArrayList<String> drawableList) {
		// mNameList = nameList;
		mDrawableList = drawableList;
		mContext = context;
		mInflater = LayoutInflater.from(context);

		params = new LinearLayout.LayoutParams(dip2px(mContext, 30), dip2px(
				mContext, 30));
		params.gravity = Gravity.CENTER;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public int getCount() {
		return mDrawableList.size();
	}

	public Object getItem(int position) {
		return mDrawableList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ItemViewTag viewTag;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.gridview_item, null);

			// construct an item tag
			viewTag = new ItemViewTag(
					(CircleImageView) convertView.findViewById(R.id.grid_icon)
			// ,(TextView) convertView.findViewById(R.id.grid_name)
			);
			convertView.setTag(viewTag);
		} else {
			viewTag = (ItemViewTag) convertView.getTag();
		}

		// set name
		// viewTag.mName.setText(mNameList.get(position));

		// set icon
		Glide.with(mContext).load(mDrawableList.get(position))
				.placeholder(R.drawable.main_rightmenu_default_head)
				.error(R.drawable.default_pic).into(viewTag.mIcon);
		// viewTag.mIcon.setBackgroundDrawable(mDrawableList.get(position));
		viewTag.mIcon.setLayoutParams(params);
		return convertView;
	}

	class ItemViewTag {
		protected CircleImageView mIcon;

		// protected TextView mName;

		/**
		 * The constructor to construct a navigation view tag
		 * 
		 * @param name
		 *            the name view of the item
		 * @param size
		 *            the size view of the item
		 * @param icon
		 *            the icon view of the item
		 */
		public ItemViewTag(CircleImageView icon) {
			// this.mName = name;
			this.mIcon = icon;
		}
	}

}
