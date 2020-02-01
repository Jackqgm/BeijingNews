package com.atguigu.beijingnews.fragment;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.beijingnews.base.BaseFragment;
import com.atguigu.beijingnews.utils.LogUtil;

/**
 * Created by Jackqgm on 2020/1/31.
 */

public class LeftMenuFragment extends BaseFragment {


    private TextView textView;

    @Override
    public View initView() {
        LogUtil.e("左侧视图被初始化了");
        textView = new TextView(context);
        textView.setTextSize(25);
        textView.setTextColor(Color.DKGRAY);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("左侧数据被初始化了");
        textView.setText("左侧菜单页面");
    }
}
