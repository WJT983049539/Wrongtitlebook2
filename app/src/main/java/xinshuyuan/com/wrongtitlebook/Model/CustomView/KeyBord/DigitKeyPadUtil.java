package xinshuyuan.com.wrongtitlebook.Model.CustomView.KeyBord;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;

import xinshuyuan.com.wrongtitlebook.Model.Common.XSYTools;
import xinshuyuan.com.wrongtitlebook.R;

/**
 * 悬浮小键盘
 * @author wjt
 *
 */
public class DigitKeyPadUtil {
	public static boolean flag=false;
	private EditText tv;
	private static int keypadWidth;
	private static int KeyPadHeigth;
	private static  View digitView;
	private Context context;
	private static  WindowManager windowmanager;
	private static int densitydpi;
	public static void showPassWdPadView(final EditText tv, final Context context,View digitViews) {
		densitydpi= XSYTools.densityDpi;

		if(densitydpi>264){
			keypadWidth=800;//800
			KeyPadHeigth=483;//483
		}else{
			keypadWidth=500;//500
			KeyPadHeigth=285;//285
		}

		digitView=digitViews;
		// 让一个视图浮动在你的应用程序之上
		windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		LayoutParams layoutparams = new LayoutParams(
				keypadWidth,KeyPadHeigth,
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE,//
				//是不是这里？？？？？FLAG_NOT_TOUCH_MODAL   TYPE_APPLICATION_PANEL
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN,//flag_not_focuable
				//SOFT_INPUT_MASK_ADJUST

				PixelFormat.RGBA_8888);
		layoutparams.gravity = Gravity.CENTER|Gravity.BOTTOM;
		//layoutparams.alpha = 120;
		//layoutparams.x = 500;
		//layoutparams.y = 500;
		//数字键盘的完成按钮
		Button btn = (Button) digitView.findViewById(R.id.bt_w);
		//字符键盘的完成按钮
		Button btn2 = (Button) digitView.findViewById(R.id.wancheng);

		btn2.setOnClickListener(new mylisten(windowmanager,digitView));

		btn.setOnClickListener(new mylisten(windowmanager,digitView));

		if (digitView.getParent() == null) {
			if(flag==false){
				windowmanager.addView(digitView, layoutparams);
				flag=true;
			}
		}
	}
	//移除view
	public static void hidePopupWindow() {
		if(flag==true){
			windowmanager.removeViewImmediate(digitView);
			flag=false;
			//removeView(digitView);
		}
	}

}
