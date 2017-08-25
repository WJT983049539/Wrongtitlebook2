package com.xinshuyuan.xinshuyuanworkandexercise.presenter.evenlisting;

import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.ibpd.xsy.varDefine.GlobalVarDefine;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.Common;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.PerferenceService;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XsyMap;
import com.xinshuyuan.xinshuyuanworkandexercise.R;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment.WorkExerciseFillBlankFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;
import work.HomeWorkConstantClass;

/**
 * 练习填空题提交
 * Created by wjt on 2017/6/22.
 */

public class FillBlankOnClickListener implements View.OnClickListener {
   private WorkExerciseFillBlankFragment fragment;
    private LinearLayout rLayoutMainActivity;
    private String AnsWerAndFenxi;
    private Handler exerciseHandler;
    private String flagg;

    public FillBlankOnClickListener(WorkExerciseFillBlankFragment exerciseFillBlankFragment, Handler exerciseHandler, String model) {
        fragment=exerciseFillBlankFragment;
        this.exerciseHandler=exerciseHandler;
        flagg=model;
    }

    @Override
    public void onClick(View v) {
            rLayoutMainActivity=fragment.getViewById(fragment.getView(), R.id.fillblank_linear_left_top);
            View pv=fragment.getViewById(fragment.getView(), R.id.fillblank_blank_container);

            WebView web=fragment.getViewById(fragment.getView(), R.id.fillblank_itemPoint);
            rLayoutMainActivity.setFocusable(false);
            rLayoutMainActivity.clearFocus();//失去焦点

            Map<String,String> map=fragment.getAnswerMap();
            if(map!=null && map.size()>0){
                JSONObject j=new JSONObject();
                TestEntity te=fragment.getTestEntity();
                AnsWerAndFenxi= te.getAnswerAnalysis();
                if(te!=null){
                    try{
                        j.put("testId", te.getTestId());
                        JSONArray ja=new JSONArray();
                        for(String id:map.keySet()){
                            JSONObject sa=new JSONObject();
                            sa.put("id", GlobalVarDefine.SUBMIT_TEST_ID_PREFIX+ id);
                            sa.put("value",map.get(id));
                            ja.put(sa);
                        }
                        j.put("answers",ja);
                        //参数组合完成，准备发送
                        //参数组合完成，准备发送！！！！！！！json,储存在缓存里面

//                        TestCommon.submitTestAns(te.getTestId(),j.toString(),itemType,te.getTestType());

                        XsyMap<String,String> commitmap=XsyMap.getInterface();
                        //列表名
                        PerferenceService service =new PerferenceService(fragment.getActivity());
                        long studId=service.getsharedPre().getLong("WorkId",0);
                        commitmap.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(studId));
                        //试题id
                        commitmap.put(HomeWorkConstantClass.PARAM_TESTID,te.getTestId().toString());
                        //提交试题的json数据
                        commitmap.put(HomeWorkConstantClass.PARAM_STUANSWER,j.toString());
                        //答题类型
                        commitmap.put(GlobalVarDefine.PARAM_ITEMTYPE,te.getTestType().toString());
                        if(flagg.equals("exercise")){
                            commitmap.put(HomeWorkConstantClass.PARAM_STUWORKID,"0");
                            //学生作业id,这个说一律传0
                        }else  if(flagg.equals("work")) {
                            commitmap.put(HomeWorkConstantClass.PARAM_STUWORKID,te.getWorkId());

                        }else if(flagg.equals("neibuExercise")){
                            commitmap.put(HomeWorkConstantClass.PARAM_STUWORKID,"0");
                        }
                        String commotUrl= XSYTools.getWorkUrl(HomeWorkConstantClass.JUDGE_URL,fragment.getActivity());
                        OkGo.post(commotUrl).params(commitmap).execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {

                                XSYTools.i(s);

                                try {
                                    JSONObject object=new JSONObject(s);
                                    String answerAnalysis=object.getString("answerAnalysis");
                                    exerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.ONLY_ANSWER,AnsWerAndFenxi+","+flagg));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                XSYTools.i(e.toString());
                            }
                        });





                    }catch(Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }

            }else{
                XSYTools.showToastmsg(fragment.getActivity(),"没有填写答案?");
            }

    }
}
