package xinshuyuan.com.wrongtitlebook.Model.Common;

import android.content.Context;
import android.widget.ImageButton;

/**
 * Created by Administrator on 2017/5/23.
 */

public class MyImageButton extends ImageButton{
    //没选中是0，选中是1
    public static final Integer STATE_UNSELECTED=0;
    public static final Integer STATE_SELECTED=STATE_UNSELECTED+1;
    private Integer state=STATE_UNSELECTED;
    private String project="";

    public MyImageButton(Context context) {
        super(context);
    }


    public Integer getState() {
        return state;
    }
    public void setState(Integer state) {
        this.state = state;
    }

    public String getProject(){
        return project;
    }


    public void setProject(String project){
        this.project=project;
    }
    //初始化
    public void init(){
        this.state=STATE_UNSELECTED;


    }

}
