package xinshuyuan.com.wrongtitlebook.Persenter.evenListener;

import android.view.View;

import com.ibpd.xsy.varDefine.GlobalVarDefine;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.GetUserInfoClass;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import work.HomeWorkConstantClass;
import xinshuyuan.com.wrongtitlebook.Model.Common.Common;
import xinshuyuan.com.wrongtitlebook.Model.Common.LineButton;
import xinshuyuan.com.wrongtitlebook.Model.Common.PerferenceService;
import xinshuyuan.com.wrongtitlebook.Model.Common.XsyMap;
import xinshuyuan.com.wrongtitlebook.Persenter.Handler.ExerciseHandler;
import xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment.ExerciseLineFragment;

/**
 *连线题提交的按钮
 * Created by Administrator on 2017/6/22.
 */

public class LineOnClickListener implements View.OnClickListener {
    private List<LineButton> listlist2;
    private TestEntity te;
    private ExerciseLineFragment lineFragment;
    private LineButton button1;
    private LineButton button2;
    private String AnsWerAndFenxi;
    private  ExerciseHandler exerciseHandler;
    public LineOnClickListener(ExerciseLineFragment exerciseLineFragment, ExerciseHandler exerciseHandler) {
        lineFragment=exerciseLineFragment;
        this.exerciseHandler=exerciseHandler;
    }

    @Override
    public void onClick(View v) {
        // 得到储存按钮的集合
        listlist2 = lineFragment.getListButton();
        if(listlist2.size()%2!=0){
            listlist2.remove(listlist2.size()-1);
        }

        if (listlist2.size()>= 2||listlist2.size()%2==0) {

            for (int i = 0; i < listlist2.size();) {
                button1 = listlist2.get(i);
                button2 = listlist2.get(i+1);
                if (button1.getType() == "left") {
                    button2.setPair(button1.getPair());
                } else {
                    button1.setPair(button2.getPair());
                }
                i += 2;// i=i+2
            }

            JSONObject j = new JSONObject();
            Integer itemType = this.lineFragment.getItemType();
            te= this.lineFragment.getTestEntity();
            AnsWerAndFenxi=te.getAnswerAnalysis();
            if (te != null) {
                if (listlist2.size() > 0) {
                    try {
                        // 放入id ,类型
                        j.put("testId", te.getTestId());
                        j.put("testType", te.getTestType());
                        // 创建数组
                        JSONArray ja = new JSONArray();
                        for (int i = 0; i < listlist2.size(); i++) {
                            JSONObject sa = new JSONObject();
                            sa.put("lineButtonId", listlist2.get(i).getLineButtonId());
                            sa.put("order", listlist2.get(i).getOder());
                            sa.put("pair", listlist2.get(i).getPair());
                            sa.put("type", listlist2.get(i).getType());
                            ja.put(sa);
                        }
                        j.put("answers", ja);
                        // 组合完毕

                        PerferenceService service=new PerferenceService(lineFragment.getActivity());
                        String subjectIdd=service.getsharedPre().getString("WrongProjectId","");


                        //提交答案

                        XsyMap<String,String> commitmap=XsyMap.getInterface();
                        //列表名
                        Long studId= new GetUserInfoClass(lineFragment.getActivity()).getUserId();
                        commitmap.put(GlobalVarDefine.PARAM_USER_ID,String.valueOf(studId));
                        //试题id
                        commitmap.put(GlobalVarDefine.PARAM_TESTID,String.valueOf(te.getTestId()));
                        //提交试题的json数据
                        commitmap.put(GlobalVarDefine.PARAM_TESTANS_JSON,j.toString());
                        commitmap.put(HomeWorkConstantClass.PARAM_SUBJECTID,subjectIdd);
                        //答题类型
                        commitmap.put(GlobalVarDefine.PARAM_ITEMTYPE,te.getTestType().toString());
                        String commotUrl= XSYTools.getWrongUrl(GlobalVarDefine.TEST_SUBMIT_ANSWER_URL,lineFragment.getActivity());

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
                                super.onError(call, response, e);
                            }
                        });




//
//                        TestCommon.submitTestAns(te.getTestId(), j.toString(),
//                                itemType, te.getTestType());










                        System.out.println("j.toString()" + j.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    XSYTools.showToastmsg(lineFragment.getActivity(),"没有连线？");
                    //清除缓存
                    this.lineFragment.cleanArrayList2();
                    this.lineFragment.inintarray();
                }
            } else {

                XSYTools.showToastmsg(lineFragment.getActivity(),"没有连线？");
                this.lineFragment.cleanArrayList2();
                this.lineFragment.inintarray();
            }
        }else{
            XSYTools.showToastmsg(lineFragment.getActivity(),"没有连线？");
            this.lineFragment.cleanArrayList2();
            this.lineFragment.inintarray();
        }

    }
}
