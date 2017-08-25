package com.xinshuyuan.xinshuyuanworkandexercise.Model;

import java.io.Serializable;

/**
 * 未完成的作业试题，每个试题的信息javabean，9个
 * Created by wjt on 2017/7/6.
 */

public class TestInfo implements Serializable{

    //试题id
    private long testId;
    //作业id
    private long stuWorkId;
    //学生id
    private long studentId;
    //学科id
    private long subjectId;
    //知识点id
    private long knowledgePointId;
    //试题类型
    private Integer testType;
    //难度
    private float difficulty;
    //题干
    private String problem;
    //正确错误
    private Boolean state;

    public int getHomeState() {
        return homeState;
    }

    public void setHomeState(int homeState) {
        this.homeState = homeState;
    }

    private int homeState;

    public int getTestState() {
        return TestState;
    }

    public void setTestState(int testState) {
        TestState = testState;
    }

    //试题状态
    private int TestState;



    public long getTestId() {
        return testId;
    }

    public void setTestId(long testId) {
        this.testId = testId;
    }

    public long getStuWorkId() {
        return stuWorkId;
    }

    public void setStuWorkId(long stuWorkId) {
        this.stuWorkId = stuWorkId;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public long getKnowledgePointId() {
        return knowledgePointId;
    }

    public void setKnowledgePointId(long knowledgePointId) {
        this.knowledgePointId = knowledgePointId;
    }

    public Integer getTestType() {
        return testType;
    }

    public void setTestType(Integer testType) {
        this.testType = testType;
    }

    public float getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(float difficulty) {
        this.difficulty = difficulty;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }


}
