package com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.AnswerEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.SelectButton;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.WebViewActivity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;
import com.xinshuyuan.xinshuyuanworkandexercise.R;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.BaseFragment;
import com.xinshuyuan.xinshuyuanworkandexercise.presenter.evenlisting.SingSelectOnClickListener;
import com.xinshuyuan.xinshuyuanworkandexercise.presenter.evenlisting.SingSelectOptionClickListener;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 练习的单选题，有提交的
 * Created by Administrator on 2017/6/21.
 */

public class WorkExerciseSelsctFragment extends BaseFragment {
    //集中式
    private Integer itemType=1;
    private TestEntity testEntity=null;
    private View mView;
    private Map<String, String> answerMap=new HashMap<String,String>();
    private Document document;
    private ScrollView singScroview;
    private Integer scrollLen;
    private Long hh;
    private Activity layering_practice_activity;;
    private Handler layering_practiceHandler;
    private String testId;
    private String Model;

    public WorkExerciseSelsctFragment(){}
    public WorkExerciseSelsctFragment(Handler layering_practiceHandler, Activity layering_practice_activity) {
        super();
        this.layering_practiceHandler=layering_practiceHandler;
        this.layering_practice_activity=layering_practice_activity;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        //这句话
        testEntity=(TestEntity) getArguments().getSerializable("testEntity");
        testId=getArguments().getString("testId");
        Model=getArguments().getString("Model");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView= inflater.inflate(R.layout.layout_work_exercise_singselect, container,false);
        try {
            initTest();
            initView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mView;
    }


    private void initView(){
        ImageButton ib=this.getViewById(mView,R.id.singselect_submit_btn);
        if(ib!=null){
            ib.setOnClickListener(new SingSelectOnClickListener(this,testEntity.getTestId(),layering_practiceHandler,Model));
            ib.setBackgroundResource(R.drawable.commit_button_background);
        }
    }

    @SuppressLint("NewApi")
    private void initTest() throws JSONException{
        if( XSYTools.isNull(testEntity)){
            XSYTools.showToastmsg(getActivity(),"初始化错误");
            return;
        }
        //赋值正常，继续
        //集中式和分布式的区别是：1、集中式不显示题干，仅显示可选项或答案填写区，分布式则得显示题干；
        //2、集中式无法切换到下一题或上一题，全靠服务器推送，分布式可自主选择题目
        String itemPoint=testEntity.getPoint();
        document = Jsoup.parse(itemPoint);
        itemPoint=document.html();
        itemPoint="<span style=\"color:red;font-weight:bold;\">[单选题]</span>"+itemPoint;
        Log.d("html", itemPoint);
        //设置题干显示相关属性，以及相关node的格式化操作
        WebView wv=this.getViewById(mView, R.id.singselect_itemPoint);
        wv.getSettings().setMediaPlaybackRequiresUserGesture(false);
        initWebView(wv);
        // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
        wv.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                WebView.HitTestResult hit = view.getHitTestResult();
                if(hit!=null){
                    int hitType=hit.getType();
                    //如果是一个url超链接转到默认浏览器
                    if (hitType == WebView.HitTestResult.SRC_ANCHOR_TYPE||hitType == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
//					 Intent i = new Intent(Intent.ACTION_VIEW);
//					 i.setData(Uri.parse(url));
//					 startActivity(i);
                        //转到activity播放
                        Intent intent=new Intent(getActivity(),WebViewActivity.class);
                        intent.putExtra("data", url);
                        getActivity().startActivity(intent);

                        //转到dialog
//					 UriView_dialog uri_dialog=new UriView_dialog(getActivity());
//					 uri_dialog.StartUri(url);
//					 uri_dialog.show();

                    }else{
                        // view.loadUrl(url);
                    }
                }
                return true;
            }
        });


        //设置字体
        wv.getSettings().setDefaultFontSize(25);
//	    WebSettings settings=wv.getSettings();
//	    settings.setTextSize(TextSize.LARGER);
        wv.loadDataWithBaseURL(null, itemPoint, "text/html", "utf-8", null);

        //题干显示完成
        //该显示选项了
        loadOption();
        //选项显示完成，集中式和分布式都在itemType中
    }
    @SuppressLint("NewApi")
    private void initWebView(WebView wv){
        //wv.setDownloadListener(new MyWebViewDownLoadListener(getActivity()));
        //设置WebView是否从网络加载资源，Application需要设置访问网络权限，否则报异常
        wv.getSettings().setJavaScriptEnabled(true);
        //wv.setWebChromeClient(new WebChromeClient());
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
    //显示选项
    @SuppressLint("DefaultLocale")
    private void loadOption() throws JSONException{
        LinearLayout ll=this.getViewById(mView, R.id.singselect_blank_container);//横排
        Log.d("viewCount", String.valueOf(ll.getChildCount()));
        if(ll.getChildCount()>0){
            ll.removeAllViews();
        }
        List<AnswerEntity> ansList=this.testEntity.getAnswerList();
        if(ansList==null || ansList.size()==0){
            XSYTools.showToastmsg(getActivity(),"没有答案可做");
            return;
        }

        //选项id
        for(AnswerEntity ans:ansList){
            Long answerId = (long) 0;

            //弄一些竖排的linearLayout来显示选项
            LinearLayout subll=new LinearLayout(this.getActivity());
            subll.setOrientation(LinearLayout.HORIZONTAL);
            //选项分两部分，一部分是选项标题（如A,B,C,D），一部分是答案，选项标题用button实现，答案用webview实现
            //就这么愉快的定了

            //左右放置 textview 和屏幕隔开
            TextView text=new TextView(this.getActivity());
            text.setWidth(XSYTools.dip2px(this.getActivity(), 20));
            subll.addView(text);
            SelectButton btn=new SelectButton(this.getActivity());
            btn.setText(ans.getOptionTitle());
            btn.setOptionId(ans.getId());
            System.out.println("ans.getId()"+ans.getId());
            hh=ans.getId();
            System.out.println(hh);
            btn.setBackgroundResource(R.drawable.sing_startbuttonbackground);
            btn.setWidth(XSYTools.dip2px(this.getActivity(), 45));
            btn.setHeight(XSYTools.dip2px(this.getActivity(), 45));
            //初始化事件,稍后再做
            //绑定事件
            btn.setOnClickListener(new SingSelectOptionClickListener(this));
            WebView ansWv=new WebView(this.getActivity());
            initWebView(ansWv);
            //设置字体
            ansWv.getSettings().setDefaultFontSize(19);
//			WebSettings settings=ansWv.getSettings();
//			settings.setTextSize(TextSize.NORMAL);
            String ansString=ans.getOptionAnswer();
            ansString=ansString.toLowerCase().trim().replace("<br />", "&nbsp;").replace("<br/>", "&nbsp;").replace("<br>", "&nbsp;");
            ansWv.loadDataWithBaseURL(null, ansString,"text/html", "utf-8", null);
            //初始化事件,稍后再做
            //没有事件
            //如果做了题找出那个选项，遍历试题id
            //得到答案id集合
//            List<Long> ansidList =AnswerStore.keylist();
//            for(int ii=0;ii<ansidList.size();ii++){
//                Long testIddd=ansidList.get(ii);
//                //如果试题id相同，说明有答案
//                if(this.testEntity.getTestId().equals(testIddd)){
//                    String ansjson=AnswerStore.getAllTestanswerMap().get(testIddd);
//                    //得到jsonshuju
//                    JSONObject a = new JSONObject (ansjson);
//                    JSONArray array=a.getJSONArray("answers");
//                    for(int j=0;j<array.length();j++){
//                        String ss=array.getJSONObject(j).getString("id");
//                        ss=ss.replace(GlobalVarDefine.SUBMIT_TEST_ID_PREFIX,"");
//                        ss=ss.trim();
//                        //得到那个已经做了的选项id
//                        answerId=Long.valueOf(ss);
//                        //如果选项id相同则放入状态和
//                        if(answerId.equals(btn.getOptionId())){
//                            btn.setBackgroundResource(R.drawable.multi_select_option_button_shape_selected);
//                            btn.setState(btn.getState()+1);
//                            fillAnswerToMap(btn.getOptionId().toString(), "");
//                        }
//                    }
//                }
//            }


            subll.addView(btn);
            subll.addView(ansWv);
            ll.addView(subll);
            //上下放置textview把他们隔开
            TextView text2=new TextView(this.getActivity());
            text2.setHeight(XSYTools.dip2px(this.getActivity(), 10));
            text.setWidth(XSYTools.dip2px(this.getActivity(), 30));
            ll.addView(text2);

        }
    }
    public void clearAnswerMap(){
        this.getAnswerMap().clear();
    }
    //返回答按
    public Map<String, String> getAnswerMap() {
        return answerMap;
    }
    public void fillAnswerToMap(String id,String value) {
        Log.d("fillAnswer", "添加试题答案@"+id+"  "+value);
        this.getAnswerMap().put(id, value);
    }
    public void removeAnswerToMap(String id) {
        Log.d("fillAnswer", "移除试题答案@"+id);
        this.getAnswerMap().remove(id);
    }
    public Integer get() {
        return itemType;
    }
    public TestEntity getTestEntity() {
        return testEntity;
    }
    public Integer getItemType() {
        // TODO Auto-generated method stub
        return itemType;
    }



}
