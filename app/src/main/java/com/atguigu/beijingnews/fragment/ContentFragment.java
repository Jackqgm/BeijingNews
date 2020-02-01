package com.atguigu.beijingnews.fragment;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.base.BaseFragment;
import com.atguigu.beijingnews.base.BasePager;
import com.atguigu.beijingnews.pager.GovaffairPager;
import com.atguigu.beijingnews.pager.HomePager;
import com.atguigu.beijingnews.pager.NewsCenterPager;
import com.atguigu.beijingnews.pager.SettingPager;
import com.atguigu.beijingnews.pager.SmartServicePager;
import com.atguigu.beijingnews.utils.LogUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Jackqgm on 2020/1/31.
 */

public class ContentFragment extends BaseFragment {

    @ViewInject(R.id.viewpager)
    private ViewPager viewpager;
    @ViewInject(R.id.rg_main)
    private RadioGroup rg_main;

    //装五个页面的集合
    private ArrayList<BasePager> basePagers;

    @Override
    public View initView() {
        LogUtil.e("正文视图被初始化了");
        View view = View.inflate(context, R.layout.content_fragment, null);
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

        //初始化五个子页面, 并放入集合中
        basePagers = new ArrayList<>();
        basePagers.add(new HomePager(context));
        basePagers.add(new NewsCenterPager(context));
        basePagers.add(new SmartServicePager(context));
        basePagers.add(new GovaffairPager(context));
        basePagers.add(new SettingPager(context));

        //设置默认选中状态
        rg_main.check(R.id.rb_home);

        //设置ViewPager的适配器
        viewpager.setAdapter(new ContentFragmentAdapter());
    }

    private class ContentFragmentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return basePagers.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            BasePager basePager = basePagers.get(position);//各个页面的实例
            View rootView = basePager.rootView;//各个子页面
            //调用各个页面的initData()
            basePager.initData();
            container.addView(rootView);

            return rootView;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }


    }
}
