package xinshuyuan.com.wrongtitlebook.Persenter.Handler;

import android.app.Fragment;
import android.os.Handler;
import android.os.Message;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;

import xinshuyuan.com.wrongtitlebook.Model.Common.Common;
import xinshuyuan.com.wrongtitlebook.Model.Common.XSYTools;
import xinshuyuan.com.wrongtitlebook.Persenter.workRunnable.DownLoadIncludeItem;
import xinshuyuan.com.wrongtitlebook.View.Activity.PreviewTestQuestionActivity;
import xinshuyuan.com.wrongtitlebook.View.Fragment.IncludeFragment;

/**预览试题界面所用的Handler
 * Created by Administrator on 2017/6/14.
 */

public class PreViewTestHandler extends Handler {
    private PreviewTestQuestionActivity myActivity;
    public PreViewTestHandler(PreviewTestQuestionActivity previewTestQuestionActivity) {
        this.myActivity=previewTestQuestionActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);


        switch (msg.what){

            case Common.VIEW_INCLUDTEST_SUBTEST:

                Fragment fragment_include=this.myActivity.getFragmentManager().findFragmentByTag(Common.FRAGMENT_TAG_INCLUDE);
                if(fragment_include!=null && fragment_include instanceof IncludeFragment){
                    IncludeFragment f=(IncludeFragment) fragment_include;
                    if(msg.obj instanceof TestEntity){
                        TestEntity te=(TestEntity) msg.obj;
                        f.doInitAnsInfo(te);
                    }
                }
                break;

                //下载嵌套题的小题
            case Common.GETINCLUDE_ITEM_TEST:

                if(!XSYTools.isEmpty(msg.obj.toString()) && XSYTools.isNumeric(msg.obj.toString())){
                    new Thread(new DownLoadIncludeItem(myActivity,this,msg.obj.toString())).start();
                }
                break;
        }


    }
}
