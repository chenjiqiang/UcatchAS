package com.chen.ucatch.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.chen.ucatch.ConstantValue;
import com.chen.ucatch.utils.BitmapUtil;
import com.chen.ucatch.utils.HttpRequestUtils;
import com.chen.ucatch.utils.JFileUtil;
import com.chen.ucatch.utils.SDCardUtil;
import com.chen.ucatch.utils.ServerUrl;
import com.chen.ucatch.utils.UserInfoUtils;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.widget.ImageView;

public class TakePhoto extends Activity {
	/**
	 * 拍到的照片
	 */
	private ImageView mImg;
	/**
	 * 拍照创建的图片的路径
	 */
	private File tempFile;
	/**
	 * 图片保存路径
	 */
	private String savePath;
	/**
	 * 拍照产生图片
	 */
	private static final int PHOTO_REQUEST_TAKEPHOTO = 10002;
	/**
	 * 图片选择完成后的请求码
	 */
	private static final int PHOTO_REQUEST_RESULT = 10003;
	/**
	 * 上传图片的请求码
	 */
	private static final int UPLOADPIC = 1003;
	private Handler mHandler;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initGetPhoto();
	}

	/**
	 * 上传图片.
	 */
	private void disposeUserPicUpdate() {
		savePath = getUserPicTempSavePath();
		mImg.setImageBitmap(BitmapUtil.getFileBitmap(savePath));
		File userPicFile = new File(savePath);
		// mCancelProgressDialog.show("头像上传中...");
		RequestParams params = new RequestParams();
		try {
			if (UserInfoUtils.getUser(mContext) == null) {
				Intent intent = new Intent();
				intent.setClass(mContext, LoginActivity.class);
				startActivity(intent);
				return;
			}
			params.put("userId", UserInfoUtils.getUser(mContext).getId());
			params.put("uploadfile", userPicFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpRequestUtils.postStream(mContext, ServerUrl.UPLOAD_PICK_PIC,
				params, mHandler, UPLOADPIC);

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
		Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 指定调用相机拍照后照片的储存路径
		cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
		startActivityForResult(cameraintent, PHOTO_REQUEST_TAKEPHOTO);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_REQUEST_TAKEPHOTO && resultCode == -1) { // 拍照
			startPhotoZoom(Uri.fromFile(tempFile));
		} else if (requestCode == PHOTO_REQUEST_RESULT && resultCode == -1) { // 处理后
			if (data != null) {
				createTempHeadIconFile(data);
				disposeUserPicUpdate();

			}
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
					photo.compress(Bitmap.CompressFormat.JPEG, 75, baos);
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
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 2);
		intent.putExtra("aspectY", 1);
		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", UcatchMainActivity.width);
		intent.putExtra("outputY", UcatchMainActivity.width / 2);
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

}
