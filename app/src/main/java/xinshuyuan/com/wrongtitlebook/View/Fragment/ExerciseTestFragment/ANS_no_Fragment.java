package xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ibpd.xsy.varDefine.WrongQuestionConstantClass;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.AnswerEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.GetUserInfoClass;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;
import xinshuyuan.com.wrongtitlebook.Model.Common.Common;
import xinshuyuan.com.wrongtitlebook.Model.Common.ExerciseParmas;
import xinshuyuan.com.wrongtitlebook.Model.Common.PerferenceService;
import xinshuyuan.com.wrongtitlebook.Model.Common.XsyMap;
import xinshuyuan.com.wrongtitlebook.Persenter.Handler.ExerciseHandler;
import xinshuyuan.com.wrongtitlebook.R;
import xinshuyuan.com.wrongtitlebook.View.Activity.ExerciseTestActivity;
import xinshuyuan.com.wrongtitlebook.View.Activity.ShowWrongBookslistActivity;
import xinshuyuan.com.wrongtitlebook.View.Fragment.BaseFragment;

/**
 * Created by Administrator on 2017/6/22.
 */

public class ANS_no_Fragment extends BaseFragment{
    private View Mview;
    private String answerAnalysis;
    private ExerciseHandler exerciseHandler;
    private TextView ans_no_jiexi;
    private Button answer_no_return_button;
    private Button answer_no_come_onbutton;
    private ExerciseParmas exerciseParmas;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Mview=inflater.inflate(R.layout.layout_answer_no,container,false);
            inint();
        return Mview;


    }

    private void inint() {
        ans_no_jiexi= (TextView) Mview.findViewById(R.id.ans_no_jiexi);
        ans_no_jiexi.setText(answerAnalysis);
        answer_no_return_button= (Button) Mview.findViewById(R.id.answer_no_return_button);
        answer_no_come_onbutton=(Button) Mview.findViewById(R.id.answer_no_come_onbutton);
        //结束答题
        answer_no_return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new Thread(new CheckLoginRunnable(getActivity())).start();
                //转到错题列表界面
                PerferenceService service=new PerferenceService(getActivity());
                String Project=service.getsharedPre().getString("Project","");
                Intent intent=new Intent(getActivity(),ShowWrongBookslistActivity.class);
                intent.putExtra("project",Project);
                getActivity().startActivity(intent);

            }
        });
        //继续答题
        answer_no_come_onbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    exerciseParmas =Common.getExerciseParmas();
                    Integer subject=exerciseParmas.getSubjectId();
                    Integer gradeId=exerciseParmas.getGradeId();
                    Long KnowledgePointId=exerciseParmas.getKnowledgePointId();
                    String different=exerciseParmas.getDifferent();


                    String getTestInfoUrl= XSYTools.getWrongUrl(WrongQuestionConstantClass.PRACTICE_URL,getActivity());

                    XsyMap map=XsyMap.getInterface();

                    Long studId= new GetUserInfoClass(getActivity()).getUserId();
                    map.put(WrongQuestionConstantClass.PARAM_STUDID,String.valueOf(studId));//学生id
                    map.put(WrongQuestionConstantClass.PARAM_SUBJECTID, String.valueOf(subject));//学科id
                    map.put(WrongQuestionConstantClass.PARAM_GRADEID,String.valueOf(gradeId));//年级id
                    map.put(WrongQuestionConstantClass.PARAM_KNOWLEDGEID,String.valueOf(KnowledgePointId));
                    map.put(WrongQuestionConstantClass.PARAM_DIFFICULT,different);

                    OkGo.post(getTestInfoUrl).params(map).execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            XSYTools.i(s);


                            try {
                                //得到试题信息
                                JSONObject object=new JSONObject(s);

                                if(object.has("status")){
                                    String aa = object.getString("status");
                                    if(aa.equals("99")){
                                        exerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.VERY_GOOD,""));
                                        return;
                                    }else if(aa.equals("-3")){
                                        XSYTools.showToastmsg(getActivity(),"试题不存在，获取错误");
                                        return;
                                    }
                                }

                                //本题类型
                                Integer TestType = object.getInt("itemType");
                                //得到题干
                                String point = object.getString("itemPoint");
                                //试题id
                                long testId=object.getLong("id");
                                double different=object.getDouble("difficulty");
                                String answerAnalysis=object.getString("answerAnalysis");

                                String Subject=object.getString("subjectName");
                                //装入javabean
                                TestEntity testEntity =new TestEntity();
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
                                Activity avt=ANS_no_Fragment.this.getActivity();
                                if(avt instanceof ExerciseTestActivity){
                                    ExerciseTestActivity exerciseActivity= (ExerciseTestActivity) avt;
                                    exerciseActivity.gbDifferent(different);

                                }
                                exerciseParmas.setDifferent(String.valueOf(different));
                                exerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.EXERCISE_TEST_SHOW,testEntity));


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

            }
        });

    }

    public void setInfo(String s, ExerciseHandler exerciseHandlers) {

        answerAnalysis=s;
        exerciseHandler=exerciseHandlers;
    }
}
