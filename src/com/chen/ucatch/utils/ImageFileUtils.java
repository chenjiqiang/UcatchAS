/*
 * Copyright 2000-2020 YGSoft.Inc All Rights Reserved.
 */
package com.chen.ucatch.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;


/**
 * 图片文件处理工具类 <br>
 * 
 * @创建人 wangjun4 <br>
 * @修改人 <br>
 * @版本 1.0.0 2015年5月27日 下午3:36:32 <br>
 */
public class ImageFileUtils {

    /**
     * 定义图片文件名称 <br>
     * 按照默认'IMG'_yyyyMMdd_HHmmss格式取名<br>
     * 
     * @创建人 wangjun4 2015年5月27日 下午3:57:30 <br>
     * @修改人 <br>
     * @param fileNameExtension
     *            文件扩展名。null或“”时，默认为jpg。
     * @return 图片文件名<br>
     */
    public static String getImageDefaultFileName(String fileNameExtension) {
        Date currDate = new Date();
        StringBuffer res = new StringBuffer(new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss").format(currDate));

        if (TextUtils.isEmpty(fileNameExtension)) {
            res.append(".jpg");
        } else {
            res.append(".").append(fileNameExtension);
        }

        return res.toString();
    }

    /**
     * 定义图片文件的临时文件 <br>
     * 
     * @创建人 wangjun4 2015年5月27日 下午4:11:09 <br>
     * @修改人 <br>
     * @param context
     *            上下文
     * @param fileNameExtension
     *            文件扩展名。null或“”时，默认为jpg。
     * @return 图片临时文件<br>
     */
    public static File getImageTempFile(Context context, String fileNameExtension) {
        File dir = new File(context.getExternalCacheDir() + "temp");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, getImageDefaultFileName(fileNameExtension));
    }

    /**
     * 定义图片文件的临时文件 <br>
     * 
     * @创建人 wangjun4 2015年5月27日 下午4:13:58 <br>
     * @修改人 <br>
     * @param context
     *            上下文
     * @param fileNameExtension
     *            文件扩展名。null或“”时，默认为jpg。
     * @return Uri格式的图片临时文件<br>
     */
    public static Uri getImageTempFileUri(Context context, String fileNameExtension) {
        File tempFile = getImageTempFile(context, fileNameExtension);
        return Uri.fromFile(tempFile);
    }
}
