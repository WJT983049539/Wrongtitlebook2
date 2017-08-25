package xinshuyuan.com.wrongtitlebook.Model.Common;

/**
 * 储存一级菜单的javabean
 * Created by Administrator on 2017/5/25.
 */

public class MenuInfo {


    public String getId() {
        return id;
    }

    public String getContent() {
        return Content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        Content = content;
    }

    private  String id;
    private  String Content;
}
