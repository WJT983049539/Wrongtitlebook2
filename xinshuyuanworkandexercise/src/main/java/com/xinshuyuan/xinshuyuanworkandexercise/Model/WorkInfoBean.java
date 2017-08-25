package com.xinshuyuan.xinshuyuanworkandexercise.Model;

import java.io.Serializable;

/**
 * 储存作业信息的bean
 * Created by Administrator on 2017/7/5.
 */

public class WorkInfoBean implements Serializable{
    //作业名称
    private String WorkName;
    //作业ID
    private long WorkId;
    //作业状态
    private int WorkState;
    //作业发布时间
    private String publlicTime;

    public String getWorkName() {
        return WorkName;
    }

    public void setWorkName(String workName) {
        WorkName = workName;
    }

    public long getWorkId() {
        return WorkId;
    }

    public void setWorkId(long workId) {
        WorkId = workId;
    }

    public int getWorkState() {
        return WorkState;
    }

    public void setWorkState(int workState) {
        WorkState = workState;
    }

    public String getPubllicTime() {
        return publlicTime;
    }

    public void setPubllicTime(String publlicTime) {
        this.publlicTime = publlicTime;
    }
}
