package com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.AnswerEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.Common;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.PerferenceService;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XsyMap;
import com.xinshuyuan.xinshuyuanworkandexercise.R;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.BaseFragment;
import com.xinshuyuan.xinshuyuanworkandexercise.WorkAndExercisectivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;
import work.HomeWorkConstantClass;

/**
 * Created by Administrator on 2017/6/22.
 */

public class WorkANS_no_Fragment extends BaseFragment {
    public WorkANS_no_Fragment(){}
    private View Mview;
    private String answerAnalysis;
    private Handler exerciseHandler;
    private TextView ans_no_jiexi;
    private Button answer_no_return_button;
    private Button answer_no_come_onbutton;
    private Activity exerciseParmas;
    private String Model;
    //现在选择的id
    private String NOWprojectId="";
    //现在选择的教材id
    private String NOWbookId="";
    //现在选择的分册id
    private String NOWfenceId="";
    //现在选择的知识点id
    private String KnowId="";
    //userID
    private String UserId="";

    public WorkANS_no_Fragment(Handler layering_practiceHandler, Activity layering_practice_activity,String flagg) {
        super();
        exerciseHandler=layering_practiceHandler;
        exerciseParmas=layering_practice_activity;
//        Model=flagg;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Model=answerAnalysis.split(",")[1];
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Mview=inflater.inflate(R.layout.work_layout_answer_no,container,false);
            inint();
        return Mview;


    }

    private void inint() {
        ans_no_jiexi= (TextView) Mview.findViewById(R.id.ans_no_jiexi);
        ans_no_jiexi.setText(answerAnalysis);
        answer_no_return_button= (Button) Mview.findViewById(R.id.answer_no_return_button);
        answer_no_come_onbutton=(Button) Mview.findViewById(R.id.answer_no_come_onbutton);
        //返回列表
        answer_no_return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Model.equals("exercise")){
                String s=Common.getListJson();
                if(s.equals("")){
                    //如果为空转到主页面
                    Intent intentt=new Intent(getActivity(), WorkAndExercisectivity.class);
                    PerferenceService service=new PerferenceService(getActivity());
                    String userName=service.getsharedPre().getString("WORKuserName","");
                    Long userId=service.getsharedPre().getLong("WorkId",0);
                    int userType=service.getsharedPre().getInt("userType",0);
                    intentt.putExtra("userName",userName);
                    intentt.putExtra("userId",userId);
                    intentt.putExtra("userType",userType);
                    getActivity().startActivity(intentt);

                }else{
                    //转到题列表界面
                    exerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.WORK_SHOW_LIST,s));
                }

            //返回作业的试题列表
            }else if(Model.equals("work")){
                //得到projectId和userId
                    PerferenceService service=new PerferenceService(exerciseParmas);
                    String ProjectId=service.getsharedPre().getString("ProjectId","");
                    long userId=service.getsharedPre().getLong("WorkId",0);
                    exerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.SHOW_WORKLIST_INFO_FRAGMENT,ProjectId+","+userId));
                }else if(Model.equals("neibuExercise")){
                    PerferenceService service=new PerferenceService(exerciseParmas);
                    String ProjectId=service.getsharedPre().getString("ProjectId","");
                    long userId=service.getsharedPre().getLong("WorkId",0);
                    exerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.SHOW_WORKLIST_INFO_FRAGMENT,ProjectId+","+userId));

                }
            }
        });
        //继续答题
        answer_no_come_onbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //练习的继续答题
                if(Model.equals("exercise")){

                    //需要学生id 学科id 教材id 分册id 知识点id
                    NOWprojectId=Common.getNOWprojectId();
                    NOWbookId=Common.getNOWbookId();
                    NOWfenceId=Common.getNOWfenceId();
                    KnowId=Common.getknowId();
                    if("".equals(NOWprojectId)||"".equals(NOWbookId)||"".equals(NOWfenceId)||"".equals("")){
                        PerferenceService service=new PerferenceService(exerciseParmas);
                        NOWprojectId= service.getsharedPre().getString("NOWprojectId","");
                        NOWbookId= service.getsharedPre().getString("NOWbookId","");
                        NOWfenceId= service.getsharedPre().getString("NOWfenceId","");
                        KnowId= service.getsharedPre().getString("KnowId","");
                        UserId= String.valueOf(service.getsharedPre().getLong("WorkId",0));
                    }



                    XsyMap<String,String> map=XsyMap.getInterface();
                    map.put(HomeWorkConstantClass.PARAM_STUDENTID,UserId);
                    map.put(HomeWorkConstantClass.PARAM_SUBJECTID,NOWprojectId);
                    map.put(HomeWorkConstantClass.PARAM_BOOKID,NOWbookId);
                    map.put(HomeWorkConstantClass.PARAM_BOOKSECTIONID,NOWfenceId);
                    map.put(HomeWorkConstantClass.PARAM_KNOWLEDGEID,KnowId);
                    OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.GETPRACTICETEST_URL,exerciseParmas)).params(map).execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            XSYTools.i("得到下一题的json数据"+s);
                            TestEntity testEntity;

                            try {
                                JSONObject object=new JSONObject(s);
                                Integer TestType = object.getInt("itemType");
                                //得到题干
                                String point = object.getString("itemPoint");
                                //试题id
                                long testId=object.getLong("id");
                                String answerAnalysis=object.getString("answerAnalysis");
                                String Subject=object.getString("subjectName");
                                //装入javabean
                                testEntity =new TestEntity();
                                testEntity.setTestId(testId);
                                testEntity.setTestType(TestType);
                                testEntity.setPoint(point);
                                testEntity.setSubject(Subject);
                                testEntity.setAnswerAnalysis(answerAnalysis);
                                JSONArray jsonarray = object.getJSONArray("answerList");
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject obb = jsonarray.getJSONObject(i);
                                    long answerid = obb.getLong("id");
                                    long answertestId = obb.getLong("testId");
                                    String optionTitle = obb.getString("optionTitle");
                                    String optionAnswer = obb.getString("optionAnswer");
                                    Boolean isRealAnswer = obb.getBoolean("isRealAnswer");

                                    AnswerEntity answerEntity = new AnswerEntity();
                                    answerEntity.setId(answerid);
                                    answerEntity.setTestId(answertestId);
                                    answerEntity.setIsRealAnswer(isRealAnswer);
                                    answerEntity.setOptionAnswer(optionAnswer);
                                    answerEntity.setOptionTitle(optionTitle);
                                    testEntity.fillAnser(answerEntity);
                                }
                                exerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.PRIVIEW_SHOW_TEXT, testEntity));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            XSYTools.i("得到下一题报错数据"+e.toString());
                        }
                    });


                    //作业的继续答题
                } else if(Model.equals("work")){

                    PerferenceService service=new PerferenceService(exerciseParmas);
                    String ProjectId= service.getsharedPre().getString("ProjectId","");
                    String userId=String.valueOf(service.getsharedPre().getLong("WorkId",0));
                    String WorkID=String.valueOf(service.getsharedPre().getString("NowWorkId",""));

                    XsyMap<String,String>map=XsyMap.getInterface();
                    map.put(HomeWorkConstantClass.PARAM_STUDENTID,userId);
                    map.put(HomeWorkConstantClass.PARAM_SUBJECTID,String.valueOf(ProjectId));
                    //作业id
                    map.put(HomeWorkConstantClass.PARAM_STUWORKID,WorkID);
                    OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.GETTEST_URL,exerciseParmas)).params(map).execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            XSYTools.i("得到下一题的json数据"+s);
                            //没题了走试题列表
                            if(s.equals("")){
                                //得到projectId和userId
                                PerferenceService service=new PerferenceService(exerciseParmas);
                                String ProjectId=service.getsharedPre().getString("ProjectId","");
                                long userId=service.getsharedPre().getLong("WorkId",0);
                                exerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.SHOW_WORKLIST_INFO_FRAGMENT,ProjectId+","+userId));
                            }else{

                                TestEntity testEntity;
                                try {
                                    JSONObject object=new JSONObject(s);
                                    Integer TestType = object.getInt("itemType");
                                    //得到题干
                                    String point = object.getString("itemPoint");
                                    String WorkId=object.getString("stuWorkId");
                                    //试题id
                                    long testId=object.getLong("id");
                                    String answerAnalysis=object.getString("answerAnalysis");
                                    String Subject=object.getString("subjectName");
                                    //装入javabean
                                    testEntity =new TestEntity();
                                    testEntity.setTestId(testId);
                                    testEntity.setTestType(TestType);
                                    testEntity.setPoint(point);
                                    testEntity.setSubject(Subject);
                                    testEntity.setWorkId(WorkId);
                                    testEntity.setAnswerAnalysis(answerAnalysis);
                                    JSONArray jsonarray = object.getJSONArray("answerList");
                                    for (int i = 0; i < jsonarray.length(); i++) {
                                        JSONObject obb = jsonarray.getJSONObject(i);
                                        long answerid = obb.getLong("id");
                                        long answertestId = obb.getLong("testId");
                                        String optionTitle = obb.getString("optionTitle");
                                        String optionAnswer = obb.getString("optionAnswer");
                                        Boolean isRealAnswer = obb.getBoolean("isRealAnswer");

                                        AnswerEntity answerEntity = new AnswerEntity();
                                        answerEntity.setId(answerid);
                                        answerEntity.setTestId(answertestId);
                                        answerEntity.setIsRealAnswer(isRealAnswer);
                                        answerEntity.setOptionAnswer(optionAnswer);
                                        answerEntity.setOptionTitle(optionTitle);
                                        testEntity.fillAnser(answerEntity);
                                    }
                                    exerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.SHOW_SING_TEST, testEntity));


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                        }
                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            XSYTools.i("得到下一题报错数据"+e.toString());
                        }
                    });



                }else if(Model.equals("neibuExercise")){

                    PerferenceService service=new PerferenceService(getActivity());
                    String NowWorkId=service.getsharedPre().getString("NowWorkId","");
                    XSYTools.i("共享参数得到的workId为"+NowWorkId);
                    Long Id=service.getsharedPre().getLong("WorkId",0);
                    String SubjectId=service.getsharedPre().getString("ProjectId","");
                    HashMap<String,String> getpraciseMap= XsyMap.getInterface();
                    getpraciseMap.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(Id));
                    getpraciseMap.put(HomeWorkConstantClass.PARAM_STUWORKID,NowWorkId);
                    getpraciseMap.put(HomeWorkConstantClass.PARAM_SUBJECTID,SubjectId);

                    OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.GETPRACISEINFO_URL,exerciseParmas)).params(getpraciseMap).execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            XSYTools.i("得到内部练习需要的参数"+s);

                            try {
                                JSONObject object=new JSONObject(s);
                                Long studentId=object.getLong("studentId");
                                Long subjectId=object.getLong("subjectId");
                                Long bookId=object.getLong("bookId");
                                Long bookSectionId=object.getLong("bookSectionId");
                                Long knowledgePointId=object.getLong("knowledgePointId");
                                Double difficulty=object.getDouble("difficulty");

                                //得到这些参数以后用这些参数取请求试题然后显示页面

                                HashMap<String,String> map=XsyMap.getInterface();
                                map.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(studentId));
                                map.put(HomeWorkConstantClass.PARAM_SUBJECTID,String.valueOf(subjectId));
                                map.put(HomeWorkConstantClass.PARAM_BOOKID,String.valueOf(bookId));
                                map.put(HomeWorkConstantClass.PARAM_BOOKSECTIONID,String.valueOf(bookSectionId));
                                map.put(HomeWorkConstantClass.PARAM_KNOWLEDGEID,String.valueOf(knowledgePointId));
                                map.put(HomeWorkConstantClass.PARAM_DIFFICULT,String.valueOf(difficulty));


                                OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.PRACTICES_URL,exerciseParmas)).params(map).execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        XSYTools.i("得到一道试题的json"+s);
                                        TestEntity testEntity;

                                        try {
                                            JSONObject object=new JSONObject(s);
                                            Integer TestType = object.getInt("itemType");
                                            //得到题干
                                            String point = object.getString("itemPoint");
                                            //试题id
                                            long testId=object.getLong("id");
                                            String answerAnalysis=object.getString("answerAnalysis");
                                            String Subject=object.getString("subjectName");
                                            Long workId=object.getLong("stuWorkId");
                                            //装入javabean
                                            testEntity =new TestEntity();
                                            testEntity.setTestId(testId);
                                            testEntity.setTestType(TestType);
                                            testEntity.setPoint(point);
                                            testEntity.setSubject(Subject);
                                            testEntity.setWorkId(String.valueOf(workId));
                                            testEntity.setAnswerAnalysis(answerAnalysis);
                                            JSONArray jsonarray = object.getJSONArray("answerList");
                                            for (int i = 0; i < jsonarray.length(); i++) {
                                                JSONObject obb = jsonarray.getJSONObject(i);
                                                long answerid = obb.getLong("id");
                                                long answertestId = obb.getLong("testId");
                                                String optionTitle = obb.getString("optionTitle");
                                                String optionAnswer = obb.getString("optionAnswer");
                                                Boolean isRealAnswer = obb.getBoolean("isRealAnswer");

                                                AnswerEntity answerEntity = new AnswerEntity();
                                                answerEntity.setId(answerid);
                                                answerEntity.setTestId(answertestId);
                                                answerEntity.setIsRealAnswer(isRealAnswer);
                                                answerEntity.setOptionAnswer(optionAnswer);
                                                answerEntity.setOptionTitle(optionTitle);
                                                testEntity.fillAnser(answerEntity);
                                            }
                                            exerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.WORK_TEST_NEIBU_EXERCISE, testEntity));

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    @Override
                                    public void onError(Call call, Response response, Exception e) {
                                        super.onError(call, response, e);
                                        XSYTools.i("获取一道试题json错误"+e.toString());

                                    }
                                });





                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            XSYTools.i("得到内部练习需要的参数服务器返回错误"+e.toString());
                        }
                    });


                }




//                XSYTools.showToastmsg(getActivity(),"暂时维修中。。。");
            }
        });

    }

    public void setInfo(String s) {
        answerAnalysis=s;
    }
}
