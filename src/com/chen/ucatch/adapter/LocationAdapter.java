package com.chen.ucatch.adapter;

import java.util.ArrayList;
import java.util.List;

import com.chen.ucatch.R;
import com.chen.ucatch.model.LocationBean;

import android.content.Context;


public class LocationAdapter extends CommonAdapter<LocationBean> {
	private List<Integer> mpos = new ArrayList<Integer>();
	public LocationAdapter(Context context, List<LocationBean> datas,int layoutId) {
		super(context, datas,layoutId);

	}
/**
 * 
 * 给布局设值
 * @see com.chen.ucatch.adapter.CommonAdapter#convert(com.chen.ucatch.adapter.ViewHolder, java.lang.Object) 
 * @author chenjiqiang 2015年8月26日 上午8:57:06 <br>
 */
	@Override
	public void convert(final ViewHolder holder, LocationBean b) {
		holder.setText(R.id.tv_location, b.getTitle());
		holder.setText(R.id.tv_location_distance, b.getDistance()+"");
	}
}
