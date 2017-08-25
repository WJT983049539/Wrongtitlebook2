package xinshuyuan.com.wrongtitlebook.Model;

import java.io.Serializable;

/**
 * 保存成绩的bean
 * Created by wjt on 2017/8/22.
 */

public class Gradebean implements Serializable {
    //学生姓名
    private String stuName;
    //学号
    private String studNum;
    //学科
    private String subject;
    //成绩
    private Double grade;
    //学期名字
    private String semesterName;
    //考试类型
    private Integer examType;

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStudNum() {
        return studNum;
    }

    public void setStudNum(String studNum) {
        this.studNum = studNum;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    public Integer getExamType() {
        return examType;
    }

    public void setExamType(Integer examType) {
        this.examType = examType;
    }
}