package com.chen.ucatch.utils;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chen.ucatch.ConstantValue;
import com.chen.ucatch.model.ReturnVO;
import com.chen.ucatch.view.RoundProgressBar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 封装AsyncHttpClient 比较常用 get 方式 和 post_from 方式 以及post_json方式提交的数据
 * 
 * @author liuhuan2
 * 
 */
public class HttpRequestUtils {
	private static AsyncHttpClient asyncHttpClient = Utility
			.getAsyncHttpClient();

	/**
	 * 使用post方式提交
	 * 
	 * @param url
	 * @param map
	 * @param handler
	 * @param handlerWath
	 */
	public static void postStreamPro(Context context, String url,
			final RoundProgressBar progress, RequestParams params,
			final Handler handler, final int handlerWhat) {
		if (!isNetworkConnected(context)) {
			Message message = handler.obtainMessage();
			message.what = ConstantValue.NETWORK_NO_RESPONE;
			handler.sendMessage(message);
			return;
		}

		asyncHttpClient.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				progress.setProgress(0);
				progress.setVisibility(View.GONE);
				String data = response.toString();
				ReturnVO vo = (ReturnVO) com.alibaba.fastjson.JSONObject
						.parseObject(data, ReturnVO.class);
				Message message = handler.obtainMessage();
				message.what = handlerWhat;
				message.obj = vo;
				handler.sendMessage(message);

			}

			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				// TODO Auto-generated method stub
				super.onProgress(bytesWritten, totalSize);
				int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
				// 上传进度显示
				progress.setProgress(count);
				Log.e("上传 Progress>>>>>", bytesWritten + " / " + totalSize);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				if (statusCode == 0) {
					Message message = handler.obtainMessage();
					message.what = ConstantValue.SERVER_NO_RESPONE;
					handler.sendMessage(message);
				} else {
					Message message = handler.obtainMessage();
					message.what = statusCode;
					handler.sendMessage(message);
				}
				Log.i("post有参数---errorResponse throwable-http请求错误",
						"errorResponse=" + errorResponse + "statusCode"
								+ statusCode);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				Message message = handler.obtainMessage();
				message.what = statusCode;
				handler.sendMessage(message);
				Log.i("post有参数---responseString throwable-http请求错误",
						"responseString=" + responseString + "statusCode"
								+ statusCode);
			}
		});
	}

	/**
	 * 使用post方式提交
	 * 
	 * @param url
	 * @param map
	 * @param handler
	 * @param handlerWath
	 */
	public static void postStream(Context context, String url,
			RequestParams params, final Handler handler, final int handlerWhat) {
		if (!isNetworkConnected(context)) {
			Message message = handler.obtainMessage();
			message.what = ConstantValue.NETWORK_NO_RESPONE;
			handler.sendMessage(message);
			return;
		}

		asyncHttpClient.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				String data = response.toString();
				ReturnVO vo = (ReturnVO) com.alibaba.fastjson.JSONObject
						.parseObject(data, ReturnVO.class);
				Message message = handler.obtainMessage();
				message.what = handlerWhat;
				message.obj = vo;
				handler.sendMessage(message);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				if (statusCode == 0) {
					Message message = handler.obtainMessage();
					message.what = ConstantValue.SERVER_NO_RESPONE;
					handler.sendMessage(message);
				} else {
					Message message = handler.obtainMessage();
					message.what = statusCode;
					handler.sendMessage(message);
				}
				Log.i("post有参数---errorResponse throwable-http请求错误",
						"errorResponse=" + errorResponse + "statusCode"
								+ statusCode);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				Message message = handler.obtainMessage();
				message.what = statusCode;
				handler.sendMessage(message);
				Log.i("post有参数---responseString throwable-http请求错误",
						"responseString=" + responseString + "statusCode"
								+ statusCode);
			}
		});
	}

	/**
	 * 使用post方式提交
	 * 
	 * @param url
	 * @param map
	 * @param handler
	 * @param handlerWath
	 */
	public static void post(Context context, String url,
			Map<String, Object> map, final Handler handler,
			final int handlerWhat) {
		if (!isNetworkConnected(context)) {
			Message message = handler.obtainMessage();
			message.what = ConstantValue.NETWORK_NO_RESPONE;
			handler.sendMessage(message);
			return;
		}
		if (map == null || map.size() == 0) {
			// 没有参数
			asyncHttpClient.post(url, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					String data = response.toString();

					ReturnVO vo = (ReturnVO) com.alibaba.fastjson.JSONObject
							.parseObject(data, ReturnVO.class);
					Message message = handler.obtainMessage();
					message.what = handlerWhat;
					message.obj = vo;
					handler.sendMessage(message);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					super.onFailure(statusCode, headers, throwable,
							errorResponse);
					if (statusCode == 0) {
						Message message = handler.obtainMessage();
						message.what = ConstantValue.SERVER_NO_RESPONE;
						handler.sendMessage(message);
					}
					Log.i("post无参数---JSONObject errorResponse-http请求错误",
							"errorResponse=" + errorResponse + "statusCode"
									+ statusCode);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						String responseString, Throwable throwable) {
					super.onFailure(statusCode, headers, responseString,
							throwable);

					Message message = handler.obtainMessage();
					message.what = statusCode;
					handler.sendMessage(message);
					Log.i("post无参数---responseString throwable-http请求错误",
							"responseString=" + responseString + "statusCode"
									+ statusCode);
				}
			});
		} else {

			// 有参数
			RequestParams params = new RequestParams();

			for (String key : map.keySet()) {
				params.put(key, map.get(key));
			}

			Log.i("post有参数 求参数==", params.toString());
			asyncHttpClient.post(url, params, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					String data = response.toString();
					ReturnVO vo = (ReturnVO) com.alibaba.fastjson.JSONObject
							.parseObject(data, ReturnVO.class);
					Message message = handler.obtainMessage();
					message.what = handlerWhat;
					message.obj = vo;
					handler.sendMessage(message);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					super.onFailure(statusCode, headers, throwable,
							errorResponse);
					if (statusCode == 0) {
						Message message = handler.obtainMessage();
						message.what = ConstantValue.SERVER_NO_RESPONE;
						handler.sendMessage(message);
					} else {
						Message message = handler.obtainMessage();
						message.what = statusCode;
						handler.sendMessage(message);
					}

					Log.i("post有参数---errorResponse throwable-http请求错误",
							"errorResponse=" + errorResponse + "statusCode"
									+ statusCode);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						String responseString, Throwable throwable) {
					super.onFailure(statusCode, headers, responseString,
							throwable);
					Message message = handler.obtainMessage();
					message.what = statusCode;
					handler.sendMessage(message);
					Log.i("post有参数---responseString throwable-http请求错误",
							"responseString=" + responseString + "statusCode"
									+ statusCode);
				}
			});
		}

	}

	/**
	 * 使用get方式提交
	 * 
	 * @param url
	 * @param map
	 * @param handler
	 * @param handlerWath
	 */
	public static void get(Context context, String url,
			Map<String, Object> map, final Handler handler,
			final int handlerWhat) {
		if (map == null || map.size() == 0) {

			// 没有参数
			asyncHttpClient.get(url, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					String data = response.toString();
					ReturnVO vo = (ReturnVO) com.alibaba.fastjson.JSONObject
							.parseObject(data, ReturnVO.class);
					Message message = handler.obtainMessage();
					message.what = handlerWhat;
					message.obj = vo;
					handler.sendMessage(message);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					super.onFailure(statusCode, headers, throwable,
							errorResponse);
					Message message = handler.obtainMessage();
					message.what = handlerWhat;
					handler.sendMessage(message);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						String responseString, Throwable throwable) {
					super.onFailure(statusCode, headers, responseString,
							throwable);

					Message message = handler.obtainMessage();
					message.what = handlerWhat;
					handler.sendMessage(message);
				}
			});
		} else {

			// 有参数
			RequestParams params = new RequestParams();

			for (String key : map.keySet()) {
				params.put(key, map.get(key));
			}
			asyncHttpClient.get(url, params, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					String data = response.toString();

					ReturnVO vo = (ReturnVO) com.alibaba.fastjson.JSONObject
							.parseObject(data, ReturnVO.class);
					Message message = handler.obtainMessage();
					message.what = handlerWhat;
					message.obj = vo;
					handler.sendMessage(message);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					super.onFailure(statusCode, headers, throwable,
							errorResponse);

					if (statusCode == 0) {
						Message message = handler.obtainMessage();
						message.what = ConstantValue.SERVER_NO_RESPONE;
						handler.sendMessage(message);
					} else {
						Message message = handler.obtainMessage();
						message.what = statusCode;
						handler.sendMessage(message);
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						String responseString, Throwable throwable) {
					super.onFailure(statusCode, headers, responseString,
							throwable);
					Message message = handler.obtainMessage();
					message.what = ConstantValue.NETWORK_NO_RESPONE;
					handler.sendMessage(message);
				}
			});
		}
	}

	/**
	 * 使用 post_json方式提交数据
	 * 
	 * @param context
	 * @param url
	 * @param map
	 * @param handler
	 * @param handlerWath
	 */
	public static void postJson(Context context, String url,
			Map<String, Object> map, final Handler handler,
			final int handlerWhat) {
		com.alibaba.fastjson.JSONObject params = new com.alibaba.fastjson.JSONObject();
		StringEntity stringEntity = null;
		try {
			if (map != null && map.size() > 0) {
				for (String key : map.keySet()) {
					params.put(key, map.get(key));
				}
				stringEntity = new StringEntity(params.toJSONString(), "UTF-8");
				Log.i("postjson 求情 参数", params.toJSONString());
			} else {
				stringEntity = new StringEntity("", "UTF-8");
			}
			stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json;charset=utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (!isNetworkConnected(context)) {
			Message message = handler.obtainMessage();
			message.what = ConstantValue.NETWORK_NO_RESPONE;
			handler.sendMessage(message);
			return;
		}
		asyncHttpClient.post(context, url, stringEntity,
				"application/json;charset=utf-8",
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						super.onSuccess(statusCode, headers, response);
						String data = response.toString();
						ReturnVO vo = (ReturnVO) com.alibaba.fastjson.JSONObject
								.parseObject(data, ReturnVO.class);
						Message message = handler.obtainMessage();
						message.what = handlerWhat;
						message.obj = vo;
						handler.sendMessage(message);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
						if (statusCode == 0) {
							Message message = handler.obtainMessage();
							message.what = ConstantValue.SERVER_NO_RESPONE;
							handler.sendMessage(message);
						} else {
							Message message = handler.obtainMessage();
							message.what = statusCode;
							handler.sendMessage(message);
						}
						Log.i("postJson JSONObject错误---JSONObject errorResponse-http请求错误",
								"errorResponse=" + errorResponse
										+ "\t statusCode==" + statusCode);

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONArray errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						super.onFailure(statusCode, headers, responseString,
								throwable);
						Message message = handler.obtainMessage();
						message.what = statusCode;
						handler.sendMessage(message);
						Log.i("postJson 第三个错误---responseString-http请求错误",
								"responseString=" + responseString
										+ "statusCode==" + statusCode);
					}
				});

	}

	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
}
