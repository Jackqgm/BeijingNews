package com.atguigu.beijingnews.menudetailpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.MainActivity;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.NewsCenterPagerBean;
import com.atguigu.beijingnews.menudetailpager.tabdetailpager.TopicDetailPager;
import com.atguigu.beijingnews.utils.LogUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackqgm on 2020/2/6.
 * 专题详情页面
 */

public class TopicMenuDetailPager extends MenuDetailBasePager {

    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;

    @ViewInject(R.id.tabLayout)
    private TabLayout tabLayout;

    @ViewInject(R.id.ib_tab_next)
    private ImageButton ib_tab_next;

    // 页签页面的数据集合
    private final List<NewsCenterPagerBean.DataBean.ChildrenBean> children;

    // 页签页面的集合
    private ArrayList<TopicDetailPager> topicDetailPagers;

    public TopicMenuDetailPager(Context context, NewsCenterPagerBean.DataBean dataBean) {
        super(context);
        children = dataBean.getChildren();
    }

    @Override
    public View initView() {
        //2.联网请求,得到数据,创建视图
        View view = View.inflate(context, R.layout.topicmenu_detail_pager, null);
        x.view().inject(TopicMenuDetailPager.this, view);

        ib_tab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("新闻详情页面数据被初始化了");
        // 准备新闻详情页面数据
        topicDetailPagers = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            topicDetailPagers.add(new TopicDetailPager(context, children.get(i)));
        }

        //设置适配器
        viewPager.setAdapter(new TopicMenuDetailPager.MyMenuDetailPagerAdapter());

        //ViewPager和TabPageIndicator关联
        //tabLayout.setViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        //以后就用TabPageIndicator监听页面的变化
       //tabLayout.setOnPageChangeListener(new TopicMenuDetailPager.MyOnPageChangeListener());
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());

        //设置滑动或固定
        //tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //tabLayout.setTabTextColors(Color.BLACK, Color.RED);
        //tabLayout.setSelectedTabIndicatorColor(Color.RED);

        /*for (int i = 0; i <tabLayout.getTabCount() ; i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i));
        }*/
    }

    /*private View getTabView(int position) {
        //View view = View.inflate(context, R.layout.item_tab, null);
        View view = LayoutInflater.from(context).inflate(R.layout.item_tab, null);
        TextView tv = view.findViewById(R.id.textview);
        tv.setText(children.get(position).getTitle());

        ImageView img = view.findViewById(R.id.imageview);
        img.setImageResource(R.drawable.title_red_bg);

        return view;
    }*/

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                //SlidingMennu可以全屏滑动
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
            }else{
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    //根据传入的参数设置SlidingMenu是否可以滑动
    private void isEnableSlidingMenu(int touchmodeNone) {
        MainActivity mainActivity= (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchmodeNone);
    }

    private class MyMenuDetailPagerAdapter extends PagerAdapter {

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return children.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return topicDetailPagers.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            TopicDetailPager topicDetailPager = topicDetailPagers.get(position);
            View rootView = topicDetailPager.rootView;
            topicDetailPager.initData();//初始化数据
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
