package xinshuyuan.com.wrongtitlebook.View.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import xinshuyuan.com.wrongtitlebook.Model.Common.XSYTools;
import xinshuyuan.com.wrongtitlebook.R;

/**答案解析碎片
 * Created by Administrator on 2017/6/15.
 */

public class AnswerAnalysisFragment extends BaseFragment{
    private View Mview;
    private TextView answerTextview;
    private String answerAnalysis;

    @Nullable
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        Mview=inflater.inflate(R.layout.answer_analysis_layout,container,false);
        inint();

        return Mview;


    }

    private void inint() {
        answerTextview= (TextView) Mview.findViewById(R.id.text_answer_analysis);
        answerTextview.setText(answerAnalysis);
        XSYTools.i("放入解析了！放入解析了！放入解析了！放入解析了！放入解析了！放入解析了！放入解析了！放入解析了！");
    }


    public void setInfo(String answerAnalysis) {

            this.answerAnalysis=answerAnalysis;

    }
}
