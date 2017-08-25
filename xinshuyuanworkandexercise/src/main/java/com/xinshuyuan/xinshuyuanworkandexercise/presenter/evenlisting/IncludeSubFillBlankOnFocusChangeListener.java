package com.xinshuyuan.xinshuyuanworkandexercise.presenter.evenlisting;

import android.util.Log;
import android.view.View;

import com.xinshuyuan.xinshuyuanworkandexercise.View.CustomView.ExerciseIncludeCustomLayout.WorkExerciseFillBlankLayout;
import com.xinshuyuan.xinshuyuanworkandexercise.View.CustomView.TestEditText;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment.WorkExerciseNESTFragment;


/**
 * Created by Administrator on 2017/6/23.
 */

public class IncludeSubFillBlankOnFocusChangeListener implements View.OnFocusChangeListener {
    private WorkExerciseNESTFragment fragment;

    public IncludeSubFillBlankOnFocusChangeListener(WorkExerciseNESTFragment fragment) {
        this.fragment=fragment;

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus){

            TestEditText et=(TestEditText) v;
         Log.d("include test", et.getAnsBlankId()+"    "+et.getText().toString());
            WorkExerciseFillBlankLayout fl=  (WorkExerciseFillBlankLayout) et.getParent().getParent().getParent().getParent();
            fl.addAnswer(et.getAnsBlankId(), et.getText().toString());
        }
    }
}
