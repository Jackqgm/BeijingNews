package com.atguigu.beijingnews.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class CacheUtils {
    private static final String CACHE_PATH = Environment.getDataDirectory().getPath() + "/news/files";

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);

        return sp.getBoolean(key, false);
    }


    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static void putString(Context context, String key, String value) {
        try {
            String fileName = MD5Encoder.encode(key);
            File file = new File(CACHE_PATH,fileName);
            //通过得到文件的父文件,判断父文件是否存在
            File parentFile = file.getParentFile();
            if (!parentFile.exists()){
                parentFile.mkdirs();
            }
            if (!file.exists()){
                file.createNewFile();//
            }
            //保存文本数据
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(value.getBytes());
            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("图片保存失败====" + e);
        }

        SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key) {
        String fileName=null;
        try {
            fileName = MD5Encoder.encode(key);
            File file = new File(CACHE_PATH,fileName);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                byte[] buff=new byte[1024];
                int len;
                while((len=fis.read(buff))!=-1){
                    stream.write(buff,0,len);
                }
                fis.close();
                stream.close();
                String result = stream.toString();
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("图片获取失败====" + e);
        }

        SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }
}
