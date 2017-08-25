package com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.AnswerEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.Common;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.PerferenceService;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestInfo;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.WorkInfoBean;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XsyMap;
import com.xinshuyuan.xinshuyuanworkandexercise.R;
import com.xinshuyuan.xinshuyuanworkandexercise.presenter.Handler.ProjectWorkShowActivityHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;
import work.HomeWorkConstantClass;

/**
 * 显示试题的fragment
 * Created by Administrator on 2017/7/5.
 */

public class ShowTestListFragment extends Fragment{
    private View mView;
    private Document document;
    private Elements imgs;
    private Elements body;
    private WorkInfoBean workinfobean;
    private ProjectWorkShowActivityHandler projectWorkShowActivityHandler;
    private ListView listView;
    private Long userId;
    private  String  projectId;
    private List<TestInfo> testInfoList=null;
    private ShowTestInfoListAdapter showTestInfoListAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workinfobean= (WorkInfoBean) getArguments().getSerializable("workinfobean");
        PerferenceService service=new PerferenceService(getActivity());
        userId=service.getsharedPre().getLong("WorkId",0);
        projectId= service.getsharedPre().getString("projectId","");
        testInfoList=new ArrayList<TestInfo>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.layout_show_test_list,container,false);
        listView= (ListView) mView.findViewById(R.id.listview_test_list);

        getData();
        return mView;
    }
    //得到数据
    private void getData() {
        Map map= XsyMap.getInterface();
        map.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(userId));
        map.put(HomeWorkConstantClass.PARAM_SUBJECTID,projectId);
        map.put(HomeWorkConstantClass.PARAM_STUWORKID,String.valueOf(workinfobean.getWorkId()));
        OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.TESTLIST_URL,getActivity())).params(map).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                testInfoList.clear();
                XSYTools.i(s);

                try {
                    JSONObject object=new JSONObject(s);
                    JSONArray jsonArray=object.getJSONArray("testList");
                     for(int i=0;i<jsonArray.length();i++){
                         //储存到javabean中
                        TestInfo testInfo=new TestInfo();
                         JSONObject OO=jsonArray.getJSONObject(i);
                         //试题类型
                         Integer testType=OO.getInt("testType");
                         //试题id
                          long testId=OO.getLong("testId");
                         //作业id
                         long stuWorkId=OO.getLong("stuWorkId");
                         //知识点id
                         long knowledgePointId=OO.getLong("knowledgePointId");

                         int  homeState=OO.getInt("homeTestState");

                         //难度
                         float difficulty= (float) OO.getDouble("difficulty");
                         //题干
                         String problem=OO.getString("problem");
                         //正确错误
                         Boolean state=OO.getBoolean("state");
                         //学生id
                         long studentId=OO.getLong("studentId");
                         //学科id
                         long subjectId=OO.getLong("subjectId");
                         testInfo.setSubjectId(subjectId);
                         testInfo.setStudentId(studentId);
                         testInfo.setTestId(testId);
                         testInfo.setStuWorkId(stuWorkId);
                         testInfo.setDifficulty(difficulty);
                         testInfo.setProblem(problem);
                         testInfo.setKnowledgePointId(knowledgePointId);
                         testInfo.setTestType(testType);
                         testInfo.setState(state);
                         testInfo.setHomeState(homeState);
                         testInfoList.add(testInfo);
                     }
                    showTestInfoListAdapter=new ShowTestInfoListAdapter(testInfoList);
                    listView.setAdapter(showTestInfoListAdapter);
//                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            //选择的试题
//                            TestInfo testInfo1=testInfoList.get(position);
//                            Long TestId = testInfoList.get(position).getTestId();
//                            final Long workId=testInfoList.get(position).getStuWorkId();
//                            XsyMap testoInfoMap = XsyMap.getInterface();
//                            testoInfoMap.put(HomeWorkConstantClass.PARAM_TESTID, String.valueOf(TestId));
//                            //获取单个详细试题信息的url
//                            String getWrongQuestionInfoUrl = XSYTools.joinUrl(HomeWorkConstantClass.GETTESTINFO_URL);
//                            OkGo.post(getWrongQuestionInfoUrl).params(testoInfoMap).execute(new StringCallback() {
//                                @Override
//                                public void onSuccess(String s, Call call, Response response) {
//                                    XSYTools.i("获取作业试题的json"+s);
//
//                                    try {
//                                        JSONObject jsonObject=new JSONObject(s);
//                                        Long testid=jsonObject.getLong("id");
//                                        String point=jsonObject.getString("itemPoint");
//                                        String testType=jsonObject.getString("itemType");
//                                        String subject=jsonObject.getString("subject");
//                                        String answerAnalysis=jsonObject.getString("answerAnalysis");
//                                        String different=jsonObject.getString("difficulty");
//                                        TestEntity testEntity=new TestEntity();
//                                        testEntity.setTestId(testid);
//                                        testEntity.setPoint(point);
//                                        testEntity.setTestType(Integer.valueOf(testType));
//                                        testEntity.setSubject(subject);
//                                        testEntity.setNowdifficulty(Double.valueOf(different));
//                                        testEntity.setAnswerAnalysis(answerAnalysis);
//                                        testEntity.setWorkId(String.valueOf(workId));
//                                        List<AnswerEntity> answerList=new LinkedList<AnswerEntity>();
//                                        JSONArray array=jsonObject.getJSONArray("answerList");
//                                        for(int i=0;i<array.length();i++){
//                                            JSONObject OBJ=array.getJSONObject(i);
//                                            AnswerEntity answerEntity=new AnswerEntity();
//                                            long answerid=OBJ.getLong("id");
//                                            long testId=OBJ.getLong("testId");
//                                            String optionTitle=OBJ.getString("optionTitle");
//                                            String optionAnswer=OBJ.getString("optionAnswer");
//                                            Boolean isRealAnswer=OBJ.getBoolean("isRealAnswer");
//                                            answerEntity.setId(answerid);
//                                            answerEntity.setTestId(testId);
//                                            answerEntity.setOptionTitle(optionTitle);
//                                            answerEntity.setOptionAnswer(optionAnswer);
//                                            answerEntity.setIsRealAnswer(isRealAnswer);
//                                            answerList.add(answerEntity);
//                                        }
//                                        testEntity.setAnswerList(answerList);
//                                        projectWorkShowActivityHandler.sendMessage(XSYTools.makeNewMessage(Common.SHOW_SING_TEST,testEntity));
//
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//
//
//                                }
//
//                                @Override
//                                public void onError(Call call, Response response, Exception e) {
//                                    super.onError(call, response, e);
//                                    XSYTools.i("获取作业试题的错误"+e.toString());
//                                }
//                            });
//
//                        }
//                    });
                } catch (JSONException e) {
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



    private class ShowTestInfoListAdapter extends BaseAdapter{

        private final int TYPE1 = 0;
        private final int TYPE2 = 1;

        List<TestInfo> list;
        public ShowTestInfoListAdapter(List<TestInfo> testInfoList) {
            list=testInfoList;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        //这个方法重写看有几种类型，它返回了有几种不同的布局
        @Override
        public int getViewTypeCount() {
            return 2;
        }

        // 每个convertView都会调用此方法，获得当前应该加载的布局样式
        @Override
        public int getItemViewType(int position) {
            TestInfo testInfo = list.get(position);
            long Type = testInfo.getTestType();

            //获取当前布局的数据
            //哪个字段不为空就说明这是哪个布局
            //比如第一个布局只有item1_str这个字段，那么就判断这个字段是不是为空，
            //如果不为空就表明这是第一个布局的数据
            //根据字段是不是为空，判断当前应该加载的布局
            //填空题
            if (Type == 5) {
                return TYPE1;
                //别的题
            } else {
                return TYPE2;
            }
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=LayoutInflater.from(getActivity());
            //初始化每个holder
            ViewHolder1 holder1 = null;
            ViewHolder2 holder2 = null;
            Long testID=list.get(position).getTestId();
            String title=list.get(position).getProblem();
            final Boolean state=list.get(position).getState();
             final int homestate=list.get(position).getHomeState();

            int type = getItemViewType(position);
            int testType= (int) list.get(position).getTestType();

            if(convertView==null){
                switch (type){
                    //填空题
                    case TYPE1:
                        convertView = inflater.inflate(R.layout.work_show_wrong_layout_t, null, false);
                        holder1 = new ViewHolder1();
                        holder1.item1_tv = (TextView) convertView.findViewById(R.id.SW_tile_type);
                        holder1.webview=(WebView)convertView.findViewById(R.id.SW_tile_testTile_web);
                        holder1.lin1=(LinearLayout) convertView.findViewById(R.id.liuliu_showrong);
                        holder1.lin2=(LinearLayout) convertView.findViewById(R.id.sisi_showrong);
                        holder1.status=(TextView)convertView.findViewById(R.id.status);
                        convertView.setTag(holder1);

                        break;
                    case TYPE2:
                        convertView=inflater.inflate(R.layout.work_show_wrong_layout_t,null,false);
                        holder2 = new ViewHolder2();
                        holder2.item1_tv = (TextView) convertView.findViewById(R.id.SW_tile_type);
                        holder2.webview=(WebView)convertView.findViewById(R.id.SW_tile_testTile_web);
                        holder2.lin1=(LinearLayout) convertView.findViewById(R.id.liuliu_showrong);
                        holder2.lin2=(LinearLayout) convertView.findViewById(R.id.sisi_showrong);
                        holder2.status=(TextView)convertView.findViewById(R.id.status);
                        convertView.setTag(holder2);
                        break;

                }

            } else {
                switch (type) {
                    case TYPE1:
                        holder1 = (ViewHolder1) convertView.getTag();
                        break;
                    case TYPE2:
                        holder2 = (ViewHolder2) convertView.getTag();
                        break;
                }

            }
            //方数据了
            switch (type){
                //填空题
                case TYPE1:
                    if (title == null) {
                        return convertView;
                    }else {
                        if(homestate==0){
                            holder1.status.setText("未完成");
                            holder1.status.setTextColor(0xffff3333);

                        }else if(homestate==1){
                            holder1.status.setText("未批阅");
                            holder1.status.setTextColor(0xffffcc33);
                        }else if(homestate==2){
                            holder1.status.setText("已经完成");
                            holder1.status.setTextColor(0xff00ff66);
                        }
                        holder1.item1_tv.setText("  填空题");
                        holder1.item1_tv.setBackgroundResource(R.drawable.flanksinbg);
                        initWebView(holder1.webview, title);
                        final ViewHolder1 finalHolder1 = holder1;
                        holder1.lin1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (homestate==2||homestate==1) {


                                } else {
                                    //还没完成的
                                v.setBackgroundColor(Color.parseColor("#6699cc"));
                                finalHolder1.lin2.setBackgroundColor(Color.parseColor("#6699cc"));

                                  Long TestId = list.get(position).getTestId();
                                  final Long stuwork=  list.get(position).getStuWorkId();
                                //获取单个详细信息的url
                                String getWrongQuestionInfoUrl = XSYTools.getWorkUrl(HomeWorkConstantClass.GETTESTINFO_URL,getActivity());
                                XsyMap testoInfoMap = XsyMap.getInterface();
                                testoInfoMap.put(HomeWorkConstantClass.PARAM_TESTID, String.valueOf(TestId));
                                testoInfoMap.put(HomeWorkConstantClass.PARAM_STUWORKID,String.valueOf(stuwork));


                                OkGo.post(getWrongQuestionInfoUrl).params(testoInfoMap).execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        XSYTools.i(s);

                                        try {
                                            JSONObject jsonObject=new JSONObject(s);
                                            Long testid=jsonObject.getLong("id");
                                            String point=jsonObject.getString("itemPoint");
                                            String testType=jsonObject.getString("itemType");
                                            String subject=jsonObject.getString("subject");
                                            String answerAnalysis=jsonObject.getString("answerAnalysis");
                                            String different=jsonObject.getString("difficulty");
                                            TestEntity testEntity=new TestEntity();
                                            testEntity.setTestId(testid);
                                            testEntity.setPoint(point);
                                            testEntity.setTestType(Integer.valueOf(testType));
                                            testEntity.setSubject(subject);
                                            testEntity.setNowdifficulty(Double.valueOf(different));
                                            testEntity.setAnswerAnalysis(answerAnalysis);
                                            testEntity.setWorkId(String.valueOf(stuwork));
                                            List<AnswerEntity> answerList=new LinkedList<AnswerEntity>();
                                            JSONArray array=jsonObject.getJSONArray("answerList");
                                            for(int i=0;i<array.length();i++){
                                                JSONObject OBJ=array.getJSONObject(i);
                                                AnswerEntity answerEntity=new AnswerEntity();
                                                long answerid=OBJ.getLong("id");
                                                long testId=OBJ.getLong("testId");
                                                    String optionTitle=OBJ.getString("optionTitle");
                                                String optionAnswer=OBJ.getString("optionAnswer");
                                                Boolean isRealAnswer=OBJ.getBoolean("isRealAnswer");
                                                answerEntity.setId(answerid);
                                                answerEntity.setTestId(testId);
                                                answerEntity.setOptionTitle(optionTitle);
                                                answerEntity.setOptionAnswer(optionAnswer);
                                                answerEntity.setIsRealAnswer(isRealAnswer);
                                                answerList.add(answerEntity);
                                            }
                                            testEntity.setAnswerList(answerList);
                                            projectWorkShowActivityHandler.sendMessage(XSYTools.makeNewMessage(Common.SHOW_SING_TEST,testEntity));

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(Call call, Response response, Exception e) {
                                        super.onError(call, response, e);
                                    }
                                });
                            }
                        }
                        });


                    }
                    break;


                case TYPE2:
                    if (title == null) {
                        return convertView;
                    }else {
                        if(testType==1){
                            holder2.item1_tv.setText("  选择题");
                            holder2.item1_tv.setBackgroundResource(R.drawable.flanksinbg);
                        }else if(testType==2){
                            holder2.item1_tv.setText("  多选题");
                            holder2.item1_tv.setBackgroundResource(R.drawable.multsingsinbg);
                        }else if(testType==3){
                            holder2.item1_tv.setText("  判断题");
                            holder2.item1_tv.setBackgroundResource(R.drawable.wokaosingsign);
                        }else if(testType==4){
                            holder2.item1_tv.setText("  涂抹题");
                            holder2.item1_tv.setBackgroundResource(R.drawable.oprasinbg);
                        }else if(testType==6){
                            holder2.item1_tv.setText("  连线题");
                            holder2.item1_tv.setBackgroundResource(R.drawable.linsinbg);
                        }else if(testType==7){
                            holder2.item1_tv.setText("  操作题");
                            holder2.item1_tv.setBackgroundResource(R.drawable.oprasinbg);
                        }else if(testType==8){
                            holder2.item1_tv.setText("  嵌套题");
                            holder2.item1_tv.setBackgroundResource(R.drawable.flanksinbg);
                        }
                        if(homestate==2){
                            holder2.status.setText("已经完成");

                            holder2.status.setTextColor(0xff00ff66);
                        }else if(homestate==1){
                            holder2.status.setText("未批阅");
                            holder2.status.setTextColor(0xffecd637);
                        }else if(homestate==0){
                            holder2.status.setText("未完成");
                            holder2.status.setTextColor(0xffff3333);
                        }
                        initotherWebView(holder2.webview, title);
                        final ViewHolder2 finalHolder2 = holder2;
                        holder2.lin1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (homestate==1||homestate==2) {

                                } else {
                                    v.setBackgroundColor(Color.parseColor("#6699cc"));
                                    finalHolder2.lin2.setBackgroundColor(Color.parseColor("#6699cc"));

                                    Long TestId = list.get(position).getTestId();
                                   final long stuwork= list.get(position).getStuWorkId();
                                    //获取单个详细信息的url
                                    String getWrongQuestionInfoUrl = XSYTools.getWorkUrl(HomeWorkConstantClass.GETTESTINFO_URL,getActivity());

                                    XsyMap testoInfoMap = XsyMap.getInterface();
                                    testoInfoMap.put(HomeWorkConstantClass.PARAM_TESTID, String.valueOf(TestId));
                                    testoInfoMap.put(HomeWorkConstantClass.PARAM_STUWORKID,String.valueOf(stuwork));
                                    //得到单个试题的详细信息
                                    OkGo.post(getWrongQuestionInfoUrl).params(testoInfoMap).execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(String s, Call call, Response response) {
                                            XSYTools.i(s);
                                            try {
                                                JSONObject jsonObject=new JSONObject(s);
                                                Long testid=jsonObject.getLong("id");
                                                String point=jsonObject.getString("itemPoint");
                                                String testType=jsonObject.getString("itemType");
                                                String subject=jsonObject.getString("subject");
                                                String answerAnalysis=jsonObject.getString("answerAnalysis");
                                                String different=jsonObject.getString("difficulty");
                                                TestEntity testEntity=new TestEntity();
                                                testEntity.setTestId(testid);
                                                testEntity.setPoint(point);
                                                testEntity.setTestType(Integer.valueOf(testType));
                                                testEntity.setSubject(subject);
                                                testEntity.setNowdifficulty(Double.valueOf(different));
                                                testEntity.setAnswerAnalysis(answerAnalysis);
                                                testEntity.setWorkId(String.valueOf(stuwork));
                                                List<AnswerEntity> answerList=new LinkedList<AnswerEntity>();
                                                JSONArray array=jsonObject.getJSONArray("answerList");
                                                for(int i=0;i<array.length();i++){
                                                    JSONObject OBJ=array.getJSONObject(i);
                                                    AnswerEntity answerEntity=new AnswerEntity();
                                                    long answerid=OBJ.getLong("id");
                                                    long testId=OBJ.getLong("testId");
                                                    String optionTitle=OBJ.getString("optionTitle");
                                                    String optionAnswer=OBJ.getString("optionAnswer");
                                                    Boolean isRealAnswer=OBJ.getBoolean("isRealAnswer");
                                                    answerEntity.setId(answerid);
                                                    answerEntity.setTestId(testId);
                                                    answerEntity.setOptionTitle(optionTitle);
                                                    answerEntity.setOptionAnswer(optionAnswer);
                                                    answerEntity.setIsRealAnswer(isRealAnswer);
                                                    answerList.add(answerEntity);
                                                }
                                                testEntity.setAnswerList(answerList);
                                                projectWorkShowActivityHandler.sendMessage(XSYTools.makeNewMessage(Common.SHOW_SING_TEST,testEntity));

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
//                                            projectWorkShowActivityHandler.sendMessage(XSYTools.makeNewMessage(Common.SHOW_SING_TEST, s));
                                        }

                                        @Override
                                        public void onError(Call call, Response response, Exception e) {
                                            super.onError(call, response, e);
                                        }
                                    });
                                }
                            }
                        });
                    }

                    break;
            }

            return convertView;
        }
    }

    private void initotherWebView(WebView webview, String title) {
            //解析网页
         document = Jsoup.parse(title);
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

    //为每种布局定义自己的ViewHolder
    //填空题
    class ViewHolder1 {
        TextView item1_tv;
        WebView webview;
        LinearLayout lin1;
        LinearLayout  lin2;
        TextView status;
    }

    //普通的别的题
    class ViewHolder2 {
        TextView item1_tv;
        WebView webview;
        LinearLayout lin1;
        LinearLayout  lin2;
        TextView status;
    }
    /**
     * 初始化WebView
     *
     * @param titleWebvie
     * @param title
     */
    private void initWebView(WebView titleWebvie, String title) {
        titleWebvie.getSettings().setJavaScriptEnabled(true);
        titleWebvie.getSettings().setDefaultTextEncodingName("utf-8");
        titleWebvie.setBackgroundColor(0); // 设置背景色
        //设置载入页面自适应手机屏幕，居中显示
        WebSettings mWebSettings = titleWebvie.getSettings();
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        titleWebvie.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });

        document = Jsoup.parse(title);
        body = document.select("body");
        body.attr("align", "center");
        imgs = document.select("img");

        //attr 挑选节点信息
        for (Element ele : imgs) {
            String group = ele.attr("group");
            if (group.toLowerCase().trim().equals("blank")) {
                ele.attr("src", "file:///android_asset/" + ele.attr("tp") + ".png");
            }
        }
        Element script = document.head().appendElement("script");
        script.attr("type", "text/javascript");
        script.attr("language", "javascript");
        script.attr("src", "file:///android_asset/fillBlank_js.js");
        String title2 = title = document.html();
        titleWebvie.getSettings().setJavaScriptEnabled(true);
        titleWebvie.getSettings().setTextSize(WebSettings.TextSize.LARGER);
        titleWebvie.loadDataWithBaseURL(null, title2, "text/html", "utf-8", null);
        XSYTools.i(title2);
    }

    public void setInfo(ProjectWorkShowActivityHandler projectWorkShowActivityHandler) {
        this.projectWorkShowActivityHandler=projectWorkShowActivityHandler;
    }
}
