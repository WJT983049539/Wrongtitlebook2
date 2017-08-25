package xinshuyuan.com.wrongtitlebook.View.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import xinshuyuan.com.wrongtitlebook.R;

/**
 * 显示详细信息的dialogActicity
 * Created by wjt on 2017/8/1.
 */

public class Dialog_commentActivity extends Activity {
    private WebView comment_dialog_webview;
    private String content;
    private Document document;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        //无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window=getWindow();
        //屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        WindowManager.LayoutParams layoutParams= window.getAttributes();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置窗口的大小及透明度
        layoutParams.width = 800;
        layoutParams.height = 600;
        layoutParams.alpha = 0.9f;
        window.setAttributes(layoutParams);
        Intent intent=getIntent();
        content=intent.getStringExtra("Content");
        setContentView(R.layout.layout_comment_dialog);
        initView();
        initData();
        super.onCreate(savedInstanceState);
    }

    protected void initView() {
        comment_dialog_webview= (WebView) findViewById(R.id.comment_dialog_webview);

    }

    protected void initData() {
        initotherWebView(comment_dialog_webview, content);
    }



    //显示网页内容
    private void initotherWebView(WebView webview, String content) {
        document = Jsoup.parse(content);
        String itemPoint=document.html();
        // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                WebView.HitTestResult hit = view.getHitTestResult();
                if(hit!=null){
                    int hitType=hit.getType();
                }
                return true;
            }
        });

        webview.setBackgroundColor(0); // 设置背景色
        WebSettings settings=webview.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setTextSize(WebSettings.TextSize.LARGER);
        webview.loadDataWithBaseURL(null, itemPoint, "text/html", "utf-8", null);
    }

}
