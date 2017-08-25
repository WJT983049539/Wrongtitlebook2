package com.xinshuyuan.xinshuyuanworkandexercise.View.Activity;

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
import android.widget.ListView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.Common;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.PerferenceService;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestInfo;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.WorkInfoBean;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XsyMap;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.javabean.Statistice;
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

import lecho.lib.hellocharts.formatter.ColumnChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleColumnChartValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.PieChartView;
import lecho.lib.hellocharts.view.PreviewColumnChartView;
import okhttp3.Call;
import okhttp3.Response;
import work.HomeWorkConstantClass;

/**试题统计页面
 * Created by Administrator on 2017/7/10.
 */

public class TestStatisticeFragment extends Fragment{
    private WorkInfoBean workinfobean;
    private Long studentId;
    private PieChartView bing_test_statistice;
    private PreviewColumnChartView zhu_test_statistice;
    private View mView;
    private ProjectWorkShowActivityHandler projectWorkShowActivityHandler;
    private String StuId;
    private ProjectWorkShowActivity projectWorkShowActivity;
    private String subjectId;
    private List<TestInfo> testInfoList=new ArrayList<TestInfo>();
    private List<String>lablelist=new ArrayList<>();
    private String fgg;

    List<Float>list=new LinkedList<Float>();
    List<Statistice>list2=new LinkedList<Statistice>();
    private ListView showtest_state;
    private PieChartData pieChardata;
    private PieChartData pd;
    private int color[]={0xff3586ff,0xffddb000,0xfffff7d7,0xffff3366};
    //y 轴集合
    List<String>ylist=new ArrayList<String>();
    //x轴显示的集合x
    List<String>xlist=new ArrayList<String>();
    private Document document;
    private Elements imgs;
    private Elements body;

    //班级ABC的柱状图颜色
    private int[] colors = {Color.RED, Color.BLUE, Color.GREEN};

    private int[] score1 = {88, 77, 99, 56, 48, 51, 33, 62, 56, 89, 98, 88, 97, 95, 81, 86, 94, 75, 86, 44};   //第一个班级的分数
    private int[] score2 = {98, 77, 89, 86, 48, 51, 13, 82, 58, 89, 98, 88, 87, 95, 81, 86, 94, 85, 86, 44};   //第二个班级的分数
    private int[] score3 = {88, 97, 99, 56, 98, 51, 33, 22, 56, 99, 98, 88, 97, 95, 81, 86, 24, 75, 26, 44};   //第三个班级的分数

    //为三种布局定义一个标识
    private final int TYPE1 = 0;
    private final int TYPE2 = 1;

    //每道试题做了多少人，做错多少人，做对多少人
    private int[] scoreNum1 = new int[3];
    private int[] scoreNum2 = new int[3];
    private int[] scoreNum3 = new int[3];


    private ColumnChartData data;

    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();   //x轴方向的坐标数据
    private List<AxisValue> mAxisYValues = new ArrayList<AxisValue>();  //y轴方向的坐标数据



    public TestStatisticeFragment(ProjectWorkShowActivity projectWorkShowActivity) {
        this.projectWorkShowActivity=projectWorkShowActivity;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        workinfobean= (WorkInfoBean) getArguments().getSerializable("workinfobean");
        PerferenceService service=new PerferenceService(projectWorkShowActivity);
        StuId=String.valueOf(service.getsharedPre().getLong("WorkId",0));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.layout_fragment_test_statistice,container,false);
            inint();
            getData();

            initTestStatistice();



        return mView;
    }

    //这里是给柱状图写入数据的
    private void initTestStatistice() {

        subjectId=Common.getSubjectId();
        if(subjectId.equals("")){
            subjectId=new PerferenceService(projectWorkShowActivity).getsharedPre().getString("ProjectId","");
        }
        XsyMap<String,String> map=XsyMap.getInterface();
        map.put(HomeWorkConstantClass.PARAM_STUDENTID,StuId);
        map.put(HomeWorkConstantClass.PARAM_STUWORKID,String.valueOf(workinfobean.getWorkId()));
        map.put(HomeWorkConstantClass.PARAM_SUBJECTID,subjectId);


        OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.TESTTATAL_URL,projectWorkShowActivity)).params(map).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                XSYTools.i("试题的详细统计"+s);
                //这里没数据，也不知道格式是什么样的。每个试题多少人做了，作对多少人，做错多少人的柱状图显示
                ylist.clear();
                xlist.clear();
                try {

                    JSONObject allobject=new JSONObject(s);
                    //取出x坐标集合
                    JSONArray axisArray=allobject.getJSONArray("axis");
                    for(int i=0;i<axisArray.length();i++){
                        String axis= (String) axisArray.get(i);
                        xlist.add(axis);
                    }


                    JSONArray array=allobject.getJSONArray("legend");
                    for(int j=0;j<array.length();j++){
                        //y轴的数据
                        String aa= (String) array.get(j);
                        ylist.add(aa);
                    }
                    JSONObject jsonObject=allobject.getJSONObject("series");
                    for(int i=0;i<xlist.size();i++){
                       JSONObject infoObject= jsonObject.getJSONObject(xlist.get(i));
                        //
                        Float rightCount= Float.valueOf(infoObject.getInt("rightCount"));
                        int wrongcount=infoObject.getInt("wrongcount");
                        Float wrongcountz=(float)wrongcount;
                        Float doCount=Float.valueOf(infoObject.getInt("doCount"));

                        Statistice statistice=new Statistice();
                        statistice.setDoCount(doCount);
                        statistice.setRightCount(rightCount);
                        statistice.setWrongcount(wrongcountz);

                        list2.add(statistice);
                        //设置x轴上面的数据
                        mAxisXValues.add(new AxisValue(i).setLabel(xlist.get(i)));

                    }


                    int numColumns =xlist.size();    //总共有多少列
                    int numSubcolumns = 3;//每列有3个柱子
                    //定义一个圆柱对象集合
                    List<Column> columns = new ArrayList<Column>();
                    //子列数据集合
                    List<SubcolumnValue> values;
//                    List<SubcolumnValue> values2;
//                    List<SubcolumnValue> values3;

                    //遍历列数numColumns
                    for (int i = 0; i < numColumns; ++i) {
                        values = new ArrayList<SubcolumnValue>();
//                        values2 = new ArrayList<SubcolumnValue>();
//                        values3 = new ArrayList<SubcolumnValue>();
                        //遍历每一列的每一个子列
                            //为每一柱图添加颜色和数值
//                            mAxisYValues.add(new AxisValue(j).setValue(j+20));// 添加Y轴显示的刻度值
                            Statistice f = list2.get(i);
                            values.add(new SubcolumnValue(f.getRightCount(), 0xff1dbc85));
                            values.add(new SubcolumnValue(f.getWrongcount(), 0xffb7b715));
                            values.add(new SubcolumnValue(f.getDoCount(),0xffbe371b));
                        //创建Column对象
                        Column column =new Column(values);
                        ColumnChartValueFormatter chartValueFormatter = new SimpleColumnChartValueFormatter(2);
                        column.setFormatter(chartValueFormatter);
                       // 是否有数据标注
                        column.setHasLabels(true);
//                        //是否有数据标注
//                        column2.setHasLabels(true);
//                        ColumnChartValueFormatter chartValueFormatter2 = new SimpleColumnChartValueFormatter(2);
//                        column2.setFormatter(chartValueFormatter2);
//                        //是否有数据标注
//                        column3.setHasLabels(true);
//                        ColumnChartValueFormatter chartValueFormatter3 = new SimpleColumnChartValueFormatter(2);
//                        column.setFormatter(chartValueFormatter3);
//                        //是否有数据标注
//                        column3.setHasLabels(true);

                        columns.add(column);
//                        columns.add(column2);
//                        columns.add(column3);
                        //创建一个带有之前圆柱对象column集合的ColumnChartData

                        //给x轴坐标设置描述
                        //给x轴坐标设置描述

                        data= new ColumnChartData(columns);

                        //定义x轴y轴相应参数
                        Axis axisX = new Axis();
                        Axis axisY = new Axis().setHasLines(true);
                        axisX.setName("试题");
                        axisY.setName("人数");//轴名称
                        axisY.hasLines();//是否显示网格线
                        axisX.hasLines();
                        axisX.setValues(mAxisXValues);

                        //设置Y轴上面的数据
                        for (int j = 0; j < 100;) {
                            j=j+5;
                            mAxisYValues.add(new AxisValue(j).setLabel("" + j));
                        }
                        //把X轴Y轴数据设置到ColumnChartData 对象中
                        axisY.setValues(mAxisYValues);
                        data.setAxisXBottom(axisX);
                        data.setAxisYLeft(axisY);
                        data.setAxisXBottom(axisX);
                        data.setAxisYLeft(axisY);
//                        data.setFillRatio(0.1F);
                        //给表填充数据，显示出来
                        zhu_test_statistice.setColumnChartData(data);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                XSYTools.i("单个试题的详细统计错误"+e.toString());
            }
        });
    }


    private void inint() {
        //饼装图
        bing_test_statistice= (PieChartView) mView.findViewById(R.id.bing_test_statistice);
        //预览柱状图
        zhu_test_statistice= (PreviewColumnChartView) mView.findViewById(R.id.zhu_test_statistice);
        //显示的列表
        showtest_state= (ListView) mView.findViewById(R.id.showtest_state);

        lablelist.add("做对题目数");
        lablelist.add("做错题目数");
        lablelist.add("未判数");
        lablelist.add("未做题目数");
    }
    //饼图设置

    //饼状图数据
    private void getData() {
         subjectId=Common.getSubjectId();
        if(subjectId.equals("")){
            subjectId=new PerferenceService(projectWorkShowActivity).getsharedPre().getString("ProjectId","");
        }
        XsyMap<String,String> map=XsyMap.getInterface();
        map.put(HomeWorkConstantClass.PARAM_STUDENTID,StuId);
        map.put(HomeWorkConstantClass.PARAM_STUWORKID,String.valueOf(workinfobean.getWorkId()));
        map.put(HomeWorkConstantClass.PARAM_SUBJECTID,subjectId);

        OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.TESTLISTSTAS_URL,projectWorkShowActivity)).params(map).execute(new StringCallback() {
            @Override

            public void onSuccess(String s, Call call, Response response) {
                //得到饼状图和list列表的数据
                XSYTools.i("作业统计饼状图和试题列表的的json"+s);
                testInfoList.clear();
                list.clear();
                try {
                    //显示列表的json
                    JSONObject object=new JSONObject(s);
                    JSONArray Array=object.getJSONArray("list");
                    for(int i=0;i<Array.length();i++) {
                        JSONObject object2 = Array.getJSONObject(i);
                        long testId = object2.getLong("testId");
                        studentId=object2.getLong("studentId");
                        //试题状态
                        int TestState = object2.getInt("homeTestState");
                        //答题状态
                        Boolean questionState = object2.getBoolean("state");
                        int testType = object2.getInt("testType");
                        //标题
                        String problem = object2.getString("problem");
                        long homeWorkId = object2.getLong("homeWorkId");
                        float difficulty = (float) object2.getDouble("difficulty");
                        TestInfo testInfo=new TestInfo();
                        testInfo.setTestId(testId);
                        testInfo.setTestState(TestState);
                        testInfo.setState(questionState);
                        testInfo.setTestType(testType);
                        testInfo.setProblem(problem);
                        testInfo.setStuWorkId(homeWorkId);
                        testInfo.setDifficulty(difficulty);
                        testInfoList.add(testInfo);
                    }
                    //得到饼状图的
                   JSONObject seriesdate=object.getJSONObject("series");
                    JSONObject  oo=seriesdate.getJSONObject(String.valueOf(studentId));
                    //作对的题目数
                    Float rightCount= Float.valueOf(oo.getInt("rightCount"));
                    //"做错题目数
                    Float wrongCount= Float.valueOf(oo.getInt("wrongCount"));
                    //"未判数
//                    Float noMarkCount= Float.valueOf(oo.getInt("noMarkCount"));
                    //未做题目数
//                    Float noDoCount= Float.valueOf(oo.getInt("noDoCount"));

                    list.add(rightCount);
                    list.add(wrongCount);
//                    list.add(noMarkCount);
//                    list.add(noDoCount);


                    pd=new PieChartData();//实例化PieChartData对象
                    pd.setHasLabels(true);
                    pd.setHasLabelsOnlyForSelected(false);

                    pd.setHasLabelsOutside(false);
                    pd.setHasCenterCircle(true);
                    pd.setSlicesSpacing(24);
                    List<SliceValue> sliceList = new ArrayList<SliceValue>();
                    for(int i=0;i<list.size();i++){//循环为饼图设置数据ChartUtils.pickColor()
                        Float aaa=list.get(i);
                        int bb=aaa.intValue();
                        sliceList.add(new SliceValue(list.get(i),color[i]).setLabel(lablelist.get(i)+bb));
                    }
                    pd.setValues(sliceList);//为饼图添加数据

//                    inintPieChartView();

                    bing_test_statistice.setViewportCalculationEnabled(true);//设置饼图自动适应大小
                    bing_test_statistice.setChartRotationEnabled(true);//设置饼图是否可以手动旋转
                    pd.setHasCenterCircle(true);//设置饼图中间是否有第二个圈
                    pd.setCenterCircleColor(0xffffffff);//设置饼图中间圈的颜色
                    pd.setCenterCircleScale(0.4f);////设置第二个圈的大小比例
                    pd.setCenterText1("个人试题统计");//设置文本
                    pd.setCenterText1Color(Color.BLACK);//设置文本颜色
                    pd.setCenterText1FontSize(14);//文字大小
//        bing_test_statistice.setCircleFillRatio(float fillRatio);//设置饼图其中的比例
//                    pd.setHasLabelsOnlyForSelected(true);
//                    pd.setHasLabelsOutside(false);
                    bing_test_statistice.setPieChartData(pd);//为饼图设置数据
                    //给list里面方数据

                    showtest_state.setAdapter(new Myadapter(testInfoList));
                    

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                XSYTools.i("作业统计饼状图的错误信息："+e.toString());
            }
        });


    }

    public void setInfo(ProjectWorkShowActivityHandler projectWorkShowActivityHandler) {
        this.projectWorkShowActivityHandler=projectWorkShowActivityHandler;
    }

    private class Myadapter extends BaseAdapter {
        LayoutInflater flater=LayoutInflater.from(projectWorkShowActivity);
        private List<TestInfo> testInfoList;
        public Myadapter(List<TestInfo> testInfoList2) {
            this.testInfoList=testInfoList2;
        }

        @Override
        public int getCount() {
            return testInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return testInfoList.get(position);
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
            TestInfo testInfo = testInfoList.get(position);
            Integer Type = testInfo.getTestType();

            //获取当前布局的数据
            //哪个字段不为空就说明这是哪个布局
            //比如第一个布局只有item1_str这个字段，那么就判断这个字段是不是为空，
            //如果不为空就表明这是第一个布局的数据
            //根据字段是不是为空，判断当前应该加载的布局
            //填空题
            if (Type == 5) {
                return TYPE1;
                //自定义题
            } else {
                return TYPE2;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewholder = null;
                ViewHolder2 viewholder2=null;
            int type = getItemViewType(position);
            if(convertView==null){
                switch (type) {

                    //填空题
                    case TYPE1:
                        viewholder2=new ViewHolder2();
                        convertView=flater.inflate(R.layout.item_flank_teststate_list_layout,parent,false);
                        viewholder2.fillBlank= (WebView) convertView.findViewById(R.id.fillblank_point);
                        viewholder2.finish_state= (TextView) convertView.findViewById(R.id.finish_state);
                        viewholder2.test_state= (TextView) convertView.findViewById(R.id.test_state);
                        convertView.setTag(viewholder2);
                        break;
                    //其他的题
                    case TYPE2:
                        viewholder=new ViewHolder();
                        convertView=flater.inflate(R.layout.item_teststate_list_layout,parent,false);
                        viewholder.textView_point= (WebView) convertView.findViewById(R.id.textView_point);
                        viewholder.finish_state= (TextView) convertView.findViewById(R.id.finish_state);
                        viewholder.test_state= (TextView) convertView.findViewById(R.id.test_state);
                        convertView.setTag(viewholder);
                        break;
                }


           }else{
                switch (type){
                    case TYPE1:
                        viewholder2= (ViewHolder2) convertView.getTag();
                        break;
                    case TYPE2:
                        viewholder= (ViewHolder) convertView.getTag();
                        break;

                }

            }

            switch (type){
                //填空题
                 case TYPE1:
                     initotherWebView(viewholder2.fillBlank,testInfoList.get(position).getProblem());
                     if(testInfoList.get(position).getState()){
                         viewholder2.finish_state.setText("对");
                         viewholder2.finish_state.setTextColor(0xff3586ff);
                     }else{
                         viewholder2.finish_state.setText("错");
                         viewholder2.finish_state.setTextColor(0xffff3300);
                     }
                     if(testInfoList.get(position).getTestState()==0){
                         fgg="已完成";
                     }else if(testInfoList.get(position).getTestState()==1){
                         fgg="未做";
                     }else if(testInfoList.get(position).getTestState()==2){
                         fgg="未判";
                     }
                     viewholder2.test_state.setText(fgg);

                     break;

                //其他的题
                case TYPE2:
                    initWebView(viewholder.textView_point,testInfoList.get(position).getProblem());
                    if(testInfoList.get(position).getState()){
                        viewholder.finish_state.setText("对");
                        viewholder.finish_state.setTextColor(0xff3586ff);
                    }else{
                        viewholder.finish_state.setText("错");
                        viewholder.finish_state.setTextColor(0xffff3300);
                    }
                    if(testInfoList.get(position).getTestState()==0){
                        fgg="已完成";
                    }else if(testInfoList.get(position).getTestState()==1){
                        fgg="未做";
                    }else if(testInfoList.get(position).getTestState()==2){
                        fgg="未判";
                    }
                    viewholder.test_state.setText(fgg);

                    break;

            }






            return convertView;
        }
    }



    class ViewHolder{
        WebView textView_point;
        TextView finish_state;
        TextView test_state;
    }

    //填空题的适配器
    class ViewHolder2{
        WebView fillBlank;
        TextView finish_state;
        TextView test_state;

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
        titleWebvie.getSettings().setDefaultFontSize(20);
        titleWebvie.loadDataWithBaseURL(null, title2, "text/html", "utf-8", null);
        XSYTools.i(title2);
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
}
