package com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.AnswerEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.Common;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.PerferenceService;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.PressureButton;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.WorkInfoBean;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XsyMap;
import com.xinshuyuan.xinshuyuanworkandexercise.R;
import com.xinshuyuan.xinshuyuanworkandexercise.presenter.Handler.ProjectWorkShowActivityHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;
import work.HomeWorkConstantClass;

/**
 * 显示统计选择界面，有统计和分层练习
 * Created by wjt on 2017/7/6.
 */

public class SelectStatisticeFragment extends Fragment{
    private View mView;
    private WorkInfoBean workinfobean;
    private PressureButton staticesButton;
    private PressureButton ExerciseButton;
    private ProjectWorkShowActivityHandler projectWorkShowActivityHandler;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workinfobean= (WorkInfoBean) getArguments().getSerializable("workinfobean");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView= inflater.inflate(R.layout.layout_fragment_selestatistice,container,false);
        staticesButton= (PressureButton) mView.findViewById(R.id.staticesButton);
        ExerciseButton= (PressureButton) mView.findViewById(R.id.ExerciseButton);
        //统计事件
        staticesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示统计表格界面
                projectWorkShowActivityHandler.sendMessage(XSYTools.makeNewMessage(Common.WORK_TEST_STATISTICE,workinfobean));
            }
        });
        //分册练习事件
        ExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerferenceService service=new PerferenceService(getActivity());
                Long Id=service.getsharedPre().getLong("WorkId",0);
                String SubjectId=service.getsharedPre().getString("ProjectId","");
                HashMap<String,String> getpraciseMap= XsyMap.getInterface();
                getpraciseMap.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(Id));
                getpraciseMap.put(HomeWorkConstantClass.PARAM_STUWORKID,String.valueOf(workinfobean.getWorkId()));
                getpraciseMap.put(HomeWorkConstantClass.PARAM_SUBJECTID,SubjectId);

                OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.GETPRACISEINFO_URL,getActivity())).params(getpraciseMap).execute(new StringCallback() {
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

                            OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.PRACTICES_URL,getActivity())).params(map).execute(new StringCallback() {
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
                                        projectWorkShowActivityHandler.sendMessage(XSYTools.makeNewMessage(Common.WORK_TEST_NEIBU_EXERCISE, testEntity));

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




                projectWorkShowActivityHandler.sendMessage(XSYTools.makeNewMessage(Common.WORK_TEST_NEIBU_EXERCISE,workinfobean));
            }
        });



        return mView;
    }

    public void setInfo(ProjectWorkShowActivityHandler projectWorkShowActivityHandler) {
        this.projectWorkShowActivityHandler=projectWorkShowActivityHandler;
    }
}
