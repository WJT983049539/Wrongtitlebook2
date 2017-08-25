package com.xinshuyuan.xinshuyuanworkandexercise.presenter.evenlisting;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.javabean.LineButton;
import com.xinshuyuan.xinshuyuanworkandexercise.R;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment.WorkExerciseLineFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/22.
 */

public class LineXiaoChuAllOnClickListener implements View.OnClickListener {
    private WorkExerciseLineFragment lineFragment;
    private Canvas canvas;
    private Paint paint;
    private LinearLayout line_zhu;
    private Bitmap baseBitmap;
    private List<LineButton> listlist2=new ArrayList<LineButton>();
    public LineXiaoChuAllOnClickListener(WorkExerciseLineFragment exerciseLineFragment) {
        lineFragment=exerciseLineFragment;
    }

    @Override
    public void onClick(View v) {

        this.lineFragment.inintarray();
        listlist2=this.lineFragment.getListButton();
        listlist2.clear();
        lineFragment.setListButton(listlist2);
        line_zhu= (LinearLayout) lineFragment.getActivity().findViewById(R.id.line_main);
        this.lineFragment.initBitmap();
        baseBitmap=this.lineFragment.GetBitmap();
        canvas = new Canvas(baseBitmap);
        canvas.drawColor(Color.WHITE);
        Drawable drawable=new BitmapDrawable(baseBitmap);
        line_zhu.setBackgroundDrawable(drawable);
        this.lineFragment.setBitMap(baseBitmap);
        //把所有按钮变回原来的颜色

//			 List<LineButton> liststart=this.lineFragment.getList_start_button();
//			 for(int i=0;i<liststart.size();i++){
//				 liststart.get(i).setBackgroundResource(R.drawable.selectlinebutton);
//			 }
        XSYTools.showToastmsg(lineFragment.getActivity(),"全部消除啦");


    }
}
