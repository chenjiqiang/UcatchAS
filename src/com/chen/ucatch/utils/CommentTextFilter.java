package com.chen.ucatch.utils;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;

/**
 * 限制评论的个数
 * @author liuhuan2
 *
 */
public class CommentTextFilter {

	
	public static InputFilter[] getInputFilter(final Context context,final int commentMaxLength){
		InputFilter[] commentTextFilter = new InputFilter[1];

		commentTextFilter[0] = new InputFilter() {

			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				// source:文本框输入内容;dest文本框输入之前的内容
				int commentLength = source.length() + dest.length();
				// 录入
				if (end > 0 && commentLength > commentMaxLength) {
					// 超过指定长度
					int exceedLength = commentLength - commentMaxLength;
					Toast.makeText(context, "超出指定字符数(140字)", Toast.LENGTH_SHORT)
							.show();
					return source
							.subSequence(0, source.length() - exceedLength);
				}
				// 删除或录入内容未达到指定长度
				return source;
			}
		};
		
		return commentTextFilter;
	}
	
	public static InputFilter[] getInputFilter(final Context context,final String  message,final int commentMaxLength){
		InputFilter[] commentTextFilter = new InputFilter[1];

		commentTextFilter[0] = new InputFilter() {

			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				// source:文本框输入内容;dest文本框输入之前的内容
				int commentLength = source.length() + dest.length();
				// 录入
				if (end > 0 && commentLength > commentMaxLength) {
					// 超过指定长度
					int exceedLength = commentLength - commentMaxLength;
					Toast.makeText(context, message, Toast.LENGTH_SHORT)
							.show();
					return source
							.subSequence(0, source.length() - exceedLength);
				}
				// 删除或录入内容未达到指定长度
				return source;
			}
		};
		
		return commentTextFilter;
	}
}
