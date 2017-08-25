package com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.ibpd.xsy.varDefine.GlobalVarDefine;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.AnswerEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.Common;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.CustomDialog;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.PerferenceService;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.WebViewActivity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XsyMap;
import com.xinshuyuan.xinshuyuanworkandexercise.R;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.BaseFragment;
import com.xinshuyuan.xinshuyuanworkandexercise.presenter.evenlisting.DaubOnClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import work.HomeWorkConstantClass;

/**
 * Created by Administrator on 2017/6/21.
 */

public class WorkExerciseDaubFragment extends BaseFragment {
    private String flagg;
    private Integer itemType;
    private WebView ansWv;
    private Handler exerciseHandler;
    private Activity exerciseTestActivity;
    private String Model;

    public WorkExerciseDaubFragment(Handler exerciseHandler, Activity exerciseTestActivity) {
        super();
        this.exerciseHandler=exerciseHandler;
        this.exerciseTestActivity=exerciseTestActivity;
    }


    public Integer getItemType() {
        return itemType;
    }
    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }
    private TestEntity testEntity;
    public TestEntity getTestEntity() {
        return testEntity;
    }
    public void setTestEntity(TestEntity testEntity) {
        this.testEntity = testEntity;
    }
    private View mView;
    public View getmView() {
        return mView;
    }
    private Document document;
    private String ansValue="";
    public WorkExerciseDaubFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        DigitKeyPadUtil.hidePopupWindow();
//        itemType=TestCommon.getDoTestType();
        testEntity=(TestEntity) getArguments().getSerializable("testEntity");
        Model=getArguments().getString("Model");
        flagg=Model;
        super.onCreate(savedInstanceState);
    }
    private void initView(){

        ImageButton ib=this.getViewById(mView, R.id.daub_submit_btn);
        if(ib!=null){
            ib.setOnClickListener(new DaubOnClickListener(this,ansWv));
            ib.setBackgroundResource(R.drawable.commit_button_background);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView= inflater.inflate(R.layout.layout_exercise_daub, container,false);
        ansWv=this.getViewById(mView, R.id.daub_ans);
        try {
            initTest();
            initView();
            //滚动条
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mView;
    }

    private void initTest() throws JSONException{
        if( XSYTools.isNull(testEntity)){
            showTip("初始化错误");
            return;
        }
        //赋值正常，继续
        //集中式和分布式的区别是：1、集中式不显示题干，仅显示可选项或答案填写区，分布式则得显示题干；
        //2、集中式无法切换到下一题或上一题，全靠服务器推送，分布式可自主选择题目
        String itemPoint=testEntity.getPoint();
        document = Jsoup.parse(itemPoint);
        itemPoint=document.html();
        itemPoint="<span style=\"color:red;font-weight:bold;\">[涂抹题]</span>"+itemPoint;
        Log.d("html", itemPoint);
        //设置题干显示相关属性，以及相关node的格式化操作
        WebView wv=this.getViewById(mView, R.id.daub_itemPoint);
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

        wv.loadDataWithBaseURL(null, itemPoint, "text/html", "utf-8", null);
        wv.getSettings().setDefaultFontSize(45);
        initWebView(wv);
        //题干显示完成
        //该显示选项了
        loadOption();
    }
    private void initWebView(WebView wv){
        wv.getSettings().setBlockNetworkImage(false);//解决图片不显示
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);

        //添加回调，,Webv
        wv.addJavascriptInterface(new DaubJSInterface(), "tool");
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


        //没有点击到图片上的提示框
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(final WebView view, String url, String message,
                                     JsResult result)
            {

                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage(message);
                builder.setTitle("提示");
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //view.reload();//重写刷新页面
                                dialog.dismiss();
                            }
                        });
                builder.create(true).show();
                result.confirm();
                return true;
            }
        });
    }
    //加载涂抹试题
    private void loadOption(){

        List<AnswerEntity> ansList=this.testEntity.getAnswerList();
        if(ansList==null || ansList.size()==0){
            XSYTools.showToastmsg(getActivity(),"没有答案可做");
            return;
        }
        for(AnswerEntity ans:ansList){
            //ansWv=this.getViewById(mView, R.id.daub_ans);
            initWebView(ansWv);
            ansWv.loadUrl(XSYTools.getWorkUrl(GlobalVarDefine.DOTEST_DAUB_URL,getActivity())+"?"+ GlobalVarDefine.PARAM_TESTID+"="+ans.getTestId());
        }
    }
    private void showTip(String msg){
        XSYTools.i(msg);
    }
    public void setAnsValue(Object obj) {
        this.ansValue=obj.toString();
        String val=this.getAnsValue();
        if(XSYTools.isEmpty(val)){
            XSYTools.showToastmsg(getActivity(),"请先答题");
            return;
        }

        JSONObject j=new JSONObject();
        TestEntity te=this.getTestEntity();
        if(te!=null){
            try {
                j.put("testId", te.getTestId());
                JSONArray ja=new JSONArray();
                JSONObject sa=new JSONObject();
                sa.put("id", GlobalVarDefine.SUBMIT_TEST_ID_PREFIX+ te.getTestId());
                sa.put("value",val);
                ja.put(sa);
                j.put("answers",ja);
                Log.d("daub data", j.toString());



                //参数组合完成，准备发送
//                TestCommon.submitTestAns(te.getTestId(),j.toString(),itemType,te.getTestType());

                XsyMap<String,String> map=XsyMap.getInterface();
                //列表名
                PerferenceService service =new PerferenceService(getActivity());
                long studId=service.getsharedPre().getLong("WorkId",0);
                map.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(studId));
                //试题id
                map.put(GlobalVarDefine.PARAM_TESTID,String.valueOf(this.testEntity.getTestId()));
                //提交试题的json数据
                map.put(HomeWorkConstantClass.PARAM_STUANSWER,j.toString());
                //答题类型
                map.put(GlobalVarDefine.PARAM_ITEMTYPE,te.getTestType().toString());
                if(flagg.equals("exercise")){
                    map.put(HomeWorkConstantClass.PARAM_STUWORKID,"0");
                    //学生作业id,这个说一律传0
                }else  if(flagg.equals("work")) {
                    map.put(HomeWorkConstantClass.PARAM_STUWORKID,te.getWorkId());

                }else if(flagg.equals("neibuExercise")){
                    map.put(HomeWorkConstantClass.PARAM_STUWORKID,"0");
                }

                OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.JUDGE_URL,exerciseTestActivity)).params(map).execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        XSYTools.i(s);
                        try {
                           JSONObject object=new JSONObject(s);
                            String answerAnalysis=object.getString("answerAnalysis");
                            exerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.ONLY_ANSWER, answerAnalysis+","+flagg));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        XSYTools.i(e.toString());
                    }
                });


            } catch (JSONException e) {
                Log.d("daub data", e.getLocalizedMessage());
                e.printStackTrace();
            }
        }else{
            XSYTools.showToastmsg(getActivity(),"没有试题?");
        }
    }
    public String getAnsValue() {
        return ansValue;
    }
    public void setAnsValues(String ansValue) {
        this.ansValue = ansValue;
    }

    class DaubJSInterface {
        @JavascriptInterface
        public void setAnsVal(String data){
            if(XSYTools.isEmpty(data)){
                return;
            }
            setAnsValue(data);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ansWv.pauseTimers();
    }

    @Override
    public void onResume() {
        super.onResume();
        ansWv.resumeTimers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ansWv.resumeTimers();
    }
}
