package xinshuyuan.com.wrongtitlebook.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/10.
 */

public class WrongListMenu implements Serializable{
    //错题id
    Long TestId;
    //错题类型
    Integer TestType;

    //错题标题
    String Title;

    public Long getTestId() {
        return TestId;
    }

    public void setTestId(Long testId) {
        TestId = testId;
    }

    public Integer getTestType() {
        return TestType;
    }

    public void setTestType(Integer testType) {
        TestType = testType;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }



}
