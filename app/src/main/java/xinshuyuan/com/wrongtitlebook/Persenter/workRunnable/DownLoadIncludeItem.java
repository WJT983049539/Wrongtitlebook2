package xinshuyuan.com.wrongtitlebook.Persenter.workRunnable;

import android.util.Log;

import com.ibpd.xsy.varDefine.GlobalVarDefine;
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
import xinshuyuan.com.wrongtitlebook.Model.Common.XsyMap;
import xinshuyuan.com.wrongtitlebook.Persenter.Handler.PreViewTestHandler;
import xinshuyuan.com.wrongtitlebook.View.Activity.PreviewTestQuestionActivity;

/**
 * 下载嵌套题的小题
 * Created by Administrator on 2017/6/15.
 */

public class DownLoadIncludeItem implements Runnable {
    private PreviewTestQuestionActivity myActivity;
    private PreViewTestHandler preViewTestHandler;
    private String id;
    public DownLoadIncludeItem(PreviewTestQuestionActivity myActivity, PreViewTestHandler preViewTestHandler, String id) {
            this.myActivity=myActivity;
        this.preViewTestHandler=preViewTestHandler;
        this.id=id;


    }
    @Override
    public void run() {


        XsyMap paramsMap=getParamMap(Long.valueOf(id));
        OkGo.post(XSYTools.getWrongUrl(GlobalVarDefine.GET_TESTENTITY_URL,myActivity)).params(paramsMap).execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        XSYTools.i(s);
                        if(XSYTools.isEmpty(s.trim()) || s.equals("{}")){
                            XSYTools.showToastmsg(myActivity,"获取的试题信息为空");
                        }
                        try {
                            JSONObject testJson=new JSONObject(s);
                            if(testJson.has("status") && testJson.has("msg")){
                                XSYTools.i(testJson.getString("msg"));
                                return ;
                            }
                            String id=XSYTools.getStringValFromJSONObject(testJson, "id");
                            String itemPoint=XSYTools.getStringValFromJSONObject(testJson, "itemPoint");
                            String itemType=XSYTools.getStringValFromJSONObject(testJson, "itemType");
                            String subject=XSYTools.getStringValFromJSONObject(testJson, "subject");
                            JSONArray answerList=(JSONArray) XSYTools.getValFromJSONObject(testJson, "answerList");
                            answerList=answerList==null?new JSONArray():answerList;
                            Log.d("testEntity", "itemPoint="+itemPoint+"\titemType="+itemType+"\tsubject="+subject+"\tanswerList="+answerList.length());

                            //获取到之后，就该给activity传了
                            if(XSYTools.isNumeric(id) && XSYTools.isNumeric(itemType)){
                                TestEntity te=new TestEntity();
                                te.setPoint(itemPoint);
                                te.setSubject(subject);
                                te.setTestId(Long.valueOf(id));
                                te.setTestType(Integer.valueOf(itemType));
                                for(Integer i=0;i<answerList.length();i++){
                                    JSONObject o=answerList.getJSONObject(i);
                                    if(o!=null){
                                        Boolean isRealAnswer=(Boolean) XSYTools.getValFromJSONObject(o, "isRealAnswer");
                                        String optionTitle=XSYTools.getStringValFromJSONObject(o, "optionTitle");
                                        String optionAnswer=XSYTools.getStringValFromJSONObject(o, "optionAnswer");
                                        String optionId=XSYTools.getStringValFromJSONObject(o, "id");
                                        AnswerEntity ae=new AnswerEntity();
                                        ae.setIsRealAnswer(isRealAnswer);
                                        ae.setOptionAnswer(optionAnswer);
                                        ae.setOptionTitle(optionTitle);
                                        ae.setTestId(te.getTestId());
                                        if(XSYTools.isNumeric(optionId)){
                                            ae.setId(Long.valueOf(optionId));
                                        }
                                        te.fillAnser(ae);
                                    }
                                }
                                preViewTestHandler.sendMessage(XSYTools.makeNewMessage(Common.VIEW_INCLUDTEST_SUBTEST,te));
                            }else{
                                Log.e("testInfo", "id["+id+"]或itemType["+itemType+"]无法转换为数字");
                            }
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

    private XsyMap getParamMap(Long testId){
        XsyMap<String,String> paramsMap=XsyMap.getInterface();
        Long studId= new GetUserInfoClass(myActivity).getUserId();
        paramsMap.put(GlobalVarDefine.PARAM_USERTYPE, GlobalVarDefine.USERTYPE_STUDENT.toString());
        paramsMap.put(GlobalVarDefine.PARAM_USERID, String.valueOf(studId));
        paramsMap.put(GlobalVarDefine.PARAM_TESTID, testId.toString());
        return paramsMap;
    }

}
