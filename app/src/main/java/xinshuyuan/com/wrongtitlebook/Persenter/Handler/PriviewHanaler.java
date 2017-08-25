package xinshuyuan.com.wrongtitlebook.Persenter.Handler;

import android.app.FragmentTransaction;
import android.os.Handler;
import android.os.Message;

import xinshuyuan.com.wrongtitlebook.Model.Common.Common;
import xinshuyuan.com.wrongtitlebook.R;
import xinshuyuan.com.wrongtitlebook.View.Activity.CheckActivity;
import xinshuyuan.com.wrongtitlebook.View.Fragment.ShowWrongListFragment;

/**
 * 检查handler
 * Created by Administrator on 2017/6/7.
 */

public class PriviewHanaler extends Handler{
    private CheckActivity checkActivity;
    public PriviewHanaler(CheckActivity checkActivity) {
        this.checkActivity=checkActivity;
    }


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
       switch (msg.what){
              //转到错题列表


       }


    }
}
