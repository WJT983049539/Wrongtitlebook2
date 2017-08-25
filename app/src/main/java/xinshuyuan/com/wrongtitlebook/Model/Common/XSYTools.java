package xinshuyuan.com.wrongtitlebook.Model.Common;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Message;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by wjt on 2017/5/5.
 * 工具类
 */

public class XSYTools {
    public static int pingmucc=0;
    public static int densityDpi=0;


    private static String PREFIX = "\\u";


    //追加 ，参数1 文件路径，参数2 内容
    public static void contentToTxt(String filePath, String content) {
        String str = new String(); //原有txt内容
        String s1 = new String();//内容更新
        try {
            File f = new File(filePath);
            if (f.exists()) {
                System.out.print("文件存在");
            } else {
                System.out.print("文件不存在");
                f.createNewFile();// 不存在则创建
            }
            BufferedReader input = new BufferedReader(new FileReader(f));

            while ((str = input.readLine()) != null) {
                s1 += str;
            }
            System.out.println(s1);
            input.close();
            s1 += content;

            BufferedWriter output = new BufferedWriter(new FileWriter(f));
            output.write(s1);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    /**
     * 验证网址Url
     *
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsUrl(String str) {
        String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
        return match(regex, str);
    }
    /**
     * @param regex
     * 正则表达式字符串
     * @param str
     * 要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 从sd卡获取html网页资源
     */
    public static  String getHtmlPathFromSD(String filename){
        String htmlname="";
        File[] files=new File(filename).listFiles();
        for(int i=0;i<files.length;i++){
            File[]	files2= files[i].listFiles();
            for(int j=0;j<files2.length;j++){
                File file = files2[j];
                if(checkIsHtmlFile(file.getPath())){
                    htmlname=file.getPath();
                    break;
                }
            }
        }
        return htmlname;

    }
    private static boolean checkIsHtmlFile(String path) {
        boolean isImageFile = false;
        // 获取扩展名
        String FileEnd = path.substring(path.lastIndexOf(".") + 1,
                path.length()).toLowerCase();
        if (FileEnd.equals("html")) {
            isImageFile = true;
        }
        return isImageFile;
    }

    /**
     * 得到文件里面是txt的内容
     * @throws IOException
     */

    public static String getFileText(String fileneme) throws IOException{
        //text 文件
        String textPathList ="";
        File file_one = new File(fileneme);
        // 得到该路径文件夹下所有的文件
        File[] file_oneFiles = file_one.listFiles();
        for (int i = 0; i < file_oneFiles.length; i++) {
            File file = file_oneFiles[i];
            if (checkText(file.getPath())) {
                textPathList=file.getPath();
            }
        }
        File file=new File(textPathList);
        StringBuffer result=new StringBuffer();
        //得到输出流
        BufferedReader reader=new BufferedReader(new FileReader(file));
        String tt="";
        //读
        while((tt=reader.readLine())!=null){
            result.append(tt);
        }
        return result.toString();
    }
    /**
     * 检查过滤text文件
     */
    private static boolean checkText(String fName){
        boolean isImageFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("txt")) {
            isImageFile = true;
        } else {
            isImageFile = false;
        }
        return isImageFile;

    }

    /**
     * 获得分辨率的字符串
     * @param context
     * @return String
     */
    public static String getResolution(Context context){
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = screenWidth = display.getWidth();
        int screenHeight = screenHeight = display.getHeight();
        String Resolution=String.valueOf(screenWidth)+"*"+String.valueOf(screenHeight);
        return Resolution;
    }

    /**
     * 获取总rom的方法
     *  Context
     * @return long
     */
    public static String getmem_UNUSED(Context mContext) {
        //"Rom总容量:"+getSDTotalSize(mContext)/1024/1024+"M"+"\n
        return  "手机Rom可用容量:"+getSDAvailableSize(mContext)/1024/1024+"M";


    }
    /**
     * 获得SD卡总大小
     *
     * @return
     */
    private static long getSDTotalSize(Context context) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return blockSize * totalBlocks;
    }
    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    private static long getSDAvailableSize(Context context) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks;
    }

    /**得到硬件唯一标识符 */
    public static String getMyUUID(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice, tmSerial, tmPhone, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = ""+ android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(),((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        Log.d("debug", "uuid=" + uniqueId);
        return uniqueId;

    }

    /**
     * 获取cup使用率
     *
     */
    public static int getProcessCpuRate() {

        StringBuilder tv = new StringBuilder();
        int rate = 0;

        try {
            String Result;
            Process p;
            p = Runtime.getRuntime().exec("top -n 1");

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((Result = br.readLine()) != null) {
                if (Result.trim().length() < 1) {
                    continue;
                } else {
                    String[] CPUusr = Result.split("%");
                    tv.append("USER:" + CPUusr[0] + "\n");
                    String[] CPUusage = CPUusr[0].split("User");
                    String[] SYSusage = CPUusr[1].split("System");
                    tv.append("CPU:" + CPUusage[1].trim() + " length:" + CPUusage[1].trim().length() + "\n");
                    tv.append("SYS:" + SYSusage[1].trim() + " length:" + SYSusage[1].trim().length() + "\n");
                    rate = Integer.parseInt(CPUusage[1].trim()) + Integer.parseInt(SYSusage[1].trim());
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(rate + "");
        return rate;
    }


    /**判断是否包含相同的字符串
     *
     * @param zong
     * @param str
     * @return
     */
    public static  Boolean  getSubStrByRole(String  zong, String str){
        Boolean subStr=false;
        String[] subNo = zong.split(",");
        for(int i=0;i<subNo.length;i++){
            if(subNo[i].indexOf(str)!=-1){
                subStr=true;
            }
        }
        return subStr;
    }

    /**自定义工具类
     * 显示提示框
     * @param context
     * @param msg
     */
    public static void showToastmsg(Context context,String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }


    public static void alertMsg(Context context,String msgTitle,String msgCont){
        SweetAlertDialog dia = new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE).setTitleText(msgTitle).setContentText(msgCont).showCancelButton(false);
        dia.show();
    }
      //延迟dialog
    public static void delayalertMsg(final Context context, final String msgTitle, final String msgCont){
        final SweetAlertDialog dia = new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE).setTitleText(msgTitle).setContentText(msgCont).showCancelButton(false);
        dia.show();
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dia.dismiss();
            }
        },2000);


        }
    public static SweetAlertDialog processMsg(Context context,String msgTitle,String msgCont){
        SweetAlertDialog dia = new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE).setTitleText(msgTitle).setContentText(msgCont);
        dia.show();
        return dia;
    }
    /**
     * 获取内置SD卡路径
     * @return
     */
    public static String getInnerSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 日志输出、记录和回传
     * Created by MG on 2017/3/23.
     */

    public static void v(String tag,String msg){
        Log.v(tag,msg);
    }
    public static void v(Object sourceObject,String msg){
        Log.v(sourceObject.getClass().getSimpleName(),msg);
    }
    public static void d(String tag,String msg){
        Log.d(tag,msg);
    }
    public static void d(Object sourceObject,String msg){
        Log.d(sourceObject.getClass().getSimpleName(),msg);
    }
    public static void i(String tag,String msg){
        Log.i(tag,msg);
    }
    public static void i(String msg){
        Log.i("XSY",msg);
    }
    public static void i(Object sourceObject,String msg){
        Log.i(sourceObject.getClass().getSimpleName(),msg);
    }
    public static void w(String tag,String msg){
        Log.w(tag,msg);
    }
    public static void w(Object sourceObject,String msg){
        Log.w(sourceObject.getClass().getSimpleName(),msg);
    }
    public static void e(String tag,String msg){
        Log.e(tag,msg);
    }
    public static void e(Object sourceObject,String msg){
        Log.e(sourceObject.getClass().getSimpleName(),msg);
    }

    /**
     * 判断是否有网络
     */
    public static boolean networkIsConnected(Context context){
        if(isNetworkConnected(context)){
            return true;
        }
        if(isWifiConnected(context)){
            return true;
        }
        if(isMobileConnected(context)){
            return true;
        }
        return false;
    }
    /**
     * 判断是否有网络连接
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断wifi是否有连接
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    /**
     * 判断移动网络是否有连接
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断网络类型
     * @param context
     * @return 返回值 -1：没有网络  1：WIFI网络2：wap网络3：net网络
     */
    public static int GetNetType(Context context)
    {
        int netType = -1;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo==null)
        {
            return netType;
        }
        int nType = networkInfo.getType();
        if(nType==ConnectivityManager.TYPE_MOBILE)
        {
            if(networkInfo.getExtraInfo().toLowerCase().equals("cmnet"))
            {
                netType = 3;
            }
            else
            {
                netType = 2;
            }
        }
        else if(nType==ConnectivityManager.TYPE_WIFI)
        {
            netType = 1;
        }
        return netType;
    }
    /** 解压文件名包含传入文字的文件
     *
     *  zipFile 压缩文件全部路径
     *  folderPath 目标文件夹
     *  nameContains 传入的文件匹配名
     *  ZipException 压缩格式有误时抛出
     * @throws IOException IO错误时抛出
     */
    public static ArrayList<File> upZipSelectedFile(File zipFile, String folderPath) throws ZipException, IOException {
        ArrayList<File> fileList = new ArrayList<File>();
        int BUFF_SIZE =2048; //这里缓冲区我们使用2KB，
        File desDir = new File(folderPath);
        //如果文件存在先删除然后创建
        if( desDir.exists()){
            desDir.delete();
            desDir.mkdir();
        }

        ZipFile zf = new ZipFile(zipFile);
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements();) {
            ZipEntry entry = ((ZipEntry)entries.nextElement());

            InputStream in = zf.getInputStream(entry);
            String str = folderPath + File.separator + entry.getName();
            str = new String(str.getBytes("8859_1"), "GB2312");
            // str.getBytes("GB2312"),"8859_1" 输出
            // str.getBytes("8859_1"),"GB2312" 输入
            File desFile = new File(str);
            if (!desFile.exists()) {
                File fileParentDir = desFile.getParentFile();
                if (!fileParentDir.exists()) {
                    fileParentDir.mkdirs();
                }
                desFile.createNewFile();
            }
            OutputStream out = new FileOutputStream(desFile);
            byte buffer[] = new byte[BUFF_SIZE];
            int realLength;
            while ((realLength = in.read(buffer)) > 0) {
                out.write(buffer, 0, realLength);
            }
            in.close();
            out.close();
            fileList.add(desFile);
        }

        return fileList;
    }

    public static Message makeNewMessage(int what, Object obj){
        Message msg=new Message();
        msg.what=what;
        msg.obj=obj;
        return msg;
    }

    /**
     * 从sd卡获取图片资源
     * @return
     */
    public static List<String> getImagePathFromSD(String fileneme) {
        // 图片列表
        List<String> imagePathList = new ArrayList<String>();
        // 得到sd卡内image文件夹的路径   File.separator(/)
        String filePath =  fileneme;
        // 得到该路径文件夹下所有的文件
        File fileAll = new File(filePath);
        File[] files = fileAll.listFiles();
        // 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (checkIsImageFile(file.getPath())) {
                imagePathList.add(file.getPath());
            }
        }
        // 返回得到的图片列表
        return imagePathList;
    }
    /**
     * 检查扩展名，得到图片格式的文件
     * @param fName  文件名
     * @return
     */
    private static boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("png")
                || FileEnd.equals("jpeg")|| FileEnd.equals("bmp") ) {
            isImageFile = true;
        } else {
            isImageFile = false;
        }
        return isImageFile;
    }
    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }


    /**
     * 下载文件
     */
    public static <T> Callback.Cancelable DownLoadFile(String url, String filepath, Callback.CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        //设置断点续传
        params.setAutoResume(true);
        params.setSaveFilePath(filepath);
        Callback.Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }

    private static final String DEFAULT_ENCODING = "UTF-8";//编码
    private static final int PROTECTED_LENGTH = 51200;// 输入流保护 50KB

    public static String readInfoStream(InputStream input) throws Exception {
        if (input == null) {
            throw new Exception("输入流为null");
        }
        //字节数组
        byte[] bcache = new byte[2048];
        int readSize = 0;//每次读取的字节长度
        int totalSize = 0;//总字节长度
        ByteArrayOutputStream infoStream = new ByteArrayOutputStream();
        try {
            //一次性读取2048字节
            while ((readSize = input.read(bcache)) > 0) {
                totalSize += readSize;
                if (totalSize > PROTECTED_LENGTH) {
                    throw new Exception("输入流超出50K大小限制");
                }
                //将bcache中读取的input数据写入infoStream
                infoStream.write(bcache,0,readSize);
            }
        } catch (IOException e1) {
            throw new Exception("输入流读取异常");
        } finally {
            try {
                //输入流关闭
                input.close();
            } catch (IOException e) {
                throw new Exception("输入流关闭异常");
            }
        }

        try {
            return infoStream.toString(DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new Exception("输出异常");
        }
    }

    /**
     * 得到txt内容
     * @param fileneme
     * @return
     * @throws IOException
     */
    public static String getFileprogramText(String fileneme) throws IOException{
        //text 文件

        File file=new File(fileneme);
        StringBuffer result=new StringBuffer();
        //得到输出流
        BufferedReader reader=new BufferedReader(new FileReader(file));
        String tt="";
        //读
        while((tt=reader.readLine())!=null){
            result.append(tt);
        }
        return result.toString();

    }

    //判断月份
    public static String getStartTime_ofmounth(int mouth,int hour,int minute,int second){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c1 = new GregorianCalendar();
        c1.set(Calendar.MONTH,mouth-1);
        c1.set(Calendar.HOUR_OF_DAY,hour);
        c1.set(Calendar.MINUTE,minute);
        c1.set(Calendar.SECOND, second);
        return df.format(c1.getTime());
    }
    //判断日
    public static String getStartTime_ofday(int day,int hour,int minute,int second){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c1 = new GregorianCalendar();
        c1.set(Calendar.DAY_OF_MONTH, day);
        c1.set(Calendar.HOUR_OF_DAY,hour);
        c1.set(Calendar.MINUTE,minute);
        c1.set(Calendar.SECOND, second);
        return df.format(c1.getTime());
    }

    /**
     * 判断当前手机是否有ROOT权限
     * @return
     */
    public static boolean isRoot(){
        boolean bool = false;

        try{
            if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())){
                bool = false;
            } else {
                bool = true;
            }
        } catch (Exception e) {

        }
        return bool;
    }

    /**
     * root截图的代码
     * @return
     * @throws Exception
     */
    public static String takeSnapShoot() throws Exception {
        String strPath = "/sdcard/screen.png";
        Process sh = Runtime.getRuntime().exec("su", null,null);
        OutputStream os = sh.getOutputStream();
        String cmd="su " + " -c " + "'"+"/system/bin/screencap -p "+strPath +"'";
        os.write((cmd).getBytes("ASCII"));
        os.flush();
        os.close();
        sh.waitFor();
        Matrix matrix = new Matrix();
        Bitmap bitmap = BitmapFactory.decodeFile(strPath);
        // 设置旋转角度
        matrix.setRotate(90);
        // 重新绘制Bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), matrix, true);
        File f=new File(strPath);
        FileOutputStream fileOut=new FileOutputStream(f);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOut);
        fileOut.flush();
        fileOut.close();
        return strPath;
    }
    /*
     * 执行命令
     * @param command
     * 1、获取root权限 "chmod 777 "+getPackageCodePath()
     * 2、关机 reboot -p
     * 3、重启 reboot
     */
    public static boolean execCmd(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command+"\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if(process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }
    public static String native2Ascii(String str) {
        char[] chars = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            sb.append(char2Ascii(chars[i]));
        }
        return sb.toString();
    }
    private static String char2Ascii(char c) {

        if (c > 255) {
            StringBuilder sb = new StringBuilder();
            sb.append(PREFIX);
            int code = (c >> 8);
            String tmp = Integer.toHexString(code);
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
            code = (c & 0xFF);
            tmp = Integer.toHexString(code);
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
            return sb.toString();
        } else {
            return Character.toString(c);
        }
    }



    public static String ascii2Native(String str) {
        StringBuilder sb = new StringBuilder();
        int begin = 0;
        int index = str.indexOf(PREFIX);
        while (index != -1) {
            sb.append(str.substring(begin, index));
            sb.append(ascii2Char(str.substring(index, index + 6)));
            begin = index + 6;
            index = str.indexOf(PREFIX, begin);
        }
        sb.append(str.substring(begin));
        return sb.toString();
    }

    /**
     * Ascii to native character.
     * @param str ascii string
     * @return native character
     */
    private static char ascii2Char(String str) {
        if (str.length() != 6) {
            throw new IllegalArgumentException(
                    "Ascii string of a native character must be 6 character.");
        }
        if (!PREFIX.equals(str.substring(0, 2))) {
            throw new IllegalArgumentException(
                    "Ascii string of a native character must start with \"\\u\".");
        }
        String tmp = str.substring(2, 4);
        int code = Integer.parseInt(tmp, 16) << 8;
        tmp = str.substring(4, 6);
        code += Integer.parseInt(tmp, 16);
        return (char) code;
    }




    /**
     * 得到屏幕高度
     */
    public static int GetDisplayHight(Context context){
        DisplayMetrics displayMetrics=context.getResources().getDisplayMetrics();
        int aa=displayMetrics.heightPixels;
        return aa;
    }
    //得到屏幕密度
    public static int GetDisplayMetrics(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int aa=displayMetrics.densityDpi;
        return aa;
    }

    /**初始化url*/
    public static String joinUrl(String url){
        String Ip=ConfigUtil.getIp();
        String Port=ConfigUtil.getPort();
        String ContentCode=ConfigUtil.getContentCode();
        String basePath="http://"+Ip+":"+Port+"/"+ContentCode+"/";
        return basePath+url;
    }

    //得到mac地址
    public static String getLocalMacAddressFromWifiInfo(Context context){
        String Macadd;
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        Macadd=info.getMacAddress();
        if(Macadd.equals("02:00:00:00:00:00"))
        {
            Macadd = getMacAddr();
        }
        return Macadd;

    }
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }


    /**
     * 得到值
     * @param obj
     * @param key
     * @return
     */
    public static String getStringValFromJSONObject(JSONObject obj,String key){
        Object o;
        try {
            o = getValFromJSONObject(obj,key);
            if(o==null){
                return null;
            }else{
                return o.toString();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 传入json 和键 得到值
     * @param obj
     * @param key
     * @return
     * @throws JSONException
     */
    public static Object getValFromJSONObject(JSONObject obj,String key) throws JSONException{
        if(obj==null){
            return null;
        }
        return obj.has(key)?obj.get(key):null;
    }
    /**不为空*/
    public static Boolean isEmpty(String s){
        if(isNull(s)){
            return true;
        }else{
            if(s.trim().equals("")){
                return true;
            }else{
                return false;
            }
        }
    }
    public static Boolean isNull(Object o){
        if(o==null)
            return true;
        else
            return false;
    }


    public static boolean isNumeric(String str){
        if(isEmpty(str)){
            return false;
        }//无法转换为数字
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
    /**
     * 判断是否为平板
     *
     * @return
     */
    public static boolean isPad(Context context) {
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        // 屏幕宽度
        float screenWidth = display.getWidth();
        // 屏幕高度
        float screenHeight = display.getHeight();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        // 屏幕尺寸
        double screenInches = Math.sqrt(x + y);
        // 大于6尺寸则为Pad
        if (screenInches >= 6.0) {
            return true;
        }
        return false;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //得到子view
    public static List<View> getAllChildViewss(View view) {
        List<View> allchildren = new ArrayList<View>();
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewchild = vp.getChildAt(i);
                allchildren.add(viewchild);
                allchildren.addAll(getAllChildViewss(viewchild));
            }
        }
        return allchildren;
    }
    /**
     * @note 获取该activity所有view
     * */
    public static List<View> getAllChildViews(Context context) {
        if(!(context instanceof Activity)){
            return null;
        }
        Activity act=(Activity) context;
        View view2 = act.getWindow().getDecorView();
        return getAllChildViewss(view2);
    }
    //图片质量压缩法
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>650) {  //循环判断如果压缩后图片是否大于650kb,大于继续压缩
            System.out.println(" baos.toByteArray().length / 1024"+ baos.toByteArray().length / 1024);
            System.out.println(baos.toByteArray().length / 1024);
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
    //图片按比例大小压缩方法（根据Bitmap图片压缩）
    public static Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 80, baos);//这里压缩80%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;

        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        System.out.println("w="+w);
        int h = newOpts.outHeight;
        System.out.println("h="+h);
        //现在主流手机比较多是1080f*1920f分辨率，所以高和宽我们设置为
        float hh = 720f;//这里设置高度为1080f  1958 648
        float ww = 1280f;//这里设置宽度为1920f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        //be = (int) ((w / ww + h/ hh) / 2);
        //System.out.println("be="+be);
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888 ;//降低图片从ARGB888到RGB56
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);

        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
        // return bitmap;
    }

    public static void saveBitmap(Bitmap image2) {
        //保存在缓存目录中
        try {
            File cachedir = new File(Common.getCacheDirPath());
            File myCaptureFilemyCaptureFile = new File(Common.getCacheDirPath()+File.separator+"123456.jpg");

            if (myCaptureFilemyCaptureFile.exists()) {
                myCaptureFilemyCaptureFile.delete();
            }

            FileOutputStream out = new FileOutputStream(myCaptureFilemyCaptureFile);
            image2.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
