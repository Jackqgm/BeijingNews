package com.atguigu.beijingnews.utils;

import android.graphics.Bitmap;

import org.xutils.cache.LruCache;

/**
 * Created by Jackqgm on 2020/2/13.
 */

public class MemoryCacheUtils {

    private LruCache<String, Bitmap> lruCache;
    public MemoryCacheUtils() {
        int maxSize= (int) (Runtime.getRuntime().maxMemory()/1024/8);
        lruCache=new LruCache<String, Bitmap>(maxSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //return super.sizeOf(key, value);
                return (value.getRowBytes()*value.getHeight())/1024;
            }
        };
    }

    public Bitmap getBitmapFromUrl(String imageUrl) {

        return lruCache.get(imageUrl);
    }

    public void putBitmap(String imageUrl, Bitmap bitmap) {
        lruCache.put(imageUrl,bitmap);
    }
}
