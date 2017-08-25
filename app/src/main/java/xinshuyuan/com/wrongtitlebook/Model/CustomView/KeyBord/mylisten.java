package xinshuyuan.com.wrongtitlebook.Model.CustomView.KeyBord;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

public class mylisten implements OnClickListener {
  private WindowManager windowmanager;
  private View digitView;
	public mylisten(WindowManager windowmanager, View digitView) {
		this.digitView=digitView;
		this.windowmanager=windowmanager;
	}

	@Override
	public void onClick(View v) {
		DigitKeyPadUtil dd=new DigitKeyPadUtil();
		if (digitView.getParent() != null) {
		if(dd.flag==true){
			//windowmanager.removeView(digitView);
			windowmanager.removeViewImmediate(digitView);
			dd.flag=false;
			}
	}

}
	}
