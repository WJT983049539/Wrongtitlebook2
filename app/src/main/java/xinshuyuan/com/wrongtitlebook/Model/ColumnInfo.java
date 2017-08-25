package xinshuyuan.com.wrongtitlebook.Model;

import java.io.Serializable;

/**
 * 栏目信息java
 * Created by wjt on 2017/8/9.
 */

public class ColumnInfo implements Serializable {
    //栏目id
    private Long LanmuId;
    private String LanmuName;

    public Long getLanmuId() {
        return LanmuId;
    }

    public void setLanmuId(Long lanmuId) {
        LanmuId = lanmuId;
    }

    public String getLanmuName() {
        return LanmuName;
    }

    public void setLanmuName(String lanmuName) {
        LanmuName = lanmuName;
    }
}

