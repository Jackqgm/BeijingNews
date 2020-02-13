package com.atguigu.beijingnews.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Jackqgm on 2020/2/11.
 * 网络缓存工具类
 */

public class NetCacheUtils {

    public static final int SUCCESS = 1;
    public static final int FAIL = 2;
    private final Handler handler;
    private final LocalCacheUtils localCacheUtils;
    private final MemoryCacheUtils memoryCacheUtils;
    private ExecutorService service;

    public NetCacheUtils(Handler handler, LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
        this.handler = handler;
        service = Executors.newFixedThreadPool(10);
        this.localCacheUtils=localCacheUtils;
        this.memoryCacheUtils=memoryCacheUtils;
    }

    //联网请求得到图片
    public void getBitmapFromNet(String imageUrl, int position) {
        //new Thread(new MyRunnable(imageUrl, position)).start();
        //Executors.newCachedThreadPool();

        service.execute(new MyRunnable(imageUrl, position));
    }

    private class MyRunnable implements Runnable {
        private final String imageUrl;
        private final int position;


        public MyRunnable(String imageUrl, int position) {
            this.imageUrl = imageUrl;
            this.position = position;
        }

        @Override
        public void run() {
            //子线程中请求网络图片
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(4000);
                connection.setReadTimeout(4000);
                int code = connection.getResponseCode();
                if (code == 200) {
                    InputStream is = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    //显示到控件上,发信息,把bitmap和position发出去
                    Message msg = Message.obtain();
                    msg.what=SUCCESS;
                    msg.arg1=position;
                    msg.obj=bitmap;
                    handler.sendMessage(msg);

                    //在内存中缓存一份
                    memoryCacheUtils.putBitmap(imageUrl, bitmap);

                    //在本地文件中缓存一份
                    localCacheUtils.putBitmap(imageUrl, bitmap);
                }

            } catch (IOException e) {
                e.printStackTrace();
                Message msg = Message.obtain();
                msg.what=FAIL;
                msg.arg1=position;
                handler.sendMessage(msg);
            }
        }
    }
}
