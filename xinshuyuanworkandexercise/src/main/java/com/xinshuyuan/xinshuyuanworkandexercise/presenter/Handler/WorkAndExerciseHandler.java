package com.xinshuyuan.xinshuyuanworkandexercise.presenter.Handler;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.Common;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XsyMap;
import com.xinshuyuan.xinshuyuanworkandexercise.R;
import com.xinshuyuan.xinshuyuanworkandexercise.WorkAndExercisectivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;
import okhttp3.Call;
import okhttp3.Response;
import work.HomeWorkConstantClass;

/**首页Handler
 * Created by wjt on 2017/7/7.
 */

public class WorkAndExerciseHandler extends Handler{
    private WorkAndExercisectivity activity;
    public WorkAndExerciseHandler(WorkAndExercisectivity workAndExercisectivity) {
        activity=workAndExercisectivity;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        switch (msg.what){
            //当天作业在班中的统计
            case Common.RANKING_STATISTICS:
                final LineChartView  lineChar= (LineChartView) activity.findViewById(R.id.line_chart);
                String gettatisticsInfourl= XSYTools.getWorkUrl(HomeWorkConstantClass.TATAL_URL,activity);
                Map<String,String> tatisticsMap= XsyMap.getInterface();
                tatisticsMap.put(HomeWorkConstantClass.PARAM_STUDENTID,msg.obj.toString());
                OkGo.post(gettatisticsInfourl).params(tatisticsMap).execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        XSYTools.i("当天作业在班中的统计数据"+s);
                        LineChartData chartData;
                        List<String> xList=new LinkedList<String>();
                        List<Long>yList=new LinkedList<Long>();
                        JSONObject object= null;
                        try {
                            object = new JSONObject(s);

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
                        //折线图放入数据
                            List<PointValue> pointValues = new ArrayList<PointValue>();// 节点数据结合
                            Axis axisY = new Axis().setHasLines(true);// Y轴属性
                            Axis axisX = new Axis();// X轴属性
                            axisY.setName("排名");//设置Y轴显示名称
                            axisX.setName("姓名");//设置X轴显示名称
                            ArrayList<AxisValue> axisValuesX = new ArrayList<AxisValue>();//定义X轴刻度值的数据集合
                            ArrayList<AxisValue> axisValuesY = new ArrayList<AxisValue>();//定义Y轴刻度值的数据集合
                            axisX.setValues(axisValuesX);//为X轴显示的刻度值设置数据集合
                            axisY.setValues(axisValuesY);
                            axisX.setLineColor(Color.BLACK);// 设置X轴轴线颜色
                            axisY.setLineColor(Color.BLACK);// 设置Y轴轴线颜色
                            axisX.setTextColor(R.color.dark);// 设置X轴文字颜色
                            axisY.setTextColor(0xff5f00bd);// 设置Y轴文字颜色
                            axisX.setTextSize(8);// 设置X轴文字大小
                            axisX.setTypeface(Typeface.DEFAULT);// 设置文字样式，此处为默认
                            axisX.setHasTiltedLabels(false);// 设置X轴文字向左旋转45度
                            axisX.setHasLines(false);// 是否显示X轴网格线
                            axisY.setHasLines(true);// 是否显示Y轴网格线
                            axisX.setHasSeparationLine(true);// 设置是否有分割线
                            axisX.setInside(false);// 设置X轴文字是否在X轴内部
                            for (int j = 0; j < yList.size(); j++) {//循环为节点、X、Y轴添加数据
                                pointValues.add(new PointValue(j, yList.get(j)));// 添加节点数据
                                axisValuesY.add(new AxisValue(j).setValue(yList.get(j)));// 添加Y轴显示的刻度值
                                axisValuesX.add(new AxisValue(j).setValue(j).setLabel(xList.get(j)));// 添加X轴显示的刻度值
                            }
                            List<Line> lines = new ArrayList<Line>();//定义线的集合
                            Line line = new Line(pointValues);//将值设置给折线
                            line.setColor(0xff1f7dcb);// 设置折线颜色
//                            line.setStrokeWidth(float w);// 设置折线宽度
//                            line.setFilled(true);// 设置折线覆盖区域是否填充
                            line.setCubic(true);// 是否设置为立体的
                            line.setPointColor(0xffff9900);// 设置节点颜色
//                            line.setPointRadius(float s);// 设置节点半径
                            line.setHasLabels(true);// 是否显示节点数据
                            line.setHasLines(true);// 是否显示折线
                            line.setHasPoints(true);// 是否显示节点
                            line.setShape(ValueShape.CIRCLE);// 节点图形样式 DIAMOND菱形、SQUARE方形、CIRCLE圆形
                            line.setHasLabelsOnlyForSelected(false);// 隐藏数据，触摸可以显示
                            lines.add(line);// 将数据集合添加线


                            chartData = new LineChartData(lines);//将线的集合设置为折线图的数据
                            chartData.setAxisYLeft(axisY);// 将Y轴属性设置到左边
                            chartData.setAxisXBottom(axisX);// 将X轴属性设置到底部
//                            chartData.setBaseValue(20);// 设置反向覆盖区域颜色
                            chartData.setValueLabelBackgroundAuto(false);// 设置数据背景是否跟随节点颜色
                            chartData.setValueLabelBackgroundColor(Color.BLUE);// 设置数据背景颜色
                            chartData.setValueLabelBackgroundEnabled(false);// 设置是否有数据背景
                            chartData.setValueLabelsTextColor(Color.BLACK);// 设置数据文字颜色
                            chartData.setValueLabelTextSize(15);// 设置数据文字大小,节点上的
                            chartData.setValueLabelTypeface(Typeface.MONOSPACE);// 设置数据文字样式
                            lineChar.setLineChartData(chartData);//最后为图表设置数据，数据类型为LineChartData
                            lineChar.setInteractive(true);
                            lineChar.setZoomType(ZoomType.HORIZONTAL);
                            lineChar.setMaxZoom((float) 2);//最大方法比例
                            lineChar.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
                            lineChar.setVisibility(View.VISIBLE);


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
                break;

            //当天作业学科成绩跟全班平均成绩的对比
//            case Common.AVERAGE_GTADR_STATISTICS:


        }
    }
}
