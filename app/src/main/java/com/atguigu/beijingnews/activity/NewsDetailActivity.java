package com.atguigu.beijingnews.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.beijingnews.R;


public class NewsDetailActivity extends Activity implements View.OnClickListener {

    private TextView tvTitle;
    private ImageButton ibMenu;
    private ImageButton ibBack;
    private ImageButton ibTextsize;
    private ImageButton ibShare;
    private WebView webview;
    private ProgressBar pbLoading;
    private String url;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        //初始化控件
        findViews();

        getData();
    }

    private void getData() {
        url = getIntent().getStringExtra("url");

        //支持JS
        webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //设置双击变大变小
        webSettings.setUseWideViewPort(true);
        //增添缩放按钮
        //webSettings.setBuiltInZoomControls(true);
        //设置文字大小
        webSettings.setTextZoom(100);

        //不让当前网页跳转到系统浏览器中
        webview.setWebViewClient(new WebViewClient() {
            //当页面加载完成时回调
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbLoading.setVisibility(View.GONE);
            }
        });
        webview.loadUrl(url);
    }

    private void findViews() {
        tvTitle = findViewById(R.id.tv_title);
        ibBack = findViewById(R.id.ib_back);
        ibTextsize = findViewById(R.id.ib_textsize);
        ibShare = findViewById(R.id.ib_share);
        webview = findViewById(R.id.webview);
        pbLoading = findViewById(R.id.pb_loading);

        tvTitle.setVisibility(View.GONE);
        ibBack.setVisibility(View.VISIBLE);
        ibTextsize.setVisibility(View.VISIBLE);
        ibShare.setVisibility(View.VISIBLE);


        ibBack.setOnClickListener(this);
        ibTextsize.setOnClickListener(this);
        ibShare.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v == ibBack) {
            // Handle clicks for ibBack
            finish();
        } else if (v == ibTextsize) {
            // Handle clicks for ibTextsize
            //Toast.makeText(this, "设置文字大小", Toast.LENGTH_SHORT).show();
            showChangeTextsizeDialog();
        } else if (v == ibShare) {
            // Handle clicks for ibShare
            Toast.makeText(this, "设置分享链接", Toast.LENGTH_SHORT).show();
        }
    }

    private int tempSize = 2;
    private int realSize = tempSize;

    private void showChangeTextsizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置文字大小:");
        String[] items = {"超大字体", "大字体", "正常", "小字体", "超小字体"};
        builder.setSingleChoiceItems(items, realSize, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tempSize = i;
            }
        });
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                realSize = tempSize;
                changeTextSize(realSize);
            }
        });

        builder.show();
    }

    private void changeTextSize(int realSize) {
        switch (realSize) {
            case 0:
                webSettings.setTextZoom(200);
                break;
            case 1:
                webSettings.setTextZoom(150);
                break;
            case 2:
                webSettings.setTextZoom(110);
                break;
            case 3:
                webSettings.setTextZoom(85);
                break;
            case 4:
                webSettings.setTextZoom(70);
                break;
            default:
                break;
        }
    }
}


