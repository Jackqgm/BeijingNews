package com.atguigu.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.beijingnews.activity.MainActivity;
import com.atguigu.beijingnews.base.BasePager;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.NewsCenterPagerBean;
import com.atguigu.beijingnews.fragment.LeftMenuFragment;
import com.atguigu.beijingnews.menudetailpager.InteractDetailPager;
import com.atguigu.beijingnews.menudetailpager.NewsMenuDetailPager;
import com.atguigu.beijingnews.menudetailpager.PhotosMenuDetailPager;
import com.atguigu.beijingnews.menudetailpager.TopicMenuDetailPager;
import com.atguigu.beijingnews.utils.CacheUtils;
import com.atguigu.beijingnews.utils.Constants;
import com.atguigu.beijingnews.utils.LogUtil;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackqgm on 2020/2/1.
 */

public class NewsCenterPager extends BasePager {

    //左侧菜单对应的数据集合
    private List<NewsCenterPagerBean.DataBean> data;
    //详情页面的集合
    private ArrayList<MenuDetailBasePager> detailBasePagers;

    public NewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();

        ib_menu.setVisibility(View.VISIBLE);
        //1.设置标题
        tv_title.setText("新闻中心页面");

        //2.联网请求,得到数据,创建视图
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        //3.把子视图添加到BasePager的FrameLayout中
        fl_content.addView(textView);

        ib_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();
            }
        });
        //4.绑定数据
        textView.setText("新闻中心页面内容");

        //获取缓存数据
        String saveJson = CacheUtils.getString(context, Constants.NEWSCENTER_PAGER_URL);
        if (!TextUtils.isEmpty(saveJson)){
            processData(saveJson);
        }

        //调用联网请求数据的方法
        getDataFromNet();
    }

    /*
    * 使用xUtils3联网请求数据
    * */
    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.NEWSCENTER_PAGER_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("使用xUtils3联网请求成功==" + result);

                //缓存数据
                CacheUtils.putString(context, Constants.NEWSCENTER_PAGER_URL, result);

                processData(result);
                //设置适配器

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("使用xUtils3联网请求失败!!!!");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("使用xUtils3==onCancelled" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("使用xUtils3--onFinished()");
            }
        });
    }

    /*
    * 解析json数据并显示
    * */
    private void processData(String json) {
        NewsCenterPagerBean bean = parsedJson(json);
        String title = bean.getData().get(0).getChildren().get(1).getTitle();
        LogUtil.e("使用Gson解析Json成功:" + title);

        //给左侧菜单传递数据
        data = bean.getData();

        MainActivity mainActivity = (MainActivity) context;
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();

        //添加详情页面
        detailBasePagers = new ArrayList<>();
        detailBasePagers.add(new NewsMenuDetailPager(context));
        detailBasePagers.add(new TopicMenuDetailPager(context));
        detailBasePagers.add(new PhotosMenuDetailPager(context));
        detailBasePagers.add(new InteractDetailPager(context));

        //把数据传递给左侧菜单
        leftMenuFragment.setData(data);

    }

    private NewsCenterPagerBean parsedJson(String json) {

        Gson gson = new Gson();
        NewsCenterPagerBean bean = gson.fromJson(json, NewsCenterPagerBean.class);
        return bean;
    }

    /*
    * 根据位置切换详情页面
    * */
    public void switchPager(int i) {
        //1.切换标题
        tv_title.setText(data.get(i).getTitle());

        //2.移除之前内容
        fl_content.removeAllViews();

        //3.添加新内容
        MenuDetailBasePager menuDetailBasePager = detailBasePagers.get(i);
        View rootView = menuDetailBasePager.rootView;
        //初始化数据
        menuDetailBasePager.initData();

        fl_content.addView(rootView);
    }
}
