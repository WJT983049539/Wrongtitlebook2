package xinshuyuan.com.wrongtitlebook.Model;

import java.io.Serializable;

/**
 * 评论信息的javabean
 * Created by wjt on 2017/8/1.
 */

public class CommentInfo implements Serializable {
    //id
    private int comment_id;
    //评论人
    private String conmentorName;
    //一级的评论栏目
    private String Commentary;
    //评论内容
    private String CommentContent;

    //被评论人
    private String studentName;

    //学期
    private String semesterName;

    //二级栏目
    private String secondItemName;

    //评论时间
    private String commentTime;

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    //分数
    private Double score;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    public String getSecondItemName() {
        return secondItemName;
    }

    public void setSecondItemName(String secondItemName) {
        this.secondItemName = secondItemName;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public String getConmentorName() {
        return conmentorName;
    }

    public void setConmentorName(String conmentorName) {
        this.conmentorName = conmentorName;
    }

    public String getCommentary() {
        return Commentary;
    }

    public void setCommentary(String commentary) {
        Commentary = commentary;
    }

    public String getCommentContent() {
        return CommentContent;
    }

    public void setCommentContent(String commentContent) {
        CommentContent = commentContent;
    }



}

