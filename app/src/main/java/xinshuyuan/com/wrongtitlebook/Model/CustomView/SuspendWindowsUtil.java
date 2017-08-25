package xinshuyuan.com.wrongtitlebook.Model.CustomView;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

/**悬浮窗显示的工具类
 * Created by Administrator on 2017/6/9.
 */

public class SuspendWindowsUtil {
    //窗口管理器
    private static WindowManager windowManager=null;
    //显示的view
    private static View mView;
    //标志windows是否显示出来
    private static Boolean flag=false;

    public static void ShowSuspendWindowView(Context context ,View view){
        mView=view;

        //实例化WindowsManager

        windowManager=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams=new WindowManager.LayoutParams();
        layoutParams.width=WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height=WindowManager.LayoutParams.MATCH_PARENT;
        Activity activity= (Activity) context;

        int WITCH= activity.getWindowManager().getDefaultDisplay().getWidth();
        int height= activity.getWindowManager().getDefaultDisplay().getHeight();
        Log.i("with",WITCH+"");
//
//        layoutParams.width=WITCH-100;
//        layoutParams.height=height-50;
        //窗口类型,宿主窗口上层
        layoutParams.type=WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        layoutParams.format = PixelFormat.TRANSLUCENT;// 支持透明
        layoutParams.alpha = 0.95f;//窗口的透明度
        layoutParams.flags=WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;//让屏幕占满屏幕，不留任何边界
        layoutParams.gravity= Gravity.CENTER;//窗口位置
        layoutParams.flags= WindowManager.LayoutParams.FIRST_SUB_WINDOW;
//        layoutParams.flags=WindowManager.LayoutParams.FLAG_DIM_BEHIND ;

        //如果这个view没有父容器，穿上衣服
        if (mView.getParent() == null) {
            flag=true;

            windowManager.addView(mView, layoutParams);
        }
    }


    /**
     * 隐藏windows
     * 如果标志是true,脱去衣服
     */

     public static void HideWindow(){
         if(flag==true){
             windowManager.removeView(mView);
             flag=false;
         }

     }

}
