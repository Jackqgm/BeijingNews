package com.atguigu.beijingnews.menudetailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.utils.LogUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Jackqgm on 2020/2/2.
 */

public class PhotosMenuDetailPager extends MenuDetailBasePager {

    @ViewInject(R.id.listview)
    private ListView listview;

    @ViewInject(R.id.gridview)
    private GridView gridview;


    public PhotosMenuDetailPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        //2.联网请求,得到数据,创建视图
        View view = View.inflate(context, R.layout.photo_menudetail_pager, null);
        x.view().inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("图组详情页面数据被初始化了");
        //4.绑定数据

    }
}
