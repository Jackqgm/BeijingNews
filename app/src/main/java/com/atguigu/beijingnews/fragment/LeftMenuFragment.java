package com.atguigu.beijingnews.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.MainActivity;
import com.atguigu.beijingnews.base.BaseFragment;
import com.atguigu.beijingnews.domain.NewsCenterPagerBean;
import com.atguigu.beijingnews.pager.NewsCenterPager;
import com.atguigu.beijingnews.utils.DensityUtil;
import com.atguigu.beijingnews.utils.LogUtil;

import java.util.List;

/**
 * Created by Jackqgm on 2020/1/31.
 */

public class LeftMenuFragment extends BaseFragment {

    private List<NewsCenterPagerBean.DataBean> data;
    private ListView listView;
    private LeftMenuFragmentAdapter adapter;

    private int prePosition;
    @Override
    public View initView() {
        LogUtil.e("左侧视图被初始化了");
        listView = new ListView(context);
        listView.setDividerHeight(0);
        listView.setPadding(0, DensityUtil.dip2px(context,80),0,0);
        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setSelector(android.R.color.transparent);//设置按下listview的item时不变色

        //设置item的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //1. 记录点击的位置,变成红色
                prePosition=i;
                adapter.notifyDataSetChanged();
                //2. 把左侧菜单关闭
                MainActivity mainActivity= (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();

                //3. 切换到对应的详情页面:新闻详情页面,专题详情页面,组图详情页面,互动详情页面
                switchPager(prePosition);
            }
        });
        return listView;
    }
    private void switchPager(int i) {
        MainActivity mainActivity= (MainActivity) context;
        ContentFragment contentFragment=mainActivity.getContentFragment();
        NewsCenterPager newsCenterPager=contentFragment.getNewsCenterPager();
        newsCenterPager.switchPager(i);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("左侧数据被初始化了");
    }

    /*
    * 接受数据
    * */
    public void setData(List<NewsCenterPagerBean.DataBean> data) {
        this.data=data;
        for (int i=0; i<data.size(); i++){
            LogUtil.e("title--"+data.get(i).getTitle());
        }
        adapter = new LeftMenuFragmentAdapter();
        listView.setAdapter(adapter);

        //设置默认页面
        switchPager(prePosition);
    }

    class LeftMenuFragmentAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView textView= (TextView) View.inflate(context, R.layout.item_leftmenu,null);
            textView.setText(data.get(i).getTitle());

            /*if (i==prePosition){
                textView.setEnabled(true);//设置红色
            }else{
                textView.setEnabled(false);
            }*/
            //设置红色
            textView.setEnabled(i==prePosition);
            return textView;
        }
    }
}
