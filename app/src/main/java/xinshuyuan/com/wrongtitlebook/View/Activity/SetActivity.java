package xinshuyuan.com.wrongtitlebook.View.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.PerferenceService;

import xinshuyuan.com.wrongtitlebook.Model.Common.ConfigUtil;
import xinshuyuan.com.wrongtitlebook.R;

/**
 * 设置界面
 * Created by Administrator on 2017/6/26.
 */

public class SetActivity extends Activity{
    private EditText ipAddress;
    private EditText portAddress;
    private EditText connectCode;
    private Button commit_setbutton;
    private Button return_set_button;
    private PerferenceService service;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window=getWindow();
        //屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        WindowManager.LayoutParams layoutParams= window.getAttributes();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏底部键盘，一直不会弹出
        layoutParams.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.setAttributes(layoutParams);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.set_layout);
        inint();

    }

    private void inint() {
        service=new PerferenceService(SetActivity.this);
        ipAddress= (EditText) findViewById(R.id.ipAddress);
        ipAddress.setText(ConfigUtil.getIp());
        portAddress=(EditText)findViewById(R.id.portAddress);
        portAddress.setText(ConfigUtil.getPort());
        connectCode= (EditText) findViewById(R.id.connectCode);
        connectCode.setText(ConfigUtil.getContentCode());
        commit_setbutton= (Button) findViewById(R.id.commit_setbutton);
        return_set_button= (Button) findViewById(R.id.return_set_button);


        return_set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetActivity.this.startActivity(new Intent( SetActivity.this,UserLoginActivity.class));
            }
        });
        commit_setbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfigUtil.setIp(ipAddress.getText().toString().replaceAll("\\s*", ""));
                ConfigUtil.setPort(portAddress.getText().toString().replaceAll("\\s*", ""));
                ConfigUtil.setContentCode(connectCode.getText().toString().replaceAll("\\s*", ""));
                //储存到共享参数里面
                service.save("ipAddress",ipAddress.getText().toString().replaceAll("\\s*", ""));
                service.save("portAddress",portAddress.getText().toString().replaceAll("\\s*", ""));
                service.save("connectCode",connectCode.getText().toString().replaceAll("\\s*", ""));
                    SetActivity.this.startActivity(new Intent(SetActivity.this,Welcome.class));
                    SetActivity.this.finish();
            }
        });


    }


}
