package com.xinshuyuan.xinshuyuanworkandexercise.Model.javabean;

import java.io.Serializable;

/**学生统计 每道题做对了多少，做错了多少，没做的多少，的javabean
 * Created by Administrator on 2017/7/25.
 */

public class Statistice implements Serializable{


    public Float getRightCount() {
        return rightCount;
    }

    public void setRightCount(Float rightCount) {
        this.rightCount = rightCount;
    }

    public Float getWrongcount() {
        return wrongcount;
    }

    public void setWrongcount(Float wrongcount) {
        this.wrongcount = wrongcount;
    }

    public Float getDoCount() {
        return doCount;
    }

    public void setDoCount(Float doCount) {
        this.doCount = doCount;
    }

    private Float rightCount;
    private Float wrongcount;
    private Float doCount;
}
