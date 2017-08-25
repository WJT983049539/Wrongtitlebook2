package xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.ibpd.xsy.varDefine.GlobalVarDefine;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.AnswerEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.GetUserInfoClass;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;
import xinshuyuan.com.wrongtitlebook.Model.Common.Common;
import xinshuyuan.com.wrongtitlebook.Model.Common.SideIndexGestureListener;
import xinshuyuan.com.wrongtitlebook.Model.Common.XsyMap;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.ExerciseIncludeCustomLayout.ExerciseFillBlankLayout;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.ExerciseIncludeCustomLayout.ExerciseMultiSelectLayout;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.ExerciseIncludeCustomLayout.ExerciseSingleSelectLayout;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.ExerciseIncludeCustomLayout.ExerciseYesOrNoSelectLayout;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.KeyBord.DigitKeyPadUtil;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.PreViewIncludeCustomLayout.IncludeLinearLayout;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.TestEditText;
import xinshuyuan.com.wrongtitlebook.Persenter.Handler.ExerciseHandler;
import xinshuyuan.com.wrongtitlebook.Persenter.evenListener.IncludeSubmitOnClickListener;
import xinshuyuan.com.wrongtitlebook.R;
import xinshuyuan.com.wrongtitlebook.View.Activity.ExerciseTestActivity;
import xinshuyuan.com.wrongtitlebook.View.Activity.WebViewActivity;
import xinshuyuan.com.wrongtitlebook.View.Fragment.BaseFragment;

/**
 * Created by Administrator on 2017/6/21.
 */

public class ExerciseNESTFragment extends BaseFragment {
    private ExerciseHandler exerciseHandler;
    private ExerciseTestActivity exerciseTestActivity;


    private ExerciseTestActivity.MyOnTouchListener myOnTouchListener;
    private GestureDetector mGestureDetector;
    private ScrollView includeScroview;
    private Integer scrollLen;
    private  static int  options=1;
    private Integer itemType;
    private TestEntity testEntity=null;
    private View mView;
    public View getmView() {
        return mView;
    }
    public void setmView(View mView) {
        this.mView = mView;
    }
    private Map<String, String> answerMap=new HashMap<String,String>();
    private Document document;
    /**
     * mslList的作用是存放本页面当中所有的多选题小题，等需要归结答案的时候，可以用这个list来归结答案
     */
    private List<ExerciseMultiSelectLayout> mslList=new ArrayList<ExerciseMultiSelectLayout>();
    /**
     * sslList的作用是存放本页面当中所有的单选题小题，等需要归结答案的时候，可以用这个list来归结答案
     */
    private List<ExerciseSingleSelectLayout> sslList=new ArrayList<ExerciseSingleSelectLayout>();
    /**
     * yonslList的作用是存放本页面当中所有的选择题小题，等需要归结答案的时候，可以用这个list来归结答案
     */
    private List<ExerciseYesOrNoSelectLayout> yonslList=new ArrayList<ExerciseYesOrNoSelectLayout>();
    /**
     * yonslList的作用是存放本页面当中所有的选择题小题，等需要归结答案的时候，可以用这个list来归结答案
     */
    private List<ExerciseFillBlankLayout> fblList=new ArrayList<ExerciseFillBlankLayout>();
    public ExerciseNESTFragment(ExerciseHandler exerciseHandler, ExerciseTestActivity exerciseTestActivity){

        this.exerciseHandler=exerciseHandler;
        this.exerciseTestActivity=exerciseTestActivity;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        //得到答题类型
        initoptions();
        DigitKeyPadUtil.hidePopupWindow();
        testEntity=(TestEntity) getArguments().getSerializable("testEntity");
        super.onCreate(savedInstanceState);
    }

    private void initoptions() {
        options=1;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView= inflater.inflate(R.layout.layout_exercise_include, container,false);
        mGestureDetector = new GestureDetector(getActivity(),new SideIndexGestureListener());
        try {
            initTest();
            initView();

            //给fragment注册监听事件
            myOnTouchListener = new ExerciseTestActivity.MyOnTouchListener() {

                @Override
                public boolean onTouch(MotionEvent ev) {
                    boolean result = mGestureDetector.onTouchEvent(ev);
                    if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                        View view = getActivity().getCurrentFocus();
                        if (isHideInput(view, ev)) {
                            HideSoftInput(view.getWindowToken());
                        }
                    }

                    return result;
                }

            };
            ((ExerciseTestActivity) getActivity()).registerMyOnTouchListener(myOnTouchListener);
            //如果输入法出来

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mView;
    }


    private void initView(){
        ImageButton ib=this.getViewById(mView,R.id.include_submit_btn);
        if(ib!=null){
            ib.setOnClickListener(new IncludeSubmitOnClickListener(this,exerciseHandler));
            ib.setBackgroundResource(R.drawable.commit_button_background);
        }
    }

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
        itemPoint="<span style=\"color:red;font-weight:bold;\">[嵌套题]</span>"+itemPoint;

        Log.d("html", itemPoint);
        //设置题干显示相关属性，以及相关node的格式化操作
        WebView wv=this.getViewById(mView, R.id.include_itemPoint);

        // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
        wv.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                WebView.HitTestResult hit = view.getHitTestResult();
                if(hit!=null){
                    int hitType=hit.getType();
                    //如果是一个url超链接转到默认浏览器
                    if (hitType == WebView.HitTestResult.SRC_ANCHOR_TYPE||hitType == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
//							 Intent i = new Intent(Intent.ACTION_VIEW);
//							 i.setData(Uri.parse(url));
//							 startActivity(i);
                        //转到activity播放
                        Intent intent=new Intent(getActivity(),WebViewActivity.class);
                        intent.putExtra("data", url);
                        getActivity().startActivity(intent);


                    }else{
                        // view.loadUrl(url);
                    }
                }
                return true;
            }
        });



        wv.loadDataWithBaseURL(null, itemPoint, "text/html", "utf-8", null);
        initWebView(wv);
        //题干显示完成
//		//该显示选项了
        loadOption();
        //选项显示完成
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
    //显示选项
    @SuppressWarnings("unused")
    private void loadOption(){

        LinearLayout ll=this.getViewById(mView, R.id.include_linear_left_middle);//横排
        if(ll.getChildCount()>0){
            ll.removeAllViews();
        }
        List<AnswerEntity> ansList=this.testEntity.getAnswerList();
        ansList=ansList==null?new ArrayList<AnswerEntity>():ansList;
        //除了填空和操作（嵌套不考虑）,其他都是有可选项的
//		if((ansList==null || ansList.size()==0) && !testEntity.getTestType().equals(GlobalVarDefine.TESTTYPE_FILL_BLANK)  && !testEntity.getTestType().equals(GlobalVarDefine.TESTTYPE_OPRATE)){
//			comm.toast("没有答案可做");
//			return;
//		}
        if(this.testEntity==null){
            XSYTools.showToastmsg(getActivity(),"没有试题可做");
            return;
        }
        //分析答案，获取小题信息
        for(AnswerEntity ans:ansList){
            //获取试题信息，然后再根据试题类型初始化出不同的效果
            String ansVal=ans.getOptionAnswer();
            //得到选项，要传的只
            // options=ans.getOptionTitle();
            if(XSYTools.isEmpty(ansVal)){
                continue;
            }
            try {
                //对于嵌套题，答案实体中的optionTitle和id是没有意义的，只有optionAnser中的json数据是有意义的，当然optionTitle的意义在于排序，显示顺序
                JSONArray a=new JSONArray(ansVal);
                if(a==null || a.length()==0){
                    continue;
                }
                JSONObject o=a.getJSONObject(0);
                if(o==null){
                    continue;
                }
                //得到id
                String sId=XSYTools.getStringValFromJSONObject(o, "id");
                if(!XSYTools.isEmpty(sId) && XSYTools.isNumeric(sId)){


                    XsyMap paramsMap=getParamMap(Long.valueOf(sId));
                    String url= XSYTools.getWrongUrl(GlobalVarDefine.GET_TESTENTITY_URL,getActivity());
                    OkGo.post(url).params(paramsMap).execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {

                            //下面的这个变量就是获取到的试题的json数据,包含了试题的基本信息和可选项等
                            if(XSYTools.isEmpty(s.trim()) || s.equals("{}")){
                                XSYTools.showToastmsg(getActivity(),"获取的试题信息为空");
                                return ;
                            }
                            try {
                                JSONObject testJson=new JSONObject(s);
                                if(testJson.has("status") && testJson.has("msg")){
                                    return;
                                }
                                String id=XSYTools.getStringValFromJSONObject(testJson, "id");
                                String itemPoint=XSYTools.getStringValFromJSONObject(testJson, "itemPoint");
                                String itemType=XSYTools.getStringValFromJSONObject(testJson, "itemType");
                                String subject=XSYTools.getStringValFromJSONObject(testJson, "subject");
                                JSONArray answerList=(JSONArray) XSYTools.getValFromJSONObject(testJson, "answerList");
                                answerList=answerList==null?new JSONArray():answerList;
                                Log.d("testEntity", "itemPoint="+itemPoint+"\titemType="+itemType+"\tsubject="+subject+"\tanswerList="+answerList.length());

                                //获取到之后，就该给activity传了
                                if(XSYTools.isNumeric(id) && XSYTools.isNumeric(itemType)){
                                    TestEntity te=new TestEntity();
                                    te.setPoint(itemPoint);
                                    te.setSubject(subject);
                                    te.setTestId(Long.valueOf(id));
                                    te.setTestType(Integer.valueOf(itemType));
                                    for(Integer i=0;i<answerList.length();i++){
                                        JSONObject o=answerList.getJSONObject(i);
                                        if(o!=null){
                                            Boolean isRealAnswer=(Boolean) XSYTools.getValFromJSONObject(o, "isRealAnswer");
                                            String optionTitle=XSYTools.getStringValFromJSONObject(o, "optionTitle");
                                            String optionAnswer=XSYTools.getStringValFromJSONObject(o, "optionAnswer");
                                            String optionId=XSYTools.getStringValFromJSONObject(o, "id");
                                            AnswerEntity ae=new AnswerEntity();
                                            ae.setIsRealAnswer(isRealAnswer);
                                            ae.setOptionAnswer(optionAnswer);
                                            ae.setOptionTitle(optionTitle);
                                            ae.setTestId(te.getTestId());
                                            if(XSYTools.isNumeric(optionId)){
                                                ae.setId(Long.valueOf(optionId));
                                            }
                                            te.fillAnser(ae);
                                        }
                                    }
                                    //message中的obj组合完了，剩下就该传过去了


                                    exerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.VIEW_INCLUDTEST_SUBTEST,te));

                                }else{
                                    Log.e("testInfo", "id["+id+"]或itemType["+itemType+"]无法转换为数字");
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            XSYTools.i(e.toString());
                        }
                    });
                }

                //到这里，基本就告一段落了，剩下的工作交给回调调用的函数部分完成 函数名:doInitAnsInfo;
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    /**
     * 供回调调用的函数，用来真正初始化可选项选区
     * @param te
     */
    public void doInitAnsInfo(TestEntity te){


        if(te==null){
            return;
        }
        ///下面这段主要是为了防止同一试题多次出现，因为服务器端为了保证客户端能最大可能收到试题，会发送多次同一试题的广播
        boolean t=false;
        List<View> views= XSYTools.getAllChildViews(this.getActivity());
        for(View view:views){
            int i=1;
            if(view instanceof IncludeLinearLayout){
                IncludeLinearLayout sb=(IncludeLinearLayout) view;
                if(sb.getTestId().equals(te.getTestId())){
                    t=true;
                    break;
                }
            }
        }
        if(t){
            return;
        }
        if(te.getTestType().equals(GlobalVarDefine.TESTTYPE_MULTI_SELECT)){//多选题
            ExerciseMultiSelectLayout msl=new ExerciseMultiSelectLayout(this.getActivity(),this);
            msl.setWeightSum(2);
            LinearLayout ll=this.getViewById(mView, R.id.include_linear_left_middle);//横排
            if(ll==null){
                return;
            }
            ll.addView(msl);
            msl.initItemPoint(te,options);
            msl.initOptions(te.getAnswerList());
            this.addMslToList(msl);
        }else if(te.getTestType().equals(GlobalVarDefine.TESTTYPE_SINGLE_SELECT)){//单选题
            ExerciseSingleSelectLayout ssl=new ExerciseSingleSelectLayout(this.getActivity(),this);
            ssl.setWeightSum(2);
            LinearLayout ll=this.getViewById(mView, R.id.include_linear_left_middle);//横排
            if(ll==null){
                return;
            }
            ll.addView(ssl);
            try {
                ssl.initItemPoint(te,options);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ssl.initOptions(te.getAnswerList());
            this.addSslToList(ssl);
        }else if(te.getTestType().equals(GlobalVarDefine.TESTTYPE_YESORNO_SELECT)){//判断题
            ExerciseYesOrNoSelectLayout ssl=new ExerciseYesOrNoSelectLayout(this.getActivity(),this);
            ssl.setWeightSum(2);
            LinearLayout ll=this.getViewById(mView, R.id.include_linear_left_middle);//横排
            if(ll==null){
                return;
            }
            ll.addView(ssl);
            ssl.initItemPoint(te,options);
            ssl.initOptions(te.getAnswerList());
            this.addYonslToList(ssl);
        }else if(te.getTestType().equals(GlobalVarDefine.TESTTYPE_FILL_BLANK)){//填空题
            ExerciseFillBlankLayout fbl=new ExerciseFillBlankLayout(this.getActivity(),this,mView);
            fbl.setWeightSum(2);
            LinearLayout ll=this.getViewById(mView, R.id.include_linear_left_middle);//横排
            if(ll==null){
                return;
            }

            //fbl.setListener(new InputWindowListener(ll,this));



            ll.addView(fbl);

            fbl.initItemPoint(te,options);
            try {
                fbl.initOptions(te);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            this.addFblToList(fbl);
        }
        options=options+1;

    }
    private XsyMap getParamMap(Long testId){
        XsyMap<String,String> paramsMap=XsyMap.getInterface();
        Long studId= new GetUserInfoClass(getActivity()).getUserId();
        paramsMap.put(GlobalVarDefine.PARAM_USERTYPE, GlobalVarDefine.USERTYPE_STUDENT.toString());
        paramsMap.put(GlobalVarDefine.PARAM_USERID,String.valueOf(studId));
        paramsMap.put(GlobalVarDefine.PARAM_TESTID, testId.toString());
        return paramsMap;
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


    //拿到装各种布局的父布局
    public LinearLayout getlll(){
        return (LinearLayout) mView.findViewById(R.id.include_linear_left_middle);
    }




    public TestEntity getTestEntity() {
        return testEntity;
    }
    public Integer getItemType() {
        // TODO Auto-generated method stub
        return itemType;
    }
    public List<ExerciseMultiSelectLayout> getMslList() {
        return mslList;
    }
    public void addMslToList(ExerciseMultiSelectLayout msl) {
        getMslList().add(msl);
    }
    public List<ExerciseSingleSelectLayout> getSslList() {
        return sslList;
    }
    public void addSslToList(ExerciseSingleSelectLayout ssl) {
        getSslList().add(ssl);
    }
    public List<ExerciseYesOrNoSelectLayout> getYonslList() {
        return yonslList;
    }
    public void addYonslToList(ExerciseYesOrNoSelectLayout ssl) {
        getYonslList().add(ssl);
    }
    public List<ExerciseFillBlankLayout> getFblList() {
        return fblList;
    }
    public void addFblToList(ExerciseFillBlankLayout fbl) {
        getFblList().add(fbl);
    }

    // 隐藏软键盘
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    // 判定是否需要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof TestEditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        ((ExerciseTestActivity) getActivity()).unregisterMyOnTouchListener(myOnTouchListener);
        super.onDestroyView();
    }



}
