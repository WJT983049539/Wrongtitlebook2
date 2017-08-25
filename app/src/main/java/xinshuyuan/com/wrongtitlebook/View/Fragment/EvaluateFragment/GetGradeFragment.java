package xinshuyuan.com.wrongtitlebook.View.Fragment.EvaluateFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import work.StudentCommentConstantClass;
import xinshuyuan.com.wrongtitlebook.Model.Common.PerferenceService;
import xinshuyuan.com.wrongtitlebook.Model.Common.XsyMap;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.MyDatePickerDialog;
import xinshuyuan.com.wrongtitlebook.Model.Gradebean;
import xinshuyuan.com.wrongtitlebook.Persenter.Handler.SHOWCommentFragmentHandler;
import xinshuyuan.com.wrongtitlebook.R;

import static xinshuyuan.com.wrongtitlebook.R.id.score_textview;

/**
 * 显示成绩的碎片
 * Created by wjt on 2017/8/10.
 */

public class GetGradeFragment extends BaseFragment{
    private List<Gradebean> gradebeenlist =null;
    private Activity context;
    private SHOWCommentFragmentHandler lookAndStatisticsHandler;
    private Spinner spinner_type;
    private TextView popuTextView;
    private TextView popuTextView2;
    private String startTime="";
    private String endTime="";
    private ArrayAdapter arrayAdapter=null;
    private String[] aaa={"期中测试","期末测试","平时测试"};
    String NowType="";
    String NowTypee="期中测试";
    private Long UserId;
    int mYear, mMonth, mDay;
    private ListView getgrade_listview;

    private TextView check_data;

    public GetGradeFragment(){}
    public GetGradeFragment(Activity context, SHOWCommentFragmentHandler lookAndStatisticsHandler) {
        this.context=context;
        this.lookAndStatisticsHandler=lookAndStatisticsHandler;
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_layout_getgrede;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    protected void initData(Bundle arguments) {
        super.initData(arguments);
        gradebeenlist =new ArrayList<Gradebean>();

    }

    @Override
    protected void initView() {

        PerferenceService servier=new PerferenceService(getActivity());
        UserId=servier.getsharedPre().getLong("EvaluateId",0);
        super.initView();
        getgrade_listview=fVB(R.id.getgrade_listview);
        check_data=fVB(R.id.check_data);
        spinner_type=fVB(R.id.spinner_type);
        popuTextView=fVB(R.id.popuTextView);
        popuTextView2=fVB(R.id.popuTextView2);
        arrayAdapter= new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, aaa);
        spinner_type.setAdapter(arrayAdapter);
        spinner_type.setSelection(0,true);
        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);

    }

    @Override
    protected void setListener() {
        super.setListener();

        popuTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();

            }
        });
        popuTextView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog2();
            }
        });

        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                NowType=aaa[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        check_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(NowType.equals("")||startTime.equals("")||endTime.equals("")){
                    XSYTools.showToastmsg(getActivity(),"请补充完整条件");

                }else{
                    if(NowType.equals("期中测试")){
                        NowTypee="1";
                    }else if(NowType.equals("期末测试")){
                        NowTypee="2";
                    }else if(NowType.equals("平时测试")){
                        NowTypee="3";
                    }
                    HashMap map= XsyMap.getInterface();
                    map.put(StudentCommentConstantClass.PARAM_STUDENTID,String.valueOf(UserId));
                    map.put(StudentCommentConstantClass.PARAM_DATE,startTime+","+endTime);
                    map.put(StudentCommentConstantClass.PARAM_EXAMTYPES,NowTypee);
                    OkGo.post(XSYTools.getEvaluateUrl(StudentCommentConstantClass.GETSTUDENTSCORES_URL,context)).params(map).execute(new StringCallback() {
                        @Override
                        /**
                         *
                         [


                         {

                         "classId":6,
                         "createId":14,
                         "createName":"\u674e\u654f",
                         "createTime":{"date":21,"day":1,"hours":11,"minutes":6,"month":7,"nanos":0,"seconds":5,"time":1503284765000,"timezoneOffset":-480,"year":117},

                         "examName":"1",
                         "examTime":{"date":3,"day":1,"hours":0,"minutes":0,"month":6,"nanos":0,"seconds":0,"time":1499011200000,"timezoneOffset":-480,"year":117},

                         "examType":2,

                         "gradeId":13,

                         "id":2,

                         "schoolId":1,

                         "score":99,

                         "semesterId":2,

                         "semesterName":"\u7b2c\u4e8c\u5b66\u671f",

                         "stageId":1,

                         "stuId":6,

                         "stuName":"\u5f20\u7476",

                         "studNum":"1701002",

                         "subjectId":11,

                         "subjectName":"\u8bed\u6587",

                         "totalScore":1

                         }]
                         */
                        public void onSuccess(String s, Call call, Response response) {
                            XSYTools.i("得到的学生信息"+s);
                            gradebeenlist.clear();

                            try {
                                JSONArray array=new JSONArray(s);

                                for(int i=0;i<array.length();i++){
                                    JSONObject object=array.getJSONObject(i);
                                //班级id
                                Long classId=object.getLong("classId");
                                //代课老师ID
                                Long createId=object.getLong("createId");
                                //代课老师名字
                                String createName=object.getString("createName");
                                //创建时间
                                String createTime=object.getString("createTime");
                                //考试类型
                                Integer examType=object.getInt("examType");
                                //考试名字
                                String examName=object.getString("examName");
                                //考试时间
                                String examTime=object.getString("examTime");
                                //年级Id
                                Long gradeId=object.getLong("gradeId");
                                //本条成绩的id
                                Long ID=object.getLong("id");
                                //学校id
                                Long schoolId=object.getLong("schoolId");
                                //分数
                                Double score=object.getDouble("score");
                                //学期id
                                Long semesterId=object.getLong("semesterId");
                                //学期名字
                                String semesterName=object.getString("semesterName");
                                //学生Id
                                Long stuId=object.getLong("stuId");
                                //学生姓名
                                String stuName=object.getString("stuName");
                                //学生编号
                                String studNum=object.getString("studNum");
                                //学科名字
                                String subjectName=object.getString("subjectName");
                                //本学科满分
                                Long totalScore=object.getLong("totalScore");

                                    Gradebean gradebean=new Gradebean();
                                    gradebean.setExamType(examType);
                                    gradebean.setGrade(score);
                                    gradebean.setSemesterName(semesterName);
                                    gradebean.setStudNum(studNum);
                                    gradebean.setSubject(subjectName);
                                    gradebean.setStuName(stuName);
                                    gradebeenlist.add(gradebean);
                                }
                                getgrade_listview.setAdapter(new MyGradeAdapter(gradebeenlist));




                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            XSYTools.i("得到的学生信息请求异常"+e.toString());
                        }
                    });
                }
            }
        });
    }

    private void showDialog2() {
        new MyDatePickerDialog(getActivity(), mdateListener2, mYear, mMonth, mDay).show();
    }

    //显示日期选择
    private void showDialog() {
        new MyDatePickerDialog(getActivity(), mdateListener, mYear, mMonth, mDay).show();
    }

    @Override
    protected void onLazyLoad() {
        super.onLazyLoad();
    }
    /**
     * 设置日期 利用StringBuffer追加
     */
    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            String aa=new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay).append(" ").toString();
            popuTextView.setText(new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay).append(" "));
            startTime=aa;
        }
    };
    /**
     * 设置日期 利用StringBuffer追加
     */
    private DatePickerDialog.OnDateSetListener mdateListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            String bb=new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay).append(" ").toString();
            popuTextView2.setText(new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay).append(" "));
            endTime=bb;
        }
    };

    /**
     * 设置日期 利用StringBuffer追加
     */
    public void display() {

    }

    private class MyGradeAdapter extends BaseAdapter {
        private List<Gradebean> gradebeenlist;
        public MyGradeAdapter(List<Gradebean> gradebeenlists) {
            this.gradebeenlist=gradebeenlists;
        }

        @Override
        public int getCount() {
            return gradebeenlist.size();
        }

        @Override
        public Object getItem(int position) {
            return gradebeenlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=LayoutInflater.from(context);
            ViewHolder getGradeHolder;
            if(convertView==null){
                getGradeHolder=new ViewHolder();
                convertView=inflater.inflate(R.layout.get_grade_item_layout,parent,false);
                getGradeHolder.stuname_textview= (TextView) convertView.findViewById(R.id.stuname_textview);
                getGradeHolder.subject_textview= (TextView) convertView.findViewById(R.id.subject_textview);
                getGradeHolder.xueqi_textview= (TextView) convertView.findViewById(R.id.xueqi_textview);
                getGradeHolder.stunum_textview= (TextView) convertView.findViewById(R.id.stunum_textview);
                getGradeHolder.type_textview= (TextView) convertView.findViewById(R.id.type_textview);
                getGradeHolder.score_textview= (TextView) convertView.findViewById(score_textview);
                convertView.setTag(getGradeHolder);
            }else {
                getGradeHolder= (ViewHolder) convertView.getTag();
            }

            getGradeHolder.stuname_textview.setText(gradebeenlist.get(position).getStuName());
            getGradeHolder.subject_textview.setText(gradebeenlist.get(position).getSubject());
            getGradeHolder.xueqi_textview.setText(gradebeenlist.get(position).getSemesterName());
            getGradeHolder.stunum_textview.setText(gradebeenlist.get(position).getStudNum());

            int type=gradebeenlist.get(position).getExamType();
            if(type==1){
                getGradeHolder.type_textview.setText("期中考试");
            }else if(type==2){
                getGradeHolder.type_textview.setText("期末考试");
            }else if(type==3){
                getGradeHolder.type_textview.setText("平时小测");
            }
            getGradeHolder.score_textview.setText(gradebeenlist.get(position).getGrade().toString());

            return convertView;
        }
    }

    public class ViewHolder{
    TextView stuname_textview;
    TextView subject_textview;
    TextView xueqi_textview;
    TextView stunum_textview;
    TextView type_textview;
    TextView score_textview;
    }
}

