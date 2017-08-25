package com.xinshuyuan.xinshuyuanworkandexercise.Model.Keybord;

import android.view.View;
import android.view.WindowManager;

/**
 * Created by Administrator on 2017/7/14.
 */

public class mylisten implements View.OnClickListener {
    private WindowManager windowmanager;
    private View digitView;
    public mylisten(WindowManager windowmanager, View digitView) {
        this.digitView=digitView;
        this.windowmanager=windowmanager;
    }

    @Override
    public void onClick(View v) {
        DigitKeyPadUtil dd=new DigitKeyPadUtil();
        if (digitView.getParent() != null) {
            if(dd.flag==true){
                //windowmanager.removeView(digitView);
                windowmanager.removeViewImmediate(digitView);
                dd.flag=false;
            }
        }

    }
}