package com.xinshuyuan.xinshuyuanworkandexercise.presenter.evenlisting;

import android.app.Activity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.Keybord.DigitKeyPadUtil;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.Keybord.DigitPasswordKeyPad;
import com.xinshuyuan.xinshuyuanworkandexercise.View.CustomView.TestEditText;

import java.lang.reflect.Method;


/**
 * Created by Administrator on 2017/6/23.
 */

public class IncludeSubFillBlankOnTouchListener implements View.OnTouchListener {
    private LinearLayout ikk;
    private Activity activity;
    private EditText editText;
    private DigitPasswordKeyPad dpk;
    private View digitView;

    public IncludeSubFillBlankOnTouchListener(Activity activity, TestEditText et) {
        this.activity=activity;
        this.setEditText(et);
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        {
            // TODO Auto-generated method stub
            DigitKeyPadUtil.hidePopupWindow();

            EditText et=IncludeSubFillBlankOnTouchListener.this.getEditText();
            int inputback = et.getInputType();
            et.setInputType(InputType.TYPE_NULL);
            et.setInputType(inputback);
            dpk = new DigitPasswordKeyPad(this.activity,et);
            //加载小键盘
            digitView = dpk.setup();
            DigitKeyPadUtil.showPassWdPadView((EditText)v,activity,digitView);
            //找到输入框

            if (android.os.Build.VERSION.SDK_INT <= 10) {
                getEditText().setInputType(InputType.TYPE_NULL);
            } else {
                activity.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                try {
                    Class<EditText> cls = EditText.class;
                    Method setShowSoftInputOnFocus;
                    setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus",
                            boolean.class);
                    setShowSoftInputOnFocus.setAccessible(true);
                    setShowSoftInputOnFocus.invoke(getEditText(), false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;




    }
    public Activity getActivity() {
        return activity;
    }
    public void setActivity(Activity activity) {
        this.activity = activity;
    }
    public EditText getEditText() {
        return editText;
    }
    public void setEditText(EditText editText) {
        this.editText = editText;
    }
}
