package xinshuyuan.com.wrongtitlebook.View.Activity;

import android.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;

import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

import xinshuyuan.com.wrongtitlebook.Model.Common.XSYTools;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.FixGridLayout;
import xinshuyuan.com.wrongtitlebook.Model.PictureButton;
import xinshuyuan.com.wrongtitlebook.R;

/**
 * 自定义拍照单选
 * Created by Administrator on 2017/6/19.
 */

public class CustomSelectFragment extends Fragment{
private TestEntity info;


    private View Mview;
    private WebView webview;
    private Document document;
    private String testImageurl ;
    private int testOptionNum2;
    private String testId;
    private String studentIds;
    private int testType;
    private Integer itemType;
    private String a[]={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","S","Y","Z"};
    private FixGridLayout ButtonLinearlayout;
    private ImageButton imagebutton;
    private LinearLayout subll;
    private LinearLayout waiSubll;
    private Map<Integer,String>list=new HashMap<Integer,String>();
    @Override
    public void onCreate(Bundle savedInstanceState) {


        testImageurl = getArguments().getString("testImageurl");
        testOptionNum2=getArguments().getInt("testOptionNum2");
        testId=getArguments().getString("testId");
        studentIds=getArguments().getString("studentIds");
        testType=getArguments().getInt("testType");
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Mview=inflater.inflate(R.layout.picture_test_layout, container,false);
        inintview();
        xianshineirong();
        return Mview;
    }



    //显示内容
    private void xianshineirong() {

        ButtonLinearlayout =(FixGridLayout)Mview.findViewById(R.id.picture_test_blank_container);
        ButtonLinearlayout.setmCellHeight(XSYTools.dip2px(getActivity(), 100));
        ButtonLinearlayout.setmCellWidth(XSYTools.dip2px(getActivity(), 100));
        //加载button
        for(int i=0;i<testOptionNum2;i++){
            waiSubll=new LinearLayout(this.getActivity());
            waiSubll.setOrientation(LinearLayout.HORIZONTAL);
            subll=new LinearLayout(this.getActivity());
            subll.setOrientation(LinearLayout.HORIZONTAL);
            TextView textview1=new TextView(this.getActivity());
            textview1.setWidth(XSYTools.dip2px(this.getActivity(), 30));
            textview1.setHeight(XSYTools.dip2px(this.getActivity(), 30));
            PictureButton btn=new PictureButton(this.getActivity());
            btn.setId(i);
            btn.setOptionId(i);
            btn.setBackgroundResource(R.drawable.sing_startbuttonbackground);
            btn.setWidth(XSYTools.dip2px(this.getActivity(), 60));
            btn.setHeight(XSYTools.dip2px(this.getActivity(), 60));
            btn.setText(a[i]);
            TextView textview2=new TextView(this.getActivity());
            textview2.setWidth(XSYTools.dip2px(this.getActivity(), 30));
            textview2.setHeight(XSYTools.dip2px(this.getActivity(), 30));
            subll.addView(textview2);
            subll.addView(btn);
            waiSubll.addView(subll);
            ButtonLinearlayout.addView(waiSubll);
            ButtonLinearlayout.addView(textview1);
        }

    }

    private void inintview() {
        //标题
        webview =(WebView)Mview.findViewById(R.id.picture_test_web_neirong);
        webview.getSettings().setBlockNetworkImage(false);
        initWebView(webview);
        webview.loadUrl(testImageurl);

    }

    //初始化webview
    private void initWebView(WebView itemwebview2) {
        //wv.setDownloadListener(new MyWebViewDownLoadListener(getActivity()));
        //设置WebView是否从网络加载资源，Application需要设置访问网络权限，否则报异常
        itemwebview2.getSettings().setJavaScriptEnabled(true);
        WebSettings mWebSettings = itemwebview2.getSettings();
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        //wv.setWebChromeClient(new WebChromeClient());
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int scale = dm.densityDpi;
        if (scale == 240) { //
            itemwebview2.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (scale == 160) {
            itemwebview2.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else {
            itemwebview2.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        }

        //下面的这段是为了防止网页中的部分按钮点击无响应而设置的,原因不详
        itemwebview2.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result)
            {
                return super.onJsAlert(view, url, message, result);
            }
        });

    }


    public void fillAnswerToMap(int  di,String str) {
        list.put(di,str);
    }


    public void removeAnswerToMap(int id1) {
        list.remove(id1);
    }


    public Map<Integer, String> getAnswerMap() {
        return this.list;
    }


    public void setInfo(TestEntity info) {
        this.info = info;
    }
}
