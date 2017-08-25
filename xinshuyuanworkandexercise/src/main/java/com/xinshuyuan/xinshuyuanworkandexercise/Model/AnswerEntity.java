package com.xinshuyuan.xinshuyuanworkandexercise.Model;

import java.io.Serializable;

/**答案实体
 * Created by wjt on 2017/6/6.
 */

public class AnswerEntity implements Serializable {

    private Boolean isRealAnswer;
    private String optionTitle;
    private String optionAnswer;
    private Long testId;
    private Long id;
    public Boolean getIsRealAnswer() {
        return isRealAnswer;
    }
    public void setIsRealAnswer(Boolean isRealAnswer) {
        this.isRealAnswer = isRealAnswer;
    }
    public String getOptionTitle() {
        return optionTitle;
    }
    public void setOptionTitle(String optionTitle) {
        this.optionTitle = optionTitle;
    }
    public String getOptionAnswer() {
        return optionAnswer;
    }
    public void setOptionAnswer(String optionAnswer) {
        this.optionAnswer = optionAnswer;
    }
    public Long getTestId() {
        return testId;
    }
    public void setTestId(Long testId) {
        this.testId = testId;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

}
