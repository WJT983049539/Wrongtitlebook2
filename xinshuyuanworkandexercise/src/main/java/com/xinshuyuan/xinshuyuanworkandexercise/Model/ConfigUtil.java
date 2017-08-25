package com.xinshuyuan.xinshuyuanworkandexercise.Model;

/**
 * 默认的参数类
 * Created by Administrator on 2017/7/4.
 */

public class ConfigUtil {


    private static String serverBasePath="";
    static {
        readCache();
    }
    private static Config config = null;
    private static Config getConfig() {
        if(config==null){
            Object o;
            try {
                o = Common.readObject(Common.getConfigCacheFileName());
                config = (Config) o;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(config==null){
            Config con = new Config();
            con.setIp("192.168.1.121");
            con.setPort("8080");
            con.setContentCode("home");
            config =con;
            ConfigUtil.setConfig(con);
        }
        return config;
    }

    //得到SD卡地址
    private static void setConfig(Config config){

        try {
            Common.writeObject(Common.getConfigCacheFileName(), config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void readCache(){
        try {
            getConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getContentCode(){
        config=getConfig();
        String contentCode=config.getContentCode();
        return contentCode;
    }

    public static void setContentCode(String ContentCode){
        config = getConfig();
        config.setIp(ContentCode);
        setConfig(config);
    }


    public static String getIp(){
        config = getConfig();
        String host = config.getIp();
        return host;
    }
    public static String  getPort(){
        config = getConfig();
        return config.getPort();
    }
    public static void setIp(String ip){
        config = getConfig();
        config.setIp(ip);
        setConfig(config);
    }
    public static void setPort(String  port){
        config = getConfig();
        config.setPort(port);
        setConfig(config);
    }
    public static String getServerBasePath() {
        return serverBasePath;
    }
    public static void setServerBasePath(String serverBasePath) {
        ConfigUtil.serverBasePath = serverBasePath;
    }


}




