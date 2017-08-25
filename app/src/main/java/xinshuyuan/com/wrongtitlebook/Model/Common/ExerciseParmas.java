package xinshuyuan.com.wrongtitlebook.Model.Common;

import java.io.Serializable;

/**
 * 为了继续答题，获取试题保存的参数
 * Created by Administrator on 2017/6/21.
 */

public class ExerciseParmas implements Serializable{

  //年级id
  Integer gradeId;
  //知识点id
  Long knowledgePointId;
  //难度
  String different;

  //学科id
  Integer subjectId;


  public Integer getSubjectId() {
    return subjectId;
  }

  public void setSubjectId(Integer subjectId) {
    this.subjectId = subjectId;
  }

  public Integer getGradeId() {
    return gradeId;
  }

  public void setGradeId(Integer gradeId) {
    this.gradeId = gradeId;
  }

  public Long getKnowledgePointId() {
    return knowledgePointId;
  }

  public void setKnowledgePointId(Long knowledgePointId) {
    this.knowledgePointId = knowledgePointId;
  }

  public String getDifferent() {
    return different;
  }

  public void setDifferent(String different) {
    this.different = different;
  }

  @Override
  public String toString() {
    return "ExerciseParmas{" +
            "subjectId=" + subjectId +
            ", gradeId=" + gradeId +
            ", knowledgePointId=" + knowledgePointId +
            ", different='" + different + '\'' +
            '}';
  }

}
