package xinshuyuan.com.wrongtitlebook.Model.Common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 共享参数的设置类
 * Created by Administrator on 2017/6/27.
 */

public class PerferenceService {
    private SharedPreferences.Editor editor;
    private SharedPreferences perferenceService;
    private Context context;
    public  PerferenceService(Context context){
        this.context=context;
        perferenceService=  context.getSharedPreferences("keHouWorkAndExercise",Context.MODE_WORLD_WRITEABLE);
        editor=perferenceService.edit();
    }
    //向外提供对象的方法
    public SharedPreferences getsharedPre(){
        return perferenceService;
    }
    /**
     * 保存参数
     * @param name 姓名
     *  age 年龄
     */
    public void save(String name,String content){

        editor.putString(name, content);
        editor.commit();    //数据提交到xml文件中
    }
    public void save(String name,int content){
        editor.putInt(name, content);
        editor.commit();
    }
    public void save(String name,long content){
        editor.putLong(name, content);
        editor.commit();
    }
    public void save(String name,float content){
        editor.putFloat(name, content);
        editor.commit();
    }
    public void save(String name,Boolean content){
        editor.putBoolean(name, content);
        editor.commit();
    }

}
