package xinshuyuan.com.wrongtitlebook.Persenter.evenListener;

import android.view.View;

import xinshuyuan.com.wrongtitlebook.Model.CustomView.ExerciseIncludeCustomLayout.ExerciseMultiSelectLayout;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.SelectButton;
import xinshuyuan.com.wrongtitlebook.R;

/**
 * 嵌套题中的多选题选项选择部分的事件实现
 * Created by Administrator on 2017/6/22.
 */

public class IncludeSubMultiSelectOptionClickListener implements View.OnClickListener {
    private ExerciseMultiSelectLayout fragment;
    public IncludeSubMultiSelectOptionClickListener(ExerciseMultiSelectLayout exerciseMultiSelectLayout) {
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
