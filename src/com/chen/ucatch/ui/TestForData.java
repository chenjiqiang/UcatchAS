package com.chen.ucatch.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.Query;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.chen.ucatch.R;
import com.chen.ucatch.adapter.LocationAdapter;
import com.chen.ucatch.model.LocationBean;
import com.chen.ucatch.utils.ShapeLoadingDialog;
import com.chen.ucatch.view.RefreshableContainer;
import com.chen.ucatch.view.RefreshableContainer.OnFooterRefreshListener;

public class TestForData extends Activity implements AMapLocationListener,
		OnPoiSearchListener, OnFooterRefreshListener, OnClickListener {
	/**
	 * 标题
	 */
	private TextView mtitle_left;
	/**
	 * 确定按钮
	 */
	private Button bt_head_ok;
	private SearchBound seach;
	private Context mContext;
	private Query query;
	private int currentPage = 1;
	/**
	 * 刷新容器
	 */
	private RefreshableContainer refresh;
	/**
	 * listview控件sss
	 */
	private ListView listView;
	private List<LocationBean> listDatas = new ArrayList<LocationBean>();
	private LocationAdapter adapter;
	/**
	 * 经纬度
	 */
	private LatLonPoint point;
	/**
	 * edittext
	 */
	private EditText mEt_Seach;
	/**
	 * 搜索按钮
	 */
	private Button mBt_Seach;
	private LocationManagerProxy mLocationManagerProxy;
	private ShapeLoadingDialog dialog;
	public static final int LOCATION_CHANGE_RESULT_CODE = 1111;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_location_change);
		mContext = this;
		init();
		initView();
		// initData();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 移除定位请求
		mLocationManagerProxy.removeUpdates(this);
		// 销毁定位
		mLocationManagerProxy.destroy();
	}

	protected void onDestroy() {
		super.onDestroy();

	}

	/**
	 * 初始化定位
	 */
	private void init() {
		// 初始化定位，只采用网络定位
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		mLocationManagerProxy.setGpsEnable(false);
		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
		// 在定位结束后，在合适的生命周期调用destroy()方法
		// 其中如果间隔时间为-1，则定位只定一次,
		// 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, 60 * 1000, 15, this);
	}

	private void initData(String key) {
		// TODO Auto-generated method stub
		query = new PoiSearch.Query(key, "", "");
		query.setPageSize(10);// 设置每页最多返回多少条poiitem
		query.setPageNum(currentPage);// 设置查第一页
		PoiSearch poiSearch = new PoiSearch(this, query);
		if (point == null) {
			point = new LatLonPoint(22.374296, 113.563196);
		}
		poiSearch.setBound(new SearchBound(point, 5000));// 设置周边搜索的中心点以及区域
		// 1,keyWord表示搜索字符串，第二个参数表示POI搜索类型，默认为：生活服务、餐饮服务、商务住宅
		// 共分为以下20种：汽车服务|汽车销售|
		// 汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
		// 住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
		// 金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
		// cityCode表示POI搜索区域，（这里可以传空字符串，空字符串代表全国在全国范围内进行搜索）

		poiSearch.setOnPoiSearchListener(this);// 设置数据返回的监听器
		poiSearch.searchPOIAsyn();// 开始搜索
	}

	private void initView() {
		dialog = new ShapeLoadingDialog(mContext);
		dialog.setLoadingText("正在加载中。。。");
		dialog.show();
		mEt_Seach = (EditText) findViewById(R.id.et_location_change);
		mBt_Seach = (Button) findViewById(R.id.location_change_search);
		mBt_Seach.setOnClickListener(this);
		mtitle_left = (TextView) findViewById(R.id.head_left);
		mtitle_left.setText("选择地理位置");
		bt_head_ok = (Button) findViewById(R.id.bt_head_ok);
		bt_head_ok.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.listView);
		adapter = new LocationAdapter(mContext, listDatas,
				R.layout.location_change_item);
		listView.setAdapter(adapter);
		refresh = (RefreshableContainer) findViewById(R.id.refresh_data);
		refresh.setOnFooterRefreshListener(this);
		refresh.setPull(false);
		refresh.setFooter(true);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				mEt_Seach.setText(listDatas.get(position).getTitle());

			}
		});
	}

	@Override
	public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	public void onPoiSearched(PoiResult result, int rCode) {
		refresh.onFooterRefreshComplete();
		dialog.dismiss();
		if (rCode == 0) {
			// 在回调函数中解析result获取POI信息
			// result.getPois()可以获取到PoiItem列表，Poi详细信息可参考PoiItem类
			// 若当前城市查询不到所需Poi信息，可以通过result.getSearchSuggestionCitys()获取当前Poi搜索的建议城市
			// 如果搜索关键字明显为误输入，则可通过result.getSearchSuggestionKeywords()方法得到搜索关键词建议
			// 返回结果成功或者失败的响应码。0为成功，其他为失败（详细信息参见网站开发指南-错误码对照表）
			List<LocationBean> datas = new ArrayList<LocationBean>();
			for (PoiItem item : result.getPois()) {
				LocationBean b = new LocationBean();
				b.setPoiId(item.getPoiId());
				b.setTitle(item.getTitle());
				b.setSnippet(item.getSnippet());
				b.setTypedes(item.getTypeDes());
				b.setDistance(item.getDistance());
				b.setEmail(item.getEmail());
				b.setLatLonPoint(item.getLatLonPoint());
				datas.add(b);
			}
			if (datas.size() > 0) {
				listDatas.addAll(datas);
				currentPage++;
				adapter.notifyDataSetChanged();
			} else {
				Toast.makeText(mContext, "没有更多数据", Toast.LENGTH_SHORT).show();
			}
			// MyAdapter adapter = new MyAdapter(mContext, datas,
			// R.layout.listview_item);
			// listView.setAdapter(adapter);
		} else {
			Toast.makeText(mContext, "后期处理" + rCode, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onFooterRefresh(RefreshableContainer view) {
		// TODO Auto-generated method stub
		initData("");
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (amapLocation != null
				&& amapLocation.getAMapException().getErrorCode() == 0) {
			point = new LatLonPoint(amapLocation.getLatitude(),
					amapLocation.getLongitude());
			initData("");
			// 定位成功回调信息，设置相关消息
			// mLocationLatlngTextView.setText(amapLocation.getLatitude() + "  "
			// + amapLocation.getLongitude());
		}

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(bt_head_ok)) {
			Toast.makeText(
					mContext,
					mEt_Seach.getEditableText().toString()
							+ point.getLatitude() + point.getLongitude(), 2000)
					.show();
			Intent intent = new Intent();
			Bundle b = new Bundle();
			b.putString("location", mEt_Seach.getEditableText().toString());
			b.putDouble("Latitude", point.getLatitude());
			b.putDouble("Longitude", point.getLongitude());
			intent.putExtras(b);
			setResult(LOCATION_CHANGE_RESULT_CODE, intent);
			finish();
		} else if (v.equals(mBt_Seach)) {
			String key = mEt_Seach.getEditableText().toString();
			initData(key);
			listDatas.clear();
			adapter.notifyDataSetChanged();
			dialog.show();
		}
	}
}
