package xinshuyuan.com.wrongtitlebook.Persenter.workRunnable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ibpd.xsy.varDefine.GlobalVarDefine;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;
import xinshuyuan.com.wrongtitlebook.Model.Common.Common;
import xinshuyuan.com.wrongtitlebook.Model.Common.PerferenceService;
import xinshuyuan.com.wrongtitlebook.Model.Common.UserEntity;
import xinshuyuan.com.wrongtitlebook.Model.Common.XSYTools;
import xinshuyuan.com.wrongtitlebook.Model.Common.XsyMap;
import xinshuyuan.com.wrongtitlebook.View.Activity.SelectSystemActivity;
import xinshuyuan.com.wrongtitlebook.View.Activity.UserLoginActivity;

/**
 * Created by Administrator on 2017/5/22.
 */
public class LoginRunnable implements Runnable {
    private UserLoginActivity userLoginActivity;
    private XsyMap map;


    public LoginRunnable(UserLoginActivity userLoginActivity, XsyMap map) {
        this.map=map;
        this.userLoginActivity=userLoginActivity;
    }

    @Override
    public void run() {
        String LoginUrl= XSYTools.joinUrl(GlobalVarDefine.USER_DEVICE_AND_LOGIN_URL);
        OkGo.post(LoginUrl).params(map).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                XSYTools.i("登录返回信息"+s);
                 if(s!=null){
                     JSONObject jo = null;
                     try {
                         jo = new JSONObject(s);

                     String id=XSYTools.getStringValFromJSONObject(jo,"userId");
                     String status=XSYTools.getStringValFromJSONObject(jo,GlobalVarDefine.JSONMSG_STATUS);
                     String msg=XSYTools.getStringValFromJSONObject(jo,GlobalVarDefine.JSONMSG_MSG);
                         Log.d("XSY", "id="+id+"=");
                         if(!XSYTools.isEmpty(id)){
                             //获取到值了
                             String userName=jo.getString("userName");
            //String password=j.getString("password");
                             if(XSYTools.isNumeric(id.trim())){
                                 XSYTools.showToastmsg(userLoginActivity,"登录成功,即将转向");

                                 PerferenceService service=new PerferenceService(userLoginActivity);
                                 /**
                                  * 把sooid存到共享参数
                                  */
                                 service.save("SSOID",Long.valueOf(id));





                                 Common.setUserInfo(Long.valueOf(id.trim()), GlobalVarDefine.USERTYPE_STUDENT, userName);
                                 UserEntity userEntity=new UserEntity();
                                 userEntity.setId(Long.valueOf(id.trim()));
                                 userEntity.setUserName(userName);
                                 userEntity.setUserType(GlobalVarDefine.USERTYPE_STUDENT);
                                 Intent intent=new Intent(userLoginActivity,SelectSystemActivity.class);
                                 Bundle bundle=new Bundle();
                                 bundle.putSerializable("userEntity",userEntity);
                                 intent.putExtras(bundle);
                                 Common.setUserEntity(userEntity);
                                 userLoginActivity.startActivity(intent);

                             }else{
                                 XSYTools.showToastmsg(userLoginActivity,"用户信息获取失败");
                             }
                         }
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                 }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                XSYTools.showToastmsg(userLoginActivity,"登录失败，请检查网络！");
                XSYTools.i(e.toString());

            }
        });

    }
}
