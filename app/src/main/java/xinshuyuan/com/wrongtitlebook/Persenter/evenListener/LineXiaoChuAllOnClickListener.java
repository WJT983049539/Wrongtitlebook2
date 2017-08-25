package xinshuyuan.com.wrongtitlebook.Persenter.evenListener;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import xinshuyuan.com.wrongtitlebook.Model.Common.LineButton;
import xinshuyuan.com.wrongtitlebook.Model.Common.XSYTools;
import xinshuyuan.com.wrongtitlebook.R;
import xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment.ExerciseLineFragment;

/**
 * Created by Administrator on 2017/6/22.
 */

public class LineXiaoChuAllOnClickListener implements View.OnClickListener {
    private ExerciseLineFragment lineFragment;
    private Canvas canvas;
    private Paint paint;
    private LinearLayout line_zhu;
    private Bitmap baseBitmap;
    private List<LineButton> listlist2=new ArrayList<LineButton>();
    public LineXiaoChuAllOnClickListener(ExerciseLineFragment exerciseLineFragment) {
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
