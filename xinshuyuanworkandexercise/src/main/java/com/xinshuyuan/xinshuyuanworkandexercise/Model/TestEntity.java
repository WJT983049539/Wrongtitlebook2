package com.xinshuyuan.xinshuyuanworkandexercise.Model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**实体的javabean
 * Created by wjt on 2017/6/6.
 */

public class TestEntity implements Serializable {
    public void setPoint(String point) {
        this.Point = point;
    }

    public void setTestType(Integer testType) {
        this.testType = testType;
    }

    public void setAnswerList(List<AnswerEntity> answerList) {
        this.answerList = answerList;
    }

    public String getPoint() {
        return Point;
    }

    public Integer getTestType() {
        return testType;
    }

    public List<AnswerEntity> getAnswerList() {
        return answerList;
    }
    public void fillAnser(AnswerEntity ae){
        if(ae!=null){
            this.getAnswerList().add(ae);
        }
    }
    private Long testId;//试题id
    private String Point;//题干
    private Integer testType;//类型
    private String subject;//主题
    private String answerAnalysis;//答案解析
    private String WorkId;//加个作业id,方便在试题提交的时候调用
    private List<AnswerEntity> answerList=new LinkedList<AnswerEntity>();//答案选项 或者答案
    private double nowdifficulty;

    public String getWorkId() {
        return WorkId;
    }

    public void setWorkId(String workId) {
        WorkId = workId;
    }



    public double getNowdifficulty() {
        return nowdifficulty;
    }

    public void setNowdifficulty(double nowdifficulty) {
        this.nowdifficulty = nowdifficulty;
    }



    public String getAnswerAnalysis() {
        return answerAnalysis;
    }

    public void setAnswerAnalysis(String answerAnalysis) {
        this.answerAnalysis = answerAnalysis;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }






    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }



}
