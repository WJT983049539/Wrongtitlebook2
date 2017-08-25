package xinshuyuan.com.wrongtitlebook.Persenter.evenListener;

import android.view.View;
import android.webkit.WebView;

import xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment.ExerciseDaubFragment;

/**
 * 涂抹题提交事件
 * Created by Administrator on 2017/6/22.
 */

public class DaubOnClickListener implements View.OnClickListener {
    private ExerciseDaubFragment exerciseDaubFragment;
    private WebView answeview;
    public DaubOnClickListener(ExerciseDaubFragment exerciseDaubFragment, WebView ansWv) {
        answeview=ansWv;
        this.exerciseDaubFragment=exerciseDaubFragment;
    }

    @Override
    public void onClick(View v) {
        answeview.loadUrl("javascript:save()");
    }
}
