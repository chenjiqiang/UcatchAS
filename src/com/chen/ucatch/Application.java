package com.chen.ucatch;

import cn.jpush.android.api.JPushInterface;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 
 * 全局的变量
 * 
 * @author chenjiqiang <br>
 * @version 1.0.0 2015年8月18日 下午3:49:03 <br>
 * @see
 * @since JDK 1.4.2.6
 */
public class Application extends android.app.Application {
	private static PersistentCookieStore mCookieStore;

	public static AsyncHttpClient masyncHttpClient = new AsyncHttpClient();

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mCookieStore = new PersistentCookieStore(this);
		masyncHttpClient.setTimeout(60000);
		masyncHttpClient.setCookieStore(mCookieStore);
		// 配置第三方的图片加载工具,主要用于给图片点击放大缩小时使用
		initImageLoaderConfig();
		// 设置开启日志,发布时请关闭日志
		JPushInterface.setDebugMode(true);
		// 初始化 JPush
		JPushInterface.init(this);
	}

	private void initImageLoaderConfig() {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
				.showImageForEmptyUri(R.drawable.ic_launcher) //
				.showImageOnFail(R.drawable.ic_launcher) //
				.cacheInMemory(true) //
				.cacheOnDisk(true) //
				.build();//
		ImageLoaderConfiguration config = new ImageLoaderConfiguration//
		.Builder(getApplicationContext())//
				.defaultDisplayImageOptions(defaultOptions)//
				.discCacheSize(50 * 1024 * 1024)//
				.discCacheFileCount(100)// 缓存一百张图片
				.writeDebugLogs()//
				.build();//
		ImageLoader.getInstance().init(config);
	}
}
