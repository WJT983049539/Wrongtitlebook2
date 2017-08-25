package xinshuyuan.com.wrongtitlebook.Model;

import java.io.Serializable;

/**
 * 列表item信息Javabean
 * Created by wjt on 2017/8/11.
 */

public class CommentListBean implements Serializable {
    //评论id
    private   Long id;
    //教师，家长，同学等别人对她的评价，评论人
    private Long creatorId;
    //评论人
    private String creatorName;
    //评论类型
    private int creatorType;
    //被评论人id，本人
    private  Long studentId;
    //被评论人
    private String studentName;

    //申诉状态
    Integer audit;
    Boolean appeal;

    public Boolean getAppeal() {
        return appeal;
    }

    public Integer getAudit() {
        return audit;
    }

    public void setAudit(Integer audit) {
        this.audit = audit;
    }

    public void setAppeal(Boolean appeal) {
        this.appeal = appeal;
    }

    //内容
    public String getContent() {
        return content;
    }




    public void setContent(String content) {
        this.content = content;
    }

    //内容
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public int getCreatorType() {
        return creatorType;
    }

    public void setCreatorType(int creatorType) {
        this.creatorType = creatorType;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
