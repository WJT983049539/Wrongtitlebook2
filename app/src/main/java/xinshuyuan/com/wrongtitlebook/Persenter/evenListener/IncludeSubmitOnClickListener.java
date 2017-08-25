package xinshuyuan.com.wrongtitlebook.Persenter.evenListener;

import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.ibpd.xsy.varDefine.GlobalVarDefine;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.GetUserInfoClass;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;
import work.HomeWorkConstantClass;
import xinshuyuan.com.wrongtitlebook.Model.Common.Common;
import xinshuyuan.com.wrongtitlebook.Model.Common.PerferenceService;
import xinshuyuan.com.wrongtitlebook.Model.Common.XsyMap;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.ExerciseIncludeCustomLayout.ExerciseFillBlankLayout;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.ExerciseIncludeCustomLayout.ExerciseMultiSelectLayout;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.ExerciseIncludeCustomLayout.ExerciseSingleSelectLayout;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.ExerciseIncludeCustomLayout.ExerciseYesOrNoSelectLayout;
import xinshuyuan.com.wrongtitlebook.Persenter.Handler.ExerciseHandler;
import xinshuyuan.com.wrongtitlebook.R;
import xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment.ExerciseNESTFragment;

/**
 * 潜逃题提交
 * Created by Administrator on 2017/6/22.
 */

public class IncludeSubmitOnClickListener implements View.OnClickListener {
    private ExerciseNESTFragment fragment;
    private ExerciseHandler exerciseHandler;
    public IncludeSubmitOnClickListener(ExerciseNESTFragment exerciseNESTFragment, ExerciseHandler exerciseHandler) {
        fragment=exerciseNESTFragment;
        this.exerciseHandler=exerciseHandler;
    }

    @Override
    public void onClick(View v) {

        WebView wv=fragment.getViewById(fragment.getmView(), R.id.include_itemPoint);
        if(wv!=null){
            wv.setFocusable(true);
        }else{
            XSYTools.showToastmsg(fragment.getActivity(),"焦点变化失败");
            return;
        }
        JSONArray ja = new JSONArray();// 所有答案的一个json数组，下面是各个题型的答题提取/组织实现
        try {
            // 多项选择题的答题提取和答题信息组织的实现
            if(!processMultiSelect(ja)){
                return;
            }
            // 单项选择题的答题提取和答题信息组织的实现
            if(!processSingleSelect(ja)){
                return;
            }
            // 选择题的答题提取和答题信息组织的实现
            if(!processYesOrNoSelect(ja)){
                return;
            }
            // 填空题的答题提取和答题信息组织的实现
            if(!processFillBlank(ja)){
                return;
            }
            Log.d("include test", ja.toString());
            //参数组合完成，准备发送
            //存入缓存

            PerferenceService service=new PerferenceService(fragment.getActivity());
            String subjectIdd=service.getsharedPre().getString("WrongProjectId","");
            XsyMap<String,String> commitmap=XsyMap.getInterface();
            Long studId= new GetUserInfoClass(fragment.getActivity()).getUserId();
            commitmap.put(GlobalVarDefine.PARAM_USER_ID,String.valueOf(studId));
            commitmap.put(HomeWorkConstantClass.PARAM_SUBJECTID,subjectIdd);
            //试题id
            commitmap.put(GlobalVarDefine.PARAM_TESTID,fragment.getTestEntity().getTestId().toString());
            //提交试题的json数据
            commitmap.put(GlobalVarDefine.PARAM_TESTANS_JSON,ja.toString());
            //答题类型
            commitmap.put(GlobalVarDefine.PARAM_ITEMTYPE,fragment.getTestEntity().getTestType().toString());
            String commotUrl= XSYTools.getWrongUrl(GlobalVarDefine.TEST_SUBMIT_ANSWER_URL,fragment.getActivity());

            OkGo.post(commotUrl).params(commitmap).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    XSYTools.i(s);

                    exerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.ONLY_ANSWER,fragment.getTestEntity().getAnswerAnalysis()));



//                    try {
//                        JSONArray array=new JSONArray(s);
//                        JSONObject object=array.getJSONObject(0);
//                        Boolean flag=object.getBoolean("msg");
//                        if(flag){
//                            exerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.ANS_OK,fragment.getTestEntity().getAnswerAnalysis()));
//
//                        }else {
//                            exerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.ANS_NO,fragment.getTestEntity().getAnswerAnalysis()));
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {

                    super.onError(call, response, e);
                }
            });

//            TestCommon.submitTestAns(fragment.getTestEntity().getTestId(),ja.toString(),fragment.getItemType(),fragment.getTestEntity().getTestType());









            //fragment.setflag(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//				}catch(Exception ex){ex.printStackTrace();}
//			}
//
//		}else{
//			comm.toast("没有填写答案?");
//		}

    }
    private Boolean processFillBlank(JSONArray ja) throws Exception {
        List<ExerciseFillBlankLayout> lst = fragment.getFblList();
        if (lst == null || lst.size() == 0) {
//			comm.toast("多项选择题似乎没有答案可做?");
//			return;
        }
        for (ExerciseFillBlankLayout l : lst) {
            View v=l.findFocus();
            if(v!=null){
                //comm.toast("手動失去焦點");
                v.setFocusable(false);
            }
        }
        for (ExerciseFillBlankLayout l : lst) {
            JSONObject o = new JSONObject();
            o.put("testId", l.getTestId());
            o.put("testType", l.getTestType());
            Map<String, String> ansMap = l.getAnswerMap();
            JSONArray ansArray = new JSONArray();
            if (ansMap.size() == 0) {
                XSYTools.showToastmsg(fragment.getActivity(),"先把填空题答完再提交吧");
                return false;
            }
            for (String ansId : ansMap.keySet()) {
                //comm.toast(GlobalVarDefine.SUBMIT_TEST_ID_PREFIX+ansId+"   "+ansMap.get(ansId));
                JSONObject ans = new JSONObject();
                ans.put("id", GlobalVarDefine.SUBMIT_TEST_ID_PREFIX+ansId);
                ans.put("value", ansMap.get(ansId));
                ansArray.put(ans);
            }
            o.put("answers", ansArray);
            ja.put(o);
        }
        return true;
    }
    private Boolean processMultiSelect(JSONArray ja) throws Exception {
        List<ExerciseMultiSelectLayout> lst = fragment.getMslList();
        if (lst == null || lst.size() == 0) {
//			comm.toast("多项选择题似乎没有答案可做?");
//			return;
        }
        for (ExerciseMultiSelectLayout l : lst) {
            JSONObject o = new JSONObject();
            o.put("testId", l.getTestId());
            o.put("testType", l.getTestType());
            Map<String, String> ansMap = l.getAnswerMap();
            JSONArray ansArray = new JSONArray();
            if (ansMap.size() == 0) {
                XSYTools.showToastmsg(fragment.getActivity(),"先把多选题答完再提交吧");
                return false;
            }
            for (String ansId : ansMap.keySet()) {
                JSONObject ans = new JSONObject();
                ans.put("id", GlobalVarDefine.SUBMIT_TEST_ID_PREFIX+ansId);
                ans.put("value", "");
                ansArray.put(ans);
            }
            o.put("answers", ansArray);
            ja.put(o);// 把一个多项选择题的答案放入到答案的array中
        }
        return true;
    }
    private Boolean processSingleSelect(JSONArray ja) throws Exception {
        List<ExerciseSingleSelectLayout> lst = fragment.getSslList();
        if (lst == null || lst.size() == 0) {
//			comm.toast("多项选择题似乎没有答案可做?");
//			return;
        }
        for (ExerciseSingleSelectLayout l : lst) {
            Map<String, String> map = l.getAnswerMap();
            JSONObject o = new JSONObject();
            o.put("testId", l.getTestId());
            o.put("testType", l.getTestType());
            Map<String, String> ansMap = l.getAnswerMap();
            JSONArray ansArray = new JSONArray();
            if (ansMap.size() == 0) {
                XSYTools.showToastmsg(fragment.getActivity(),"先把单选题答完再提交吧");
                return false;
            }
            for (String ansId : ansMap.keySet()) {
                JSONObject ans = new JSONObject();
                ans.put("id", GlobalVarDefine.SUBMIT_TEST_ID_PREFIX+ansId);
                ans.put("value", "");
                ansArray.put(ans);
            }
            o.put("answers", ansArray);
            ja.put(o);// 把一个多项选择题的答案放入到答案的array中
        }
        return true;
    }
    private Boolean processYesOrNoSelect(JSONArray ja) throws Exception {
        List<ExerciseYesOrNoSelectLayout> lst = fragment.getYonslList();
        if (lst == null || lst.size() == 0) {
//			comm.toast("多项选择题似乎没有答案可做?");
//			return;
        }
        for (ExerciseYesOrNoSelectLayout l : lst) {
            Map<String, String> map = l.getAnswerMap();
            JSONObject o = new JSONObject();
            o.put("testId", l.getTestId());
            o.put("testType", l.getTestType());
            Map<String, String> ansMap = l.getAnswerMap();
            JSONArray ansArray = new JSONArray();
            if (ansMap.size() == 0) {
                XSYTools.showToastmsg(fragment.getActivity(),"先把选择题答完再提交吧");
                return false;
            }
            for (String ansId : ansMap.keySet()) {
                JSONObject ans = new JSONObject();
                ans.put("id", GlobalVarDefine.SUBMIT_TEST_ID_PREFIX+ansId);
                ans.put("value", "");
                ansArray.put(ans);
            }


            o.put("answers", ansArray);
            ja.put(o);// 把一个多项选择题的答案放入到答案的array中
        }
        return true;
    }
}
