package xinshuyuan.com.wrongtitlebook.View.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xinshuyuan.com.wrongtitlebook.R;

/**
 * Created by Administrator on 2017/5/24.
 */

public class fuckFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,  Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.test_layout,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
