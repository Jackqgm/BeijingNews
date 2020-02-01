package com.atguigu.beijingnews.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.atguigu.beijingnews.R;

/**
 * Created by Jackqgm on 2020/2/1.
 */

public class BasePager {

    public final Context context;//MainActivity
    //视图,代表不同的页面
    public View rootView;
    //显示标题
    public TextView tv_title;
    //点击侧滑的
    public ImageButton ib_menu;

    //加载子页面的
    public FrameLayout fl_content;

    public BasePager(Context context){
        this.context=context;
        rootView=initView();
    }
    //用于初始化公共视图,并且初始化加载子视图的FrameLayout
    private View initView() {
        View view = View.inflate(context, R.layout.base_pager, null);
        tv_title = view.findViewById(R.id.tv_title);
        ib_menu = view.findViewById(R.id.ib_menu);
        fl_content = view.findViewById(R.id.fl_content);
        return view;
    }

    //初始化数据;当孩子需要初始化数据,或者绑定数据,联网请求数据并且绑定的时候,重写该方法
    public void initData(){

    }
}
