package com.atguigu.beijingnews;

import android.app.Application;

import org.xutils.x;

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
    }
}
