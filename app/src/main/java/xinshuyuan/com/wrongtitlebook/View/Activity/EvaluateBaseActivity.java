package xinshuyuan.com.wrongtitlebook.View.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import xinshuyuan.com.wrongtitlebook.Model.Tools.ActivityController;

/**
 * 基础类
 * Created by wjt    on 2017/8/21.
 */

public abstract class EvaluateBaseActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i("suzhi","onCreate启动");
        super.onCreate(savedInstanceState);
        //无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window=getWindow();
        //屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        WindowManager.LayoutParams layoutParams= window.getAttributes();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(setLayout());
        initView();
        initData();
        ActivityController.addActivity(this);
    }



    protected abstract int setLayout();


    /**
     * 通过action启动Activity
     * @param pAction
     */
    protected void openActivity(String pAction) {
        openActivity(pAction, null);
    }


    //通过action启动Activity并携带Bundle数据
    protected void openActivity(String pAction, Bundle pBundle) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }




    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     初始化数据
     */
    protected abstract void initData();



    //通过类名启动Activity
    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }
    //通过类启动Activity,然后销毁自己
    protected void openActicity(Class<?> hClass){
        openActivityAndDestoryme(hClass,null);
    }


    /**
     * //通过类名启动Activity并携带Bundle数据
     *
     * @param pClass
     * @param pBundle
     */
    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }



    /**
     * //通过类名启动Activity并携带Bundle数据,这个是跳转后销毁的
     *
     * @param pClass
     * @param pBundle
     */
    protected void openActivityAndDestoryme(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    //结束所有Activity允许子类调用
    protected void finishAll() {
        ActivityController.RemoveAllActivity();
    }

    @Override
    protected void onStart() {
        Log.i("suzhi","onStart启动");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.i("suzhi","onRestart启动");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.i("suzhi","onResume启动");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i("suzhi","onPause启动");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i("suzhi","onStop启动");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i("suzhi","onDestroy启动");
        super.onDestroy();
        ActivityController.RemoveActivity(this);
    }


    /**
     * 简化findViewById()
     * @param resId
     * @param <T>
     * @return
     */
    protected <T extends View> T fvbi(int resId){
        return (T) findViewById(resId);
    }




}
