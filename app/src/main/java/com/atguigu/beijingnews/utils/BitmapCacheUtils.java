package com.atguigu.beijingnews.utils;

import android.graphics.Bitmap;
import android.os.Handler;

/**
 * Created by Jackqgm on 2020/2/11.
 * 图片缓存工具类
 * >>>三级缓存设计步骤
 * 从内存中获取图片
 * 从本地文件中获取图片并向内存中保持一份
 * 从网络中获取图片,在控件中显示
 * 并向内存中保持一份
 * 并向内存中保持一份
 */

public class BitmapCacheUtils {

    //网络缓存工具类
    private NetCacheUtils netCacheUtils;

    //本地缓存工具类
    private LocalCacheUtils localCacheUtils;

    //内存缓存工具类
    private MemoryCacheUtils memoryCacheUtils;

    public BitmapCacheUtils(Handler handler) {
        memoryCacheUtils = new MemoryCacheUtils();
        localCacheUtils = new LocalCacheUtils(memoryCacheUtils);
        netCacheUtils = new NetCacheUtils(handler, localCacheUtils, memoryCacheUtils);
    }

    public Bitmap getBitmap(String imageUrl, int position) {
        // 1.从内存中获取图片
        if (memoryCacheUtils != null) {
            Bitmap bitmap = memoryCacheUtils.getBitmapFromUrl(imageUrl);
            if (bitmap != null) {
                LogUtil.e("从内存获取图片成功===" + position);
                return bitmap;
            }
        }

        // 2.从本地文件中获取图片
        if (localCacheUtils != null) {
            Bitmap bitmap = localCacheUtils.getBitmapFromUrl(imageUrl);
            if (bitmap != null) {
                LogUtil.e("从本地获取图片成功===" + position);
                return bitmap;
            }
        }

        // 3.从网络中获取图片
        netCacheUtils.getBitmapFromNet(imageUrl, position);


        return null;
    }
}
