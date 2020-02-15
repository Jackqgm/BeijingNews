package com.atguigu.beijingnews.menudetailpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.NewsCenterPagerBean;
import com.atguigu.beijingnews.domain.PhotosMenuDetailPagerBean;
import com.atguigu.beijingnews.utils.BitmapCacheUtils;
import com.atguigu.beijingnews.utils.CacheUtils;
import com.atguigu.beijingnews.utils.Constants;
import com.atguigu.beijingnews.utils.LogUtil;
import com.atguigu.beijingnews.utils.NetCacheUtils;
import com.atguigu.beijingnews.volley.VolleyManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Jackqgm on 2020/2/2.
 */

public class InteractDetailPager extends MenuDetailBasePager {

    private final NewsCenterPagerBean.DataBean dataBean;

    @ViewInject(R.id.listview)
    private ListView listview;

    @ViewInject(R.id.gridview)
    private GridView gridview;
    private String url;
    private List<PhotosMenuDetailPagerBean.DataBean.NewsBean> news;
    private InteractDetailPager.PhotosMenuDetailPagerAdapter adapter;

    private final BitmapCacheUtils bitmapCacheUtils;//图片三级缓存工具类
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NetCacheUtils.SUCCESS:
                    int position = msg.arg1;
                    Bitmap bitmap = (Bitmap) msg.obj;

                    if (listview.isShown()) {
                        ImageView iv_icon = listview.findViewWithTag(position);
                        if (iv_icon != null && bitmap != null) {
                            iv_icon.setImageBitmap(bitmap);
                        }
                    } else if (gridview.isShown()) {
                        ImageView iv_icon = gridview.findViewWithTag(position);
                        if (iv_icon != null && bitmap != null) {
                            iv_icon.setImageBitmap(bitmap);
                        }
                    }

                    LogUtil.e("请求图片成功" + position);
                    break;
                case NetCacheUtils.FAIL:
                    position = msg.arg1;
                    LogUtil.e("请求图片失败" + position);
                    break;
            }
        }
    };

    public InteractDetailPager(Context context, NewsCenterPagerBean.DataBean dataBean) {
        super(context);
        this.dataBean = dataBean;
        bitmapCacheUtils = new BitmapCacheUtils(handler);
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
        url = Constants.BASE_URL + dataBean.getUrl();

        String savaJson = CacheUtils.getString(context, url);
        if (!TextUtils.isEmpty(savaJson)) {
            processData(savaJson);
        }
        getDataFromNet();
    }

    /*
    * 使用Volley联网请求
    * */
    private void getDataFromNet() {
        //请求队列
        //RequestQueue queue= Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //结束时间
                long endTime = SystemClock.uptimeMillis();

                //long passTime=endTime-startTime;

                //LogUtil.e("passTime>>>>>>>$$$$$$$$$$$$$$$$$$$$$:"+passTime);

                LogUtil.e("使用Volley联网请求成功==" + s);
                //缓存数据
                CacheUtils.putString(context, url, s);

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

    private void processData(String json) {
        PhotosMenuDetailPagerBean bean = parsedJson(json);
        LogUtil.e("title???????????????????????" + bean.getData().getNews().get(0).getTitle());

        isShowListView = true;
        //设置适配器
        news = bean.getData().getNews();

        adapter = new InteractDetailPager.PhotosMenuDetailPagerAdapter();
        listview.setAdapter(adapter);
    }

    private boolean isShowListView = true;

    public void switchListAndGrid(ImageButton ib_switch_grid) {
        if (isShowListView) {
            isShowListView = false;
            gridview.setVisibility(View.VISIBLE);
            adapter = new InteractDetailPager.PhotosMenuDetailPagerAdapter();
            gridview.setAdapter(adapter);
            listview.setVisibility(View.GONE);
            ib_switch_grid.setImageResource(R.drawable.icon_pic_list_type);

        } else {
            isShowListView = true;
            listview.setVisibility(View.VISIBLE);
            adapter = new InteractDetailPager.PhotosMenuDetailPagerAdapter();
            listview.setAdapter(adapter);
            gridview.setVisibility(View.GONE);
            ib_switch_grid.setImageResource(R.drawable.icon_pic_grid_type);
        }
    }

    private class PhotosMenuDetailPagerAdapter extends BaseAdapter {

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
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            PhotosMenuDetailPager.ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_photo_menudetail_pager, null);
                viewHolder = new PhotosMenuDetailPager.ViewHolder();
                viewHolder.iv_icon = convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_title = convertView.findViewById(R.id.tv_title);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (PhotosMenuDetailPager.ViewHolder) convertView.getTag();
            }
            //根据位置得到相应的数据
            PhotosMenuDetailPagerBean.DataBean.NewsBean newsBean = news.get(position);
            viewHolder.tv_title.setText(newsBean.getTitle());
            //使用Volley请求并设置图片
            String imageUrl = Constants.BASE_URL + newsBean.getSmallimage();
            //loaderImager(viewHolder, imageUrl);

            //使用自定义的图片三级缓存工具类请求图片
           /* viewHolder.iv_icon.setTag(position);
            Bitmap bitmap = bitmapCacheUtils.getBitmap(imageUrl, position);//本地或内存
            if (bitmap != null) {
                viewHolder.iv_icon.setImageBitmap(bitmap);
            }*/

            // 使用Picasso请求图片
            /*Picasso.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.home_scroll_default)
                    .error(R.drawable.home_scroll_default)
                    .into(viewHolder.iv_icon);*/

            // 使用Glide请求图片
            Glide.with(context)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.home_scroll_default)
                    .error(R.drawable.home_scroll_default)
                    .into(viewHolder.iv_icon);

            //使用ImageLoader加载图片
            /*DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_stub)
                    .showImageForEmptyUri(R.drawable.ic_empty)
                    .showImageOnFail(R.drawable.ic_error)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new RoundedBitmapDisplayer(10))
                    .build();
            com.nostra13.universalimageloader.core.ImageLoader
                    .getInstance().displayImage(imageUrl, viewHolder.iv_icon, options);*/


            return convertView;
            //13040651853
        }
    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
    }

    private PhotosMenuDetailPagerBean parsedJson(String json) {
        return new Gson().fromJson(json, PhotosMenuDetailPagerBean.class);
    }

    private void loaderImager(final PhotosMenuDetailPager.ViewHolder viewHolder, String imageurl) {

        viewHolder.iv_icon.setTag(imageurl);
        //直接在这里请求会乱位置
        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageContainer != null) {

                    if (viewHolder.iv_icon != null) {
                        if (imageContainer.getBitmap() != null) {
                            viewHolder.iv_icon.setImageBitmap(imageContainer.getBitmap());
                        } else {
                            viewHolder.iv_icon.setImageResource(R.drawable.home_scroll_default);
                        }
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //如果出错，则说明都不显示（简单处理），最好准备一张出错图片
                viewHolder.iv_icon.setImageResource(R.drawable.home_scroll_default);
            }
        };
        VolleyManager.getImageLoader().get(imageurl, listener);
    }
}
