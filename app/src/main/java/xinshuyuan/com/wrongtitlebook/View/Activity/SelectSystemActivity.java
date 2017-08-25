package xinshuyuan.com.wrongtitlebook.View.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XsyMap;
import com.xinshuyuan.xinshuyuanworkandexercise.WorkAndExercisectivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;
import work.HomeWorkConstantClass;
import xinshuyuan.com.wrongtitlebook.Model.Common.Common;
import xinshuyuan.com.wrongtitlebook.Model.Common.PerferenceService;
import xinshuyuan.com.wrongtitlebook.Model.Common.UserEntity;
import xinshuyuan.com.wrongtitlebook.Persenter.workRunnable.LoginSuccessRunnable;
import xinshuyuan.com.wrongtitlebook.R;

/**
 * 选择作业与练习，还是课前预习，还是错题本的Activity
 * Created by Administrator on 2017/7/3.
 */

public class SelectSystemActivity extends Activity implements View.OnClickListener{
    private ImageView text_work_and_exercise;
    private ImageView wrong_book_system;
    private String userName;
    private  Long ALLUserId;
    private UserEntity userEntity;
    private TextView selse_studentname;
    private TextView setbutton;

    //综合素质评价
    private ImageView cqvl;
    //预习与导学
    private ImageView PreviewAndGuidance;
    //我的网盘
    private ImageView mynetdisc;
    //学科工具
    private ImageView subject_tools;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window=getWindow();
        WindowManager.LayoutParams layoutParams= window.getAttributes();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent=getIntent();
        Bundle bundle= intent.getExtras();
        userEntity= (UserEntity) bundle.getSerializable("userEntity");
        userName=userEntity.getUserName();
        ALLUserId=userEntity.getId();
        setContentView(R.layout.testselectsystemlayout);
        inint();

    }

    private void inint() {
        PerferenceService service=new PerferenceService(SelectSystemActivity.this);
        String ip=service.getsharedPre().getString("ipAddress","");
        selse_studentname= (TextView) findViewById(R.id.selse_studentname);
        selse_studentname.setText(userName+"同学，欢迎您！");
        wrong_book_system=(ImageView)findViewById(R.id.wrong_book_system);
        setbutton= (TextView) findViewById(R.id.setbutton);
        text_work_and_exercise= (ImageView) findViewById(R.id.text_work_and_exercise);
        cqvl=(ImageView)findViewById(R.id.cqvl);
        PreviewAndGuidance=(ImageView)findViewById(R.id.preview_and_guidance);
        mynetdisc=(ImageView)findViewById(R.id.mynetdisc);
        subject_tools=(ImageView)findViewById(R.id.subject_tools);

        subject_tools.setOnClickListener(this);
        mynetdisc.setOnClickListener(this);
        PreviewAndGuidance.setOnClickListener(this);
        setbutton.setOnClickListener(this);
        wrong_book_system.setOnClickListener(this);
        text_work_and_exercise.setOnClickListener(this);
        cqvl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            /**
             * 设置按钮点击事件
             */
            case R.id.setbutton:
                startActivity(new Intent(SelectSystemActivity.this,SetActivity.class));
                break;
            /**
             * 错题系统
             */
            case R.id.wrong_book_system:

                //得到错题系统的访问路径
                HashMap<String,String> getSystemUrl=XsyMap.getInterface();
                getSystemUrl.put(HomeWorkConstantClass.PARAM_APPKEY,"WQT");
                OkGo.post(XSYTools.joinUrl(HomeWorkConstantClass.GETINTERURL_URL)).params(getSystemUrl).execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        XSYTools.i("得到错题系统的访问url"+s);
                        try {
                            JSONObject object = new JSONObject(s);
                            String WrongUrl = object.getString("interUrl");
                            PerferenceService getUrlservice = new PerferenceService(SelectSystemActivity.this);
                            //把url保存到共享参数
                            getUrlservice.save("WrongUrl", WrongUrl);
                            String url= XSYTools.getNowWorkSystemUrl(SelectSystemActivity.this);
                            HashMap<String,String> mapp= XsyMap.getInterface();
                            mapp.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(userEntity.getId()));

                            OkGo.post(XSYTools.getWrongUrl(HomeWorkConstantClass.GETSTUDENTINFO_URL,SelectSystemActivity.this)).params(mapp).execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    XSYTools.i("得到错题本的userId"+s);

                                    try {
                                        JSONObject jsonObject=new JSONObject(s);

                                        JSONObject OO=jsonObject.getJSONObject("student");
                                        //得到作业的userID
                                        Long WrongId=OO.getLong("id");
                                        PerferenceService setWorkID=new PerferenceService(SelectSystemActivity.this);
                                        setWorkID.save("WrongId",WrongId);
                                        new Thread(new LoginSuccessRunnable(SelectSystemActivity.this,userName)).start();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                    XSYTools.i("得到错题本id访问出错"+e.toString());
                                }
                            });



                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);

                        XSYTools.i("得到错题系统url错误"+e.toString());


                    }
                });

                break;
            /**
             * 作业与练习
             */
            case R.id.text_work_and_exercise:
                //得到作业系统的访问路径
                HashMap<String,String> getworkSystemUrl=XsyMap.getInterface();
                getworkSystemUrl.put(HomeWorkConstantClass.PARAM_APPKEY,"HAP");
                OkGo.post(XSYTools.joinUrl(HomeWorkConstantClass.GETINTERURL_URL)).params(getworkSystemUrl).execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        XSYTools.i("得到请求系统的url"+s);
                        try {
                            JSONObject object=new JSONObject(s);
                            String workUrl=object.getString("interUrl");
                            PerferenceService getUrlservice=new PerferenceService(SelectSystemActivity.this);
                            getUrlservice.save("NowWorkSystemUrl",workUrl);
                            //得到作业系统的URL
//                            Common.getNowWorkSystemUrl(SelectSystemActivity.this);
                            String getwrodUserIdurl= XSYTools.getNowWorkSystemUrl(SelectSystemActivity.this);
                            HashMap<String,String> mapp= XsyMap.getInterface();
                            mapp.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(userEntity.getId()));
                            OkGo.post(getwrodUserIdurl+"/"+HomeWorkConstantClass.STUDENTINFO_URL).params(mapp).execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    XSYTools.i("得到本系统的userId"+s);

                                    try {
                                        JSONObject jsonObject=new JSONObject(s);
                                        JSONObject OO=jsonObject.getJSONObject("student");
                                        //得到作业的userID
                                        Long WorkUserId=OO.getLong("id");
                                        PerferenceService setWorkID=new PerferenceService(SelectSystemActivity.this);
                                        setWorkID.save("WorkId",WorkUserId);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    //转到作业与练习界面
                                    Intent intent=new Intent(SelectSystemActivity.this,WorkAndExercisectivity.class);
                                    intent.putExtra("userName",userEntity.getUserName());
                                    intent.putExtra("userId",userEntity.getId());
                                    intent.putExtra("userType",userEntity.getUserType());
                                    SelectSystemActivity.this.startActivity(intent);


                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                    XSYTools.i("得到本系统的userId"+e.toString());
                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        XSYTools.i("请求系统的url失败"+e.toString());
                    }
                });
                break;
            //综合素质评价
            case R.id.cqvl:

                HashMap<String,String> getCQVLSystemUrlMap=XsyMap.getInterface();
                getCQVLSystemUrlMap.put(HomeWorkConstantClass.PARAM_APPKEY,"EVA");
                OkGo.post(XSYTools.joinUrl(HomeWorkConstantClass.GETINTERURL_URL)).params(getCQVLSystemUrlMap).execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                    XSYTools.i("得到的综合素质和评价访问路径"+s);

                        try {
                            JSONObject object=new JSONObject(s);
                            String evaluateUrl=object.getString("interUrl");
                            //把路径储存到共享参数中
                            PerferenceService getUrlservice=new PerferenceService(SelectSystemActivity.this);
                            getUrlservice.save("EvaluateSystemUrl",evaluateUrl);

                            String EvaluateSystemUrl= XSYTools.getEvaluateSystemUrl(SelectSystemActivity.this);

                            HashMap<String,String> mapp= XsyMap.getInterface();
                            mapp.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(userEntity.getId()));
                            OkGo.post(EvaluateSystemUrl+"/"+HomeWorkConstantClass.STUDENT_EVALUATE_URL).params(mapp).execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    XSYTools.i("得到综合素质评价的userIDJSON"+s);

                                    try {
                                        JSONObject jsonObject=new JSONObject(s);
                                        JSONObject OO=jsonObject.getJSONObject("student");
                                        //得到作业的evaluateUserid
                                        Long evaluateId=OO.getLong("id");
                                        PerferenceService setevaluateID=new PerferenceService(SelectSystemActivity.this);
                                        //综合素质评价的userId
                                        setevaluateID.save("EvaluateId",evaluateId);
                                        Common.setEvaluateUserId(evaluateId);
                                        Intent intent=new Intent(SelectSystemActivity.this, EvaluateActivity.class);
                                        startActivity(intent);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                    XSYTools.i("得到综合素质评价的userIDJSON请求失败"+e.toString());
                                }
                            });


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    XSYTools.i("得到综合素质评价访问路径失败"+e.toString());

                    }
                });

                break;
            //预习与导学
            case R.id.preview_and_guidance:

                XSYTools.showToastmsg(SelectSystemActivity.this,"正在开发中");


                break;

            //我的网盘
            case R.id.mynetdisc:

                XSYTools.showToastmsg(SelectSystemActivity.this,"正在开发中");

                break;
                //学科工具
            case R.id.subject_tools:

                XSYTools.showToastmsg(SelectSystemActivity.this,"正在开发中");

                break;
        }
    }
}
