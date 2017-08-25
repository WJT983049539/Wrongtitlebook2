package xinshuyuan.com.wrongtitlebook.Model.Common;

import java.io.Serializable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Button;

@SuppressLint("AppCompatCustomView")
public class LineButton extends Button implements Serializable{

	//id
	private String lineButtonId;
	//类型
	private String type;
	//左右链接指定
	private String pair;
	//左右位置标识符
	private String oder;
	//判断是否已经链接
	private boolean yesnoline=false;


	public boolean isYesnoline() {
		return yesnoline;
	}
	public void setYesnoline(boolean yesnoline) {
		this.yesnoline = yesnoline;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOder() {
		return oder;
	}
	public void setOder(String oder) {
		this.oder = oder;
	}
	public LineButton(Context context) {
		super(context);
	}
	public String getPair() {
		return pair;
	}
	public void setPair(String pair) {
		this.pair = pair;
	}
	public String getLineButtonId() {
		return lineButtonId;
	}
	public void setLineButtonId(String lineButtonId) {
		this.lineButtonId = lineButtonId;
	}


}
