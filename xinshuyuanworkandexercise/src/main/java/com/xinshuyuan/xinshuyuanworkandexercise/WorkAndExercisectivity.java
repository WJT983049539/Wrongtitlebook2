package com.xinshuyuan.xinshuyuanworkandexercise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.Common;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.PerferenceService;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XsyMap;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Activity.Layering_practice_Activity;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Activity.ShowProjectListActivity;
import com.xinshuyuan.xinshuyuanworkandexercise.presenter.Handler.WorkAndExerciseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import lecho.lib.hellocharts.formatter.ColumnChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleColumnChartValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import okhttp3.Call;
import okhttp3.Response;
import work.HomeWorkConstantClass;

import static com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools.getWorkUrl;

public class WorkAndExercisectivity extends Activity {
        private WorkAndExerciseHandler workAndExerciseHandler;
        private TextView asdasd;
        private  Long userId;
        private Integer userType;
        private String userName;
        private ImageView imageView_work;
        private ImageView imageView_exercise;
        private ImageView image_headportrait;

        private TextView NameTextView;
        private TextView textView_stucode;
        private TextView textView_login_count;
        private TextView textView_login_lasttime;

        private ListView dynamic_info_show;
        private ListInfoShowAdapter myadapter;
        private List<String>infolist=null;

        private ColumnChartView columnChartView;
        private ColumnChartData data;
        private ColumnChartView average_grades;

        private String Ip;
        private String Port;
        private String ContentCode;
        private Timer timer=null;
        private Long workUserId;

    PerferenceService service=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window=getWindow();
        WindowManager.LayoutParams layoutParams= window.getAttributes();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent=getIntent();
        //这里得到的是sooId
        userName=intent.getStringExtra("userName");
        userId=intent.getLongExtra("userId",0);
        userType=intent.getIntExtra("userType",0);
        service=new PerferenceService(this);
        timer=new Timer();
        workUserId=service.getsharedPre().getLong("WorkId",0);
        workAndExerciseHandler=new WorkAndExerciseHandler(this);
        setContentView(R.layout.activity_work_and_exercisectivity_copy);
        Inint();
    }
    //初始化，添加数据
    private void Inint() {
        //平均成绩统计柱状图
        average_grades= (ColumnChartView) findViewById(R.id.average_grade_column_chart);
        average_grades.setZoomEnabled(true);//设置是否支持缩放
        average_grades.setInteractive(true);//设置图表是否可以与用户互动
        // 柱状图
        columnChartView= (ColumnChartView) findViewById(R.id.clumn_chart);
        columnChartView.setZoomEnabled(true);//设置是否支持缩放
        columnChartView.setInteractive(true);//设置图表是否可以与用户互动
        //储存动态显示的数据
        infolist=new ArrayList<String>();
        //作业按钮
        imageView_work= (ImageView) findViewById(R.id.imageView_work);
        //练习按钮
        imageView_exercise=(ImageView)findViewById(R.id.imageView_exercise);
        //分册练习点击事件,转到分册练习的界面
        imageView_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent( WorkAndExercisectivity.this, Layering_practice_Activity.class);
                intent.putExtra("userId",workUserId);
                intent.putExtra("userName",userName);
                WorkAndExercisectivity.this.startActivity(intent);
            }
        });

        //动态显示数据
        dynamic_info_show= (ListView) findViewById(R.id.dynamic_info_show);
        //头像的imageview
        image_headportrait= (ImageView) findViewById(R.id.image_headportrait);
        NameTextView= (TextView) findViewById(R.id.NameTextView);
        textView_stucode= (TextView) findViewById(R.id.textView_stucode);
        textView_login_count= (TextView) findViewById(R.id.textView_login_count);
        textView_login_lasttime= (TextView) findViewById(R.id.textView_login_lasttime);
        /**
         * 作业按钮
         */
        imageView_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent( WorkAndExercisectivity.this, ShowProjectListActivity.class);
                Long workUserId=service.getsharedPre().getLong("WorkId",0);
                intent.putExtra("userId",workUserId);
                intent.putExtra("userName",userName);
                WorkAndExercisectivity.this.startActivity(intent);
            }
        });
        //放入统计信息
        getTatisticsInfo();
        //当天作业该学生在班中排名的统计
        rankingTatustucsInfo();
        //得到学生信息
        getDate();
        //得到头像并放上去
        getBitmap();
//        //动态更新作业动态，这里使用timer做个循环获取数据，并放上去,20秒更新一次
        timer.schedule(new TimerTask() {
            @Override
                public void run() {
                    Map mapS=XsyMap.getInterface();
                    mapS.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(workUserId));
                    OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.NEWLIST_URL,WorkAndExercisectivity.this)).params(mapS).execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            try {
                                infolist.clear();
                            XSYTools.i("作业动态"+s);

                            if(s.equals("")){
                                XSYTools.i("没有动态更新");
                                infolist.add("没有动态更新");

                            }else{

                                JSONObject oo=new JSONObject(s);
                                String rows=oo.getString("list");
                                if(rows.equals("")){
                                    XSYTools.i("没有动态更新");
                                    infolist.add("没有动态更新");
                                }else{

                                JSONArray jsonArray=new JSONArray(rows);

                                for(int i=0;i<jsonArray.length();i++){

                                    JSONObject aaa=jsonArray.getJSONObject(i);
                                    String workName=aaa.getString("workName");
                                    //发布作业的时间
                                    String publishTime= aaa.getString("publishTime");
                                    JSONObject publishTimeobject=new JSONObject(publishTime);
                                    String year=String.valueOf(publishTimeobject.getInt("year"));
                                    String month=String.valueOf(publishTimeobject.getInt("month"));
                                    String day=String.valueOf(publishTimeobject.getInt("day"));
                                    String hours=String.valueOf(publishTimeobject.getInt("hours"));
                                    String minutes=String.valueOf(publishTimeobject.getInt("minutes"));
                                    String seconds=String.valueOf(publishTimeobject.getInt("seconds"));
                                    Long times=publishTimeobject.getLong("time");

                                    Date date=new Date(times);
                                    SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String nowtime=sim.format(date);
                                    String infoList="老师在"+nowtime
                                            +"发布了"+workName;
                                    infolist.add(infoList);
                                }
                            }
                                myadapter=new ListInfoShowAdapter(infolist);
                                dynamic_info_show.setAdapter(myadapter);
                            }
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
        },200,10000);
    }

    private void rankingTatustucsInfo() {
        workAndExerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.RANKING_STATISTICS,String.valueOf(workUserId)));
        String getAveragetatisticsInfourl=XSYTools.getWorkUrl(HomeWorkConstantClass.WORKLISTSTAS_URL,WorkAndExercisectivity.this);
        Map<String,String> AveragetatisticsMap= XsyMap.getInterface();
        AveragetatisticsMap.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(workUserId));
        OkGo.post(getAveragetatisticsInfourl).params(AveragetatisticsMap).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                XSYTools.i("平均成绩对比的统计数据："+s);
                LineChartData chartData;
                List<String> xList=new LinkedList<String>();
                List<Float>score=new LinkedList<Float>();
//                List<Float>avegScore=new LinkedList<Float>();
                JSONObject object= null;

                try {
                    object = new JSONObject(s);
                    //横轴
                    JSONArray array = object.getJSONArray("axis");
                    String data2 = object.getString("totalMap");
                    JSONObject object1 = new JSONObject(data2);
                    for(int i=0;i<array.length();i++){
                        //x轴数据
                        String project= (String) array.get(i);
                        xList.add(project);
                        xList.add(project+"全班平均成绩");
                        String yobject=object1.getString(project);
                        //y轴数据
                        float Score= (float) new JSONObject(yobject).getDouble("score");
                        float AvegScore= (float) new JSONObject(yobject).getDouble("avegScore");
                        score.add(Score);
                        score.add(AvegScore);
                    }

                    //得到数据准备放入数据
                    // 使用的 7列，每列1个subcolumn。
                    int numSubcolumns = 1;
                    int numColumns = xList.size();
                    //定义一个圆柱对象集合
                    List<Column> columns = new ArrayList<Column>();
                    //子列数据集合
                    List<SubcolumnValue> values;

                    List<AxisValue> axisValues = new ArrayList<AxisValue>();
                    ArrayList<AxisValue> axisValuesX = new ArrayList<AxisValue>();//定义X轴刻度值的数据集合
                    ArrayList<AxisValue> axisValuesY = new ArrayList<AxisValue>();//定义Y轴刻度值的数据集合
                    //遍历列数numColumns
                    for (int i = 0; i < numColumns; ++i) {

                        values = new ArrayList<SubcolumnValue>();
                        //遍历每一列的每一个子列
                        for (int j = 0; j < numSubcolumns; ++j) {
                            //为每一柱图添加颜色和数值
                            float f = score.get(i);
                            values.add(new SubcolumnValue(f, ChartUtils.pickColor()));
                            axisValuesY.add(new AxisValue(j).setValue(j+20));// 添加Y轴显示的刻度值
//                            axisValuesX.add(new AxisValue(j).setValue(j).setLabel( String label));// 添加X轴显示的刻度值并设置X轴显示的内容
                        }
                        //创建Column对象
                        Column column = new Column(values);
                        //这一步是能让圆柱标注数据显示带小数的重要一步 让我找了好久问题
                        //作者回答https://github.com/lecho/hellocharts-android/issues/185
//                            ColumnChartValueFormatter chartValueFormatter = new SimpleColumnChartValueFormatter(2);
//                            column.setFormatter(chartValueFormatter);
                        //是否有数据标注
                        column.setHasLabels(true);
                        //是否是点击圆柱才显示数据标注
                        column.setHasLabelsOnlyForSelected(false);
                        columns.add(column);
                        //给x轴坐标设置描述
                        axisValues.add(new AxisValue(i).setLabel(xList.get(i)));
                    }
                    //创建一个带有之前圆柱对象column集合的ColumnChartData
                    data= new ColumnChartData(columns);

                    //定义x轴y轴相应参数
                    Axis axisX = new Axis();
                    Axis axisY = new Axis().setHasLines(true);
                    axisY.setName("排名");//轴名称
                    axisY.hasLines();//是否显示网格线
                    axisY.setTextColor(R.color.dark);//颜色
                    axisX.setName("科目");
//                    ArrayList<AxisValue> axisValuesX = new ArrayList<AxisValue>();//定义X轴刻度值的数据集合
//                    ArrayList<AxisValue> axisValuesY = new ArrayList<AxisValue>();//定义Y轴刻度值的数据集合
                    axisX.setValues(axisValuesY);//为X轴显示的刻度值设置数据集合

                    axisX.setLineColor(0xff223edd);// 设置X轴轴线颜色

                     axisX.hasLines();
                    axisX.setTextColor(R.color.dark);
                    axisX.setValues(axisValues);
                    //把X轴Y轴数据设置到ColumnChartData 对象中
                    data.setAxisXBottom(axisX);
                    data.setAxisYLeft(axisY);
                    data.setFillRatio(0.1F);
                    //给表填充数据，显示出来
                    average_grades.setColumnChartData(data);
                    //用来控制柱形图视图窗口的缩放。
                    //这里设置 -0.7f 的原因是因为 不知道为什么，
                    //第一个柱形图只画了一半，另一半无法显示。
                    // 最高*1.25f Y轴才会正常，
                    //如果只设置setCurrentViewport（）的viewport，是不行的
                    Viewport viewportMax =new Viewport(-0.7f, average_grades.getMaximumViewport().height()*1.25f, 6, 0);
                    average_grades.setMaximumViewport(viewportMax);

                }catch (Exception e){
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

    //得到统计信息
    private void getTatisticsInfo() {
        //获取学生信息
        String gettatisticsInfourl= getWorkUrl(HomeWorkConstantClass.ORDERlIST_URL,WorkAndExercisectivity.this);
        Map<String,String> tatisticsMap=XsyMap.getInterface();
        tatisticsMap.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(workUserId));
        OkGo.post(gettatisticsInfourl).params(tatisticsMap).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                XSYTools.i("统计数据"+s);
                XSYTools.showToastmsg(WorkAndExercisectivity.this,"order统计数据为："+s);
                List<String>xList=new LinkedList<String>();
                List<Long>yList=new LinkedList<Long>();
                try {
                    JSONObject object=new JSONObject(s);
                    //横轴
                    JSONArray array=object.getJSONArray("axis");
                    String data2=object.getString("totalMap");
                    JSONObject object1=new JSONObject(data2);

                     for(int i=0;i<array.length();i++){
                         //x轴数据
                         String project= (String) array.get(i);
                         xList.add(project);
                         String yobject=object1.getString(project);
                         //y轴数据
                         Long y=new JSONObject(yobject).getLong("order");
                         yList.add(y);
                     }

                        //得到数据准备放入数据
                        // 使用的 7列，每列1个subcolumn。
                        int numSubcolumns = 1;
                        int numColumns = xList.size();
                        //定义一个圆柱对象集合
                        List<Column> columns = new ArrayList<Column>();
                        //子列数据集合
                        List<SubcolumnValue> values;

                        List<AxisValue> axisValues = new ArrayList<AxisValue>();
                        //遍历列数numColumns
                        for (int i = 0; i < numColumns; ++i) {

                            values = new ArrayList<SubcolumnValue>();
                            //遍历每一列的每一个子列
                            for (int j = 0; j < numSubcolumns; ++j) {
                                //为每一柱图添加颜色和数值
                                float f = yList.get(i);
                                values.add(new SubcolumnValue(f, ChartUtils.pickColor()));
                            }
                            //创建Column对象
                            Column column = new Column(values);
                            //这一步是能让圆柱标注数据显示带小数的重要一步 让我找了好久问题
                            //作者回答https://github.com/lecho/hellocharts-android/issues/185
                            ColumnChartValueFormatter chartValueFormatter = new SimpleColumnChartValueFormatter(2);
                            column.setFormatter(chartValueFormatter);
                            //是否有数据标注
                            column.setHasLabels(true);
                            //是否是点击圆柱才显示数据标注
                            column.setHasLabelsOnlyForSelected(false);
                            columns.add(column);
                            //给x轴坐标设置描述
                            axisValues.add(new AxisValue(i).setLabel(xList.get(i)));
                        }
                        //创建一个带有之前圆柱对象column集合的ColumnChartData
                         data= new ColumnChartData(columns);

                        //定义x轴y轴相应参数
                        Axis axisX = new Axis();
                        Axis axisY = new Axis().setHasLines(true);
                        axisY.setName("排名");//轴名称
                        axisY.hasLines();//是否显示网格线
                        axisY.setTextColor(R.color.dark);//颜色

                        axisX.setName("科目");
                        axisX.hasLines();
                        axisX.setTextColor(R.color.dark);
                        axisX.setValues(axisValues);
                        //把X轴Y轴数据设置到ColumnChartData 对象中
                        data.setAxisXBottom(axisX);
                        data.setAxisYLeft(axisY);
                       data.setFillRatio(0.1F);
                        //给表填充数据，显示出来
//                    columnChartView= (ColumnChartView) findViewById(R.id.clumn_chart);
//                    columnChartView.setInteractive(true);
//                    columnChartView.setZoomType(ZoomType.HORIZONTAL);
//                    columnChartView.setMaxZoom((float) 2);//最大方法比例
//                    columnChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
//                    columnChartView.setVisibility(View.VISIBLE);
                    columnChartView.setColumnChartData(data);




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

    private void getDate() {

        //获取学生信息
//        String getstuinfourl= getWorkUrl(HomeWorkConstantClass.STUDENTINFO_URL,WorkAndExercisectivity.this);
        String getstuinfourl=XSYTools.getWorkUrl(HomeWorkConstantClass.STUDENTINFO_URL,WorkAndExercisectivity.this);
        Map<String ,String> getstuinfomap=XsyMap.getInterface();
        getstuinfomap.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(userId));
        OkGo.post(getstuinfourl).params(getstuinfomap).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                XSYTools.i(s);
                try {
                    JSONObject objects=new JSONObject(s);
                    String studentinfo= objects.getString("student");
                    JSONObject object=new JSONObject(studentinfo);
                    //学生姓名
                    String studentName=object.getString("studName");

                    NameTextView.setText(studentName);

                    //学号
                    String stuNum=object.getString("studNum");
                    textView_stucode.setText(stuNum);
                    //登录次数
                    int loginCount=object.getInt("longinCount");
                    textView_login_count.setText(String.valueOf(loginCount));

                    //最后一次登录时间
                    String lastLoginTime=object.getString("lastLoginTime");
                    JSONObject object2=new JSONObject(lastLoginTime);
                    Long Time=object2.getLong("time");
                    String year=String.valueOf(object2.getInt("year"));
                    String month=String.valueOf(object2.getInt("month"));
                    String day=String.valueOf(object2.getInt("day"));
                    String hours=String.valueOf(object2.getInt("hours"));
                    String minutes=String.valueOf(object2.getInt("minutes"));
                    String seconds=String.valueOf(object2.getInt("seconds"));

                    Date dateee=new Date(Time);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String nowTime=sdf.format(dateee);
                    textView_login_lasttime.setText(nowTime);

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

    //得到数据，然后放入数据
    private void getBitmap() {
        //获取头像
        //因为储存参数的在文件中，所以获取的参数都是一样的
        String url=XSYTools.getWorkUrl(HomeWorkConstantClass.STUDIMG_URL,WorkAndExercisectivity.this);
        Map<String ,String> map=XsyMap.getInterface();
        map.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(workUserId));
        OkGo.post(url).params(map).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
            XSYTools.i(s);
                //{"fieldName":"files/14001.jpg"}
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    String url=jsonObject.getString("fieldName");
                    url= XSYTools.getWorkUrl(url,WorkAndExercisectivity.this);
                    Glide.with(WorkAndExercisectivity.this).load(url).bitmapTransform(new RoundedCornersTransformation(WorkAndExercisectivity.this,30,0, RoundedCornersTransformation.CornerType.ALL)).into(image_headportrait);
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





    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }

    

    private class ListInfoShowAdapter extends BaseAdapter{
        private List<String>list;
        public ListInfoShowAdapter(List<String> infolist) {
            list=infolist;
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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=LayoutInflater.from(WorkAndExercisectivity.this);
            ViewHolder holder1;
            if(convertView==null){
                convertView=inflater.inflate(R.layout.info_show_item,parent,false);
                holder1=new ViewHolder();
                holder1.tt= (TextView) convertView.findViewById(R.id.infoshow_item_textView);
                convertView.setTag(holder1);
            }else {
                holder1= (ViewHolder) convertView.getTag();
            }
            holder1.tt.setText(list.get(position));
            return convertView;
        }
    }

    private class ViewHolder {
        TextView tt;
    }
}
