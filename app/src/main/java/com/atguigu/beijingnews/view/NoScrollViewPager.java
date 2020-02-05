package com.atguigu.beijingnews.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Jackqgm on 2020/2/1.
 */

public class NoScrollViewPager extends ViewPager{

    //通常在代码实例化时使用
    public NoScrollViewPager(@NonNull Context context) {
        super(context);
    }

    //在布局文件中使用该类时,实例化该类用该构造方法,不用会软件崩溃
    public NoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    //重写触摸事件,消费掉
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
