package com.atguigu.beijingnews;

import android.app.Application;

import com.atguigu.beijingnews.volley.VolleyManager;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Jackqgm on 2020/2/1.
 * 代表整个软件
 */

public class BeijingNewsApplication extends Application {

    //所以组件被创建之前执行
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.setDebug(true);
        x.Ext.init(this);

        VolleyManager.init(this);

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
    }
}
