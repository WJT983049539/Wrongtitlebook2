package xinshuyuan.com.wrongtitlebook.Persenter.Handler;


import android.os.Handler;
import android.os.Message;

import xinshuyuan.com.wrongtitlebook.View.Activity.Welcome;

/**
 * welcome的登录handler
 * Created by Administrator on 2017/5/22.
 */

public class LoginHandler extends Handler {
   private Welcome welcome;
    public LoginHandler(Welcome welcome) {
        this.welcome=welcome;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);


        switch (msg.what){

            case 1:



            break;


        }



    }
}
