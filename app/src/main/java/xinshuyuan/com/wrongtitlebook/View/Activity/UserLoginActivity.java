package xinshuyuan.com.wrongtitlebook.View.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ibpd.xsy.varDefine.GlobalVarDefine;

import xinshuyuan.com.wrongtitlebook.Model.Common.XSYTools;
import xinshuyuan.com.wrongtitlebook.Model.Common.XsyMap;
import xinshuyuan.com.wrongtitlebook.Persenter.workRunnable.LoginRunnable;
import xinshuyuan.com.wrongtitlebook.R;

/**
 * 登录界面
 * Created by wjt on 2017/5/22.
 */
public class UserLoginActivity extends Activity{
    private  Button student_login_button_set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Window window=getWindow();
        WindowManager.LayoutParams layoutParams= window.getAttributes();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_userlogin_layout);
        student_login_button_set= (Button) findViewById(R.id.student_login_button_set);
        if(!XSYTools.isPad(this)){
            RelativeLayout linear= (RelativeLayout) this.findViewById(R.id.teacher_login_layout_logo);
            if(linear!=null){
                linear.setVisibility(LinearLayout.GONE);
            }
        }
        student_login_button_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserLoginActivity.this.startActivity(new Intent(UserLoginActivity.this,SetActivity.class));
//                UserLoginActivity.this.finish();
            }
        });
    }

    public void doLogin(View v){
        EditText userNameVal= (EditText) this.findViewById(R.id.student_login_edit_account);
        EditText passwordVal= (EditText) this.findViewById(R.id.student_login_edit_password);
        if(!XSYTools.isNull(userNameVal) && !XSYTools.isNull(passwordVal) ){
            String userName=userNameVal.getText().toString();
            String password=passwordVal.getText().toString();
            String mac=XSYTools.getLocalMacAddressFromWifiInfo(this);
            mac=mac.replace(":", "");
            Log.d("userLogin", userName+" \t"+password);
            XsyMap map=XsyMap.getInterface();
            map.put(GlobalVarDefine.PARAM_USER_NAME, userName);
            map.put(GlobalVarDefine.PARAM_PASSWORD, password);
            map.put(GlobalVarDefine.PARAM_USER_TYPE, GlobalVarDefine.USERTYPE_STUDENT.toString());
            map.put(GlobalVarDefine.PARAM_MAC, mac);
            new Thread(new LoginRunnable(this,map)).start();

        }
    }

    private void initModel(){
        EditText userNameVal= (EditText) this.findViewById(R.id.student_login_edit_account);
        EditText passwordVal= (EditText) this.findViewById(R.id.student_login_edit_password);
        Button btn= (Button) this.findViewById(R.id.student_login_button_submit);
        if(!XSYTools.isNull(userNameVal) && !XSYTools.isNull(passwordVal) && !XSYTools.isNull(btn)){
            btn.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    doLogin(v);
                }
            });
        }
    }

}
