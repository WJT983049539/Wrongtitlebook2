package xinshuyuan.com.wrongtitlebook.View.Activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.Common;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;

import xinshuyuan.com.wrongtitlebook.Persenter.Handler.SHOWCommentFragmentHandler;
import xinshuyuan.com.wrongtitlebook.R;

/**
 * Created by wjt on 2017/8/24.
 */

public class SHOWCommentFragmentActivity extends EvaluateBaseActivity{
    private LinearLayout showfragment_lin;
    private SHOWCommentFragmentHandler showCommentFragmentHandler;
    private  String displayOrder;
    @Override
    protected int setLayout() {
        return R.layout.layout_act_show_commentfragment;
    }

    @Override
    protected void initView() {
        showCommentFragmentHandler=new SHOWCommentFragmentHandler(this);
        showfragment_lin=fvbi(R.id.showfragment_lin);

        Intent intent=getIntent();
        displayOrder= intent.getStringExtra("jsondate");
    }

    @Override
    protected void initData() {
        //查看成绩的
        if(displayOrder.equals("getGrade")){
            showCommentFragmentHandler.sendMessage(XSYTools.makeNewMessage(Common.GET_GRADE,""));
        //栏目查看
        }else if(displayOrder.equals("getLanmu")){
            showCommentFragmentHandler.sendMessage(XSYTools.makeNewMessage(Common.SHOWLANMUCOMMENTINFO,""));
        }else{
            showCommentFragmentHandler.sendMessage(XSYTools.makeNewMessage(Common.SHOW_COMMENT_FRAGMETN,displayOrder));
        }

    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        this.finish();
        return true;
    }
}
