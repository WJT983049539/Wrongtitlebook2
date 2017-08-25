package xinshuyuan.com.wrongtitlebook.Persenter.workRunnable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.ibpd.xsy.varDefine.WrongQuestionConstantClass;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.GetUserInfoClass;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;
import xinshuyuan.com.wrongtitlebook.Model.Common.Common;
import xinshuyuan.com.wrongtitlebook.Model.Common.Subjectbean;
import xinshuyuan.com.wrongtitlebook.Model.Common.XsyMap;
import xinshuyuan.com.wrongtitlebook.View.Activity.MainActivity;

/**
 * 登录成功，获取学科，然后到MianActivity
 * Created by Administrator on 2017/5/23.
 */
public class LoginSuccessRunnable implements Runnable {
    private  Context context;
    private String userName;
    public LoginSuccessRunnable(Context context,String userName) {
        this.context=context;
        this.userName=userName;
    }


    @Override
    public void run() {
        getSubgect();

    }

    private void getSubgect() {
        {
            String getSubjectUrl= XSYTools.getWrongUrl(WrongQuestionConstantClass.SUBJECT_URL,context);
            XsyMap map= XsyMap.getInterface();
            Long studId= new GetUserInfoClass(context).getUserId();
            map.put(WrongQuestionConstantClass.PARAM_STUDID,String.valueOf(studId));
            OkGo.post(getSubjectUrl).params(map).execute(new StringCallback() {
                @Override
                //{"subject":[{"gradeIds":",1,","id":1,"order":1,"ssoId":1,"subjectName":"\u8bed\u6587"},{"gradeIds":",1,","id":9,"order":1,"ssoId":9,"subjectName":"\u81ea\u7136"},{"gradeIds":",1,","id":8,"order":1,"ssoId":8,"subjectName":"\u79d1\u5b66"},{"gradeIds":",1,","id":7,"order":1,"ssoId":7,"subjectName":"\u54c1\u5fb7\u4e0e\u793e\u4f1a"},{"gradeIds":",1,","id":6,"order":1,"ssoId":6,"subjectName":"\u4f53\u80b2"},{"gradeIds":",1,","id":5,"order":1,"ssoId":5,"subjectName":"\u7f8e\u672f"},{"gradeIds":",1,","id":4,"order":1,"ssoId":4,"subjectName":"\u97f3\u4e50"},{"gradeIds":",1,","id":3,"order":1,"ssoId":3,"subjectName":"\u82f1\u8bed"},{"gradeIds":",1,","id":2,"order":1,"ssoId":2,"subjectName":"\u6570\u5b66"},{"gradeIds":",1,","id":10,"order":1,"ssoId":10,"subjectName":"\u4fe1\u606f"}]}
                public void onSuccess(String s, Call call, Response response) {
                    //得到学科列表JsonArray
                    XSYTools.i("S="+s);
                    ArrayList<String> list=new ArrayList<String>();
                    ArrayList<Subjectbean> list2=new ArrayList<Subjectbean>();

                    //键为学科，值为id的集合
                    Map<String,String> subject=new HashMap<String, String>();
                    try {
                        list2.clear();

                        JSONObject object=new JSONObject(s);
                        String jaonarray=object.getString("subject");
                        JSONArray array=new JSONArray(jaonarray);
                        for(int i=0;i<array.length();i++){
                            Subjectbean subjectbean=new Subjectbean();
                            String subjectName=new JSONObject(array.get(i).toString()).getString("subjectName");
                            String subjectId=String.valueOf(new JSONObject(array.get(i).toString()).getString("id"));
                            list.add(subjectName);
                            subject.put(subjectName,subjectId);
                            subjectbean.setSubject(subjectName);
                            subjectbean.setSubjectId(subjectId);
                            list2.add(subjectbean);

                        }
                        //把存有id和学科的集合放入缓存

                        Common.init();
                        Common.setMap(subject);


                        Intent intent=new Intent(context,MainActivity.class);
                        intent.putStringArrayListExtra("subjectList",list);
                        intent.putExtra("userName",userName);
                        context.startActivity(intent);
                        ((Activity)context).finish();
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

    }


}
