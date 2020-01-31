package com.atguigu.beijingnews.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class CacheUtils {

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);

        return sp.getBoolean(key, false);
    }


    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }
}
