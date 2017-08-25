package com.xinshuyuan.xinshuyuanworkandexercise.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/4.
 */

public class Config implements Serializable {


    private static final long serialVersionUID = -3721129029921488136L;

    public String getIp() {
        return Ip;
    }

    public void setIp(String ip) {
        Ip = ip;
    }

    public String getPort() {
        return Port;
    }

    public void setPort(String port) {
        Port = port;
    }

    public String getContentCode() {
        return ContentCode;
    }

    public void setContentCode(String contentCode) {
        ContentCode = contentCode;
    }

    private  String Ip;
    private  String Port;
    private  String ContentCode;
}
