package xinshuyuan.com.wrongtitlebook.Persenter.evenListener;

import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.ibpd.xsy.varDefine.GlobalVarDefine;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.GetUserInfoClass;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;
import work.HomeWorkConstantClass;
import xinshuyuan.com.wrongtitlebook.Model.Common.Common;
import xinshuyuan.com.wrongtitlebook.Model.Common.PerferenceService;
import xinshuyuan.com.wrongtitlebook.Model.Common.XsyMap;
import xinshuyuan.com.wrongtitlebook.Persenter.Handler.ExerciseHandler;
import xinshuyuan.com.wrongtitlebook.R;
import xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment.ExerciseFillBlankFragment;

/**
 * 练习填空题提交
 * Created by wjt on 2017/6/22.
 */

public class FillBlankOnClickListener implements View.OnClickListener {
   private ExerciseFillBlankFragment fragment;
    private LinearLayout rLayoutMainActivity;
    private String AnsWerAndFenxi;
    private ExerciseHandler exerciseHandler;

    public FillBlankOnClickListener(ExerciseFillBlankFragment exerciseFillBlankFragment, ExerciseHandler exerciseHandler) {
        fragment=exerciseFillBlankFragment;
        this.exerciseHandler=exerciseHandler;
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



                        PerferenceService service=new PerferenceService(fragment.getActivity());
                        String subjectIdd=service.getsharedPre().getString("WrongProjectId","");

                        XsyMap<String,String> commitmap=XsyMap.getInterface();
                        //列表名
                        Long studId= new GetUserInfoClass(fragment.getActivity()).getUserId();
                        commitmap.put(GlobalVarDefine.PARAM_USER_ID,String.valueOf(studId));
                        //试题id
                        commitmap.put(GlobalVarDefine.PARAM_TESTID,te.getTestId().toString());
                        //提交试题的json数据
                        commitmap.put(GlobalVarDefine.PARAM_TESTANS_JSON,j.toString());
                        commitmap.put(HomeWorkConstantClass.PARAM_SUBJECTID,subjectIdd);
                        //答题类型
                        commitmap.put(GlobalVarDefine.PARAM_ITEMTYPE,te.getTestType().toString());
                        String commotUrl= XSYTools.getWrongUrl(GlobalVarDefine.TEST_SUBMIT_ANSWER_URL,fragment.getActivity());
                        OkGo.post(commotUrl).params(commitmap).execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {

                                XSYTools.i(s);

                                try {
                                    JSONArray array=new JSONArray(s);
                                    JSONObject object=array.getJSONObject(0);
                                    Boolean flag=object.getBoolean("msg");
                                    if(flag){
                                        exerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.ANS_OK,AnsWerAndFenxi));

                                    }else {
                                        exerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.ANS_NO,AnsWerAndFenxi));
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
