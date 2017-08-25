package com.xinshuyuan.xinshuyuanworkandexercise.presenter.evenlisting;

import android.app.Activity;
import android.view.View;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.SelectButton;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;
import com.xinshuyuan.xinshuyuanworkandexercise.R;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment.WorkExerciseSelsctFragment;

import java.util.List;


/**单选题按钮提交事件
 * Created by Administrator on 2017/6/21.
 */

public class SingSelectOptionClickListener implements View.OnClickListener {
    private WorkExerciseSelsctFragment fragment;
    public SingSelectOptionClickListener(WorkExerciseSelsctFragment exerciseSelsctFragment) {
        this.fragment=exerciseSelsctFragment;
    }

    @Override
    public void onClick(View v) {

        Activity act=fragment.getActivity();


        //每次点进来每个的状态值都移除
        List<View> views= XSYTools.getAllChildViews(act);
        for(View view:views){
            if(view instanceof SelectButton){
                SelectButton sb=(SelectButton) view;
                sb.setState(sb.getState()-1);
                sb.setBackgroundResource(R.drawable.multi_select_option_button_shape);
                fragment.removeAnswerToMap(sb.getOptionId().toString());
                sb.setTextColor(0xff000000);
            }
        }
        //选中后改变
        SelectButton btn=(SelectButton) v;
        btn.setState(btn.getState()+1);
        btn.setBackgroundResource(R.drawable.multi_select_option_button_shape_selected);
        fragment.fillAnswerToMap(btn.getOptionId().toString(), btn.getText().toString());
        //放入字体颜色
        btn.setTextColor(0xffffffff);

    }
}
