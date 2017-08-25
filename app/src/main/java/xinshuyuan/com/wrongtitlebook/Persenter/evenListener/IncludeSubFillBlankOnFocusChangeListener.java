package xinshuyuan.com.wrongtitlebook.Persenter.evenListener;

import android.util.Log;
import android.view.View;

import xinshuyuan.com.wrongtitlebook.Model.CustomView.ExerciseIncludeCustomLayout.ExerciseFillBlankLayout;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.TestEditText;
import xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment.ExerciseNESTFragment;

/**
 * Created by Administrator on 2017/6/23.
 */

public class IncludeSubFillBlankOnFocusChangeListener implements View.OnFocusChangeListener {
    private ExerciseNESTFragment fragment;

    public IncludeSubFillBlankOnFocusChangeListener(ExerciseNESTFragment fragment) {
        this.fragment=fragment;

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus){

            TestEditText et=(TestEditText) v;
            Log.d("include test", et.getAnsBlankId()+"    "+et.getText().toString());
            ExerciseFillBlankLayout fl=  (ExerciseFillBlankLayout) et.getParent().getParent().getParent().getParent();
            fl.addAnswer(et.getAnsBlankId(), et.getText().toString());
        }
    }
}
