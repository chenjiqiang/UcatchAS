package com.chen.ucatch.utils;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Handler;

/**
 * 
 * 收藏，点赞，评论,关注
 * 
 * @author chenjiqiang <br>
 * @version 1.0.0 2015年11月2日 下午8:05:33 <br>
 * @see
 * @since JDK 1.4.2.6
 */
public class TopTitleUtils {

	/**
	 * 添加收藏
	 */
	public final static int ADD_COLLECT = 1001;
	/**
	 * 取消收藏
	 */
	public final static int CANCEL_COLLECT = 1002;
	/**
	 * 添加点赞
	 */
	public final static int ADD_PRAISE = 1003;
	/**
	 * 取消点赞
	 */
	public final static int CANCEL_PRAISE = 1004;
	/**
	 * 添加评论
	 */
	public final static int ADD_COMMENT = 1005;

	/**
	 * 提交收藏信息
	 * 
	 * @param context
	 *            上下文
	 * @param isCollec
	 *            true 为添加收藏，false 为取消收藏
	 * @param newsId
	 *            收藏新闻/报料/行程的id
	 * @param handler
	 * @param handlerWaht
	 */
	public static void submitCollect(final Context context,
			final boolean isCollect, String newsId, String userId,
			final Handler handler, final int handlerWhat) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("topicId", newsId);
		params.put("userId", userId);
		String url = null;
		if (isCollect) {
			url = ServerUrl.ADD_COLLECT;
			HttpRequestUtils.post(context, url, params, handler, handlerWhat);
		} else {
			url = ServerUrl.CANCEL_COLLECT;
			HttpRequestUtils.post(context, url, params, handler, handlerWhat);
		}

	}

	/**
	 * 
	 * 点赞和取消点赞
	 * 
	 * @author chenjiqiang 2015年11月15日 下午4:42:28 <br>
	 * @param context
	 * @param isSavePraise
	 * @param userId
	 * @param shareId
	 * @param handler
	 * @param handlerWhat
	 */
	public static void submitPraiseInfo(final Context context,
			final boolean isSavePraise, String userId, String shareId,
			final Handler handler, final int handlerWhat) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("shareId", shareId);
		String url = null;
		if (isSavePraise) {
			params.put("cancelFlag", 0);
		} else {
			params.put("cancelFlag", 1);
		}
		url = ServerUrl.SAVE_PRAISE;
		HttpRequestUtils.post(context, url, params, handler, handlerWhat);
	}

	/**
	 * 
	 * 保存评论
	 * 
	 * @author chenjiqiang 2015年11月3日 下午8:11:50 <br>
	 * @param context
	 * @param userId
	 * @param shareId
	 * @param content
	 * @param handler
	 * @param handlerWaht
	 */
	public static void submitCommentInfo(final Context context, String userId,
			String shareId, String content, final Handler handler,
			final int handlerWaht) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("shareId", shareId);
		params.put("content", content);
		String url = ServerUrl.SAVE_COMMENT;
		HttpRequestUtils.post(context, url, params, handler, handlerWaht);
	}

	/**
	 * 
	 * 获取详情评论列表
	 * 
	 * @author chenjiqiang 2015年11月3日 下午7:59:01 <br>
	 * @param context
	 * @param userId
	 * @param bizId
	 * @param queryType
	 * @param pageNo
	 * @param pageSize
	 * @param handler
	 * @param handlerWaht
	 */
	public static void getComment(final Context context, String userId,
			String bizId, String queryType, int pageNo, int pageSize,
			final Handler handler, final int handlerWaht) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("bizId", bizId);
		params.put("queryType", queryType);
		params.put("pageNo", pageNo);
		params.put("pageSize", pageSize);
		String url = ServerUrl.GET_COMMENT;
		HttpRequestUtils.post(context, url, params, handler, handlerWaht);
	}

	/**
	 * 
	 * 关注与取消关注
	 * 
	 * @author chenjiqiang 2015年11月15日 下午1:17:05 <br>
	 * @param context
	 * @param curUserId
	 * @param destUserId
	 * @param cancelFlag
	 * @param handler
	 * @param handlerWaht
	 */
	public static void attention(final Context context, String curUserId,
			String destUserId, int cancelFlag, final Handler handler,
			final int handlerWaht) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("curUserId", curUserId);
		params.put("destUserId", destUserId);
		params.put("cancelFlag", cancelFlag);
		String url = ServerUrl.ISATTENTION;
		HttpRequestUtils.post(context, url, params, handler, handlerWaht);
	}

	/**
	 * 
	 * 获取好友列表
	 * 
	 * @author chenjiqiang 2015年11月17日 上午11:31:01 <br>
	 * @param context
	 * @param userId
	 * @param handler
	 * @param handlerWaht
	 */
	public static void getFriends(final Context context, String userId,
			final Handler handler, final int handlerWaht) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		String url = ServerUrl.GETFRIENDS;
		HttpRequestUtils.post(context, url, params, handler, handlerWaht);
	}

	public static void getUserVODetail(final Context context, String userId,
			final Handler handler, final int handlerWaht) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		String url = ServerUrl.GET_USERVODETAIL;
		HttpRequestUtils.post(context, url, params, handler, handlerWaht);
	}

	/**
	 * 
	 * 点赞留言
	 * 
	 * @author chenjiqiang 2016年3月17日 下午9:01:44 <br>
	 * @param context
	 * @param isSavePraise
	 * @param userId
	 * @param shareId
	 * @param handler
	 * @param handlerWhat
	 */
	public static void submitPraise(final Context context,
			final boolean isSavePraise, String userId, String subjectId,
			final Handler handler, final int handlerWhat) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("subjectId", subjectId);
		String url = null;
		if (isSavePraise) {
			params.put("cancelFlag", 0);
		} else {
			params.put("cancelFlag", 1);
		}
		url = ServerUrl.SAVE_MSG_PRAISE;
		HttpRequestUtils.post(context, url, params, handler, handlerWhat);
	}

	/**
	 * 
	 * 保存留言评论
	 * 
	 * @author chenjiqiang 2016年3月24日 下午7:22:39 <br>
	 * @param context
	 * @param userId
	 * @param subjectId
	 * @param content
	 * @param handler
	 * @param handlerWaht
	 */
	public static void submitMsgCommentInfo(final Context context,
			String userId, String subjectId, String content,
			final Handler handler, final int handlerWhat) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("subjectId", subjectId);
		params.put("content", content);
		String url = ServerUrl.SAVE_MESSAGE_COMMENT;
		HttpRequestUtils.post(context, url, params, handler, handlerWhat);
	}

	/**
	 * 
	 * TODO 在此写上方法的相关说明. <br>
	 * 
	 * @author chenjiqiang 2016年3月30日 下午9:20:17 <br>
	 * @param context
	 * @param req
	 * @param handler
	 * @param handlerWaht
	 */
	public static void Loginout(final Context context, String req,
			final Handler handler, final int handlerWhat) {
		Map<String, Object> params = new HashMap<String, Object>();
		String url = ServerUrl.LOGINOUT;
		HttpRequestUtils.post(context, url, params, handler, handlerWhat);
	}
}
