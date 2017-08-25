package xinshuyuan.com.wrongtitlebook.Persenter.evenListener;

import android.app.Activity;
import android.view.View;

import java.util.List;

import xinshuyuan.com.wrongtitlebook.Model.Common.XSYTools;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.SelectButton;
import xinshuyuan.com.wrongtitlebook.R;
import xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment.ExerciseJudgeFragment;

/**
 * Created by Administrator on 2017/6/21.
 */

public class JudgeOptionClickListener implements View.OnClickListener {

   private ExerciseJudgeFragment judgeFragment;
    public JudgeOptionClickListener(ExerciseJudgeFragment exerciseJudgeFragment) {
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
