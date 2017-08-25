package com.xinshuyuan.xinshuyuanworkandexercise.presenter.evenlisting;

import android.view.View;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.SelectButton;
import com.xinshuyuan.xinshuyuanworkandexercise.R;
import com.xinshuyuan.xinshuyuanworkandexercise.View.CustomView.ExerciseIncludeCustomLayout.WorkExerciseMultiSelectLayout;


/**
 * 嵌套题中的多选题选项选择部分的事件实现
 * Created by Administrator on 2017/6/22.
 */

public class IncludeSubMultiSelectOptionClickListener implements View.OnClickListener {
    private WorkExerciseMultiSelectLayout fragment;
    public IncludeSubMultiSelectOptionClickListener(WorkExerciseMultiSelectLayout exerciseMultiSelectLayout) {
        this.fragment = exerciseMultiSelectLayout;
    }


    @Override
    public void onClick(View v) {

        SelectButton btn=(SelectButton) v;
        if(btn.getState().equals(SelectButton.STATE_UNSELECTED)){
            btn.setState(btn.getState()+1);
            btn.setBackgroundResource(R.drawable.multi_select_option_button_shape_selected);
            fragment.addAnswer(btn.getOptionId().toString());
        }else{
            btn.setState(btn.getState()-1);
            btn.setBackgroundResource(R.drawable.multi_select_option_button_shape);
            fragment.removeAnswer(btn.getOptionId().toString());
        }

    }
}
