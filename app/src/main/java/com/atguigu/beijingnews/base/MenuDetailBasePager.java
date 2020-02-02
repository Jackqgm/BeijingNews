package com.atguigu.beijingnews.base;

import android.content.Context;
import android.view.View;

/**
 * Created by Jackqgm on 2020/2/2.
 * 详情页面的基类
 */

public abstract class MenuDetailBasePager {

    /*
    * 上下文
    * */
    public final Context context;


    //代表各个详情页面的视图
    public View rootView;

    public MenuDetailBasePager(Context context){
        this.context=context;
        rootView=initView();
    }

    //抽象方法,强制孩子实现该方法,每个页面实现不同的效果
    public abstract View initView();

    //子页面绑定数据,联网请求数据时,重写该方法
    public void initData(){

    }
}
