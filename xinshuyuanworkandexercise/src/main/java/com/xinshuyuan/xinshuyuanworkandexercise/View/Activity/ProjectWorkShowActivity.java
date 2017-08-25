package com.xinshuyuan.xinshuyuanworkandexercise.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.Common;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.PerferenceService;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.javabean.ProjectInfo;
import com.xinshuyuan.xinshuyuanworkandexercise.R;
import com.xinshuyuan.xinshuyuanworkandexercise.presenter.Handler.ProjectWorkShowActivityHandler;

import java.util.ArrayList;

/**显示本科目作业列表的Activity
 * Created by Administrator on 2017/7/5.
 */

public class ProjectWorkShowActivity extends BaseActivity{

    private LinearLayout showproject_info_list_fragment;
    private ProjectWorkShowActivityHandler projectworkshowHandler;
    private ProjectInfo prijectInfo;
    private  long userId;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        userId =intent.getLongExtra("userId",1);
        prijectInfo= (ProjectInfo) bundle.getSerializable("SelectProject");
        //把选择的学科id存到缓存
        Common.setSubjectId(String.valueOf(prijectInfo.getSubjectId()));
        PerferenceService service=new PerferenceService(this);
        service.save("ProjectId",String.valueOf(prijectInfo.getSubjectId()));

        setContentView(R.layout.project_work_show_layout);
        inint();
    }

    private void inint() {
        //显示作业列表
        projectworkshowHandler=new ProjectWorkShowActivityHandler(this);
        projectworkshowHandler.sendMessage(XSYTools.makeNewMessage(Common.SHOW_WORKLIST_INFO_FRAGMENT,prijectInfo.getSubjectId()+","+userId));
    }
    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(10);

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            listener.onTouch(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }

    public interface MyOnTouchListener {

        boolean onTouch(MotionEvent ev);
    }
}
