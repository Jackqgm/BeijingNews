package com.atguigu.beijingnews.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.base.BaseFragment;
import com.atguigu.beijingnews.utils.LogUtil;

/**
 * Created by Jackqgm on 2020/1/31.
 */

public class ContentFragment extends BaseFragment {

    private ViewPager viewpager;
    private RadioGroup rg_main;

    @Override
    public View initView() {
        LogUtil.e("正文视图被初始化了");
        View view =View.inflate(context, R.layout.content_fragment, null);

        viewpager = view.findViewById(R.id.viewpager);
        rg_main = view.findViewById(R.id.rg_main);
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
