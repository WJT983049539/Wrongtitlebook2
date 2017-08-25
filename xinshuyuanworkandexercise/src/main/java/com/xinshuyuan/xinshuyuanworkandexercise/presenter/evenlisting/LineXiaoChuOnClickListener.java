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

public class LineXiaoChuOnClickListener implements View.OnClickListener {
    private WorkExerciseLineFragment lineFragment;
    private Canvas canvas;
    private Paint paint;
    private Bitmap baseBitmap;
    private LinearLayout line_zhu;
    public LineXiaoChuOnClickListener(WorkExerciseLineFragment exerciseLineFragment) {
        this.lineFragment=lineFragment;
    }

    @Override
    public void onClick(View v) {

        line_zhu= (LinearLayout) lineFragment.getActivity().findViewById(R.id.line_main);
        {
            //消除一次的，需要消除一对xy点也就是4个，它永远都是2的倍数
            float[] cc=lineFragment.GetFloatsorry();
            if(cc==null||cc.length<=4){
                line_zhu= (LinearLayout) lineFragment.getActivity().findViewById(R.id.line_main);
                baseBitmap=this.lineFragment.GetBitmap();
                canvas = new Canvas(baseBitmap);
                canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(baseBitmap, 0, 0, paint);
                Drawable drawable=new BitmapDrawable(baseBitmap);
                line_zhu.setBackgroundDrawable(drawable);
                this.lineFragment.setBitMap(baseBitmap);
                //把xy点清除
                this.lineFragment.inintarray();
                //把按钮也清除
                this.lineFragment.cleanArrayList2();

                //全部变成原来的颜色
//					 List<LineButton> liststart=this.lineFragment.getList_start_button();
//					 for(int i=0;i<liststart.size();i++){
//						 liststart.get(i).setBackgroundResource(R.drawable.selectlinebutton);
//					 }

                XSYTools.showToastmsg(lineFragment.getActivity(),"全部消除啦");
            }else if(cc.length%4!=0){
                //把多余的xy轴点删除，这是判断比4多肯定多一组按钮 的处理
                List<Float> listee_one=new ArrayList<Float>(cc.length);
                for(int i=0;i<cc.length;i++){
                    listee_one.add(cc[i]);
                }
                listee_one.remove(listee_one.size()-1);
                listee_one.remove(listee_one.size()-1);
                float[] ff=new float[listee_one.size()];
                //在转成数组方法ff[]
                for(int i=0;i<listee_one.size();i++){
                    ff[i]=listee_one.get(i);
                }
                //处理完放入缓存
                this.lineFragment.setFloatsorry(ff);

                //顺便把多余的一个按钮也删除，肯定多一个按钮
                List<LineButton> listlist_xiaochuyici=this.lineFragment.getListButton();
                if(listlist_xiaochuyici.size()%2!=0){
                    listlist_xiaochuyici.remove(listlist_xiaochuyici.size()-1);
                    this.lineFragment.setListButton(listlist_xiaochuyici);
                }



                //把多余的去掉以后然后在删除

                //剩下的就是比4大并且不是奇数的了，可以一下删除一对按钮，4个点的了
                //得到净化以后的数组转化成集合
                float ddd[]=this.lineFragment.GetFloatsorry();
                List<Float>listee=new ArrayList<Float>(ddd.length);
                for(int i=0;i<ddd.length;i++){
                    listee.add(ddd[i]);
                }
                //移除1组xy点，一对button,并且放进缓存
                listee.remove(listee.size()-1);
                listee.remove(listee.size()-1);
                listee.remove(listee.size()-1);
                listee.remove(listee.size()-1);
                List<LineButton>listtwo=this.lineFragment.getListButton();
                //移除button
                listtwo.remove(listtwo.size()-1);
                listtwo.remove(listtwo.size()-1);

                //操作完成转换成数组
                float[] dd=new float[listee.size()];
                for(int i=0;i<listee.size();i++){
                    dd[i]=listee.get(i);
                }

                if(dd.length==0){
                    this.lineFragment.inintarray();
                    this.lineFragment.cleanArrayList2();
                    //画出空白页
                    XSYTools.showToastmsg(lineFragment.getActivity(),"全部消除啦");
                    // 创建一张画布
                    this.lineFragment.initBitmap();
                    baseBitmap=this.lineFragment.GetBitmap();
                    canvas = new Canvas(baseBitmap);
                    canvas.drawColor(Color.WHITE);
                    canvas.drawBitmap(baseBitmap, 0, 0, paint);
                    Drawable drawable=new BitmapDrawable(baseBitmap);
                    line_zhu.setBackgroundDrawable(drawable);
                    this.lineFragment.setBitMap(baseBitmap);

                }else{

                    this.lineFragment.initBitmap();
                    baseBitmap = this.lineFragment.GetBitmap();
                    // 创建一张画布
                    canvas = new Canvas(baseBitmap);
                    canvas.drawColor(Color.WHITE);
                    // 创建画笔
                    paint = new Paint();
                    paint.setColor(Color.BLACK);
                    paint.setStrokeWidth(5);
                    canvas.drawLines(dd, paint);
                    Drawable drawable=new BitmapDrawable(baseBitmap);
                    line_zhu.setBackgroundDrawable(drawable);
                    this.lineFragment.setBitMap(baseBitmap);

                    this.lineFragment.setFloatsorry(dd);
                    this.lineFragment.setListButton(listtwo);
                }



                //*****************************

            }else{
                //剩下的就是比4大并且不是奇数的了，可以一下删除一对按钮，4个点的了
                //得到净化以后的数组转化成集合
                float ddd[]=this.lineFragment.GetFloatsorry();
                List<Float>listee=new ArrayList<Float>(ddd.length);
                for(int i=0;i<ddd.length;i++){
                    listee.add(ddd[i]);
                }
                //移除1组xy点，一对button,并且放进缓存
                listee.remove(listee.size()-1);
                listee.remove(listee.size()-1);
                listee.remove(listee.size()-1);
                listee.remove(listee.size()-1);
                List<LineButton>listtwo=this.lineFragment.getListButton();
                //移除button
                listtwo.remove(listtwo.size()-1);
                listtwo.remove(listtwo.size()-1);

                //操作完成转换成数组
                float[] dd=new float[listee.size()];
                for(int i=0;i<listee.size();i++){
                    dd[i]=listee.get(i);
                }

                if(dd.length==0){
                    this.lineFragment.inintarray();
                    //画出空白页
                    XSYTools.showToastmsg(lineFragment.getActivity(),"全部消除啦");
                    // 创建一张画布
                    this.lineFragment.initBitmap();
                    baseBitmap=this.lineFragment.GetBitmap();
                    canvas = new Canvas(baseBitmap);
                    canvas.drawColor(Color.WHITE);
                    canvas.drawBitmap(baseBitmap, 0, 0, paint);
                    Drawable drawable=new BitmapDrawable(baseBitmap);
                    line_zhu.setBackgroundDrawable(drawable);
                    this.lineFragment.setBitMap(baseBitmap);

                }else{

                    this.lineFragment.initBitmap();
                    baseBitmap = this.lineFragment.GetBitmap();
                    // 创建一张画布
                    canvas = new Canvas(baseBitmap);
                    canvas.drawColor(Color.WHITE);
                    // 创建画笔
                    paint = new Paint();
                    paint.setColor(Color.BLACK);
                    paint.setStrokeWidth(5);
                    canvas.drawLines(dd, paint);
                    Drawable drawable=new BitmapDrawable(baseBitmap);
                    line_zhu.setBackgroundDrawable(drawable);
                    this.lineFragment.setBitMap(baseBitmap);

                    this.lineFragment.setFloatsorry(dd);

                }

                if(listtwo.size()!=0){
                    this.lineFragment.setListButton(listtwo);
                }else{
                    this.lineFragment.cleanArrayList2();
                }
            }
        }

    }
}
