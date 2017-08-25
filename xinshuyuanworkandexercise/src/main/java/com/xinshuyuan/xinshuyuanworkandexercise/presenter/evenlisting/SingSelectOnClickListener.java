package com.xinshuyuan.xinshuyuanworkandexercise.presenter.evenlisting;

import android.os.Handler;
import android.view.View;

import com.ibpd.xsy.varDefine.GlobalVarDefine;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.Common;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.PerferenceService;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XsyMap;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment.WorkExerciseSelsctFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;
import work.HomeWorkConstantClass;

/**
 * Created by Administrator on 2017/6/21.
 */

public class SingSelectOnClickListener implements View.OnClickListener {
    private WorkExerciseSelsctFragment fragment;
    private Long testId;
    private String AnsWerAndFenxi;
    private Handler exerciseHandler;
    private String flagg;

    public SingSelectOnClickListener(WorkExerciseSelsctFragment exerciseSelsctFragment, Long testId, Handler exerciseHandler, String model) {
        this.fragment=exerciseSelsctFragment;
        this.testId=testId;
        this.exerciseHandler=exerciseHandler;
        flagg=model;
    }

    @Override
    public void onClick(View v) {

        Map<String,String> map=fragment.getAnswerMap();
         if(map!=null && map.size()>0){
            JSONObject j=new JSONObject();
            Integer itemType=GlobalVarDefine.TESTTYPE_CENTER;;
            TestEntity te=fragment.getTestEntity();
            AnsWerAndFenxi= te.getAnswerAnalysis();
            if(te!=null){
                try{
                    j.put("testId", te.getTestId());
                    JSONArray ja=new JSONArray();
                    for(String id:map.keySet()){
                        JSONObject sa=new JSONObject();
                        sa.put("id", GlobalVarDefine.SUBMIT_TEST_ID_PREFIX+ id);
                        System.out.println(id);
                        sa.put("value",map.get(id));
                        ja.put(sa);
                    }
                    j.put("answers",ja);


//				System.out.println("组合完成");
                    //参数组合完成，准备发送！！！！！！！json,储存在缓存里面


                    XsyMap<String,String> commitmap=XsyMap.getInterface();
                    //列表名
                    PerferenceService service =new PerferenceService(fragment.getActivity());
                    long stuid=service.getsharedPre().getLong("WorkId",0);

//                    Long studId= new GetUserInfoClass(fragment.getActivity()).getUserId();
                    commitmap.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(stuid));
                    //试题id
                    commitmap.put(HomeWorkConstantClass.PARAM_TESTID,testId.toString());
                    //提交试题的json数据
                    commitmap.put(HomeWorkConstantClass.PARAM_STUANSWER,j.toString());
                    if(flagg.equals("exercise")){
                        commitmap.put(HomeWorkConstantClass.PARAM_STUWORKID,"0");
                        //学生作业id,这个说一律传0
                    }else if(flagg.equals("work")){
                        commitmap.put(HomeWorkConstantClass.PARAM_STUWORKID,te.getWorkId());
                    }else if(flagg.equals("neibuExercise")){
                        commitmap.put(HomeWorkConstantClass.PARAM_STUWORKID,"0");
                    }


                    //答题类型
                    commitmap.put(HomeWorkConstantClass.PARAM_ITEMTYPE,te.getTestType().toString());
                    String commotUrl= XSYTools.getWorkUrl(HomeWorkConstantClass.JUDGE_URL,fragment.getActivity());
                    OkGo.post(commotUrl).params(commitmap).execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {

                            XSYTools.i(s);

                            try {
                                JSONArray array=new JSONArray(s);
                                JSONObject object=array.getJSONObject(0);
                                Boolean flag=object.getBoolean("state");
                                object.getString("answerAnalysis");
                                if(flag){
                                    exerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.ANS_OK,AnsWerAndFenxi+","+flagg));

                                }else {
                                     exerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.ANS_NO,AnsWerAndFenxi+","+flagg));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            XSYTools.i(e.toString());
                        }
                    });
                }catch(Exception ex){
                    ex.printStackTrace();}
            }

        }else{
            XSYTools.showToastmsg(fragment.getActivity(),"没有填写答案?");
        }

    }
}
