package com.chen.ucatch.utils;

import org.afinal.simplecache.ACache;

import android.content.Context;

import com.chen.ucatch.model.UserVO;
public class UserInfoUtils {

	public static UserVO getUser(Context context) {
		ACache mCache = ACache.get(context);
		UserVO userVO = new UserVO();
		if (mCache.getAsJSONObject("user") != null) {
			userVO = com.alibaba.fastjson.JSONObject.parseObject(mCache
					.getAsJSONObject("user").toString(), UserVO.class);
			if (StringUtil.isEmptyString(userVO.getId())) {
				return null;
			}
			return userVO;
		} else {
			return null;
		}
	}
}
