package com.xinshuyuan.xinshuyuanworkandexercise.View.CustomView.ExerciseIncludeCustomLayout;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.AnswerEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.SelectButton;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;
import com.xinshuyuan.xinshuyuanworkandexercise.R;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment.WorkExerciseNESTFragment;
import com.xinshuyuan.xinshuyuanworkandexercise.presenter.evenlisting.IncludeSubYesOrNoSelectOptionClickListener;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017/6/22.
 */

public class WorkExerciseYesOrNoSelectLayout extends IncludeLinearLayout {
    /*
     * testId,这里的ID其实就是嵌套题中的小题的ID了
     */
    private List<AnswerEntity> ansList=null;
    private Context context;
    private LinearLayout mainLayout=null;
    private LinearLayout itemLayout=null;
    private LinearLayout optionsLayout=null;
    private Activity activity;
    private WorkExerciseNESTFragment includeFragment;
    private Map<String, String> answerMap=new HashMap<String,String>();
    public WorkExerciseYesOrNoSelectLayout(Context context, WorkExerciseNESTFragment includeFragment) {
        super(context);
        this.includeFragment=includeFragment;
        this.context=context;
        mainLayout=new LinearLayout(context);
        initMainLayout();
        activity=(Activity)context;
    }

    private void initMainLayout(){
        if(mainLayout==null)
            return;
        mainLayout.setOrientation(LinearLayout.VERTICAL);//主布局采用多行排列，然后在主布局中分别实现第一行 题干，第二行可选项
        itemLayout=new LinearLayout(context);
        itemLayout.setWeightSum(8);
        optionsLayout=new LinearLayout(context);
        LinearLayout.LayoutParams lps1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lps1.leftMargin=30;
        optionsLayout.setLayoutParams(lps1);
        optionsLayout.setWeightSum(2);
        mainLayout.addView(itemLayout);
        mainLayout.addView(optionsLayout);
        this.addView(mainLayout);
    }
    public void initOptions(List<AnswerEntity> ansList) {
        this.ansList=ansList;
        try {
            initOptions();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void initOptions() throws JSONException{
        if(this.ansList==null){
            return;
        }
        LinearLayout entitysLayout=new LinearLayout(context);
        entitysLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lps=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //	lps.topMargin=40;
//		lps.leftMargin=30;
        entitysLayout.setLayoutParams(lps);
        for(AnswerEntity ans:ansList){
            LinearLayout entityLayout=new LinearLayout(context);
            entityLayout.setWeightSum(2.5f);
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.topMargin=40;
//			lp.leftMargin=30;
            entityLayout.setLayoutParams(lp);
            entityLayout.setOrientation(LinearLayout.HORIZONTAL);
            SelectButton btn=new SelectButton(context);
            btn.setText(ans.getOptionTitle());
            btn.setOptionId(ans.getId());//记录下选项的Id
            btn.setBackgroundResource(R.drawable.sing_startbuttonbackground);
            btn.setWidth(XSYTools.dip2px(context, 40));
            btn.setHeight(XSYTools.dip2px(context, 40));
            btn.setOnClickListener(new IncludeSubYesOrNoSelectOptionClickListener(this));
            WebView ansWv=new WebView(context);
            initWebView(ansWv);//像大众标
            ansWv.loadDataWithBaseURL(null, ans.getOptionAnswer(),"text/html", "utf-8", null);



            entityLayout.addView(btn);
            entityLayout.addView(ansWv);
//			entityLayout.setBackgroundResource(R.drawable.fillblank_smallshape);
            entitysLayout.addView(entityLayout);
        }
        optionsLayout.addView(entitysLayout);
    }
    private void initWebView(WebView wv){
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebChromeClient(new WebChromeClient());
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int scale = dm.densityDpi;
        if (scale == 240) { //
            wv.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (scale == 160) {
            wv.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else {
            wv.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        }
        //下面的这段是为了防止网页中的部分按钮点击无响应而设置的,原因不详
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result)
            {
                return super.onJsAlert(view, url, message, result);
            }
        });
    }

    public void initItemPoint(TestEntity te, int options) {
        //把序号转成字符型
        String title=String.valueOf(options);
        title=title+"、";
        String itemPoint=te.getPoint();
        title=title+itemPoint;
        Document document = Jsoup.parse(title);
        itemPoint=document.html();
        //设置题干显示相关属性，以及相关node的格式化操作
        WebView wv=new WebView(context);
        // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
        wv.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                WebView.HitTestResult hit = view.getHitTestResult();
                if(hit!=null){
                    int hitType=hit.getType();
                    //如果是一个url超链接转到默认浏览器
                    if (hitType == WebView.HitTestResult.SRC_ANCHOR_TYPE||hitType == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
//										 Intent i = new Intent(Intent.ACTION_VIEW);
//										 i.setData(Uri.parse(url));
//										 startActivity(i);
                        //转到activity播放
//						Intent intent=new Intent(activity,WebViewActivity.class);
//						intent.putExtra("data", url);
//						activity.startActivity(intent);

                        //转到dialog
//										 UriView_dialog uri_dialog=new UriView_dialog(getActivity());
//										 uri_dialog.StartUri(url);
//										 uri_dialog.show();

                    }else{
                        // view.loadUrl(url);
                    }
                }
                return true;
            }
        });



        wv.loadDataWithBaseURL(null, itemPoint, "text/html", "utf-8", null);

        initWebView(wv);
        itemLayout.addView(wv);
        this.setTestId(te.getTestId());
        this.setTestType(te.getTestType());
        //题干显示完成
    }

    public Map<String, String> getAnswerMap() {
        return answerMap;
    }

    public void setAnswerMap(Map<String, String> answerMap) {
        this.answerMap = answerMap;
    }

    public void addAnswer(String testId){
        Map<String, String> map=getAnswerMap();
        if(map==null){
            map=new HashMap<String, String>();
        }
        map.put(testId, testId);
    }
    public void removeAnswer(String testId){
        Map<String, String> map=getAnswerMap();
        if(map==null){
            map=new HashMap<String, String>();
        }
        map.remove(testId);
    }
}
