package com.xinshuyuan.xinshuyuanworkandexercise.Model;

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
    //转到显示作业列表的fragment
    public static final int SHOW_WORKLIST_INFO_FRAGMENT=0x012;
    //转到试题列表的界面
    public static final int TEST_LIST_SHOW=0x013;
    //转到统计的界面
    public static final int STATISTICE_FTAGMENT=0x014;
    //显示单个详细试题
    public static final int SHOW_SING_TEST=0x015;
    //当天作业在班中的排名
    public static final int RANKING_STATISTICS=0x016;
    //对比自己跟全班的平局成绩的对比
    public static final int AVERAGE_GTADR_STATISTICS=0x017;
    //整个作业的统计
    public static final int WORK_TEST_STATISTICE=0x018;
    //显示试题列表
    public static final int WORK_SHOW_LIST=0x01;
    //显示上面选择的下拉框
    public static final int WORK_SHOW_TOP=0x019;
    //分册练习
    public static final int WORK_TEST_NEIBU_EXERCISE=0x020;

    //回答正确
    public static final  int NEIBUANS_OK=0x0021;
    //回答错误
    public static final int NEIBUANS_NO=0x0022;
    //不正确也对
    public static final int NEIBUANS_ONLT=0x0023;



    //评论管理界面
    public final static int COMMENTMANAGE=0x024;
    //评论修改页面
    public final static int UPDATECOMMENT=0x025;
    //评论添加页面
    public final static int ADDCOMMENT=0x026;
    //添加成绩的页面
    public final static int GET_GRADE=0x027;
    //评论信息展示页面
    public final static int SHOW_COMMENT_FRAGMETN=0x028;
    //得到栏目评论信息
    public final static int SHOWLANMUCOMMENTINFO=0x029;
    //申诉页面
    public final static int  SHENSUBEGIN=0x030;



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
    //储存集合的json数据缓存
    private static String listJson="";



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
    public static Object readObject(String fileName) throws Exception{
        File cachedir = new File(getCacheDirPath());
        File f=new File(cachedir.getAbsoluteFile()+File.separator+fileName);
        Log.i("shenmgui",f.toString());
        if(f.exists()){
            InputStream is=new FileInputStream(f);
            ObjectInputStream ois=new ObjectInputStream(is);
            return ois.readObject();
        }else{
            return null;
        }
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

    /**
     * 这个是试题列表的数据保存
     * @param s
     */
    public static void setListJson(String s) {
        listJson=s;
    }
    public static String getListJson(){
        return listJson;
    }
    //保存现在的科目
    private static String NoWprojectId;
    public static void setNOWprojectId(String noWprojectId) {
        NoWprojectId=noWprojectId;
    }
    public static String getNOWprojectId(){
        return  NoWprojectId;
    }
    private static String NowbookId;
    //保存现在选择的教材版本
    public static void setNOWbookId(String noWbookId) {
        NowbookId=noWbookId;
    }
    public static String getNOWbookId()
    {
        return NowbookId;
    }

    private static String NowfenceId;
    //保存现在选择的分册
    public static void setNOWfenceId(String noWfenceId) {
        NowfenceId=noWfenceId;
    }
    public static String getNOWfenceId(){
        return NowfenceId;
    }
    //保存现在选择的知识点的id
    private static String knowId;
    public static void setKnowId(String knowId) {
        knowId=knowId;
    }
    public static String getknowId(){
        return knowId;
    }
    //保存选择de学科id
    public static void setSubjectId(String sbtId) {
        SubjectId=sbtId;
    }
    public static String getSubjectId(){
        return SubjectId;
    }
    private static String NowKnowName;
    //存放现在选择的知识点名称
    public static void setNowKnowName(String NowKnowNamea) {
        NowKnowName=NowKnowNamea;
    }
    public static String getNowKnowName(){
        return NowKnowName;
    }
    private static String nowProjectName;
    public static void setNowProjectName(String nowProjectNamea) {
        nowProjectName=nowProjectNamea;
    }
    public static String getnowProjectName(){
        return nowProjectName;
    }
    private static String nowBookName;
    public static void setNowBookName(String nowBookNamea) {
        nowBookName=nowBookNamea;
    }

    public static String getnowBookName(){
        return nowBookName;
    }
    private static String nowfenceName;
    public static void setNowfenceName(String nowfenceNamea) {
        nowfenceName=nowfenceNamea;
    }
    public static String getNowfenceName(){
        return nowfenceName;
    }

    private static String  nowDifferent;
    public static void setnowDifferent(String nowDifferents) {
        nowDifferent=nowDifferents;
    }
    public static String getnowDifferent(){
        return nowDifferent;
    }

}
