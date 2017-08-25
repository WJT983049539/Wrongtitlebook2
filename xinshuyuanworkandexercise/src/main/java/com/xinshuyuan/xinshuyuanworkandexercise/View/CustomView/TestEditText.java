package com.xinshuyuan.xinshuyuanworkandexercise.View.CustomView;

import android.content.Context;
import android.widget.EditText;

/**
 * Created by Administrator on 2017/7/14.
 */

public class TestEditText extends EditText {

    private String ansBlankId;
    private String tp="";
    public TestEditText(Context context) {
        super(context);
    }
    public String getAnsBlankId() {
        return ansBlankId;
    }
    public void setAnsBlankId(String ansBlankId) {
        this.ansBlankId = ansBlankId;
    }
    public String getTp() {
        return tp;
    }
    public void setTp(String tp) {
        this.tp = tp;
    }

}

