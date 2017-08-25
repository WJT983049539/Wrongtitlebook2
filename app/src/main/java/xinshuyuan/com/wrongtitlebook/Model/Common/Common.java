package xinshuyuan.com.wrongtitlebook.Model.Common;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import xinshuyuan.com.wrongtitlebook.View.Activity.SelectSystemActivity;

/**
 * Created by Administrator on 2017/5/22.
 */
public class Common {

    //转到语文界面
    public static  final  int LANGUAGE_FRAGMENT=0x001;
    //显示错题列表
    public static final int SHOWWRONG_LIST=0x002;
    //显示单个详细信息
    public static final int PRIVIEW_SHOW_TEXT=0x003;
    //嵌套题的ｉｔｅｍ信息
    public static final int VIEW_INCLUDTEST_SUBTEST=0x004;
    //获取嵌套题小题的
    public static final int GETINCLUDE_ITEM_TEST=0x005;
    //展现练习试题的
    public static final  int EXERCISE_TEST_SHOW=0x006;
    //回答正确
    public static final  int ANS_OK=0x007;
    //回答错误
    public static final int ANS_NO=0x008;
    //涂抹题放入值得回调
    public static final int Daub_SET_VAL=0x009;
    //只显示答案解析，这个是无法判断对错题型的界面，直接显示答案解析即可
    public static final int ONLY_ANSWER=0x010;
    //答题完成了已经掌握本知识点的试题
    public static final int VERY_GOOD=0X011;


    //涂抹题tag
    public static final String FRAGMENT_TAG_DAUB="daub";
    //嵌套题tag
    public static final String FRAGMENT_TAG_INCLUDE="include";
    //连线题标志
    public static final String FRAGMENT_TAG_LINE="line";
    //涂抹题
    public static final String FRAGMENT_TAG_PAINT ="paint";
    //判断题
    public static final String FRAGMENT_TAG_YESORNO_SELECT="yesornoSelect";
    //多选题
    public static final String FRAGMENT_TAG_MULTISELECT="multiSelect";
    //填空题
    public static final String FRAGMENT_TAG_FILLBLANK="fillBlank";
    //单选题
    public static final String FRAGMENT_TAG_SINGLESELECT="singleSelect";
    //操作题
    public static final String FRAGMENT_TAG_OPRATE="oprate";
    //自定义拍照的tag
    public static final String FRAGMENT_TAG_CUSTOM="customTest";
    //储存检索条件的难度
    public static String NowDifferent="";
    //现在选择的时间排序
    public static String NowTimeOrder="";
    //现在选择的排序
    public static String NowOrder="";
    //已经选择的教材id
    public static String SubjectId;
    //已经选择的教材版本id
    public static String selectBookId;
    //已经选择的分册
    public static String bookSection;
    //已经选择的一级知识点id
    public static String oneKnowleageId;
    //已经选择的二级知识点id
    public static String twoKnowleageId;

    //得到试题所需要的参数
    public static ExerciseParmas exerciseParmas=null;

    /**
     * 获取配置文件名称
     * @return
     */
    public static String getConfigCacheFileName() {
        return "conf.li";
    }

    /**
     * 从磁盘中读取被序列化存储的对象
     * @param fileName
     * @return
     * @throws Exception
     */
    public static Object readObject(String fileName) {
        Object object = null;
        File cachedir = new File(getCacheDirPath());
        File f=new File(cachedir.getAbsoluteFile()+File.separator+fileName);
        Log.i("shenmgui",f.toString());
        if(f.exists()){
            InputStream is= null;
            ObjectInputStream ois=null;

            try {
                is = new FileInputStream(f);
                ois=new ObjectInputStream(is);
                object=ois.readObject();
               
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    if(ois!=null) is.close();
                    if(is!=null) is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }else{
            return null;
        }
        return object;
    }

    /**
     * 读取本机缓存文件夹，默认是机身外置存储（一级存储），如果一级存储不行，会读取data文件夹，但这个文件夹好多机子都不让写数据
     * @return
     */
    public static String getCacheDirPath(){
        String f="";
        try{
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                f=new File(Environment.getExternalStorageDirectory().getCanonicalPath() , "XSYwtb").getAbsolutePath();
                //Log.d("debug", "使用外部存储卡");
            }else{
                File fl=new File(Environment.getRootDirectory().getCanonicalPath(),"XSYwtb");
                f=fl.getAbsolutePath();

                //Log.d("debug", "使用内置存储空间");
            }
            File file=new File(f);
            if(!file.exists()){
                file.mkdir();
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return f;
    }

    /**
     * 将对象序列化到磁盘文件中
     * @param
     * @throwsException
     */
    public static void writeObject(String fileName,Object o) throws Exception{
        File cachedir = new File(getCacheDirPath());
        Long space=cachedir.getFreeSpace();
        if(space<1024*1024*10){
            Long s=space/1024L/1024L;
            //Toast.makeText(XsyApplication.getApp(), "sd卡空间不足,剩余空间为:"+s.toString(), Toast.LENGTH_SHORT).show();
            return;
        }
        File f=new File(cachedir.getAbsoluteFile()+File.separator+fileName);
        if(f.exists()){
            f.delete();
        }
        f.createNewFile();
        FileOutputStream os=new FileOutputStream(f);
        ObjectOutputStream oos=new ObjectOutputStream(os);
        oos.writeObject(o);
        oos.close();
        os.close();
    }

    private static UserEntity userInfo;
    public static void setUserInfo(Long id,Integer userType,String userName){
        Log.d("userInfo", "id="+id+"\tuserName="+userName);
        userInfo=new UserEntity();
        userInfo.setId(id);
        userInfo.setUserName(userName);
        userInfo.setUserType(userType);
    }
    //得到用户信息
    public static UserEntity getUserInfo(){
        return userInfo;
    }

    //储存学科id的map集合
    public static Map<String,String> subjectmap=new HashMap<String, String>();

    public static Map<String, String> getMap() {
        return subjectmap;
    }

    public static void setMap(Map<String, String> map) {
        subjectmap = map;
    }
    public static void init(){
        subjectmap.clear();
    }


    public static void setExerciseParmas(ExerciseParmas exerciseParmasa) {
        exerciseParmas=exerciseParmasa;
    }
    public static ExerciseParmas getExerciseParmas(){
        return exerciseParmas;
    }
    //得到作业系统的Url
    public static String getNowWorkSystemUrl(SelectSystemActivity selectSystemActivity) {
        String nowurl="";
        PerferenceService service=new PerferenceService(selectSystemActivity);
        nowurl=service.getsharedPre().getString("NowWorkSystemUrl","");
        return nowurl;
    }
    private static Long EvaluateUserId;
    public static Long getEvaluateUserId(){
        return EvaluateUserId;
    }
    public static void setEvaluateUserId(Long aaa){
        EvaluateUserId=aaa;
    }
    private static UserEntity userEntity;
    public static void setUserEntity(UserEntity userEntitys) {
        userEntity=userEntitys;
    }
    public static UserEntity getUserEntity(){
        return userEntity;
    }
}
