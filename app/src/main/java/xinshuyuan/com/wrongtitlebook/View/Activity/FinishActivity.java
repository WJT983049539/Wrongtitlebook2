package xinshuyuan.com.wrongtitlebook.View.Activity;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import xinshuyuan.com.wrongtitlebook.Model.Common.PerferenceService;
import xinshuyuan.com.wrongtitlebook.Persenter.workRunnable.LoginSuccessRunnable;
import xinshuyuan.com.wrongtitlebook.R;

/**
 * Created by Administrator on 2017/6/19.
 */

public class FinishActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window=getWindow();
        //屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        WindowManager.LayoutParams layoutParams= window.getAttributes();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏底部键盘，一直不会弹出
        layoutParams.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.setAttributes(layoutParams);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        setContentView(R.layout.finish_layout);
        inint();
    }

    private void inint() {
//        Timer timer=new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                startActivity(new Intent(FinishActivity.this,MainActivity.class));
//                FinishActivity.this.finish();
//            }
//        },2000);

        PerferenceService service=new PerferenceService(this);
        String userName=service.getsharedPre().getString("UserName","");
        new Thread(new LoginSuccessRunnable(this,userName)).start();
//        new Thread(new CheckLoginRunnable(this)).start();
    }


}
