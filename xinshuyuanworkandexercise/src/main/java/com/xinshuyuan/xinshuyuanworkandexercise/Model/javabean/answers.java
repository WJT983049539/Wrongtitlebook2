package com.xinshuyuan.xinshuyuanworkandexercise.Model.javabean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/14.
 */

public class answers implements Serializable {

    private String order;
    private String pair;
    private String text;
    private String type;
    public String getOrder() {
        return order;
    }
    public void setOrder(String order) {
        this.order = order;
    }
    public String getPair() {
        return pair;
    }
    public void setPair(String pair) {
        this.pair = pair;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }


}
