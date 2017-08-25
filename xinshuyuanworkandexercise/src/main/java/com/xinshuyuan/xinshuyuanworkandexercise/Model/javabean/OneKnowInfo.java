package com.xinshuyuan.xinshuyuanworkandexercise.Model.javabean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/10.
 */

public class OneKnowInfo implements Serializable{
    private String oneKnowId;
    private String oneKnowString;

    public String getOneKnowId() {
        return oneKnowId;
    }

    public void setOneKnowId(String oneKnowId) {
        this.oneKnowId = oneKnowId;
    }

    public String getOneKnowString() {
        return oneKnowString;
    }

    public void setOneKnowString(String oneKnowString) {
        this.oneKnowString = oneKnowString;
    }
}
