package com.xinshuyuan.xinshuyuanworkandexercise.presenter.evenlisting;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;

import com.xinshuyuan.xinshuyuanworkandexercise.View.CustomView.TestEditText;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment.WorkExerciseFillBlankFragment;

import org.jsoup.nodes.Document;


/**
 * Created by Administrator on 2017/6/22.
 */

public class FillBlankOnFocusListener implements View.OnFocusChangeListener {
    private WorkExerciseFillBlankFragment frag;
    private WebView wv;
    private String id;
    private Document document;
    public FillBlankOnFocusListener(WorkExerciseFillBlankFragment exerciseFillBlankFragment, WebView wv, String id, Document document) {
        this.frag=exerciseFillBlankFragment;
        this.wv=wv;
        this.id=id;
        this.document=document;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        Log.d("focus", String.valueOf(hasFocus));
        //创建编辑框
        TestEditText et=null;
        if(v instanceof TestEditText){
            et=(TestEditText) v;
        }
        if(et==null){
            return;
        }
        //没焦点，把编辑框的答案和id放入集合
        if(!hasFocus){
            frag.fillAnswerToMap(et.getAnsBlankId(), et.getText().toString());
            //在上面的空中显示数据
            wv.loadUrl("javascript:updateVal('"+id+"','"+et.getText().toString()+"');");

        }else{
            if(et.getTp().equals("bracket")){

            }else{
                frag.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }

        }




    }
}
