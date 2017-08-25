package xinshuyuan.com.wrongtitlebook.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/9.
 */

public class FaciInfo implements Serializable{
    public String getFasciName() {
        return FasciName;
    }

    public void setFasciName(String fasciName) {
        FasciName = fasciName;
    }

    public String getFasciId() {
        return FasciId;
    }

    public void setFasciId(String fasciId) {
        FasciId = fasciId;
    }

    private String FasciName;
    private String FasciId;
}
