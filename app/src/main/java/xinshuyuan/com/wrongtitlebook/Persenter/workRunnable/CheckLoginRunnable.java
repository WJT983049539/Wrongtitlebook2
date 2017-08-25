package xinshuyuan.com.wrongtitlebook.Persenter.workRunnable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ibpd.xsy.varDefine.GlobalVarDefine;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;
import xinshuyuan.com.wrongtitlebook.Model.Common.Common;
import xinshuyuan.com.wrongtitlebook.Model.Common.PerferenceService;
import xinshuyuan.com.wrongtitlebook.Model.Common.UserEntity;
import xinshuyuan.com.wrongtitlebook.Model.Common.XSYTools;
import xinshuyuan.com.wrongtitlebook.Model.Common.XsyMap;
import xinshuyuan.com.wrongtitlebook.Persenter.Handler.LoginHandler;
import xinshuyuan.com.wrongtitlebook.View.Activity.SelectSystemActivity;
import xinshuyuan.com.wrongtitlebook.View.Activity.SetActivity;
import xinshuyuan.com.wrongtitlebook.View.Activity.UserLoginActivity;

/**
 * //判断是否已经登录，如果登录转到 Mainactivity，否则转到登录界面
 * Created by Administrator on 2017/5/22.
 */
public class CheckLoginRunnable implements Runnable {
    private Activity welcome;
    private String userName;
    private LoginHandler loginHandler;
    public  CheckLoginRunnable(Activity welcome) {
        this.welcome=welcome;

    }
    @Override
    public void run() {
        if(XSYTools.networkIsConnected(welcome)){
            String mac=XSYTools.getLocalMacAddressFromWifiInfo(welcome);
            mac=mac.replace(":", "");
            XsyMap<String,String> map= XsyMap.getInterface();
            map.put(GlobalVarDefine.PARAM_MAC, mac);
            map.put(GlobalVarDefine.PARAM_USERTYPE, GlobalVarDefine.USERTYPE_STUDENT.toString());
            String url=XSYTools.joinUrl(GlobalVarDefine.USER_AUTHENTICATION_URL);
            OkGo.post(url.replaceAll("\\s*","")).params(map).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    String url=XSYTools.joinUrl(GlobalVarDefine.USER_AUTHENTICATION_URL);
                                    XSYTools.i("登陆成功"+s+"  url=="+url);
                    if(s!=null){

                        try {
                            //如果数据库存在学生绑定信息，直接返回绑定信息数组，
                            //  否则返回“[{\"status\":\""+GlobalVarDefine.NO_BIND_MAC+"\",\"msg\":\""+GlobalVarDefine.NO_BIND_MAC_MSG+"\"}]”
                            JSONArray array=new JSONArray(s);
                            String aaa=array.getString(0);
                            JSONObject jo = new JSONObject(aaa);
                            String id=XSYTools.getStringValFromJSONObject(jo,"userId");
                            String status=XSYTools.getStringValFromJSONObject(jo,GlobalVarDefine.JSONMSG_STATUS);
                            String msg=XSYTools.getStringValFromJSONObject(jo,GlobalVarDefine.JSONMSG_MSG);
                            if(!XSYTools.isEmpty(id)){
                                //获取到值了
                                userName=jo.getString("userName");
                                if(XSYTools.isNumeric(id.trim())){
//                                    XSYTools.showToastmsg(welcome,"登录成功,即将转向");
                                    //在这里获取学科信息传入MainActivity

                                    PerferenceService service=new PerferenceService(welcome);
                                    /**
                                     * 把sooid存到共享参数
                                     */
                                    service.save("SSOID",Long.valueOf(id));
                                    //返回科目列表需要这个值
                                    service.save("UserName",userName);

                                    //把用户信息封装起来,然后选择转到选择课前课后和错题本的界面
                                    UserEntity userEntity=new UserEntity();
                                    userEntity.setId(Long.valueOf(id));
                                    userEntity.setUserName(userName);
                                    userEntity.setUserType(GlobalVarDefine.USERTYPE_STUDENT);
                                    Common.setUserEntity(userEntity);
                                    Intent intent=new Intent(welcome,SelectSystemActivity.class);
                                    Bundle bundle=new Bundle();
                                    bundle.putSerializable("userEntity",userEntity);
                                    intent.putExtras(bundle);
                                    welcome.startActivity(intent);

//                                    new Thread(new LoginSuccessRunnable(welcome,userName)).start();
                                }else{
                                    XSYTools.showToastmsg(welcome,"用户信息获取失败");
                                }
                            }else{
                                if(XSYTools.isNumeric(status)){
                                    Integer istatus=Integer.valueOf(status);
                                    if(istatus.equals(GlobalVarDefine.NO_BIND_MAC)){
                                        //转到登录页面
//						                comm.toast(msg+"\n返回值:"+status);
                                        Log.d("XSY", "转到登录页面");
                                        Intent intent=new Intent(welcome,UserLoginActivity.class);
                                        welcome.startActivity(intent);
                                    }
                                }else{

                                    XSYTools.showToastmsg(welcome,"未知状态"+status);
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
                    XSYTools.showToastmsg(welcome,"连接失败");
                    XSYTools.i(e.toString());
                    XSYTools.showToastmsg(welcome,"连接失败");
                    //转到设置界面

                    Intent intent=new Intent(welcome,SetActivity.class);
                    welcome.startActivity(intent);
                    welcome.finish();
                }
            });

              }else {

                    welcome.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            XSYTools.showToastmsg(welcome,"没有网络");
                        }
                    });

             }

    }
}
