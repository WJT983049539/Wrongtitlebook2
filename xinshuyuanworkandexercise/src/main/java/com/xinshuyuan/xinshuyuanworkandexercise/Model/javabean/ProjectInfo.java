package com.xinshuyuan.xinshuyuanworkandexercise.Model.javabean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/5.
 */

public class ProjectInfo implements Serializable{
    private String subjectName;
    private Long subjectId;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }
}
