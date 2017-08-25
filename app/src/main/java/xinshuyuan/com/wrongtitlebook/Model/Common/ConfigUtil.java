package xinshuyuan.com.wrongtitlebook.Model.Common;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.Config;

/**
 * @author  lx
 * @date 创建时间：2016-8-19 下午5:23:05
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class ConfigUtil {

    private static String serverBasePath="";
    static {
        readCache();
    }
    private static Config config;
    private static Config getConfig() {
        if(config==null){
            Object o;
            try {
                 o = Common.readObject(Common.getConfigCacheFileName());
                if(o!=null){
                config = (Config) o;

                }else{
                        Config con = new Config();
                        con.setIp("192.168.1.124");
                        con.setPort("8080");
                        con.setContentCode("home");
                        config =con;
                        ConfigUtil.setConfig(con);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        if(config==null){
            config = getConfig();
        }

        String contentCode=config.getContentCode();
        return contentCode;
    }

    public static void setContentCode(String ContentCode){
        if(config==null){
            config = getConfig();
        }

        config.setContentCode(ContentCode);
        setConfig(config);
    }


    public static String getIp(){
        if(config==null){
            config = getConfig();
        }

        String host = config.getIp();
        return host;
    }
    public static String  getPort(){
        if(config==null){
            config = getConfig();
        }

        return config.getPort();
    }
    public static void setIp(String ip){
        if(config==null){
            config = getConfig();
        }

        config.setIp(ip);
        setConfig(config);
    }
    public static void setPort(String  port){
        if(config==null){
            config = getConfig();
        }

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
