package com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment;

import android.app.Fragment;
import android.view.View;

/**
 * Created by Administrator on 2017/7/11.
 */

public class BaseFragment extends Fragment {
    public <T extends View> T getViewById(View v, int id){
        if(v==null)
            return null;
        return (T) v.findViewById(id);




    }
}