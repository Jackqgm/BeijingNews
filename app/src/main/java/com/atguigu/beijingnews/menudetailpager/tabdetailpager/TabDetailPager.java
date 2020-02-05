package com.atguigu.beijingnews.menudetailpager.tabdetailpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.NewsCenterPagerBean;
import com.atguigu.beijingnews.domain.TabDetailpagerBean;
import com.atguigu.beijingnews.utils.CacheUtils;
import com.atguigu.beijingnews.utils.Constants;
import com.atguigu.beijingnews.utils.LogUtil;
import com.atguigu.beijingnews.view.HorizontalScrollViewPager;
import com.atguigu.beijingnews.view.RefreshListView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

/**
 * Created by Jackqgm on 2020/2/3.
 * 页签详情页面
 */

public class TabDetailPager extends MenuDetailBasePager {

    private HorizontalScrollViewPager viewPager;
    private TextView tv_title;
    private LinearLayout ll_point_group;
    private RefreshListView listView;
    private ImageOptions imageOptions;

    private final NewsCenterPagerBean.DataBean.ChildrenBean childrenBean;
    private String url;
    //顶部轮播图的新闻数据
    private List<TabDetailpagerBean.DataBean.TopnewsBean> topnews;

    //新闻列表数据的集合
    private List<TabDetailpagerBean.DataBean.NewsBean> news;
    private TabDetailPagerListAdapter adapter;

    //下一页的联网路径
    private String moreUrl;
    private Boolean isMoreLoad=false;

    public TabDetailPager(Context context, NewsCenterPagerBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
        imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(135), DensityUtil.dip2px(107))
                .setRadius(DensityUtil.dip2px(2))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.news_pic_default)
                .setFailureDrawableId(R.drawable.news_pic_default)
                .build();
    }

    @Override
    public View initView() {
        //LogUtil.e("页签详情页面数据被初始化了");
        View view = View.inflate(context, R.layout.tabdetail_pager, null);
        listView = (RefreshListView) view.findViewById(R.id.listview);
        //初始化控件
        View topNewsView = View.inflate(context, R.layout.topnews, null);
        viewPager = topNewsView.findViewById(R.id.viewpager);
        tv_title = topNewsView.findViewById(R.id.tv_title);
        ll_point_group = topNewsView.findViewById(R.id.ll_point_group);

        //把顶部轮播图部分的视图,以头Item方式添加到ListView中
        //listView.addHeaderView(topNewsView);
        listView.addTopNewsView(topNewsView);

        //设置下拉刷新监听
        listView.setOnRefreshListener(new MyOnRefreshListener());

        return view;
    }

    private class MyOnRefreshListener implements RefreshListView.OnRefreshListener {
        @Override
        public void onPullDownRefresh() {
            Toast.makeText(context, "下拉刷新被回调了", Toast.LENGTH_SHORT).show();
            getDataFromNet();
        }

        @Override
        public void onLoadMore() {
            if (TextUtils.isEmpty(moreUrl)) {
                Toast.makeText(context, "无法加载更多", Toast.LENGTH_SHORT).show();
                listView.setOnRefreshFinish(false);
            } else {
                Toast.makeText(context, "加载更多被回调了", Toast.LENGTH_SHORT).show();
                getMoreDataFromNet();
            }
        }
    }

    private void getMoreDataFromNet() {
        RequestParams params = new RequestParams(moreUrl);
        params.setConnectTimeout(5000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("加载更多联网成功==" + result);
                listView.setOnRefreshFinish(false);
                isMoreLoad = true;
                //解析数据
                processData(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("加载更多联网失败==" + ex.getMessage());
                listView.setOnRefreshFinish(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("加载更多联网onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("加载更多联网onFinished()");
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        url = Constants.BASE_URL + childrenBean.getUrl();

        //取出缓存数据
        String saveJson = CacheUtils.getString(context, url);
        if (!TextUtils.isEmpty(saveJson)) {
            //解析数据
            processData(saveJson);
        }

        //LogUtil.e("页签详情页面数据被初始化了")
        //LogUtil.e(childrenBean.getTitle()+"联网地址+++"+url);

        //4.联网请求并绑定数据
        getDataFromNet();
    }

    //之前亮点显示的位置
    private int prePosition;

    private void processData(String json) {
        TabDetailpagerBean bean = prasedJson(json);
        LogUtil.e(childrenBean.getTitle() + "解析成功了******" + bean.getData().getNews().get(0).getTitle());

        //下一页的联网全路径
        moreUrl = "";
        if (TextUtils.isEmpty(bean.getData().getMore())) {
            moreUrl = "";
        } else {
            moreUrl = Constants.BASE_URL + bean.getData().getMore();
        }
        LogUtil.e("moreUrl--------------" + moreUrl);
        //默认和加载更多
        if (!isMoreLoad) {

            topnews = bean.getData().getTopnews();

            //设置ViewPager的适配器
            viewPager.setAdapter(new TabDetailPagerTopNewsAdapter());

            ll_point_group.removeAllViews();
            for (int i = 0; i < topnews.size(); i++) {
                ImageView imageView = new ImageView(context);
                imageView.setBackgroundResource(R.drawable.point_selector);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(6), DensityUtil.dip2px(6));

                if (i == 0) {
                    imageView.setEnabled(true);
                } else {
                    imageView.setEnabled(false);
                    params.leftMargin = DensityUtil.dip2px(7);
                }
                imageView.setLayoutParams(params);
                ll_point_group.addView(imageView);
            }
            //监听页面的改变, 设置红点变化和文本变化
            viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
            tv_title.setText(topnews.get(prePosition).getTitle());

        /*
        * 1.准备ListView对应的数据集合
        * 2.设置ListViewd的适配器
        * */
            news = bean.getData().getNews();

            adapter = new TabDetailPagerListAdapter();
            listView.setAdapter(adapter);
        } else {
            //加载更多
            isMoreLoad = false;
            //List<TabDetailpagerBean.DataBean.NewsBean> moreNews = bean.getData().getNews();
            //添加到原来的集合中
            //news.addAll(moreNews);
            news.addAll(bean.getData().getNews());
            //刷新适配器
            adapter.notifyDataSetChanged();
        }
    }


    private class TabDetailPagerListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return news.size();
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
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = android.view.View.inflate(context, R.layout.item_tabdetail_pager, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_title = convertView.findViewById(R.id.tv_title);
                viewHolder.tv_time = convertView.findViewById(R.id.tv_time);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //根据位置得到数据
            TabDetailpagerBean.DataBean.NewsBean newsBean = news.get(i);
            String imageUrl = Constants.BASE_URL + newsBean.getListimage();
            //使用xUtils请求图片
            //x.image().bind(viewHolder.iv_icon, imageUrl, imageOptions );
            //x.image().bind(viewHolder.iv_icon, imageUrl);
            //使用Glide请求图片
            Glide
                    .with(context)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.news_pic_default)
                    .error(R.drawable.news_pic_default)
                    .into(viewHolder.iv_icon);
            //设置标题和时间
            viewHolder.tv_title.setText(newsBean.getTitle());
            viewHolder.tv_time.setText(newsBean.getPubdate());

            return convertView;
        }
    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_time;
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //设置文本
            tv_title.setText(topnews.get(position).getTitle());

            //把之前点改成灰色
            ll_point_group.getChildAt(prePosition).setEnabled(false);
            //把当前点变成红色
            ll_point_group.getChildAt(position).setEnabled(true);
            prePosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class TabDetailPagerTopNewsAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.home_scroll_default);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            //联网请求图片
            TabDetailpagerBean.DataBean.TopnewsBean topnewsBean = topnews.get(position);
            String imageUrl = Constants.BASE_URL + topnewsBean.getTopimage();
            //x.image().bind(imageView, imageUrl, imageOptions);
            x.image().bind(imageView, imageUrl);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

    }

    private TabDetailpagerBean prasedJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, TabDetailpagerBean.class);
    }

    private void getDataFromNet() {

        RequestParams params = new RequestParams(url);
        params.setConnectTimeout(5000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //缓存数据
                CacheUtils.putString(context, url, result);
                LogUtil.e(childrenBean.getTitle() + "页面数据请求成功+++" + result);

                //解析数据
                processData(result);

                // 隐藏下拉刷新控件,更新时间,重新显示数据
                listView.setOnRefreshFinish(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e(childrenBean.getTitle() + "页面数据请求失败+++" + ex.getMessage());
                //隐藏下拉刷新控件,不更新时间
                listView.setOnRefreshFinish(true);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e(childrenBean.getTitle() + "页面数据请求onCancelled++" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e(childrenBean.getTitle() + "页面数据请求onFinished()+");
            }
        });
    }
}
