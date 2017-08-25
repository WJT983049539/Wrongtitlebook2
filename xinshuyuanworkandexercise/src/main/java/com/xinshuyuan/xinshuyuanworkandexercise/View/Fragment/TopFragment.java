package com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xinshuyuan.xinshuyuanworkandexercise.R;

/**
 * Created by Administrator on 2017/7/5.
 */

public class TopFragment extends Fragment {
    private View mView;
    private Button UptakePhone;
    private TextView name_textview;
    private String userName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView=inflater.inflate(R.layout.work_fragment_top_layout,container,false);
        UptakePhone=(Button)mView.findViewById(R.id.updatetakephone);

        name_textview=(TextView)mView.findViewById(R.id.name_textView);
        name_textview.setText(userName);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    public void setInfo(String userName) {
        this.userName=userName;
    }
}