package com.xinshuyuan.xinshuyuanworkandexercise.Model;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.xinshuyuan.xinshuyuanworkandexercise.R;

/**
 * Created by Administrator on 2017/7/12.
 */

public class WebViewActivity extends Activity {
    private String videoUrl;
    private WebView web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.webview_activity_layout);
        web=(WebView)findViewById(R.id.avtivity_webView);
        WebSettings setting = web.getSettings();
        setting.setLoadWithOverviewMode(true);
        setting.setUseWideViewPort(true);
        setting.setUseWideViewPort(true); // 关键点
        setting.setAllowFileAccess(true); // 允许访问文件
        setting.setSupportZoom(true); // 支持缩放
        setting.setDomStorageEnabled(true);
        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);
        setting.setAllowFileAccess(true);
        setting.setDefaultTextEncodingName("UTF-8");
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setUseWideViewPort(true);
        setting.setDatabaseEnabled(true);
        setting.setGeolocationEnabled(true);
        setting.setAppCacheEnabled(true);
        setting.setCacheMode(WebSettings.LOAD_DEFAULT);
        setting.setBuiltInZoomControls(true);
        setting.setDisplayZoomControls(false);
        Intent intent=this.getIntent();
        videoUrl=intent.getStringExtra("data");
        //poster='',预览图
        String html="<html><body><video width='950' height='550' controls='controls' preload='none' > <source src="+videoUrl+"></source></vedio><object classid='clsid:D27CDB6E-AE6D-11cf-96B8-444553540000' width='600' height='350' style='margin-top:-10px;margin-left:-8px; id='FLVPlayer1'><param name='movie' value='FLVPlayer_Progressive.swf'/><param name='quality' value='high'/><param name='wmode' value='opaque' /><param name='scale' value='noscale'/><param name='salign' value='lt' /><param name='FlashVars' value='&amp;MM_ComponentVersion=1&amp;skinName=public/swf/Clear_Skin_3&amp;streamName=public/video/test&amp;autoPlay=false&amp;autoRewind=false' /><param name='swfversion' value='8,0,0,0' /><param name='expressinstall' value='expressInstall.swf' /></object> </body></html>";
        web.loadData(html, "text/html", "UTF-8");
    }

    //点击外部关闭activity
    public boolean onTouchEvent(MotionEvent event)
    {
        finish();
        return true;
    }

    @Override
    protected void onPause() {
        web.reload ();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        web.reload ();
        super.onDestroy();
    }


}

