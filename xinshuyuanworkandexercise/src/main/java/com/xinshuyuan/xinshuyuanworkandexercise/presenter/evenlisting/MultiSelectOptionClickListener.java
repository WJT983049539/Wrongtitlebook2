package com.xinshuyuan.xinshuyuanworkandexercise.presenter.evenlisting;

import android.view.View;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.SelectButton;
import com.xinshuyuan.xinshuyuanworkandexercise.R;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment.WorkExerciseMultSelectFragment;


/**
 * Created by Administrator on 2017/6/21.
 */

public class MultiSelectOptionClickListener implements View.OnClickListener {
    private WorkExerciseMultSelectFragment fragment;
    public MultiSelectOptionClickListener(WorkExerciseMultSelectFragment exerciseMultSelectFragment) {
     this.fragment=exerciseMultSelectFragment;

    }


    @Override
    public void onClick(View v) {
        //未选中
        SelectButton btn=(SelectButton) v;
        if(btn.getState().equals(SelectButton.STATE_UNSELECTED)){
            btn.setState(btn.getState()+1);
            btn.setBackgroundResource(R.drawable.multi_select_option_button_shape_selected);
            fragment.fillAnswerToMap(btn.getOptionId().toString(),  btn.getText().toString());

            btn.setTextColor(0xffffffff);
        }else{
            //选中后
            btn.setState(btn.getState()-1);
            btn.setBackgroundResource(R.drawable.multi_select_option_button_shape);
            fragment.removeAnswerToMap(btn.getOptionId().toString());
            btn.setTextColor(0xff000000);
        }
    }
}
