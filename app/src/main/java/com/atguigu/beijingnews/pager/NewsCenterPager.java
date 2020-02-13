package com.atguigu.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
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
import com.atguigu.beijingnews.volley.VolleyManager;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
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
    //起始时间
    private long startTime;

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
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }

        startTime = SystemClock.uptimeMillis();
        //调用联网请求数据的方法
        //getDataFromNet();
        getDataFromNetByVolley();
    }

    /*
    * 使用Volley联网请求
    * */
    private void getDataFromNetByVolley() {
        //请求队列
        //RequestQueue queue= Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, Constants.NEWSCENTER_PAGER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //结束时间
                long endTime = SystemClock.uptimeMillis();

                long passTime = endTime - startTime;

                LogUtil.e("passTime>>>>>>>$$$$$$$$$$$$$$$$$$$$$:" + passTime);

                LogUtil.e("使用Volley联网请求成功==" + s);
                //缓存数据
                CacheUtils.putString(context, Constants.NEWSCENTER_PAGER_URL, s);

                processData(s);
                //设置适配器

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("使用Volley联网请求失败==" + volleyError);
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String parsed;
                try {
                    parsed = new String(response.data, "utf-8");
                    return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException var4) {
                    parsed = new String(response.data);
                }
                return super.parseNetworkResponse(response);
            }
        };
        //添加队列
        VolleyManager.getRequestQueue().add(request);
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
        detailBasePagers.add(new NewsMenuDetailPager(context, data.get(0)));
        detailBasePagers.add(new TopicMenuDetailPager(context, data.get(0)));
        detailBasePagers.add(new PhotosMenuDetailPager(context, data.get(2)));
        detailBasePagers.add(new InteractDetailPager(context, data.get(2)));

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

        if (i == 2) {
            //组图详情页面可见
            ib_switch_grid.setVisibility(View.VISIBLE);
            ib_switch_grid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //得到组图详情页面对象
                    PhotosMenuDetailPager detailPager = (PhotosMenuDetailPager) detailBasePagers.get(2);
                    //调用对象切换listview和gridview
                    detailPager.switchListAndGrid(ib_switch_grid);
                }
            });
        } else {
            //其他页面不可见
            ib_switch_grid.setVisibility(View.GONE);
        }
    }
}
