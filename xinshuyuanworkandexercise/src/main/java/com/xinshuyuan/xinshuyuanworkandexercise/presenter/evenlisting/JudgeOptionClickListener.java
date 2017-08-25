package com.xinshuyuan.xinshuyuanworkandexercise.presenter.evenlisting;

import android.app.Activity;
import android.view.View;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.SelectButton;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;
import com.xinshuyuan.xinshuyuanworkandexercise.R;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment.WorkExerciseJudgeFragment;

import java.util.List;


/**
 * Created by Administrator on 2017/6/21.
 */

public class JudgeOptionClickListener implements View.OnClickListener {

   private WorkExerciseJudgeFragment judgeFragment;
    public JudgeOptionClickListener(WorkExerciseJudgeFragment exerciseJudgeFragment) {
        judgeFragment=exerciseJudgeFragment;
    }



    @Override
    public void onClick(View v) {

        Activity act=judgeFragment.getActivity();
        List<View> views= XSYTools.getAllChildViews(act);
        for(View view:views){
            if(view instanceof SelectButton){
                SelectButton sb=(SelectButton) view;
                sb.setState(sb.getState()-1);
                sb.setBackgroundResource(R.drawable.multi_select_option_button_shape);
                judgeFragment.removeAnswerToMap(sb.getOptionId().toString());
                sb.setTextColor(0xff000000);
            }
        }

        SelectButton btn=(SelectButton) v;
        btn.setState(btn.getState()+1);
        btn.setBackgroundResource(R.drawable.multi_select_option_button_shape_selected);
        judgeFragment.fillAnswerToMap(btn.getOptionId().toString(), btn.getText().toString());
        btn.setTextColor(0xffffffff);

    }
}
