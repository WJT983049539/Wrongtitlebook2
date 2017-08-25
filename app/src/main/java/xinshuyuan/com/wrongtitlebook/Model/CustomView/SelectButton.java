package xinshuyuan.com.wrongtitlebook.Model.CustomView;

import android.content.Context;
import android.widget.Button;

public class SelectButton extends android.support.v7.widget.AppCompatButton {

	public static final Integer STATE_UNSELECTED=0;
	public static final Integer STATE_SELECTED=STATE_UNSELECTED+1;
	private Integer state=STATE_UNSELECTED;
	private Long optionId=-1L;
	public SelectButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public Long getOptionId() {
		return optionId;
	}
	public void setOptionId(Long optionId) {
		this.optionId = optionId;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}


}
