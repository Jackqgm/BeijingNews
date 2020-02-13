package com.atguigu.beijingnews.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Jackqgm on 2020/2/11.
 */

class LocalCacheUtils {

    private static final String CACHE_PATH = Environment.getDataDirectory().getPath() + "/news";
    private final MemoryCacheUtils memoryCacheUtils;

    public LocalCacheUtils(MemoryCacheUtils memoryCacheUtils) {
        this.memoryCacheUtils=memoryCacheUtils;
    }

    public Bitmap getBitmapFromUrl(String imageUrl) {
        String fileName=null;
        try {
            fileName = MD5Encoder.encode(imageUrl);
            File file = new File(CACHE_PATH,fileName);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                if (bitmap!=null){
                    memoryCacheUtils.putBitmap(imageUrl, bitmap);
                    LogUtil.e("从本地保存到内存中");
                }
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("图片获取失败====" + e);
        }

        return null;
    }

    public void putBitmap(String imageUrl, Bitmap bitmap) {
        try {
            String fileName = MD5Encoder.encode(imageUrl);
            File file = new File(CACHE_PATH,fileName);
            //通过得到文件的父文件,判断父文件是否存在
            File parentFile = file.getParentFile();
            if (!parentFile.exists()){
                parentFile.mkdirs();
            }
            if (!file.exists()){
                file.createNewFile();//
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("图片保存失败====" + e);
        }
    }
}
