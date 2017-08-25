package xinshuyuan.com.wrongtitlebook.View.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import xinshuyuan.com.wrongtitlebook.R;


/**
 * 语文检索界面
 * Created by Administrator on 2017/5/24.
 */

public class LanguageFragment extends Fragment {

    private View mView;
    private Spinner textbookSpinner;
    private Spinner volumeSpinner;
    private Spinner difficultySpinner;
    private Spinner knowledage_pointSpinner;
    private Spinner timesearchSpinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Mview=inflater.inflate(R.layout.fragment_language_know_layout,container,false);

        mView=inflater.inflate(R.layout.fragment_language_know_layout,container,false);
        return mView;
//        init();

    }
   //初始化控件
    private void init() {
        textbookSpinner= (Spinner) mView.findViewById(R.id.textbook);
        volumeSpinner= (Spinner) mView.findViewById(R.id.volume);
        difficultySpinner= (Spinner) mView.findViewById(R.id.difficulty);
        knowledage_pointSpinner= (Spinner) mView.findViewById(R.id.knowledage_point);
        timesearchSpinner= (Spinner) mView.findViewById(R.id.timesearch);
    }
}
