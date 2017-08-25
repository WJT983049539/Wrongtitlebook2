package xinshuyuan.com.wrongtitlebook.Persenter.Appactionclass;

import android.app.Application;

import com.lzy.imagepickeruu.ImagePicker;
import com.lzy.imagepickeruu.view.CropImageView;
import com.lzy.okgo.OkGo;

import xinshuyuan.com.wrongtitlebook.Persenter.Handler.AppHandler;
import xinshuyuan.com.wrongtitlebook.Persenter.workRunnable.GlideImageLoader;

/**
 * 错题本应用类
 * Created by Administrator on 2017/6/5.
 */

public class WrongtitlebiikAppaction extends Application{
   private AppHandler appHandler=null;
    @Override
    public void onCreate() {

        init();
        super.onCreate();
        //必须调用初始化
        OkGo.init(this);
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(false);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素

    }
    private void init(){
        appHandler=new AppHandler(this);
    }
}
