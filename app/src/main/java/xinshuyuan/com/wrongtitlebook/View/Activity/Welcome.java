package xinshuyuan.com.wrongtitlebook.View.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import xinshuyuan.com.wrongtitlebook.Model.Common.XSYTools;
import xinshuyuan.com.wrongtitlebook.Persenter.Handler.LoginHandler;
import xinshuyuan.com.wrongtitlebook.Persenter.workRunnable.CheckLoginRunnable;
import xinshuyuan.com.wrongtitlebook.R;

public class Welcome extends Activity {
    private LoginHandler loginHandler;
    private static int smallestScreenWidth ;
    private TextView openset;
    private WebView welcome_web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window=getWindow();
        //屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        WindowManager.LayoutParams layoutParams= window.getAttributes();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        //获取密度
        getDesci();

        welcome_web= (WebView) findViewById(R.id.welcome_web);
        inintWeb(welcome_web);

        loginHandler=new LoginHandler(Welcome.this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new CheckLoginRunnable(this)).start();
    }

    private void inintWeb(WebView welcome_web) {
        //声明WebSettings子类
        WebSettings webSettings = welcome_web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        welcome_web.setBackgroundColor(0);
// 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
// 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        String url="file:///android_asset/welcome.html";
        welcome_web.loadUrl(url);
    }

    private void getDesci() {

        Configuration config = getResources().getConfiguration();
        //获取宽度
        smallestScreenWidth = config.smallestScreenWidthDp;
        XSYTools.pingmucc=smallestScreenWidth;
        System.out.println("smallestScreenWidth=="+smallestScreenWidth);
        //获取密度
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width=metrics.widthPixels;
        int height=metrics.heightPixels;
        //获取密度
        float densitty=metrics.density;
        int densitydpi=metrics.densityDpi;
        XSYTools.densityDpi=densitydpi;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        startActivity(new Intent(this,SetActivity.class));

        return true;
    }
}
