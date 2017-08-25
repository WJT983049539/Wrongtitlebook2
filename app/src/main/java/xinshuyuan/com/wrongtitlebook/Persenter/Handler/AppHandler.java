package xinshuyuan.com.wrongtitlebook.Persenter.Handler;

import android.os.Handler;
import android.os.Message;

import xinshuyuan.com.wrongtitlebook.Persenter.Appactionclass.WrongtitlebiikAppaction;

/**
 * AppHandler
 * Created by Administrator on 2017/6/5.
 */

public class AppHandler extends Handler{
    private WrongtitlebiikAppaction wrongtitlebiikAppaction;
    public AppHandler(WrongtitlebiikAppaction wrongtitlebiikAppaction) {
        this.wrongtitlebiikAppaction=wrongtitlebiikAppaction;
    }



    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
    }
}
