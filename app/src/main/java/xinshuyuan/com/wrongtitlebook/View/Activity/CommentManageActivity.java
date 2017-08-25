package xinshuyuan.com.wrongtitlebook.View.Activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.Common;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;

import xinshuyuan.com.wrongtitlebook.Persenter.Handler.CommentManaHandler;
import xinshuyuan.com.wrongtitlebook.R;
import xinshuyuan.com.wrongtitlebook.View.Fragment.EvaluateFragment.CommentManagerUpdateFragment;

/**
 *  * 评论管理Activity页面
 * Created by wjt on 2017/7/31.
 */

public class CommentManageActivity extends EvaluateBaseActivity{
    private CommentManaHandler CMHandler;
    private CommentManagerUpdateFragment fragment;

    @Override
    protected int setLayout() {
        return R.layout.layout_commentmanage;
    }

    @Override
    protected void initView() {
        CMHandler=new CommentManaHandler(this);
        //转到评论管理界面
        CMHandler.sendMessage(XSYTools.makeNewMessage(Common.COMMENTMANAGE,""));
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        startActivity(new Intent(this,EvaluateActivity.class));
        return true;
    }
}
