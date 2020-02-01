package com.atguigu.beijingnews.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.base.BaseFragment;
import com.atguigu.beijingnews.utils.LogUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Jackqgm on 2020/1/31.
 */

public class ContentFragment extends BaseFragment {

    @ViewInject(R.id.viewpager)
    private ViewPager viewpager;
    @ViewInject(R.id.rg_main)
    private RadioGroup rg_main;

    @Override
    public View initView() {
        LogUtil.e("正文视图被初始化了");
        View view =View.inflate(context, R.layout.content_fragment, null);
//        viewpager = view.findViewById(R.id.viewpager);
//        rg_main = view.findViewById(R.id.rg_main);
        //把视图注入到框架中,让ContentFragment.this和view关联起来
        x.view().inject(ContentFragment.this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("正文数据被初始化了");

        //设置默认选中状态
        rg_main.check(R.id.rb_home);
     }
}
