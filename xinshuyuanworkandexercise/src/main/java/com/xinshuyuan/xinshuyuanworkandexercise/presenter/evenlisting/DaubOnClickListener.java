package com.xinshuyuan.xinshuyuanworkandexercise.presenter.evenlisting;

import android.view.View;
import android.webkit.WebView;

import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment.WorkExerciseDaubFragment;

/**
 * 涂抹题提交事件
 * Created by Administrator on 2017/6/22.
 */

public class DaubOnClickListener implements View.OnClickListener {
    private WorkExerciseDaubFragment exerciseDaubFragment;
    private WebView answeview;
    public DaubOnClickListener(WorkExerciseDaubFragment exerciseDaubFragment, WebView ansWv) {
        answeview=ansWv;
        this.exerciseDaubFragment=exerciseDaubFragment;
    }

    @Override
    public void onClick(View v) {
        answeview.loadUrl("javascript:save()");
    }
}
