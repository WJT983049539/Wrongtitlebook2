package xinshuyuan.com.wrongtitlebook.Model.CustomView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

public class MyDatePickerDialog extends DatePickerDialog {

    public MyDatePickerDialog (Context context, OnDateSetListener callBack,
                               int year, int monthOfYear, int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);

        this.setTitle("选择日期");
        this.setButton2("取消", (OnClickListener)null);
        this.setButton("确定", this);  //setButton和this参数组合表示这个按钮是确定按钮

    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        super.onDateChanged(view, year, month, day);
        this.setTitle("选择日期");
    }

}