package xinshuyuan.com.wrongtitlebook.Model;

import android.content.Context;
import android.widget.Button;

/**
 * Created by Administrator on 2017/6/19.
 */

public class PictureButton extends Button {

    public static final Integer STATE_UNSELECTED=0;
    public static final Integer STATE_SELECTED=STATE_UNSELECTED+1;
    private Integer state=STATE_UNSELECTED;
    private int optionId=-1;
    public PictureButton(Context context) {
        super(context);
    }
    public int getOptionId() {
        return optionId;
    }
    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }
    public Integer getState() {
        return state;
    }
    public void setState(Integer state) {
        this.state = state;
    }
}