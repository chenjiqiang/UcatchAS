package com.chen.ucatch.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.afinal.simplecache.ACache;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.chen.ucatch.ConstantValue;
import com.chen.ucatch.R;
import com.chen.ucatch.model.GPSPointVO;
import com.chen.ucatch.model.PictureVO;
import com.chen.ucatch.model.ReturnVO;
import com.chen.ucatch.model.SubjectVO;
import com.chen.ucatch.model.UserVO;
import com.chen.ucatch.utils.BitmapUtil;
import com.chen.ucatch.utils.CommentTextFilter;
import com.chen.ucatch.utils.HttpRequestUtils;
import com.chen.ucatch.utils.JFileUtil;
import com.chen.ucatch.utils.SDCardUtil;
import com.chen.ucatch.utils.ServerUrl;
import com.chen.ucatch.utils.StringUtil;
import com.chen.ucatch.utils.TopTitleUtils;
import com.chen.ucatch.utils.UserInfoUtils;
import com.chen.ucatch.view.CancelProgressDialog;
import com.chen.ucatch.view.MyDialog;
import com.chen.ucatch.view.RoundProgressBar;
import com.loopj.android.http.RequestParams;

public class PickActivity extends MyBaseActivity implements
		AMapLocationListener, OnClickListener {
	private final static String TAG = "PickActivity";
	/**
	 * 定位
	 */
	private LocationManagerProxy mLocationManagerProxy;
	/**
	 * 拍到的照片
	 */
	private ImageView mImg;
	/**
	 * 用户头像
	 */
	private ImageView mUserHead_Pic;
	/**
	 * 拾取到的留言
	 */
	private TextView mTv_Leave_Msg;
	/**
	 * 地理位置
	 */
	private EditText mLocation;

	/**
	 * 定位的图标
	 */
	private ImageView iv_click;
	/**
	 * 发布
	 */
	private Button mbt_publish;
	private Handler mHandler;
	private TextView tv_praise;
	private TextView tv_comment;
	/**
	 * 坐标
	 */
	private GPSPointVO gprsPointVO;
	/**
	 * 位置标签
	 */
	private String addressLabel;
	/**
	 * 是否发布
	 */
	// private Integer publishFlag = 1;
	/**
	 * 是否点赞
	 */
	private boolean isPraise;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 留言布局
	 */
	private LinearLayout ll_msg;
	/**
	 * 评论编辑框
	 */
	private EditText commentEdit;
	/**
	 * 评论过滤器 .主要用来限制 评论的长度
	 */
	private InputFilter[] commentTextFilter;
	/**
	 * 评论内容的长度最大值
	 */
	private final int COMMENT_MAX_LENGTH = 140;
	private UserVO mUserVO;
	/**
	 * 拾取发布请求
	 */
	private static final int PHOTO_SHAPE = 1001;
	/**
	 * 获取留言
	 */
	private static final int GET_MSG = 1002;
	/**
	 * 点赞取消点赞的请求码
	 */
	private static final int PRAISE = 1005;
	/**
	 * 拍照创建的图片的路径
	 */
	private File tempFile;
	/**
	 * 图片保存路径
	 */
	private String savePath;
	/**
	 * 从相册图库中选择
	 */
	private static final int PHOTO_REQUEST_GALLERY = 10001;
	/**
	 * 拍照产生图片
	 */
	private static final int PHOTO_REQUEST_TAKEPHOTO = 10002;
	/**
	 * 图片选择完成后的请求码
	 */
	private static final int PHOTO_REQUEST_RESULT = 10003;
	/**
	 * 跳转到评论
	 */
	private static final int TURNTOCOMMENT = 10004;
	/**
	 * 上传图片的请求码
	 */
	private static final int UPLOADPIC = 1003;
	/**
	 * 对话框
	 */
	private CancelProgressDialog mDialog;
	/**
	 * 图片vo
	 */
	private PictureVO mPictureVO;
	/**
	 * 留言的vo
	 */
	private SubjectVO mSubjectVO;

	private ACache mCache;
	private Uri photoUri;
	public static final String SUBJECTID = "subjectId";
	private RoundProgressBar progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pick_layout);
		mUserVO = new UserVO();
		gprsPointVO = new GPSPointVO();
		mPictureVO = new PictureVO();
		mSubjectVO = new SubjectVO();
		mDialog = new CancelProgressDialog(mContext);
		initLocation();
		initView();
		initHandler();
		initGetPhoto();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// // // 移除定位请求
		// mLocationManagerProxy.removeUpdates(this);
		// // // 销毁定位
		// mLocationManagerProxy.destory();
	}

	protected void onDestroy() {
		super.onDestroy();

	}

	private void initLocation() {
		// TODO Auto-generated method stub
		// 初始化定位，只采用网络定位
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		mLocationManagerProxy.setGpsEnable(false);
		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
		// 在定位结束后，在合适的生命周期调用destroy()方法
		// 其中如果间隔时间为-1，则定位只定一次,
		// 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, -1, 15, this);
//		mDialog.show("获取当前位置...");
	}

	private void initData() {
		/**
		 * 拾取留言,返回留言id
		 * 
		 * @param userId
		 *            当前用户id
		 * @param addressGpsJson
		 *            gps坐标对象json字符串(GPSPointVO对象)
		 * @return
		 */

		if (UserInfoUtils.getUser(mContext) == null) {
			Intent intent = new Intent();
			intent.setClass(mContext, LoginActivity.class);
			startActivity(intent);
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", UserInfoUtils.getUser(mContext).getId());
		params.put("addressGpsJson", gprsPointVO.toString());
		HttpRequestUtils.post(mContext, ServerUrl.GET_MSG, params, mHandler,
				GET_MSG);
	}

	private void initHandler() {
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				ReturnVO vo = (ReturnVO) msg.obj;
				if (mDialog.isShowing()) {
					mDialog.cancel();
				}
				if (vo == null) {
					if (savePath != null) {
						disposeUserPicUpdate(savePath);
					}
					if (msg.what == ConstantValue.NETWORK_NO_RESPONE) {
						Toast.makeText(mContext,
								ConstantValue.NETWORK_NO_RESPONE_TEXT, 2000)
								.show();
					} else if (msg.what == ConstantValue.SERVER_NO_RESPONE) {
						Toast.makeText(mContext,
								ConstantValue.SERVER_NO_RESPONE_TEXT, 2000)
								.show();
					} else {
						Toast.makeText(mContext, "数据加载失败" + msg.what, 1000)
								.show();
					}
					return;
				}
				if (vo.getSuccessFlag() == 1) {
					switch (msg.what) {
					case PHOTO_SHAPE:
						finish();
						break;
					case GET_MSG:
						mUserHead_Pic.setVisibility(View.VISIBLE);
						ll_msg.setVisibility(View.VISIBLE);
						mSubjectVO = com.alibaba.fastjson.JSONObject
								.parseObject(vo.getData().toString(),
										SubjectVO.class);
						mTv_Leave_Msg.setText(mSubjectVO.getTitle());
						break;
					case UPLOADPIC:
						TopTitleUtils.submitPraise(mContext, isPraise,
								UserInfoUtils.getUser(mContext).getId(),
								mSubjectVO.getId(), mHandler, PRAISE);
						finish();
						// mPictureVO = com.alibaba.fastjson.JSONObject
						// .parseObject(vo.getData().toString(),
						// PictureVO.class);
						break;
					default:
						break;
					}
				} else if (vo.getSuccessFlag() == 0) {
					if (msg.what == GET_MSG) {
						MyDialog dialog = new MyDialog(mContext);
						dialog.show();
						return;
					}
					Toast.makeText(mContext, vo.getMessage(),
							Toast.LENGTH_SHORT).show();

				} else {
					Toast.makeText(mContext, vo.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	private void initView() {
		progress = (RoundProgressBar) findViewById(R.id.roundProgressBar1);
		mbt_publish = (Button) findViewById(R.id.head_right);
		mbt_publish.setVisibility(View.VISIBLE);
		mbt_publish.setText("发布");
		mbt_publish.setOnClickListener(this);
		mImg = (ImageView) findViewById(R.id.iv_takephoto);
		mUserHead_Pic = (ImageView) findViewById(R.id.userhead_pic);
		mTv_Leave_Msg = (TextView) findViewById(R.id.pick_layout_catch);
		ll_msg = (LinearLayout) findViewById(R.id.ll_pick_layout_msg);
		iv_click = (ImageView) findViewById(R.id.pick_location);
		iv_click.setOnClickListener(this);
		mLocation = (EditText) findViewById(R.id.location);
		mLocation.setOnClickListener(this);
		LayoutParams params = new LayoutParams(UcatchMainActivity.width,
				UcatchMainActivity.width / 2);
		mImg.setLayoutParams(params);
		mImg.setScaleType(ScaleType.CENTER_CROP);
		commentEdit = (EditText) findViewById(R.id.share_detail_comment_edit);
		// 设置最大可以评论多少个字
		commentTextFilter = CommentTextFilter.getInputFilter(this,
				COMMENT_MAX_LENGTH);
		commentEdit.setFilters(commentTextFilter);
		tv_praise = (TextView) findViewById(R.id.tv_pick_praise);
		tv_comment = (TextView) findViewById(R.id.tv_pick_comment);
		tv_praise.setOnClickListener(this);
		tv_comment.setOnClickListener(this);
	}

	/**
	 * 
	 * 修改点赞状态
	 * 
	 * @author chenjiqiang 2016年3月17日 下午8:29:49 <br>
	 * @param textview
	 * @param isPraised
	 * @param position
	 */
	private void changeDrawable(TextView textview, Boolean isPraised) {
		Drawable drawable = null;
		if (isPraised) {
			// 1表示已经点赞过了，现在修改为没有点赞
			drawable = mContext.getResources().getDrawable(
					R.drawable.common_praise_do);
			isPraise = !isPraised;
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			textview.setCompoundDrawables(drawable, null, null, null);
		} else {
			// 点赞
			// operationPraised.put(list.get(position).getAffairguideCommentId(),
			// 1);
			drawable = mContext.getResources().getDrawable(
					R.drawable.common_praise_done);
			isPraise = !isPraised;
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			textview.setCompoundDrawables(drawable, null, null, null);
		}

	}

	@Override
	public void onClick(View v) {
		if (v.equals(tv_praise)) {
			changeDrawable(tv_praise, isPraise);
		} else if (v.equals(tv_comment)) {
			Intent intent = new Intent();
			intent.setClass(mContext, CommentAcitivity.class);
			Bundle b = new Bundle();
			b.putString(SUBJECTID, mSubjectVO.getId());
			intent.putExtras(b);
			startActivityForResult(intent, TURNTOCOMMENT);
		} else if (v.equals(mbt_publish)) {
			if (UserInfoUtils.getUser(mContext) == null) {
				Intent intent = new Intent();
				intent.setClass(mContext, LoginActivity.class);
				startActivity(intent);
				return;
			}
			if (StringUtil.isEmptyString(savePath)) {
				Toast.makeText(mContext, "请返回上一层重新拾取~", Toast.LENGTH_SHORT)
						.show();
			} else {
				disposeUserPicUpdate(savePath);
			}
		}
	}

	/**
	 * 上传图片.
	 */
	private void disposeUserPicUpdate(String path) {
		// savePath = getUserPicTempSavePath();
		// mImg.setImageBitmap(BitmapUtil.getFileBitmap(savePath));
		File userPicFile = new File(path);
		// mCancelProgressDialog.show("头像上传中...");
		RequestParams params = new RequestParams();
		try {
			if (UserInfoUtils.getUser(mContext) == null) {
				Intent intent = new Intent();
				intent.setClass(mContext, LoginActivity.class);
				startActivity(intent);
				return;
			}
			description = commentEdit.getText().toString();
			params.put("userId", UserInfoUtils.getUser(mContext).getId()
					.toString());
			params.put("addressGpsJson", gprsPointVO.toString());
			params.put("addressLabel", addressLabel);
			params.put("description", description);
			params.put("subjectId", mSubjectVO.getId());
			params.put("file", userPicFile);
			mDialog.show("发布中...");
			Log.i(TAG, params.toString());
			progress.setVisibility(View.VISIBLE);
			HttpRequestUtils.postStreamPro(mContext, ServerUrl.PHOTOSHAPE,
					progress, params, mHandler, UPLOADPIC);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * 调用拍照
	 * 
	 * @author chenjiqiang 2015年10月16日 下午8:15:05 <br>
	 */
	private void initGetPhoto() {
		tempFile = new File(Environment.getExternalStorageDirectory(),
				getPhotoFileName());
		// 执行拍照前，应该先判断SD卡是否存在
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {
			Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// 指定调用相机拍照后照片的储存路径
			cameraintent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(tempFile));
			startActivityForResult(cameraintent, PHOTO_REQUEST_TAKEPHOTO);
		} else {
			Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_REQUEST_GALLERY && resultCode == -1) { // 相册
			if (data != null)
				startPhotoZoom(data.getData());
			// createTempHeadIconFile(data);
			// initLocation();
			// disposeUserPicUpdate();
		} else if (requestCode == PHOTO_REQUEST_TAKEPHOTO && resultCode == -1) { // 拍照
			// startPhotoZoom(Uri.fromFile(tempFile));
			// createTempHeadIconFile(data);
			doPhoto(requestCode, Uri.fromFile(tempFile));
		} else if (requestCode == PHOTO_REQUEST_RESULT && resultCode == -1) { // 处理后
			if (data != null) {
				createTempHeadIconFile(data);
				// disposeUserPicUpdate();
			}
		} else if (requestCode == TURNTOCOMMENT) {

		} else {
			finish();
		}
	}

	/**
	 * 
	 * 对剪裁缩放后的图片压缩并生成临时文件，以适应原方法用到userIcon.png的地方
	 * 
	 * @author chenjiqiang 2015年10月16日 下午1:40:27 <br>
	 * @param picdata
	 */
	private void createTempHeadIconFile(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		if (bundle != null) {
			Bitmap photo = bundle.getParcelable("data");
			if (photo != null) {
				ByteArrayOutputStream baos = null; // 压缩图片
				try {
					baos = new ByteArrayOutputStream();
					photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
					byte[] photodata = baos.toByteArray();
					JFileUtil.write(getUserPicTempSavePath(), photodata); // 生成临时文件
				} catch (Exception e) {
					e.getStackTrace();
				} finally {
					if (baos != null) {
						try {
							baos.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * 裁剪图
	 * 
	 * @author chenjiqiang 2015年8月19日 下午9:54:59 <br>
	 * @param uri
	 */
	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "false");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 2);
		intent.putExtra("aspectY", 1);
		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", 400);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, PHOTO_REQUEST_RESULT);
	}

	/**
	 * 
	 * 使用系统当前日期加以调整作为照片的名称
	 * 
	 * @author chenjiqiang 2015年8月19日 下午9:59:07 <br>
	 * @return
	 */
	private String getPhotoFileName() {
		java.util.Date date = new java.util.Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss", Locale.CHINESE);
		return dateFormat.format(date) + ".jpg";
	}

	/**
	 * 
	 * 获取用户头像更改临时保存路径 .
	 * 
	 * @author chenjiqiang 2015年8月19日 下午10:02:31 <br>
	 * @return
	 */
	private String getUserPicTempSavePath() {
		String savePath = SDCardUtil.getInstance().getSDCardPath()
				+ ConstantValue.SDCASHEPATH;
		return savePath;
	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
//		if (mDialog.isShowing()) {
//			mDialog.cancel();
//		}
		gprsPointVO.setLon(amapLocation.getLongitude());
		gprsPointVO.setLat(amapLocation.getLatitude());
		addressLabel = amapLocation.getAddress();
		mLocation.setText(amapLocation.getAddress());
		if (amapLocation.getLongitude() == 0 && amapLocation.getLatitude() == 0) {
			initLocation();
		} else {
			initData();
		}
		// 移除定位请求
		mLocationManagerProxy.removeUpdates(this);
		// // 销毁定位
		mLocationManagerProxy.destory();
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	/**
	 * 选择图片后，获取图片的路径
	 * 
	 * @param requestCode
	 * @param data
	 */
	private void doPhoto(int requestCode, Uri uri) {
		// 从相册取图片，有些手机有异常情况，请注意
		if (requestCode == PHOTO_REQUEST_TAKEPHOTO) {
			if (uri == null) {
				Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
			photoUri = uri;
			if (photoUri == null) {
				Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
		}
		if (photoUri.getPath() != null) {
			Bitmap bm = BitmapUtil.getBitmapCompression(photoUri.getPath(),
					720f, 100);
			mImg.setImageBitmap(bm);
			savePath = getUserPicTempSavePath();
			JFileUtil.write(savePath,
					BitmapUtil.bitmap2Bytes(BitmapUtil.compressImage(bm)));
		}
		// if (photoUri.getPath() != null) {
		// BitmapFactory.Options option = new BitmapFactory.Options();
		// option.inSampleSize = 4;
		// Bitmap bm = BitmapFactory.decodeFile(photoUri.getPath(), option);
		// BitmapUtil.compressImage(bm);
		// mImg.setImageBitmap(bm);
		// savePath = getUserPicTempSavePath();
		// JFileUtil.write(savePath,
		// BitmapUtil.bitmap2Bytes(BitmapUtil.compressImage(bm)));
		// // initLocation();
		// // disposeUserPicUpdate(savePath);
		// }
		// if (photoUri.getPath() != null) {
		// savePath = photoUri.getPath();
		// BitmapFactory.Options option = new BitmapFactory.Options();
		// // 压缩图片:表示缩略图大小为原始图片大小的几分之一，1为原图
		// option.inSampleSize = 4;
		// // 根据图片的SDCard路径读出Bitmap
		// Bitmap bm = BitmapFactory.decodeFile(savePath, option);
		// // 显示在图片控件上
		// mImg.setImageBitmap(bm);
		// initLocation();
		// // disposeUserPicUpdate(savePath);
		// }
		else {
			Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
		}
		// String[] pojo = { MediaColumns.DATA };
		// The method managedQuery() from the type Activity is deprecated
		// Cursor cursor = managedQuery(photoUri, pojo, null, null, null);
		// Cursor cursor = mContext.getContentResolver().query(photoUri, pojo,
		// null, null, null);
		// if (cursor != null) {
		// int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
		// cursor.moveToFirst();
		// savePath = cursor.getString(columnIndex);
		// // 4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)
		// if (Integer.parseInt(Build.VERSION.SDK) < 14) {
		// cursor.close();
		// }
		// }
		// // 如果图片符合要求将其上传到服务器
		// if (savePath != null
		// && (savePath.endsWith(".png") || savePath.endsWith(".PNG")
		// || savePath.endsWith(".jpg") || savePath
		// .endsWith(".JPG"))) {
		// BitmapFactory.Options option = new BitmapFactory.Options();
		// // 压缩图片:表示缩略图大小为原始图片大小的几分之一，1为原图
		// option.inSampleSize = 1;
		// // 根据图片的SDCard路径读出Bitmap
		// Bitmap bm = BitmapFactory.decodeFile(savePath, option);
		// // 显示在图片控件上
		// mImg.setImageBitmap(bm);
		// initLocation();
		// disposeUserPicUpdate(savePath);
		// pd = ProgressDialog.show(mContext, null," 正在上传图片，请稍候...");
		// new Thread(uploadImageRunnable).start();
		// } else {
		// Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
		// }
	}

}
