package xinshuyuan.com.wrongtitlebook.Persenter.Handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import xinshuyuan.com.wrongtitlebook.Model.Common.Common;
import xinshuyuan.com.wrongtitlebook.View.Activity.MainActivity;
import xinshuyuan.com.wrongtitlebook.View.Activity.ShowWrongBookslistActivity;

/**
 * 主界面MainActivity的Handler
 * Created by Administrator on 2017/5/24.
 */
public class MainHandler extends Handler{

    private MainActivity mainActivity;
    public MainHandler(MainActivity mainActivity) {
    this.mainActivity=mainActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        switch (msg.what){

             //转到相应的检索界面

            case Common.LANGUAGE_FRAGMENT:

                //转到检索Activity
                    Intent intent=new Intent(mainActivity,ShowWrongBookslistActivity.class);
                    intent.putExtra("project",msg.obj.toString());
                    mainActivity.startActivity(intent);
//                    mainActivity.finish();

            break;

        }

    }
}
