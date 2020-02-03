package com.atguigu.beijingnews.menudetailpager.tabdetailpager;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.NewsCenterPagerBean;
import com.atguigu.beijingnews.domain.TabDetailpagerBean;
import com.atguigu.beijingnews.utils.CacheUtils;
import com.atguigu.beijingnews.utils.Constants;
import com.atguigu.beijingnews.utils.LogUtil;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Jackqgm on 2020/2/3.
 * 页签详情页面
 */

public class TabDetailPager extends MenuDetailBasePager {

    private final NewsCenterPagerBean.DataBean.ChildrenBean childrenBean;
    private String url;

    public TabDetailPager(Context context, NewsCenterPagerBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean=childrenBean;
    }

    @Override
    public View initView() {
        //LogUtil.e("页签详情页面数据被初始化了");
        View view=View.inflate(context, R.layout.tabdetail_pager, null);

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        url = Constants.BASE_URL+childrenBean.getUrl();

        //取出缓存数据
        String saveJson = CacheUtils.getString(context, url);
        if (!TextUtils.isEmpty(saveJson)){
            //解析数据
            ProcessData(saveJson);
        }

        //LogUtil.e("页签详情页面数据被初始化了")
        //LogUtil.e(childrenBean.getTitle()+"联网地址+++"+url);

        //4.联网请求并绑定数据
        getDataFromNet();
    }

    private void ProcessData(String json) {
        TabDetailpagerBean bean=prasedJson(json);
        LogUtil.e(childrenBean.getTitle()+"解析成功了******"+bean.getData().getNews().get(0).getTitle());
    }

    private TabDetailpagerBean prasedJson(String json) {
        Gson gson=new Gson();
        return gson.fromJson(json, TabDetailpagerBean.class);
    }

    private void getDataFromNet() {

        RequestParams params=new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //缓存数据
                CacheUtils.putString(context, url, result);

                LogUtil.e(childrenBean.getTitle()+"页面数据请求成功+++"+result);

                //解析数据
                ProcessData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e(childrenBean.getTitle()+"页面数据请求失败+++"+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e(childrenBean.getTitle()+"页面数据请求onCancelled++"+cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e(childrenBean.getTitle()+"页面数据请求onFinished()+");
            }
        });
    }
}
