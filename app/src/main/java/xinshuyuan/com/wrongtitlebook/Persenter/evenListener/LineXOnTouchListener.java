package xinshuyuan.com.wrongtitlebook.Persenter.evenListener;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import xinshuyuan.com.wrongtitlebook.Model.Common.LineButton;
import xinshuyuan.com.wrongtitlebook.R;
import xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment.ExerciseLineFragment;

/**
 * Created by Administrator on 2017/6/22.
 */

public class LineXOnTouchListener implements View.OnTouchListener {
    private ExerciseLineFragment lineFragment;
    private Paint paint;
    private Canvas canvas;
    private ImageView imageView;
    private LinearLayout line_zhu;
    private Bitmap baseBitmap;
    private LineButton button;
    float X;
    float Y;
    float endX;
    float endY;
    String pair;
    String Order;
    Float lxx = null;
    Float lyy=null;
    Float rxx = null;
    Float ryy=null;
    float[] aa;
    private List<Float> list=null;
//	public  Bundle b ;

    private static List<Map> listlist=new ArrayList<Map>();
    private   List<LineButton> listlist2;
    Map<Float,Float>xymap=null;
    Map<String,Map>option=new Hashtable<String,Map>();
    private LinearLayout line_right_ddex_layout;

    public LineXOnTouchListener(ExerciseLineFragment exerciseLineFragment) {
        this.lineFragment=exerciseLineFragment;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                button= (LineButton)v;
                System.out.println("button.getLeft()"+button.getLeft());
                System.out.println("button.getTop()"+button.getTop());
                System.out.println("button.getRight()"+button.getRight());
                System.out.println("button.getTop()"+button.getBottom());
                System.out.println("button.getX()"+button.getX());
                System.out.println("button.gety()"+button.getY());
                System.out.println("button.getRotationX()"+button.getRotationX());
                System.out.println("button.getRotationY()"+button.getRotationY());

                int[] location = new int[2];
                v.getLocationOnScreen(location);
                X = location[0];
                Y = location[1];
                System.out.println("X"+X);
                System.out.println("X"+Y);
                //得到存放按钮的数组
                listlist2=this.lineFragment.getListButton();
                System.out.println("得到button大小为："+listlist2.size());
                //判断是否存在重复
                if(listlist2.contains(button)){
                    break;
                }else{
                    listlist2.add(button);
                    this.lineFragment.setListButton(listlist2);
                    //得到数组
                    float bb[]=this.lineFragment.GetFloatsorry();
                    list=new ArrayList<Float>();
                    //如果是第一次添加
                    if(bb==null){
                        //XY坐标
                        list.add(X+10);
                        list.add(Y);
                        aa=new float[list.size()];
                        for(int j=0;j<list.size();j++){
                            aa[j]=list.get(j);
                        }
                        //把添加的xy值放入缓存
                        this.lineFragment.setFloatsorry(aa);

                    }else{
                        //转成集合
                        System.out.print("bb"+ bb.length);
                        for(int i=0;i<bb.length;i++){
                            list.add(bb[i]);
                        }
                        //XY坐标
                        list.add(X+10);
                        list.add(Y);
                        aa=new float[list.size()];
                        for(int j=0;j<list.size();j++){
                            aa[j]=list.get(j);
                        }
                        System.out.println("放进去数组大小为："+aa.length);
                        this.lineFragment.setFloatsorry(aa);
                    }
                }
                System.out.println("X"+X);
                System.out.println("Y"+Y);

                //如果是成对的,只要有2个按钮，或者2的倍数就判断
                //从缓存里面得到数据

                List<LineButton> listlist3=this.lineFragment.getListButton();

                if( listlist3.size()%2==0){

                    //得到最后一个
                    LineButton test2= listlist3.get(listlist3.size()-1);
                    //得到倒数第二个
                    LineButton test1= listlist3.get(listlist3.size()-2);
                    if(test2.getType().equals(test1.getType())){
                        //如果在一边就把前一个替换掉，然后把最后一个删除
                        listlist3.set(listlist3.size()-2, test2);
                        listlist3.remove(listlist3.size()-1);
                        //把那个按钮的xy坐标也删除了
                        float yy[]=this.lineFragment.GetFloatsorry();

                        list=new ArrayList<Float>(yy.length);
                        for(int i=0;i<yy.length;i++){
                            list.add(yy[i]);
                        }
                        //替换操作，用倒数2个数替换倒数34个数

                        float d1=list.get(list.size()-1);
                        float d2=list.get(list.size()-2);
                        float d3=list.get(list.size()-3);
                        float d4=list.get(list.size()-4);
                        list.set(list.size()-3, d1);
                        list.set(list.size()-4, d2);
                        list.remove(list.size()-1);
                        list.remove(list.size()-1);

                        float[] pp=new float[list.size()];
                        for(int j=0;j<list.size();j++){
                            pp[j]=list.get(j);
                        }
                        System.out.println("放进去数组1大小为："+pp.length);
                        this.lineFragment.setFloatsorry(pp);
                    }
                }
                //如果连上，就把状态值变为true
//	           for(int o=0;o<listlist2.size();o++){
//	        	   listlist2.get(o).setYesnoline(true);
//	           }
                this.lineFragment.setListButton(listlist2);


                break;
            case MotionEvent.ACTION_UP:
                if(this.lineFragment.GetFloatsorry()==null){}else{
                    float[] gg=new float[this.lineFragment.GetFloatsorry().length];
                    gg=this.lineFragment.GetFloatsorry();
                    System.out.println("得到的up数组大小："+gg.length);
                    if(gg.length>=4&&gg.length%4==0){
                        // 创建一张画布
                        paint = new Paint(Paint.DITHER_FLAG);//创建一个画笔
                        //paint.setStyle(Paint.Style.STROKE);//设置非填充
                        paint.setStrokeWidth(5);//笔宽5像素
                        paint.setColor(Color.BLACK);//设置为红笔
                        paint.setAntiAlias(true);//锯齿不显示
                        paint.setDither(true);//设置图像抖动处理
                        //paint.setStrokeJoin(Paint.Join.ROUND);//设置图像的结合方式
                        paint.setStrokeCap(Paint.Cap.ROUND);//设置画笔为圆形样式
                        line_zhu= (LinearLayout) lineFragment.getActivity().findViewById(R.id.line_main);
                        baseBitmap=this.lineFragment.GetBitmap();
                        canvas = new Canvas(baseBitmap);
                        canvas.drawColor(Color.WHITE);
                        canvas.drawBitmap(baseBitmap, 0, 0, paint);
                        canvas.drawLines(gg, paint);
                        Drawable drawable=new BitmapDrawable(baseBitmap);
                        line_zhu.setBackgroundDrawable(drawable);
                        this.lineFragment.setBitMap(baseBitmap);
                    }
                }
                //脸上的线按钮都变色
//		   List<LineButton> listlist3= this.lineFragment.getListButton();
//		    for(int i=0;i<listlist3.size();i++){
//		    	listlist3.get(i).setBackgroundResource(R.drawable.linebutton_cliked);
//		    }
                break;
        }
        return false;

    }
}
