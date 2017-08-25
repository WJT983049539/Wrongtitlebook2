package xinshuyuan.com.wrongtitlebook.View.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;

import xinshuyuan.com.wrongtitlebook.Model.Common.XSYTools;
import xinshuyuan.com.wrongtitlebook.R;

/**
 * Created by Administrator on 2017/6/19.
 */

public class CustomUpTestFragment extends Fragment{
        private View mView;
        private WebView webview;
        private TestEntity testEntity;
        private String impoint;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.custom_up_layout,container,false);
        inint();
        XSYTools.i("怎么跑到上传的fragment");
        return mView;
    }

    private void inint() {
        impoint=testEntity.getPoint();
        webview=(WebView)mView.findViewById(R.id.custom_point_webView);
        initWebView();
        webview.loadUrl(impoint);
    }

    private void initWebView() {
        webview.getSettings().setBlockNetworkImage(false);//解决图片不显示
        webview.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });

        webview.getSettings().setDefaultTextEncodingName("utf-8") ;
        webview.setBackgroundColor(0); // 设置背景色
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setInitialScale(56);
        //支持屏幕缩放
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);
        //不显示webview缩放按钮
        webview.getSettings().setDisplayZoomControls(false);
        webview.setWebChromeClient(new WebChromeClient());
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int scale = dm.densityDpi;
        if (scale == 240) { //
            webview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (scale == 160) {
            webview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else {
            webview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        }
        //下面的这段是为了防止网页中的部分按钮点击无响应而设置的,原因不详
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result)
            {
                return super.onJsAlert(view, url, message, result);
            }
        });
    }

    public void setInfo(TestEntity testEntity) {
        this.testEntity=testEntity;
    }
}
